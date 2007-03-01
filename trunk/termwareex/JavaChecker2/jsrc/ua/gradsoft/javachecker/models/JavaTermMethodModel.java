/*
 * JavaMethodModel.java
 *
 * Created  23, 02, 2004, 10:00
 */

package ua.gradsoft.javachecker.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.ITermVisitor;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.checkers.HidingChecker;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermHolder;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;



/**
 *Model for java method 
 *TODO: build block model
 * @author  Ruslan Shevchenko
 */
public class JavaTermMethodModel extends JavaMethodModel implements JavaTermTopLevelBlockOwnerModel
{
    
    
    /**
     * Creates a new instance of JavaMethodModel
     */
    public JavaTermMethodModel(int modifiers, Term t, JavaTermTypeAbstractModel owner) throws TermWareException
    {
        super(owner);
        t_=t;
        if (t.getName().equals("MethodDeclaration")) {
            Term md=t.getSubtermAt(METHOD_DECLARATOR_INDEX);
            Term nameTerm=md.getSubtermAt(METHOD_DECLARATOR__IDENTIFIER_INDEX);
            methodName_=nameTerm.getSubtermAt(0).getName();
        }else{
            throw new AssertException("term must be MethodDeclaration:"+TermHelper.termToString(t));
        }        
        modifiers_=new JavaModifiersModel(modifiers);
        Term blockTerm = t.getSubtermAt(BLOCK_INDEX);        
        blockModel_= (blockTerm.isNil() ? null : new JavaTermTopLevelBlockModel(this,blockTerm.getSubtermAt(0)));
    }
    
    
    /**
     * check name patterns.
     */
    public boolean checkNamePatterns() throws TermWareException
    {
     boolean retval=true;
     if (getJavaFacts().isCheckEnabled("MethodNamePatterns")) {
        if (!methodName_.matches(getJavaFacts().getMethodNamePattern())) {
            getJavaFacts().violationDiscovered("MethodNamePatterns","bad name of method", t_);
            retval=false;
        }
     }
     
     TermHolder termHolder=new TermHolder(TermWare.getInstance().getTermFactory().createBoolean(retval));
     if (getJavaFacts().isCheckEnabled("VariablePatterns")) {
         visitFormalParameterIdentifiers(new ITermVisitor()
         {
             public boolean doFirst(Term t, TermHolder result, HashSet<Term> trace) throws TermWareException
             {     
               if (t.getName().equals("Identifier")) {
                  checkFormalParameterName(t,t.getSubtermAt(0).getName(),result);
               }else if (t.getName().equals("java_array_declarator")) {
                   checkFormalParameterName(t.getSubtermAt(0),t.getSubtermAt(0).getSubtermAt(0).getName(),result);
               }else{
                  throw new AssertException("first subterm of formal parameter must have name java_identifier or java_array_declarator");
               }
               return true;
             }
             public boolean doSecond(Term t, TermHolder result, HashSet<Term> trace)
             {return true;}
             
             private void checkFormalParameterName(Term t,String formalParameterName,TermHolder result) throws TermWareException
             {
               if (!formalParameterName.matches(getJavaFacts().getLocalVariableNamePattern())) {
                  getJavaFacts().violationDiscovered("MethodNamePatterns","bad formal parameter name",t);
                  result.setValue(TermWare.getInstance().getTermFactory().createBoolean(false));
               }                 
             }
             
         },termHolder,null);
         
         visitLocalVariableDeclarators(getMethodBody(),new ITermVisitor()
         {
             public boolean doFirst(Term t, TermHolder result, HashSet<Term> trace) throws TermWareException {
                 Term nameTerm=t.getSubtermAt(0);
                 String variableName=nameTerm.getString();
                 if (!variableName.matches(getJavaFacts().getLocalVariableNamePattern())) {
                     getJavaFacts().violationDiscovered("VariablePatterns","violation of variable name conventions",t);
                     result.setValue(TermWare.getInstance().getTermFactory().createBoolean(false));
                 }
                 return true;
             }
             
             public boolean doSecond(Term t, TermHolder result, HashSet<Term> trace)
             { return true; }
         },termHolder,null);
     }
     
     return retval;
    }
    
    public  boolean canCheck()
    { return true; }
    
    public  boolean check() throws TermWareException
    {
      return checkNamePatterns() && checkHidingOfFormalParameters();
    }
    
    private class HidingVisitor implements ITermVisitor
    {
        public HidingVisitor(HashSet<String> names)
        {
          names_=names;
        }
        
        public boolean doFirst(Term t,TermHolder result,HashSet<Term> trace) throws TermWareException
        {
          if (t.getName().equals("Identifier")) {
             names_.add(t.getSubtermAt(0).getString());  
          }else if (t.getName().equals("java_array_declarator")) {
             names_.add(t.getSubtermAt(0).getSubtermAt(0).getString());
          }else{
              throw new AssertException("formal parameter term must be identifier or java_array_declarator");
          }
          return true;  
        }
        
        public boolean doSecond(Term t,TermHolder result, HashSet<Term> trace)
        { return true; }
        
        private HashSet<String> names_;
    }
    
    /**
     * check that local variables does not hide formal parameters
     */
    public boolean checkHidingOfFormalParameters() throws TermWareException
    {
      boolean retval=true;
      if (getJavaFacts().isCheckEnabled("Hiding")) {
        HashSet<String> names=new HashSet<String>();
        ITermVisitor visitor=new HidingVisitor(names);
        visitFormalParameterIdentifiers(visitor, null, null);
        HidingChecker hidingChecker=Main.getHidingChecker();
        retval=hidingChecker.check(getMethodBody().termClone(), names, HidingChecker.HIDING_OF_FORMAL_PARAMETER);
      }
      return retval;
    }
    
    public  String getName()
    { return methodName_; }
    
    public JavaModifiersModel getModifiers()
    { return modifiers_; }
    
    public JavaTypeModel getResultType() throws TermWareException
    { 
     Term resultType = getResultTypeAsTerm();
     if (resultType.getName().equals("ResultType")) {
         resultType=resultType.getSubtermAt(0);
     }
     try {   
        return JavaResolver.resolveTypeToModel(resultType,getTypeModel(),getTypeParameters()); 
     }catch(EntityNotFoundException ex){
        return JavaUnknownTypeModel.INSTANCE;  
     }
    }
    
    public  Term  getResultTypeAsTerm() throws TermWareException
    { return t_.getSubtermAt(RESULT_TYPE_TERM_INDEX); }
    
    public  Term  getFormalParametersTerm() throws TermWareException
    {        
        return t_.getSubtermAt(METHOD_DECLARATOR_INDEX).getSubtermAt(METHOD_DECLARATOR__FORMAL_PARAMETERS_INDEX).getSubtermAt(0);
    }

    
    public List<JavaFormalParameterModel> getFormalParametersList() throws TermWareException
    {      
      if (formalParametersList_==null) {
        Term formalParametersTerm = getFormalParametersTerm();
        formalParametersList_ = TermUtils.buildFormalParametersList(formalParametersTerm,this);
      }
      return formalParametersList_;
    }                
        
    public Map<String,JavaFormalParameterModel> getFormalParametersMap() throws TermWareException
    {      
      if (formalParametersMap_==null) {
        Term formalParametersTerm = getFormalParametersTerm();
        formalParametersMap_ = TermUtils.buildFormalParametersMap(formalParametersTerm,this);
      }
      return formalParametersMap_;
    }        
    
    public List<JavaTypeModel> getFormalParametersTypes() throws TermWareException
    {
      Map<String,JavaFormalParameterModel> fps=getFormalParametersMap();
      JavaTypeModel[] retval = new JavaTypeModel[fps.size()];
      for(Map.Entry<String,JavaFormalParameterModel> e:fps.entrySet())
      {
          retval[e.getValue().getIndex()]=e.getValue().getTypeModel();
      }
      return Arrays.asList(retval);
    }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException
    {
        Term tpt = t_.getSubtermAt(TYPE_PARAMETERS_TERM_INDEX);
        return TermUtils.buildTypeParameters(tpt,getTypeModel());
    }
    
    public  Term  getMethodBody() throws TermWareException
    { return t_.getSubtermAt(BLOCK_INDEX); }
    
    //public JavaMethodModel substituteTypeParameters(List<JavaTypeVariableAbstractModel> typeVariables, List<JavaTypeModel> values)
    //{
    //  return new JavaArgumentBoundMethodModel(this,typeVariables,values);
    //}
    
    public void  visitFormalParameterIdentifiers(ITermVisitor visitor,TermHolder result,HashSet<Term> hs) throws TermWareException
    {
     Term t=getFormalParametersTerm();
     while(!t.isNil()) {
        if (t.getName().equals("cons")) {
          Term formalParameter=t.getSubtermAt(0);
          Term variableDeclaratorId=formalParameter.getSubtermAt(FORMAL_PARAMETER_VARIABLEDECLARATORID_INDEX);
          Term identifierTerm=variableDeclaratorId.getSubtermAt(0);
          visitor.doFirst(identifierTerm, result, hs);
          t=t.getSubtermAt(1);
        }else{
          throw new AssertException("formal parameters must be a list");
        }
     }     
    }
    
    public void visitLocalVariableDeclarators(Term t,ITermVisitor visitor, TermHolder result, HashSet<Term> trace) throws TermWareException
    {
      if (t.isComplexTerm()) {
          if (t.getName().equals("LocalVariableDeclaration")) {
              Term type=t.getSubtermAt(LOCALVARIABLEDECLARATION_TYPE_INDEX);
              Term variableDeclaratorsList=t.getSubtermAt(LOCALVARIABLEDECLARATION_VARIABLEDECLARATORS_INDEX);
              Term current=variableDeclaratorsList;
              while(!current.isNil()) {
                  Term variableDeclarator=current.getSubtermAt(0);
                  Term variableDeclaratorId=variableDeclarator.getSubtermAt(0);
                  Term ident=variableDeclaratorId.getSubtermAt(0);
                  visitor.doFirst(ident,result,trace);
                  current=current.getSubtermAt(1);
                  // now vizit in initialization clause ?
                  //  (can be exists subblocks ?.  TODO: check syntax)
                  //Term iniclause=variableDeclarator.getSubtermAt(1);
              }
          }else{
              for(int i=0; i<t.getArity(); ++i) {
                  visitLocalVariableDeclarators(t.getSubtermAt(i),visitor,result,trace);
              }
          }
      }
    }

    public boolean isSynchronized()  {
        return modifiers_.isSynchronized();
    }
    
    
    
    public JavaTermTypeAbstractModel getTermTypeAbstractModel()
    { return (JavaTermTypeAbstractModel)getTypeModel();  }
    
    
    public boolean isSupportBlockModel()
    { return true; }

    /**
     * MethodModel(modifiers,typeParameters,ResultType,name,formalParameters,throws,block,context)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term mt = modifiers_.getModelTerm();
      Term tpt = TermUtils.buildTypeParametersModelTerm(getTypeParameters(),t_.getSubtermAt(TYPE_PARAMETERS_TERM_INDEX));
      Term rt = TermUtils.createTerm("TypeRef",getResultTypeAsTerm(),TermUtils.createJTerm(getResultType()));
      Term identifier = t_.getSubtermAt(METHOD_DECLARATOR_INDEX).getSubtermAt(METHOD_DECLARATOR__IDENTIFIER_INDEX);
      Term ofp = t_.getSubtermAt(METHOD_DECLARATOR_INDEX).getSubtermAt(METHOD_DECLARATOR__FORMAL_PARAMETERS_INDEX);
      Term fp = ofp; /*buildFormalParametersModelTerm(formalParametersMap_,ofp);*/
      Term tht = t_.getSubtermAt(THROWS_SPECIFICATION_INDEX);
      Term blockModelTerm = TermUtils.createNil();
      if (blockModel_!=null) {
          blockModelTerm=blockModel_.getModelTerm();
      }
      JavaPlaceContext ctx = JavaPlaceContextFactory.createNewMethodContext(this);
      Term tctx = TermUtils.createJTerm(ctx);
      Term retval = TermUtils.createTerm("MethodModel",mt,tpt,rt,identifier,fp,tht,blockModelTerm,tctx);
      return retval;
    }
    
    /**
     *return top-level block of method or null if one is not defined.
     *(for abstract method)
     *@Nullable
     */    
    public JavaTopLevelBlockModel getTopLevelBlockModel()
    { return blockModel_; }
        
    
    private String methodName_;
    private Term t_; 
    private JavaTermTopLevelBlockModel blockModel_ =null;    
    private JavaModifiersModel   modifiers_=null;    
    
    // some cashed valuse
    private Map<String,JavaFormalParameterModel> formalParametersMap_=null;
    private List<JavaFormalParameterModel>       formalParametersList_=null;
    
    public static int TYPE_PARAMETERS_TERM_INDEX=0;
    public static int RESULT_TYPE_TERM_INDEX=1;
    public static int METHOD_DECLARATOR_INDEX=2;    
    public static int THROWS_SPECIFICATION_INDEX=3;
    public static int BLOCK_INDEX=4;
    
    
    public static int FORMAL_PARAMETER_MODIFIER_INDEX=0;
    public static int FORMAL_PARAMETER_TYPE_INDEX=1;
    public static int FORMAL_PARAMETER_VARIABLEDECLARATORID_INDEX=2;
    
    public static final int METHOD_DECLARATOR__IDENTIFIER_INDEX=0;
    public static final int METHOD_DECLARATOR__FORMAL_PARAMETERS_INDEX=1;
    
    public static final int LOCALVARIABLEDECLARATION_TYPE_INDEX=1;
    public static final int LOCALVARIABLEDECLARATION_VARIABLEDECLARATORS_INDEX=2;
    
}
