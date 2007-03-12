/*
 * JavaTermTypeAbstractModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.CheckerComment;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.InvalidCheckerCommentException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.JTerm;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Abstract class for Term models of Java Types
 * @author Ruslan Shevchenko
 */
public abstract class JavaTermTypeAbstractModel extends JavaTypeModel
{
    
    /** Creates a new instance of JavaTermTypeAbstractModel */
    public JavaTermTypeAbstractModel(int modifiers, Term t, JavaPackageModel packageModel, JavaUnitModel cuModel) throws TermWareException
    {
        super(packageModel);
        modifiers_=new JavaModifiersModel(modifiers);
        t_=t;
        t_=TermHelper.setAttribute(t_,"model",new JTerm(this));
        Term commentTerm=TermHelper.getAttribute(t, "comment");
        if (!commentTerm.isNil()) {
          try {
            checkerComment_=CheckerComment.extract(commentTerm.getString());
          }catch(InvalidCheckerCommentException ex){
              packageModel.getFacts().violationDiscovered("InvalidCheckerComments",ex.getMessage(),t);
          }
        }

        methodModels_=new TreeMap<String,List<JavaMethodModel> >();
        fieldModels_=new TreeMap<String,JavaMemberVariableModel>();
        nestedTypes_=new TreeMap<String,JavaTypeModel>();
        constructors_=new LinkedList<JavaTermConstructorModel>();
        typeVariables_=new LinkedList<JavaTypeVariableAbstractModel>();
        initializers_=new LinkedList<JavaTermInitializerModel>();
        setUnitModel(cuModel);
    }
    
    public String getName()
    {
        return name_;
    }
    
    public Term getShortNameAsTerm() throws TermWareException
    {
       return TermUtils.createIdentifier(name_);
    }
    
   public boolean isPrimitiveType()
   { return false; }
  
   public boolean isNull()
   { return false; }
   
   public boolean isUnknown()
   { return false; }

     
    public CheckerComment  getCheckerComment()
    {
      return checkerComment_;  
    }
        
    
    public boolean hasMethodModels()
    {
        return true;
    }
    
    public Map<String, List<JavaMethodModel>> getMethodModels() throws NotSupportedException
    {
        return methodModels_;
    }
    
    public boolean hasMemberVariableModels()
    { return true; }
    
    public Map<String, JavaMemberVariableModel> getMemberVariableModels()
    { return fieldModels_; }

    public boolean isNested()
    { return parentType_!=null; }
    
    public JavaTypeModel getEnclosedType()
    { return parentType_; }
    
    public List<JavaTypeVariableAbstractModel> getTypeParameters()
    { return typeVariables_; }
    
    public boolean hasNestedTypeModels()
    { return true; }
    
    public Map<String,JavaTypeModel>  getNestedTypeModels()
    {
      return nestedTypes_;  
    }
    
    void setParentType(JavaTypeModel parentType)
    {
        parentType_=parentType;
    }
    
    public JavaModifiersModel getModifiersModel()
    { return modifiers_; }
    
    public boolean isArray()
    { return false; }
    
    public boolean isLocal()
    {
      return isLocal_;
    }
    
    public JavaStatementModel  getEnclosedStatement()
    { return statement_; }
    
    
    /**
     * throw NotSupportedException
     */
    public JavaTypeModel  getReferencedType() throws NotSupportedException
    {
        throw new NotSupportedException();
    }
    
    public boolean isTypeArgument()
    { return false; }
    
    public boolean isWildcardBounds()
    { return false; }
    
    public JavaTypeModel getSuperClass() throws TermWareException
    {
      if (resolvedSuperClass_==null) {
          if (isClass()) {
              if (superClassTerm_==null) {
                  resolvedSuperClass_=JavaResolver.resolveJavaLangObject();   
                  // we can read jdk sources ;)
                  if (getName().equals("Object")) {
                      if (getPackageModel().getName().equals("java.lang")) {
                          resolvedSuperClass_=JavaNullTypeModel.INSTANCE;
                      }
                  }
              }else{                  
                  try {
                     if (isNested()) {
                         resolvedSuperClass_=JavaResolver.resolveTypeToModel(superClassTerm_,getEnclosedType(),getTypeParameters());
                     }else{  
                         resolvedSuperClass_=JavaResolver.resolveTypeToModel(superClassTerm_,getUnitModel(),getPackageModel(),null,getTypeParameters(),null);
                     }
                  }catch(EntityNotFoundException ex){
                      throw new AssertException(ex.getMessage(),ex);
                  }
              }
          }else if(isEnum()) {
              try {
                resolvedSuperClass_=JavaResolver.resolveTypeModelByFullClassName("java.lang.Enum"); 
              }catch(EntityNotFoundException ex){
                  throw new AssertException("Can't resolve java.lang.Enum");
              }
          }else{
              resolvedSuperClass_=JavaResolver.resolveJavaLangObject();
          }
      }       
      return resolvedSuperClass_;
    }
    

    
    /**
     * return list of interfaces.
     */
    public List<JavaTypeModel> getSuperInterfaces() throws TermWareException
    {
      if (resolvedSuperInterfaces_==null) {
          if (superInterfacesTerms_==null) {
              resolvedSuperInterfaces_=Collections.emptyList();
          }else{
            try {  
              resolvedSuperInterfaces_=new LinkedList<JavaTypeModel>();
              for(Term t:superInterfacesTerms_){                  
                  resolvedSuperInterfaces_.add(JavaResolver.resolveTypeToModel(t,this));
              }
            }catch(EntityNotFoundException ex){
                throw new AssertException(ex.getMessage()+" in "+getName(),ex);
            }
          }
      } 
      return resolvedSuperInterfaces_;
    }

    
    
    /**
    *add initializer 
    */
    public void addInitializer(Term initializer) throws TermWareException
    {
      JavaTermInitializerModel initializerModel = new JavaTermInitializerModel(this,initializer);
      initializers_.add(initializerModel);
    }
    
    
    
    /**
     *add super class, denoted by <code> classOrInterfaceTerm </code>.
     *@param classOrInterfaceTerm - ClassOrInterfaceTerm, contains description of superclass.
     */
    public void addSuperClass(Term classOrInterfaceTerm)
    {
      superClassTerm_=classOrInterfaceTerm;  
    }
    
    /**
     *add super interface, denoted by <code> t </code>.
     *@param t - ClassOrInterfaceTerm, contains description of super interface.
     */
    public void addSuperInterface(Term t)
    {
      if (superInterfacesTerms_==null) {
          superInterfacesTerms_=new LinkedList<Term>();
      }
      superInterfacesTerms_.add(t);
    }
    
    
    /**
    * add included class.
    */
    public void addClassOrInterfaceDeclaration(int modifiers, Term declaration) throws TermWareException
    {
      JavaTermClassOrInterfaceModel newModel=new JavaTermClassOrInterfaceModel(modifiers,declaration,this.getPackageModel(),this.getUnitModel());
      newModel.setParentType(this);
      nestedTypes_.put(newModel.getName(),newModel); 
      newModel.setUnitModel(getUnitModel());
    }

    /**
    * add  enum in scope
    */
    public void addEnumDeclaration(int modifiers, Term declaration)  throws TermWareException
    {
      JavaTermEnumModel newModel=new JavaTermEnumModel(modifiers,declaration,this.getPackageModel(), this.getUnitModel());
      newModel.setParentType(this);
      nestedTypes_.put(newModel.getName(),newModel);   
      newModel.setUnitModel(getUnitModel());
    }

   /**
    * add 
    */
    public void addConstructorDeclaration(int modifiers, Term declaration)  throws TermWareException
    {
      JavaTermConstructorModel m=new JavaTermConstructorModel(modifiers,declaration,this);
      constructors_.add(m);
    }
    
    
    public void addMethodDeclaration(int modifiers, Term methodDeclaration) throws TermWareException
    {                       
     JavaMethodModel methodModel=new JavaTermMethodModel(modifiers,methodDeclaration,this);
     String methodName=methodModel.getName();
     List<JavaMethodModel> l=methodModels_.get(methodName);                 
     if (l==null) {
         l=new LinkedList<JavaMethodModel>();
         methodModels_.put(methodName,l);
     }
     l.add(methodModel);
    }

    public void addFieldDeclaration(int modifiers, Term fieldDeclaration) throws TermWareException
    {
      if (!fieldDeclaration.getName().equals("FieldDeclaration")) {
          throw new AssertException("FieldDeclaration expected, have:"+TermHelper.termToString(fieldDeclaration));
      }        
      Term fieldType=fieldDeclaration.getSubtermAt(0);
      Term variableDeclarators=fieldDeclaration.getSubtermAt(1);      
      while(!variableDeclarators.isNil()) {          
          JavaTermMemberVariableModel fieldModel=new JavaTermMemberVariableModel(modifiers,fieldType,variableDeclarators.getSubtermAt(0),this);
          fieldModels_.put(fieldModel.getName(),fieldModel);          
          variableDeclarators=variableDeclarators.getSubtermAt(1);
      }
    }
    
    public void addTypeParameter(Term typeParameter) throws TermWareException
    {
     // System.out.println("addTypeParameter "+TermHelper.termToString(typeParameter));  
      JavaTermTypeVariableModel model=new JavaTermTypeVariableModel(typeParameter,this);
      typeVariables_.add(model);
    }

    public int getLastLocalTypeIndex()
    { return localTypeIndex_; }
    
    int nextLocalTypeIndex()
    {
      return ++localTypeIndex_;  
    }
    
    public int getLastAnonimousTypeIndex()
    { return anonimousTypeIndex_; }
    
    public boolean isAnonimous()
    {
       return isAnonimous_; 
    }
    
    int nextAnonimousTypeIndex()
    {
      return ++anonimousTypeIndex_;  
    }
    
    public Term getTerm()
    { return t_; }
    
    public boolean hasASTTerm()
    { return true; }
    
    public Term getASTTerm()
    { return t_; }
            
    
    void setIsLocal(JavaTermStatementModel statement)
    {
      JavaTermTypeAbstractModel enclosedType=statement.getTermTopLevelBlockModel().getOwnerTermModel().getTermTypeAbstractModel();  
      isLocal_=true;  
      localIndexInEnclosed_=enclosedType.nextLocalTypeIndex();  
      String externalName="$"+localIndexInEnclosed_+getName();  
      enclosedType.nestedTypes_.put(externalName,this);
      setParentType(enclosedType);
      setUnitModel(enclosedType.getUnitModel());
      statement_=statement;
    }
    
    void addNestedType(String name,JavaTermTypeAbstractModel nestedType)
    {
      nestedTypes_.put(name,nestedType);  
      nestedType.setUnitModel(getUnitModel());
    }

    
    public void setUnitModel(JavaUnitModel unitModel)
    {
      super.setUnitModel(unitModel);  
      for(JavaTypeVariableAbstractModel tv: typeVariables_) {
          tv.setUnitModel(unitModel);
      }
      for(JavaTypeModel nested: nestedTypes_.values()) {
          nested.setUnitModel(unitModel);
      }
    }
    
    public Term getMemberModelsList() throws TermWareException, EntityNotFoundException
    {
       Term retval=TermUtils.createNil();       
       if (initializers_!=null) {
          for(JavaInitializerModel x: initializers_) {
              retval=TermUtils.createTerm("cons",x.getModelTerm(),retval);
          }          
       }   
       if (constructors_!=null) {
           for(JavaConstructorModel x: constructors_) {
               retval=TermUtils.createTerm("cons",x.getModelTerm(),retval);
           }
       }
       if (methodModels_!=null) {
           for(Map.Entry<String,List<JavaMethodModel>> e: methodModels_.entrySet()) {
               for(JavaMethodModel x: e.getValue()) {
                   retval=TermUtils.createTerm("cons",x.getModelTerm(),retval);
               }
           }
       }
       if (fieldModels_!=null) {
           for(Map.Entry<String, JavaMemberVariableModel> e:fieldModels_.entrySet()) {
               JavaMemberVariableModel x = e.getValue();
               retval=TermUtils.createTerm("cons",x.getModelTerm(),retval);
           }
       }
       if (nestedTypes_!=null) {
           for(Map.Entry<String,JavaTypeModel> e: nestedTypes_.entrySet()) {
              JavaTypeModel x = e.getValue();
              if (!x.isAnonimous() && !x.isLocal()) {
                  retval=TermUtils.createTerm("cons",x.getModelTerm(),retval);
              }
           }
       }
       retval=TermUtils.reverseListTerm(retval);
       return retval;
    }
    
    /**
     * return type parameters model.
     *(for now - return unchanged typeParametersTerm, in future - return term,
     *where references to other types are substituted to TypeRef(name,typeModel))
     */
    public Term getTypeParametersModel(Term typeParametersTerm)
    {
      return typeParametersTerm;  
      /*  
      List<JavaTypeVariableAbstractModel> tvs=getTypeParameters();  
      Term retval=TermUtils.createNil();
      if (tvs.isEmpty()) {
          return retval;
      }
      Term typeParametersList=typeParametersTerm.getSubtermAt(0);
      Iterator<JavaTypeVariableAbstractModel> tvsit = tvs.iterator();
      while(!typeParametersList.isNil()) {
          Term tp=typeParametersList.getSubtermAt(0);
          typeParametersList=typeParametersList.getSubtermAt(1);
          
      }
      */
    }
    
    protected Term  superClassTerm_ = null;
    protected JavaTypeModel resolvedSuperClass_ = null;
    
    protected List<Term>  superInterfacesTerms_ = null;
    protected List<JavaTypeModel>  resolvedSuperInterfaces_ = null;
    
    
    protected JavaTypeModel parentType_=null;
    protected TreeMap<String, List<JavaMethodModel>> methodModels_;
    protected TreeMap<String, JavaMemberVariableModel> fieldModels_;
    protected List<JavaTermInitializerModel>                  initializers_;
    protected List<JavaTermConstructorModel>                  constructors_;
    protected List<JavaTypeVariableAbstractModel>             typeVariables_;
    
    protected TreeMap<String,JavaTypeModel>                   nestedTypes_;
   
    
    private JavaModifiersModel modifiers_;
        
    protected Term   t_;
    protected CheckerComment   checkerComment_ = null;
    protected String name_=null;
    
    private   int anonimousTypeIndex_ = 0;
    private   int localTypeIndex_ = 0;

    private boolean isLocal_=false;
    private int     localIndexInEnclosed_=-1;        
    
    protected boolean isAnonimous_=false;
    
    /**
     * statement, in which this class is defined if this is local or anonimous class.
     */
    private JavaTermStatementModel  statement_;

}
