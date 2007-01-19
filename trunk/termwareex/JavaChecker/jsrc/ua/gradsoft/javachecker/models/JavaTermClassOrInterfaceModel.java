/*
 * JavaClassModel.java
 *
 * Created 18, 02, 2004, 8:33
 */

package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for Java Type ( Class or Interface )
 * @author  Ruslan Shevchenko
 */
public class JavaTermClassOrInterfaceModel extends JavaTermTypeAbstractModel {
    
    
    /** Creates a new instance of JavaClassModel */
    public JavaTermClassOrInterfaceModel(int modifiers, Term t, JavaPackageModel packageModel) throws TermWareException {
        super(modifiers, t, packageModel);
        fillModels();
    }
    
    
    private void fillModels() throws TermWareException {
        if (!t_.getName().equals("ClassOrInterfaceDeclaration"))  {
            throw new AssertException("type must be ClassOrInterfaceDeclaration");
        }
        Term t=t_.getSubtermAt(CLASS_OR_INTERFACE_TERM_INDEX);
        if (t.getName().equals("class")) {
            isClass_=true;
        }else if(t.getName().equals("interface")) {
            isInterface_=true;
        }else{
            throw new AssertException("class or interface atom expected");
        }
        Term termName=t_.getSubtermAt(NAME_IDENTIFIER_TERM_INDEX);
        if (termName.getName().equals("Identifier")) {
            name_=termName.getSubtermAt(0).getString();
        }else{
            throw new AssertException("Type name must have form Identifier :"+TermHelper.termToString(termName));
        }
        
        Term typeParametersList = t_.getSubtermAt(TYPE_PARAMETERS_TERM_INDEX);
        if (!typeParametersList.isNil()) {
            Term curr = typeParametersList.getSubtermAt(0);
            while(!curr.isNil()) {
                Term tp = curr.getSubtermAt(0);
                curr=curr.getSubtermAt(1);
                addTypeParameter(tp);
            }
        }
        //System.out.println("!!!:"+TermHelper.termToPrettyString(t_));
        
        Term extendsList = t_.getSubtermAt(EXTENDS_TERM_INDEX);
        if (isInterface_) {
            extendsList = extendsList.getSubtermAt(0);
            while (!extendsList.isNil()) {
                Term classOrInterfaceTypeTerm = extendsList.getSubtermAt(0);
                addSuperInterface(classOrInterfaceTypeTerm);
                extendsList=extendsList.getSubtermAt(1);
            }
        }else{
            extendsList = extendsList.getSubtermAt(0);
            if (!extendsList.isNil()) {
              Term classOrInterfaceTypeTerm = extendsList.getSubtermAt(0);
              addSuperClass(classOrInterfaceTypeTerm);
            }                            
            Term implementsList = t_.getSubtermAt(IMPLEMENTS_TERM_INDEX);
            if (!implementsList.isNil()) {              
              implementsList = implementsList.getSubtermAt(0);
              while (!implementsList.isNil()) {
                  Term classOrInterfaceTypeTerm = implementsList.getSubtermAt(0);
                  addSuperInterface(classOrInterfaceTypeTerm);
                  implementsList=implementsList.getSubtermAt(1);
              }
            }
        }
        
        
        Term membersList=t_.getSubtermAt(CLASS_OR_INTERFACE_BODY_INDEX);
        if (!membersList.getName().equals("ClassOrInterfaceBody")) {
            throw new AssertException("ClassOrInterfaceBody expected instead "+TermHelper.termToString(membersList));
        }
        membersList=membersList.getSubtermAt(0);
        
        while(!membersList.isNil()) {
            if (membersList.getName().equals("cons")) {
                Term declaration=membersList.getSubtermAt(0);
                if (!declaration.getName().equals("ClassOrInterfaceBodyDeclaration")) {
                    throw new AssertException("ClassOrInterfaceBodyDeclaration expected:"+TermHelper.termToString(declaration));
                }
                if (declaration.getArity() > 0) {
                    Term st=declaration.getSubtermAt(0);
                    if (st.getName().equals("Initializer")) {
                        addInitializer(st);
                    }else{
                        int modifiers=declaration.getSubtermAt(0).getSubtermAt(0).getInt();
                        declaration=declaration.getSubtermAt(1);
                        if (declaration.getName().equals("ClassOrInterfaceDeclaration")) {
                            addClassOrInterfaceDeclaration(modifiers,declaration);
                        }else if(declaration.getName().equals("EnumDeclaration")) {
                            addEnumDeclaration(modifiers,declaration);
                        }else if(declaration.getName().equals("ConstructorDeclaration")) {
                            addConstructorDeclaration(modifiers,declaration);
                        }else if(declaration.getName().equals("FieldDeclaration")) {
                            addFieldDeclaration(modifiers,declaration);
                        }else if(declaration.getName().equals("MethodDeclaration")) {
                            addMethodDeclaration(modifiers,declaration);
                        }else{
                            throw new AssertException("Unknown declaration:"+TermHelper.termToString(declaration));
                        }
                    }
                }else{
                    // skip empty declaration, i. e. do nothing.
                }
                membersList=membersList.getSubtermAt(1);
            }else{
                throw new AssertException("membersList must be a list term");
            }
        }
        
    }
    
    
    public boolean isClass() {
        return isClass_;
    }
    
    public boolean isInterface() {
        return isInterface_;
    }
    
    public boolean isEnum() {
        return false; }

    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
        throw new NotSupportedException();
    }
    
    
    public boolean isAnnotationType() {
        return false; }
    
    
    public boolean hasTypeParameters() {
        return true; }
    

    
    public boolean check() throws TermWareException {
        boolean retval=true;
        if(!checkDisabled()) {
            retval=checkNamePatterns();
            retval &= checkBean();
            retval &= checkOverloadedEqualsAndHashCode();
            retval &= super.check();
        }
        return retval;
    }
    
    public boolean checkNamePatterns() throws TermWareException {
        boolean retval=true;
        if (getJavaFacts().isCheckEnabled("ClassNamePatterns")) {
            if (!name_.matches(getJavaFacts().getClassNamePattern())) {
                getJavaFacts().violationDiscovered("ClassNamePatterns","bad classname pattern",t_);
                retval=false;
            }
        }
        
        return retval;
    }
    
    public boolean checkOverloadedEqualsAndHashCode() throws TermWareException
    {
       boolean retval=true;
       boolean overloadedEquals=true;
       boolean overloadedHashCode=true;
       if (getJavaFacts().isCheckEnabled("OverloadedEqualsAndHashCode")) {
           try {
             List<JavaMethodModel> eml=this.findMethodModels("equals");      
           }catch(EntityNotFoundException ex){
               overloadedEquals=false;
           }catch(NotSupportedException ex){
              ; /*impossible*/
           }
           try {
               List<JavaMethodModel> hml=findMethodModels("hashCode");
           }catch(EntityNotFoundException ex){
               overloadedHashCode=false;
           }catch(NotSupportedException ex){
               ; /* impossible */
           }
           if  (overloadedEquals && !overloadedHashCode) {
               Main.getFacts().violationDiscovered("OverloadedEquals","equals is overloaded, but hashCode - not",this.getTerm());                              
               retval=false;
           }else if(!overloadedEquals && overloadedHashCode) {
               Main.getFacts().violationDiscovered("OverloadedEquals","hashCode is overloaded, but equals - not",this.getTerm());                              
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
    public boolean checkBean() throws TermWareException {
        if (checkerComment_==null) {
            return true;
        }
        if (!checkerComment_.isBean()) {
            return true;
        }
        if (!isClass_) {
            getJavaFacts().violationDiscovered("Beans","interface with bean checker comment",t_);
            return false;
        }
        
        
        // TODO: check for default constructor
        
        boolean foundDefaultConstructor=false;
        
        if (!foundDefaultConstructor){
            getJavaFacts().violationDiscovered("Beans","bean class have no default constructor",t_);
        }
        return foundDefaultConstructor;
    }
    
    private boolean isInterface_=false;
    private boolean isClass_=false;
    
    public static int CLASS_OR_INTERFACE_TERM_INDEX=0;
    public static int NAME_IDENTIFIER_TERM_INDEX=1;
    public static int TYPE_PARAMETERS_TERM_INDEX=2;
    public static int EXTENDS_TERM_INDEX=3;
    public static int IMPLEMENTS_TERM_INDEX=4;
    public static int CLASS_OR_INTERFACE_BODY_INDEX=5;
        

}
