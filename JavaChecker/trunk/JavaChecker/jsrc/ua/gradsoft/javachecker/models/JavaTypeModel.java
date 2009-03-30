/*
 * JavaTypeModel.java
 *
 * Copyright (c) 2004-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.attributes.AttributedEntity;
import ua.gradsoft.javachecker.attributes.AttributesData;
import ua.gradsoft.javachecker.attributes.JavaTypeModelAttributes;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Abstract class for type model of Java Language term
 * @author Ruslan Shevchenko
 */
public abstract class JavaTypeModel implements AttributedEntity
{
    
  JavaTypeModel(JavaPackageModel packageModel)
  {
    packageModel_=packageModel;
  }
    
  public abstract String getName();
  
  public abstract String getErasedName();
  
  public abstract Term getShortNameAsTerm() throws TermWareException;
  
  public abstract Term getFullNameAsTerm() throws TermWareException;
  
  
  /**
   * return full name (i. e. with package part).
   */
  public String getFullName()
  { 
      if (isNested()) {
         try { 
            return getEnclosedType().getFullName()+"."+getName();
         }catch(TermWareException ex){
             return packageModel_.getName()+".<error>."+getName();
         }
      }else{
          return getPackageModel().getName()+"."+getName(); 
      }
  }  

  /**
   * return full name (i. e. with package part).
   */
  public String getErasedFullName()
  { 
      if (isNested()) {
         try {
           if (getEnclosedType()==null) {
             return packageModel_.getName()+"."+getErasedName();
           }else{
             return getEnclosedType().getErasedFullName()+"."+getErasedName();
           }
         }catch(TermWareException ex){
             return packageModel_.getName()+".<error>."+getErasedName();
         }
      }else{
          return getPackageModel().getName()+"."+getErasedName(); 
      }
  }  
  
  
  
  /**
   * get canonical name, siutable for use in JVM
   */
  public String getCanonicalName()
  {
      if (isNested()) {
         try{
           if (getEnclosedType()==null) {
             return packageModel_.getName()+"$"+getName();
           }else{
             return getEnclosedType().getFullName()+"$"+getName();
           }
         }catch(TermWareException ex){
             return packageModel_.getName()+"$<error>$"+getName();
         }
      }else{
          return getPackageModel().getName()+"."+getName(); 
      }      
  }
  
  public abstract boolean isClass();
  
  public abstract boolean isInterface();
  
  public abstract boolean isEnum();
  
  public abstract boolean isAnnotationType();
    
  public abstract boolean isPrimitiveType();
  
  public abstract boolean isArray();
  
  public abstract boolean isTypeVariable();
  
  public abstract boolean isWildcardBounds();
  
  public abstract boolean isNull();
  
  /**
   *return true, if this is unknown typemodel.
   *(clients of library can create Unknonwn model, when it is not possible
   * to resolve entity, but resolving is not critical in context)
   */
  public abstract boolean isUnknown();
  
  
    /**
     * get modifiers moder
     * @return Modifiers model
     */
  public abstract JavaModifiersModel  getModifiersModel();
  
  /**
   *get superclass.  
   *does not supported for primitive types, type arguments, wildcard bounds
   */
  public abstract JavaTypeModel getSuperClass() throws TermWareException, EntityNotFoundException;

  /**
   *get all superinterfaces.  
   *does not supported for primitive types, type arguments, wildcard bounds, arrays.
   *@return all direct superinterfaces.
   */
  public abstract List<JavaTypeModel> getSuperInterfaces() throws TermWareException;
  
  
  /**
   *return enclosed class
   */
  public abstract JavaTypeModel  getEnclosedType() throws TermWareException;
  
  
  /**
   *true, if this is local type
   */
  public abstract boolean isLocal();
  
  /**
   *true, if this is anonimous type.
   */
  public abstract boolean isAnonimous();
  
  /**
   *Nullable
   *@return referenced type. Works only if isArray()==true, otherwise
   * return null.
   */
  public abstract JavaTypeModel  getReferencedType() throws TermWareException;
  

  /**
   *@return true if type has methods.
   */
  public abstract boolean hasMethodModels();
    
  /**
   * key of return values are names of methods.  
   *When type is unappropriative for methods (example - TypeVariable) return empty map
   */
  public abstract Map<String, List<JavaMethodModel>>   getMethodModels();
    
  
  public List<JavaMethodModel>  findMethodModels(String name) throws EntityNotFoundException
  {
    List<JavaMethodModel> retval=getMethodModels().get(name);
    if (retval==null) {
        throw new EntityNotFoundException("method",name, " in"+this.getFullName());
    }
    return retval;
  }

  public abstract boolean hasMemberVariableModels();
  
  /***
   * key of return values are names of member variables.
   *When type is unappropriative for methods - return empty map
   */
  public abstract Map<String, JavaMemberVariableModel> getMemberVariableModels();
  
  
  public JavaMemberVariableModel findMemberVariableModel(String name) throws EntityNotFoundException
  {
    JavaMemberVariableModel retval=getMemberVariableModels().get(name);
    if (retval==null) {
        throw new EntityNotFoundException("Member variable",name,"in "+this.getFullName());
    }
    return retval;
  }
  
  /**
   *get list of constructor models.
   */
  public abstract List<JavaConstructorModel>  getConstructorModels();
  
  /**
   *get list of initializer models
   */
  public abstract List<JavaInitializerModel>  getInitializerModels();
  
  
  /***
   * key of return values are names of enum constants.
   *When type is not enum - return empty map.
   */  
  public abstract Map<String,JavaEnumConstantModel> getEnumConstantModels();
  
  
  /**
   * if this is annotation, get annotation instance model, otherwise return null;
   */
  public abstract JavaAnnotationInstanceModel getDefaultAnnotationInstanceModel() throws TermWareException, EntityNotFoundException;
  
  
  /**
   * return true if this type is nested inside some other type.
   */
  public abstract boolean isNested();
  
  /**
   *when return true, getNestedTypeModels() returns set of nested types. Otherwise getNestedTypeModels throws 
   * NotSupportedException
   */
  public abstract boolean hasNestedTypeModels();
  
  /**
   *@return set of nested types.
   */
  public abstract Map<String,JavaTypeModel> getNestedTypeModels() throws TermWareException;
 
  
  public JavaTypeModel findNestedTypeModel(String name) throws EntityNotFoundException,  TermWareException
  {
    JavaTypeModel retval=getNestedTypeModels().get(name);
    if (retval==null) {
        throw new EntityNotFoundException("Nested type",name,"in "+this.getFullName());
    }
    return retval;
  }
  
  /**
   * if this type hase type parameters ?
   */
  public abstract boolean hasTypeParameters() throws TermWareException;
  
  /**
   * return type parameters. (i. e.  for class<U,B> { ... }  definitions are <U,B> )
   *in case of absence of type paramters return empty list
   */
  public abstract  List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException;
  
  /**
   *if this is local or anonimous class and it is possible to get enclosing
   *statement - return enclosed statement, otherwise - null
   */
  public abstract  JavaStatementModel  getEnclosedStatement();
     
  /**
   *return model of package
   */
  public  JavaPackageModel  getPackageModel()
  { return packageModel_; }
  
  /**
   *return true, if this type is annotated by annotation with type
   *<code>annotationTypeName</code>, otherwise false
   */
  public boolean hasAnnotation(String annotationTypeName) throws TermWareException
  { return getAnnotationsMap().containsKey(annotationTypeName); }
  
  /**
   *return annottation instance, if one exists. Otherwise - null.
   */
  public JavaAnnotationInstanceModel  getAnnotation(String annotationName) throws TermWareException
  { 
     JavaAnnotationInstanceModel retval = getAnnotationsMap().get(annotationName);
     return retval;
  }
 
  /**
   * get map of annotations, declared in source code.
   */
  public abstract Map<String,JavaAnnotationInstanceModel>  getAnnotationsMap() throws TermWareException;
   
  
  
  /**
   *return true, if type model have AST Term
   */
  public abstract boolean hasASTTerm();
  
  /**
   * return AST term, if exoists, otherwise - null
   */
  public abstract Term  getASTTerm() throws TermWareException, EntityNotFoundException;
  
  /**
   *return TypeModel term.
   *TypeModel is a term which have next form:
   * TypeModel( Model-of-ASTTree, PlaceContext )
   */
  public  abstract Term  getModelTerm() throws TermWareException, EntityNotFoundException;

  /**  
   * @return default initializer erxpression, if entity is not initialized. (i.e. null for complex types, and so on)
   * @throws ua.gradsoft.termware.TermWareException
   * @throws ua.gradsoft.javachecker.EntityNotFoundException
   */
  public abstract JavaExpressionModel  getDefaultInitializerExpression() throws TermWareException, EntityNotFoundException;
  
  public AttributesData getAttributesData() throws TermWareException
  {
      return getAttributes().getData();
  }
  
  public Term getAttribute(String name) throws TermWareException
  {
      return getAttributes().getTypeAttribute(name);      
  }
  
  
  public void setAttribute(String name, Term value) throws TermWareException
  {
      getAttributes().setTypeAttribute(name,value);
  }
  
  public AttributedEntity  getChildAttributes(String childName) throws TermWareException
  {
      return getAttributes().getData().getOrCreateChild(childName);
  }
  
  /**
   *return attributes of this model
   */
  public JavaTypeModelAttributes getAttributes()
  {
     synchronized(this) { 
       if (attributes_==null) {
          attributes_=new JavaTypeModelAttributes(this);
       }
       return attributes_;
     }
  }     
  
  public  JavaUnitModel  getUnitModel()
  { return unitModel_; }
  
  public  void setUnitModel(JavaUnitModel unitModel)
  { unitModel_=unitModel; }
    
  
  public  JavaFacts  getJavaFacts()
  { return packageModel_.getFacts(); }
  
  private JavaPackageModel packageModel_;
  private JavaUnitModel    unitModel_;
  private JavaTypeModelAttributes attributes_=null;
 
}
