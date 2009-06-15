/*
 * JavaClassModel.java
 *
 * Created 18, 02, 2004, 8:33
 */

package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.expressions.JavaTermNullLiteralExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for Java Type ( Class or Interface )
 * @author  Ruslan Shevchenko
 */
public  class JavaTermClassOrInterfaceModel extends JavaTermTypeAbstractModel {
    
    
    /** Creates a new instance of JavaClassModel */
    public JavaTermClassOrInterfaceModel(Term modifiers, Term t, JavaPackageModel packageModel, JavaUnitModel cuModel) throws TermWareException {
        super(modifiers, t, packageModel, cuModel);
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
                        Term modifiersTerm = declaration.getSubtermAt(0);                                               
                        int modifiers=modifiersTerm.getSubtermAt(0).getInt();                      
                        declaration=declaration.getSubtermAt(1);
                        if (declaration.getName().equals("ClassOrInterfaceDeclaration")) {
                            addClassOrInterfaceDeclaration(modifiersTerm,declaration);
                        }else if(declaration.getName().equals("EnumDeclaration")) {
                            addEnumDeclaration(modifiersTerm,declaration);
                        }else if(declaration.getName().equals("ConstructorDeclaration")) {
                            addConstructorDeclaration(modifiersTerm,declaration);
                        }else if(declaration.getName().equals("FieldDeclaration")) {
                            addFieldDeclaration(modifiersTerm,declaration);
                        }else if(declaration.getName().equals("MethodDeclaration")) {
                            addMethodDeclaration(modifiersTerm,declaration);
                        }else if(declaration.getName().equals("AnnotationTypeDeclaration")){
                            addAnnotationTypeDeclaration(modifiersTerm,declaration);
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

    /**
     *@return empty map.
     */
    public Map<String, JavaEnumConstantModel> getEnumConstantModels() {
        return Collections.emptyMap();
    }
        
    /**
     *@return false
     */
    public boolean isAnnotationType() {
        return false; }

    /**
     *@return null
     */    
    public JavaAnnotationInstanceModel getDefaultAnnotationInstanceModel()  
    {
         return null;
    }
    
    /**
     *true, if this type su[pports TypeParameters.
     *(note, that list of typeParameters can be empty)
     */
    public boolean hasTypeParameters() {
        return true; }
    

    /**
     * ClassOrInterfaceModel(modifiers,"class"|"interface",Identifier,typeParameters,extendsList,ImplementsList,ClassOrInterfaceBody(membersList),context)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
       Term modifiers = getModifiersModel().getModelTerm(); 
       Term classOrInterface=t_.getSubtermAt(CLASS_OR_INTERFACE_TERM_INDEX);
       Term nameTerm=t_.getSubtermAt(NAME_IDENTIFIER_TERM_INDEX);
       Term typeParametersTerm = getTypeParametersModel(t_.getSubtermAt(TYPE_PARAMETERS_TERM_INDEX));
       JavaTypeModel superClass = getSuperClass();
       Term extendsListModel = TermUtils.createNil();
       if (!JavaTypeModelHelper.same(JavaResolver.resolveJavaLangObject(),superClass)) {
           Term oel = t_.getSubtermAt(EXTENDS_TERM_INDEX);
           Term oe = oel.getSubtermAt(0);
           Term superClassTypeRef=TermUtils.createTerm("TypeRef",oe,TermUtils.createJTerm(superClass));
           extendsListModel=TermUtils.createTerm("cons",superClassTypeRef,extendsListModel);
       }
       Term implementsListModel = TermUtils.createNil();
       List<JavaTypeModel> superInterfaces = getSuperInterfaces();
       Term oimplementsList = t_.getSubtermAt(IMPLEMENTS_TERM_INDEX);
       if (!oimplementsList.isNil()) {
           oimplementsList=oimplementsList.getSubtermAt(0);
           Iterator<JavaTypeModel> siit = superInterfaces.iterator();
           while(!oimplementsList.isNil()) {
              Term tpi=oimplementsList.getSubtermAt(0);
              oimplementsList=oimplementsList.getSubtermAt(1);
              Term tjt = TermUtils.createJTerm(siit.next());
              Term tr = TermUtils.createTerm("TypeRef",tpi,tjt);
              implementsListModel=TermUtils.createTerm("cons",tr,implementsListModel);
           }
       }
       Term membersList=getMemberModelsList();
       Term classOrInterfaceBody=TermUtils.createTerm("ClassOrInterfaceBody",membersList);
       JavaPlaceContext ctx = JavaPlaceContextFactory.createNewTypeContext(this);
       Term tctx = TermUtils.createJTerm(ctx);
       return TermUtils.createTerm("ClassOrInterfaceModel",modifiers,classOrInterface,nameTerm,typeParametersTerm,extendsListModel,implementsListModel,classOrInterfaceBody,tctx);
    }
    
    public JavaTermExpressionModel getDefaultInitializerExpression() throws TermWareException
    {
      Term nlTerm = TermUtils.createTerm("NullLiteral");
      return new JavaTermNullLiteralExpressionModel(nlTerm,null,this);
    }
      
    
    private boolean isInterface_=false;
    private boolean isClass_=false;
    
    public static final int CLASS_OR_INTERFACE_TERM_INDEX=0;
    public static final int NAME_IDENTIFIER_TERM_INDEX=1;
    public static final int TYPE_PARAMETERS_TERM_INDEX=2;
    public static final int EXTENDS_TERM_INDEX=3;
    public static final int IMPLEMENTS_TERM_INDEX=4;
    public static final int CLASS_OR_INTERFACE_BODY_INDEX=5;
        

}
