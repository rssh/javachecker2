/*
 * JavaTermAnonimousType.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
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
    public JavaTermAnonimousTypeModel(JavaTermStatementModel statement, Term t) throws TermWareException
    {   
        super(0,t,statement.getTermTopLevelBlockModel().getOwnerTermModel().getTermTypeAbstractModel().getPackageModel(),
                  statement.getTermTopLevelBlockModel().getOwnerTermModel().getTermTypeAbstractModel().getUnitModel()
                );
        statement_=statement;       
        fillModels();          
        JavaTermTypeAbstractModel parent = statement.getTermTopLevelBlockModel().getOwnerTermModel().getTermTypeAbstractModel();        
        setParentType(parent);
        anonimousIndexInParent_=parent.nextAnonimousTypeIndex();        
        name_="$"+anonimousIndexInParent_;
        isAnonimous_=true;
        parent.addNestedType(name_,this);
    }

    public String getName() {
        return name_;
    }
    
    public boolean isAnnotationType()
    {
      return false;  
    }
    
    public boolean isEnum()
    { return false; }

    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
        throw new NotSupportedException();
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
    
      Term classOrInterfaceBody = t_.getSubtermAt(CLASS_OR_INTEFACE_BODY_TERM_INDEX);
      Term membersList = classOrInterfaceBody.getSubtermAt(0);
      
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
            lazyInitSupers();
            supersAreInitialized_=true;
        }
        return super.getSuperInterfaces();
    }
    
    /**
     * we must call resolving of type names after fully reading of type model,
     *to prevent recursive loading of the same class, when anonimpus class extends
     *own public enclosing class. 
     */
    private void lazyInitSupers() throws TermWareException
    {
      Term extendsOrImplements = t_.getSubtermAt(CLASS_OR_INTERFACE_TERM_INDEX);        
      if (extendsOrImplements.getName().equals("ClassOrInterfaceType")||extendsOrImplements.getName().equals("Identifier")) {                    
          JavaTypeModel superTypeModel=null;
          try{
             superTypeModel=JavaResolver.resolveTypeToModel(extendsOrImplements,statement_);
          }catch(EntityNotFoundException ex){
              superTypeModel=JavaUnknownTypeModel.INSTANCE;
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

    private String name_;    
    private int     anonimousIndexInParent_;        
    private JavaTermStatementModel statement_;
    private boolean supersAreInitialized_=false;
    
    
    public static final int CLASS_OR_INTERFACE_TERM_INDEX=0;
    public static final int TYPE_ARGUMENTS_TERM_INDEX=1;
    public static final int CLASS_OR_INTEFACE_BODY_TERM_INDEX=3;        
    
}
