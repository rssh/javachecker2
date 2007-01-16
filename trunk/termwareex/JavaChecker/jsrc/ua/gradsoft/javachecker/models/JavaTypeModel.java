/*
 * JavaTypeModel.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Abstract class for type model of Java Language term
 * @author Ruslan Shevchenko
 */
public abstract class JavaTypeModel {
    
  JavaTypeModel(JavaPackageModel packageModel)
  {
    packageModel_=packageModel;
  }
    
  public abstract String getName();
  
  public abstract Term getShortNameAsTerm() throws TermWareException;
  
  
  /**
   * return full name (i. e. with package part).
   */
  public String getFullName()
  { 
      if (isNested()) {
         try { 
          return getEnclosedType().getFullName()+"."+getName();
         }catch(NotSupportedException ex){
             return packageModel_.getName()+"."+getName();
         }catch(TermWareException ex){
             return packageModel_.getName()+".<error>."+getName();
         }
      }else{
          return getPackageModel().getName()+"."+getName(); 
      }
  }  
   
  
  /**
   * get canonical name, siutable for use in JVM
   */
  public String getCanonicalName()
  {
      if (isNested()) {
         try { 
          return getEnclosedType().getFullName()+"$"+getName();
         }catch(NotSupportedException ex){
             return packageModel_.getName()+"$"+getName();
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
  
  public abstract boolean isTypeArgument();
  
  public abstract boolean isWildcardBounds();
  
  public abstract boolean isNull();
  
  public abstract boolean isUnknown();
  
  /**
   *get superclass.  
   *does not supported for primitive types, type arguments, wildcard bounds
   */
  public abstract JavaTypeModel getSuperClass() throws NotSupportedException, TermWareException;

  /**
   *get all superinterfaces.  
   *does not supported for primitive types, type arguments, wildcard bounds, arrays.
   *@return all direct superinterfaces.
   */
  public abstract List<JavaTypeModel> getSuperInterfaces() throws NotSupportedException, TermWareException;
  
  
  /**
   *return enclosed class
   */
  public abstract JavaTypeModel  getEnclosedType() throws NotSupportedException, TermWareException;
  
  
  public abstract boolean isLocal();
  
  /**
   *@return referenced type. Works only if isArray()==true, otherwise
   *throws NotSupportedException
   */
  public abstract JavaTypeModel  getReferencedType() throws NotSupportedException, TermWareException;
  
  public abstract boolean canCheck();
  
  public abstract boolean check() throws TermWareException;

  public abstract boolean hasMethodModels();
    
  /**
   * key of return values is name of methods.  
   *When type is unappropriative for methods (example - TypeVariable) throws NotSupportedException 
   */
  public abstract Map<String,List<JavaMethodAbstractModel> >   getMethodModels() throws NotSupportedException;
    
  
  public List<JavaMethodAbstractModel>  findMethodModels(String name) throws EntityNotFoundException, NotSupportedException
  {
    List<JavaMethodAbstractModel> retval=getMethodModels().get(name);
    if (retval==null) {
        throw new EntityNotFoundException("Method",name," in"+this.getFullName());
    }
    return retval;
  }

  public abstract boolean hasMemberVariableModels();
  
  public abstract Map<String,JavaMemberVariableAbstractModel> getMemberVariableModels() throws NotSupportedException;
  
  
  public JavaMemberVariableAbstractModel findMemberVariableModel(String name) throws EntityNotFoundException, NotSupportedException
  {
    JavaMemberVariableAbstractModel retval=getMemberVariableModels().get(name);
    if (retval==null) {
        throw new EntityNotFoundException("Member variable",name,"in "+this.getFullName());
    }
    return retval;
  }
  
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
  public abstract Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException, TermWareException;
 
  public JavaTypeModel findNestedTypeModel(String name) throws EntityNotFoundException, NotSupportedException, TermWareException
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
  public abstract boolean hasTypeParameters();
  
  /**
   * return type parameters. (i. e.  for class<U,B> { ... }  definitions are <U,B> )
   *in case of absence of type paramters return empty list
   */
  public abstract  List<JavaTypeVariableAbstractModel>  getTypeParameters();
  
  /**
   *if this is local or anonimous class and it is possible to get enclosing
   *statement - return enclosed statement, otherwise - null
   */
  public abstract  JavaStatementModel  getEnclosedStatement();
  
  
  
  public  JavaPackageModel  getPackageModel()
  { return packageModel_; }
  
  public  JavaUnitModel  getUnitModel()
  { return unitModel_; }
  
  public  void setUnitModel(JavaUnitModel unitModel)
  { unitModel_=unitModel; }
  
  
  
  
  public  JavaFacts  getJavaFacts()
  { return packageModel_.getFacts(); }
  
  private JavaPackageModel packageModel_;
  private JavaUnitModel    unitModel_;
 
}
