/*
 * JavaArrayTypeModel.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Type model for Java array
 * @author Ruslan Shevchenko
 */
public class JavaArrayTypeModel extends JavaTypeModel
{
    
    /** Creates a new instance of JavaArrayTypeModel */
    public JavaArrayTypeModel(JavaTypeModel referencedType) {
       super(referencedType.getPackageModel());
       referencedType_=referencedType;
    }
    
    public String getName()
    {  return referencedType_.getName()+"[]"; }
  
    public Term getShortNameAsTerm() throws TermWareException
    {
       return TermUtils.createTerm("ReferenceType",referencedType_.getShortNameAsTerm(),1); 
    }
  
    /*
    public Term getFullNameAsTerm() throws TermWareException
    {
       return TermUtils.createTerm("ReferenceType",referencedType_.getFullNameAsTerm(),1); 
    }
     */
  
    
    public boolean isClass()
    { return false; }
  
    public boolean isInterface()
    { return false; }
  
    public boolean isEnum()
    { return false; }
  
    public boolean isAnnotationType()
    { return false; }
    
    public boolean isPrimitiveType()
    { return false; }
  
    public  boolean isArray()
    { return true; }
  
    public boolean isWildcardBounds()
    { return false; }
    
    public boolean isUnknown()
    { return false; }

    public boolean isNull()
    { return false; }
    
    
    public boolean isTypeArgument()
    { return false; }
  
  public  JavaTypeModel  getEnclosedType() throws NotSupportedException
  { throw new NotSupportedException(); }
  
  /**
   *@return referenced type. 
   */
  public JavaTypeModel  getReferencedType() throws NotSupportedException
  {
    return referencedType_;  
  }
  
  public  boolean canCheck()
  { return referencedType_.canCheck(); }
  
  public boolean check() throws TermWareException
  { return referencedType_.check(); }

  
  public boolean hasTypeParameters() throws TermWareException
  { return referencedType_.hasTypeParameters(); }
  
  public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException
  { return referencedType_.getTypeParameters(); }

  public boolean hasMethodModels()
  { return false; }
    
  /**
   * key of return values is name of methods.   
   */
  public Map<String, List<JavaMethodModel>>   getMethodModels() throws NotSupportedException
  { throw new NotSupportedException(); }
        
  public boolean hasMemberVariableModels()
  { return false; }
  
  public Map<String, JavaMemberVariableModel> getMemberVariableModels() throws NotSupportedException
  { throw new NotSupportedException(); }        

  public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
      throw new NotSupportedException();
    }
  
  public boolean isNested()
  { return false; }
  
  public boolean hasNestedTypeModels()
  { return false; }
  
  public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException
  { throw new NotSupportedException(); }
           
  public JavaTypeModel  getSuperClass() throws TermWareException
  {
     return JavaResolver.resolveJavaLangObject(); 
  }
  
  public List<JavaTypeModel> getSuperInterfaces()
  {
     return Collections.emptyList(); 
  }
  
  public boolean isLocal()
  { return false; }
  
  public boolean isAnonimous()
  { return false; }
  
  public JavaStatementModel getEnclosedStatement()
  { return null; } 
  
  public Term getModelTerm() throws TermWareException
  {
    return TermUtils.createTerm("ReferencedType",referencedType_.getModelTerm());  
  }
  
    private JavaTypeModel referencedType_;

}
