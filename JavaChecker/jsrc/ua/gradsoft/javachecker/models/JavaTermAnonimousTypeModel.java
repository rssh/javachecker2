/*
 * JavaTermAnonimousType.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.models.expressions.JavaTermNullLiteralExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Class for anonimous type definitions.
 *(which can be situated inside AllocatorExpressions)
 * @author Ruslan Shevchenko
 */
public class JavaTermAnonimousTypeModel extends JavaTermTypeAbstractModel
{
    
    /**
     *create new model of  anonimous type, where t is AllocatorExpression with type
     *definition, 
     */
    public JavaTermAnonimousTypeModel(JavaTermStatementModel statement, Term t, JavaTypeModel enclosedType) throws TermWareException
    {   
        super(TermUtils.createTerm("Modifiers",TermUtils.createInt(0),TermUtils.createNil()),
                t,
                statement!=null ?
                    statement.getTermTopLevelBlockModel().getOwnerTermModel().getTermTypeAbstractModel().getPackageModel()
                   :
                    enclosedType.getPackageModel() 
                 ,
                statement!=null ?
                  statement.getTermTopLevelBlockModel().getOwnerTermModel().getTermTypeAbstractModel().getUnitModel()
                  :
                   enclosedType.getUnitModel() 
                );
        statement_=statement;       
        fillModels();                        
        JavaTermTypeAbstractModel parent = null;
        if (statement!=null) {
          parent = statement.getTermTopLevelBlockModel().getOwnerTermModel().getTermTypeAbstractModel();        
        }else{            
          if (enclosedType instanceof JavaTermTypeAbstractModel) { 
            parent = (JavaTermTypeAbstractModel)enclosedType; 
          }else{
            throw new AssertException("Enclosed type for JavaTermAnonimousTypeModel must be instance of JavaTermTypeAbstractModel");  
          }
        } 
        setParentType(parent);
        anonimousIndexInParent_=parent.nextAnonimousTypeIndex();        
        name_="$"+anonimousIndexInParent_;
        isAnonimous_=true;
        parent.addNestedType(name_,this);
    }

    @Override
    public String getName() {
        return name_;
    }
    
    public boolean isAnnotationType()
    {
      return false;  
    }
    
    public boolean isEnum()
    { return false; }

    /**
     * @return empty map
     */
    public Map<String, JavaEnumConstantModel> getEnumConstantModels() {
        return Collections.emptyMap();
    }

    /**
     *@return null
     */    
    public JavaAnnotationInstanceModel getDefaultAnnotationInstanceModel() 
    {
         return null;
    }

    
    
    public boolean isInterface()
    { return false; }
    
    public boolean isClass()
    { return true; }
    
   
    
    public boolean hasTypeParameters()
    {
      return !t_.getSubtermAt(TYPE_ARGUMENTS_TERM_INDEX).isNil();
    }
    
        
    private void fillModels() throws TermWareException
    {

      Term classOrInterfaceBody=null;
      Term membersList=null;

      try {  
        classOrInterfaceBody = t_.getSubtermAt(CLASS_OR_INTEFACE_BODY_TERM_INDEX);
        membersList = classOrInterfaceBody.getSubtermAt(0);
      }catch(Exception ex){
          throw new AssertException("exception: t_="+TermHelper.termToString(t_),ex);
      }
      
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
                        Term modifiers=declaration.getSubtermAt(0);
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
    
   
    public JavaTypeModel getSuperClass() throws TermWareException, EntityNotFoundException
    {
       if (!supersAreInitialized_) {
           lazyInitSupers();
           supersAreInitialized_=true;
       } 
       return super.getSuperClass();
    }


    public List<JavaTypeModel> getSuperInterfaces() throws TermWareException
    {
        if (!supersAreInitialized_) {
          try{  
            lazyInitSupers();
          }catch(EntityNotFoundException ex){
              ex.setFileAndLine(JUtils.getFileAndLine(t_));
              throw new AssertException(ex.getMessage(),ex);
          }
            supersAreInitialized_=true;
        }
        return super.getSuperInterfaces();
    }
    
    /**
     * we must call resolving of type names after fully reading of type model,
     *to prevent recursive loading of the same class, when anonimpus class extends
     *own public enclosing class. 
     */
    private void lazyInitSupers() throws TermWareException, EntityNotFoundException
    {
      Term extendsOrImplements = t_.getSubtermAt(CLASS_OR_INTERFACE_TERM_INDEX);        
      if (extendsOrImplements.getName().equals("ClassOrInterfaceType")||extendsOrImplements.getName().equals("Identifier")) {                    
          JavaTypeModel superTypeModel=null;
          if (statement_!=null) {            
             superTypeModel=JavaResolver.resolveTypeToModel(extendsOrImplements,statement_);
          }else if (getEnclosedType()!=null) {
             superTypeModel=JavaResolver.resolveTypeToModel(extendsOrImplements,getEnclosedType()); 
          } else {
              throw new InvalidJavaTermException("Anonimous type definition outside statement or type",t_);
          }
          Term ta=t_.getSubtermAt(TYPE_ARGUMENTS_TERM_INDEX);
          if (!ta.isNil()) {
              superTypeModel = new JavaTypeArgumentBoundTypeModel(superTypeModel,ta,this,null,statement_);
          }
          if (superTypeModel.isInterface()) {
              this.addSuperInterface(extendsOrImplements);
          }else{
              this.addSuperClass(extendsOrImplements);
          }                    
      }else{
          throw new AssertException("is not valid super of anonimous class:"+TermHelper.termToString(extendsOrImplements));
      }        
    }
    
    /**
     * AnonimousClassModel(name_,super, membersList,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term idTerm=TermUtils.createIdentifier(name_);
        Term superTerm=null;
        JavaTypeModel superClass = getSuperClass();
        List<JavaTypeModel> superInterfaces = getSuperInterfaces();
        if (superInterfaces.isEmpty()) {
            Term tsc=TermUtils.createJTerm(superClass);
            Term scTerm = t_.getSubtermAt(CLASS_OR_INTERFACE_TERM_INDEX);
            Term taTerm = t_.getSubtermAt(TYPE_ARGUMENTS_TERM_INDEX);
            if (!taTerm.isNil()) {
                Term sclTerm=TermUtils.createTerm("cons",scTerm,TermUtils.createTerm("cons",taTerm,TermUtils.createNil()));
                scTerm=TermUtils.createTerm("ClassOrInterfaceType",sclTerm);
            }
            superTerm=TermUtils.createTerm("TypeRef",scTerm,tsc);            
        }else{
            JavaTypeModel superInterface=superInterfaces.get(0);
            Term tsc=TermUtils.createJTerm(superInterface);
            Term scTerm = t_.getSubtermAt(CLASS_OR_INTERFACE_TERM_INDEX);
            Term taTerm = t_.getSubtermAt(TYPE_ARGUMENTS_TERM_INDEX);
            if (!taTerm.isNil()) {
                Term sclTerm=TermUtils.createTerm("cons",scTerm,TermUtils.createTerm("cons",taTerm,TermUtils.createNil()));
                scTerm=TermUtils.createTerm("ClassOrInterfaceType",sclTerm);
            }            
            superTerm=TermUtils.createTerm("TypeRef",scTerm,tsc); 
        }
        Term membersList=getMemberModelsList();
        JavaPlaceContext ctx=JavaPlaceContextFactory.createNewTypeContext(this);
        Term tctx=TermUtils.createJTerm(ctx);
        return TermUtils.createTerm("AnonimousClassModel",idTerm,superTerm,membersList,tctx);
    }



    @Override
    public JavaExpressionModel getDefaultInitializerExpression() throws TermWareException, EntityNotFoundException {
        return JavaTermNullLiteralExpressionModel.getNull();
    }

    private int     anonimousIndexInParent_;        
    private boolean supersAreInitialized_=false;
    
    
    public static final int CLASS_OR_INTERFACE_TERM_INDEX=0;
    public static final int TYPE_ARGUMENTS_TERM_INDEX=1;
    public static final int CLASS_OR_INTEFACE_BODY_TERM_INDEX=3;        
    
}
