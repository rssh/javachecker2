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
     * can be called only by Factory from this package
     */
    JavaPlaceContext()
    {}
    
    /**
     * get package, inside which we situated.
     */ 
    public JavaPackageModel getPackageModel()
    { return packageModel_; }
    
    /**
     * set package model (and reset type, blockOwner and statement models)
     */
    void  setPackageModel(JavaPackageModel packageModel)
    { packageModel_=packageModel; 
      typeModel_=null;
      topLevelBlockOwnerModel_=null;
      statementModel_=null;
    }
    
    /**
     * get type model, inside which we situated.
     */
    public  JavaTypeModel  getTypeModel()
    { return typeModel_; 
    }
    
    /**
     *set type model, inside which we situated.
     *package model is set to package of this type, blockOwner and statements
     *models are erased.
     */
    void  setTypeModel(JavaTypeModel typeModel)
    { typeModel_=typeModel; 
      packageModel_=typeModel.getPackageModel();
      topLevelBlockOwnerModel_=null;
      statementModel_=null;
    }
    
    /**
     * get top-level block, in which we situated or null if one 
     *was not defined.
     */
    public  JavaTopLevelBlockOwnerModel  getTopLeveBlockOwnerModel()
    {
      return topLevelBlockOwnerModel_;
    }
    
    /**
     *set topLevelBlockModel. typeModel and packageModel will be setted appropriatively.
     *statement model is cleared.
     */
    void setTopLevelBlockOwnerModel(JavaTopLevelBlockOwnerModel topLevelBlockOwnerModel)
    {       
      topLevelBlockOwnerModel_=topLevelBlockOwnerModel;  
      typeModel_=topLevelBlockOwnerModel.getTypeModel();
      packageModel_=typeModel_.getPackageModel();
    }
    
    /**
     * return statement model, in which we situated or null 
     *if we outside statement
     */
    public JavaStatementModel getStatementModel()
    {
      return statementModel_;  
    }
    
    void setStatementModel(JavaStatementModel statementModel)
    {
        statementModel_=statementModel;
        topLevelBlockOwnerModel_=statementModel.getTopLevelBlockModel().getOwnerModel();
        typeModel_=topLevelBlockOwnerModel_.getTypeModel();
        packageModel_=typeModel_.getPackageModel();        
    }
    
    private JavaPackageModel  packageModel_=null;
    private JavaTypeModel     typeModel_=null;
    private JavaTopLevelBlockOwnerModel   topLevelBlockOwnerModel_=null;
    private JavaStatementModel       statementModel_=null;
    
    
    
}
