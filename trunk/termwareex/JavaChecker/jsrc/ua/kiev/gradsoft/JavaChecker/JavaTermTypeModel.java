/*
 * JavaClassModel.java
 *
 * Created 18, 02, 2004, 8:33
 */

package ua.kiev.gradsoft.JavaChecker;

import java.util.*;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Model for Java Type ( Class or Interface )
 * @author  Ruslan Shevchenko
 */
public class JavaTermTypeModel extends JavaTypeModel
{
    
    
    
    /** Creates a new instance of JavaClassModel */
    public JavaTermTypeModel(ITerm t, JavaPackageModel packageModel) throws TermWareException
    {
        super(packageModel);
        t_=TermHelper.setAttribute(t,"model",new JTerm(this));
        ITerm commentTerm=TermHelper.getAttribute(t, "comment");
        if (!commentTerm.isNil()) {
          try {
            checkerComment_=CheckerComment.extract(commentTerm.getString());
          }catch(InvalidCheckerCommentException ex){
              packageModel.getFacts().violationDiscovered("InvalidCheckerComments",ex.getMessage(),t);
          }
        }
        methodModelVectors_=new HashMap();
        fieldModels_=new HashMap();
        fillModels();
    }
    
    /**
     * get type term, attributed with own model.
     */
    public ITerm getAttributed()
    {
      return t_;
    }
    
    private void fillModels() throws TermWareException
    {
      ITerm membersList=null;
      if (t_.isComplexTerm() && t_.getName().equals("java_interface_declaration")) {
          isInterface_=true;
          membersList=t_.getSubtermAt(INTERFACE_MEMBERS_INDEX);
      }else if (t_.getName().equals("java_class_declaration")) {
          isClass_=true;
          membersList=t_.getSubtermAt(CLASS_MEMBERS_INDEX);
      }else{
          throw new AssertException("Invalid Java Type:"+TermHelper.termToString(t_));
      }
      ITerm termName=t_.getSubtermAt(0);
      if (termName.getName().equals("java_identifier")) {
          typeName_=termName.getSubtermAt(0).getString();
          ITerm thisTerm = new JTerm(this);
          t_=TermHelper.attributeTree(t_,"classModel",thisTerm, new ITermCondition() {
              public boolean check(ITerm t) { return t.getName().equals("java_identifier"); }
          });
      }else{
          throw new AssertException("Type name must have form java_identifier :"+TermHelper.termToString(termName));
      }
      if (!membersList.getName().equals("empty_list")) {
        while(!membersList.isNil()) {
          if (membersList.getName().equals("cons")) {
              ITerm declaration=membersList.getSubtermAt(0);
              if (declaration.getName().equals("java_method_declaration")) {
                  JavaMethodModel methodModel=new JavaTermMethodModel(declaration,this);
                  String methodName=methodModel.getMethodName();
                  Object o=methodModelVectors_.get(methodName);
                  Vector v=null;
                  if (o==null) {
                      v=new Vector();
                      methodModelVectors_.put(methodName,v);
                  }else{
                      v=(Vector)o;
                  }
                  v.add(methodModel);
              }else if(declaration.getName().equals("java_field_declaration")) {
                  ITerm fieldProperties=declaration.getSubtermAt(0);
                  ITerm fieldType=declaration.getSubtermAt(1);
                  ITerm fieldNames=declaration.getSubtermAt(2);
                  while(!fieldNames.isNil()) {
                      ITerm variableDeclarator = fieldNames.getSubtermAt(0);
                      JavaMemberVariableModel fieldModel=new JavaMemberVariableModel(variableDeclarator,this, fieldType, fieldProperties);
                      String fieldName=fieldModel.getMemberVariableName();
                      fieldModels_.put(fieldName,fieldModel);
                      fieldNames=fieldNames.getSubtermAt(1);
                  }
              }// TODO: fill nested class declarations
              membersList=membersList.getSubtermAt(1);
          }else{
              throw new AssertException("membersList is not list in java type term");
          }
        }
      }
    }
    
    public String getTypeName()
    { return typeName_; }
    
    public  JavaMemberVariableModel findFieldModel(String name) throws EntityNotFoundException
    {
      Object o=fieldModels_.get(name); 
      if (o==null) throw new EntityNotFoundException("field",name, " in class "+typeName_);
      return (JavaMemberVariableModel)o;  
    }
    
    public Collection findMethodModels(String name) throws EntityNotFoundException
    {
      Object o=methodModelVectors_.get(name);
      if (o==null) {
          throw new EntityNotFoundException("method", name,  " in class "+typeName_);
      }
      Vector v=(Vector)o;
      return v;
    }
    
    public Map getMethodModels()
    {
        return methodModelVectors_;
    }
    
    public boolean check() throws TermWareException
    {
        boolean disableAll=false;
        if (checkerComment_!=null) {
            disableAll=checkerComment_.isDisable("All");
        }
        if (disableAll) return true;
            
        boolean retval=checkNamePatterns();
        
        if (Main.getBTPatternChecker().isEnabled()) {
          Main.getBTPatternChecker().check(t_.term_clone());
        }

        checkBean();
      
        // check method models.
        Iterator it=methodModelVectors_.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry me=(Map.Entry)it.next();
            Vector methodModels =(Vector)(me.getValue());
            Iterator it1=methodModels.iterator();
            while(it1.hasNext()) {
                JavaMethodModel methodModel=(JavaMethodModel)it1.next();
                retval &= methodModel.check();
            }
        }
        
        it=fieldModels_.entrySet().iterator();
        while(it.hasNext()) {
             Map.Entry me=(Map.Entry)it.next();
             JavaMemberVariableModel variableModel=(JavaMemberVariableModel)me.getValue();
             retval &= variableModel.check();
        }
        
        return retval;

    }
    
    public boolean checkNamePatterns() throws TermWareException
    {
     boolean retval=true;
     if (getFacts().isCheckEnabled("ClassNamePatterns")) {
        if (!typeName_.matches(getFacts().getClassNamePattern())) {
            getFacts().violationDiscovered("ClassNamePatterns","bad classname pattern",t_);
            retval=false;
        }
     }
     
     return retval;
    }
    
    /**
     * check bean-s constraint.
     *  (i. e. when checkerComment_.isBean()).
     *<ul>
     * <li> have default constructor </li>
     *<ul>
     */
    public boolean checkBean() throws TermWareException
    {
        if (checkerComment_==null) {
           return true;
        }
        if (!checkerComment_.isBean()) {
           return true;
        }
        if (!isClass_) {
           getFacts().violationDiscovered("Beans","interface with bean checker comment",t_);
           return false;
        }
        ITerm membersList=t_.getSubtermAt(CLASS_MEMBERS_INDEX);
        boolean foundDefaultConstructor=false;
        while(!membersList.isNil()){
            ITerm candidate=membersList.getSubtermAt(0);
            if (candidate.getName().equals("java_constructor_declaration")){
                ITerm params=candidate.getSubtermAt(CONSTRUCTOR_PARAMS_INDEX);
                if (params.isNil()||params.getName().equals("empty_list")){
                   foundDefaultConstructor=true;
                   break;
                }
            }
            membersList=membersList.getSubtermAt(1);
        }
        if (!foundDefaultConstructor){
            getFacts().violationDiscovered("Beans","bean class have no default constructor",t_);
        }
        return foundDefaultConstructor;
    }
    
    
    public boolean canCheck() {
        return true;
    }    
    
    public boolean hasMethodModels() {
        return true;
    }    
    
    private String  typeName_=null;
    private boolean isInterface_=false;
    private boolean isClass_=false;
    private HashMap methodModelVectors_;
    private HashMap nestedTypeModels_;
    private HashMap fieldModels_;
    private ITerm t_;
    private CheckerComment checkerComment_=null;
    
    public static final int CLASS_MEMBERS_INDEX = 4;
    public static final int INTERFACE_MEMBERS_INDEX = 4;
    
    public static final int CONSTRUCTOR_PARAMS_INDEX = 2;
}
