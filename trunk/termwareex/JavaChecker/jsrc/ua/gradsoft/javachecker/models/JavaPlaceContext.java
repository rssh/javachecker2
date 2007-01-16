/*
 * JavaPlaceContext.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

/**
 *This class keep context of program place.
 *(i. e. what is block model, and so on)
 *Note, that class is immutable: i. e. we
 * @author Ruslan Shevchenko
 */
public class JavaPlaceContext {
    
    
    /**
     * get package, inside which we situated.
     */ 
    public JavaPackageModel getPackageModel()
    { return packageModel_; }
    
    /**
     * set package
     */
    public  void  setPackageModel(JavaPackageModel packageModel)
    { packageModel_=packageModel; }
    
    /**
     * get type model, inside which we situated.
     */
    public  JavaTypeModel  getTypeModel()
    { return typeModel_; }
    
    public  void  setTypeModel(JavaTypeModel typeModel)
    { typeModel_=typeModel; }
    
    /**
     * get method, inside which we situated.
     */
    public  JavaMethodAbstractModel  getMethodModel()
    {
      return methodModel_;  
    }
    
    public void setMethodModel(JavaMethodAbstractModel methodModel)
    {
      methodModel_=methodModel;  
    }
    
    private JavaPackageModel  packageModel_=null;
    private JavaTypeModel     typeModel_=null;
    private JavaMethodAbstractModel   methodModel_=null;
    
    
}
