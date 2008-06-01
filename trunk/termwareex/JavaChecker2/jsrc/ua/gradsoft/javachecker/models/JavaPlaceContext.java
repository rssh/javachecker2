/*
 * JavaPlaceContext.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.FileAndLine;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

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
    { 
        return typeModel_; 
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

    /**
     * return term statement model, in which we situated or null 
     *if we outside statement or statement model is not term
     */
    public JavaTermStatementModel getTermStatementModel()
    {
      if (statementModel_!=null) {
          if (statementModel_ instanceof JavaTermStatementModel) {
              return (JavaTermStatementModel)statementModel_;
          }
      }
      return null;
    }
    
    
    void setStatementModel(JavaStatementModel statementModel)
    {
        statementModel_=statementModel;
        topLevelBlockOwnerModel_=statementModel.getTopLevelBlockModel().getOwnerModel();
        typeModel_=topLevelBlockOwnerModel_.getTypeModel();
        packageModel_=typeModel_.getPackageModel();        
    }
    
    public JavaExpressionModel  getExpressionModel()
    {
        return expressionModel_;
    }
    
    void  setExpressionModel(JavaExpressionModel expressionModel)
    {
       expressionModel_=expressionModel;
       statementModel_=expressionModel.getStatementModel();
       if (statementModel_!=null) {
         topLevelBlockOwnerModel_=statementModel_.getTopLevelBlockModel().getOwnerModel();
       }
       typeModel_=expressionModel.getEnclosedType();
       if (typeModel_!=null) {
         packageModel_=typeModel_.getPackageModel();
       }
    }
    
    //
    //  API, which is used from Model rules.
    //
    
    public JavaTypeModel  resolveTypeTerm(Term typeTerm) throws TermWareException, EntityNotFoundException
    {      
        return JavaResolver.resolveTypeTerm(typeTerm,this);
    }
    
    public JavaTypeModel  resolveFullClassName(String typeName) throws TermWareException, EntityNotFoundException
    {
        return JavaResolver.resolveTypeModelByFullClassName(typeName);
    }
    
    public boolean subtypeOrSame(JavaTypeModel x,JavaTypeModel y) throws TermWareException, EntityNotFoundException
    {
        //System.err.println("JavaPlaceContext.subtypeOrSame("+x.getFullName()+","+y.getFullName()+")");
        return JavaTypeModelHelper.subtypeOrSame(x,y);
    }
    
    public boolean sameTypes(JavaTypeModel x, JavaTypeModel y) throws TermWareException
    {
        return JavaTypeModelHelper.same(x,y);
    }
    
    public  FileAndLine  getFileAndLine() 
    {
      FileAndLine retval=null;  
      try {          
        if (expressionModel_!=null) {
            if (expressionModel_ instanceof JavaTermExpressionModel) {
                JavaTermExpressionModel termExpressionModel = (JavaTermExpressionModel)expressionModel_;
                Term t = termExpressionModel.getTerm();
                retval = JUtils.getFileAndLine(t);
            }
        }
        if (retval==null) {
            if (statementModel_!=null) {
                if (statementModel_ instanceof JavaTermStatementModel) {
                    JavaTermStatementModel termStatementModel = (JavaTermStatementModel)statementModel_;
                    Term t = termStatementModel.getTerm();
                    retval = JUtils.getFileAndLine(t);
                }
            }
        }
        if (retval==null) {
            if (topLevelBlockOwnerModel_!=null) {
                if (topLevelBlockOwnerModel_ instanceof JavaTermTopLevelBlockOwnerModel) {
                    JavaTermTopLevelBlockOwnerModel ttbom = (JavaTermTopLevelBlockOwnerModel)topLevelBlockOwnerModel_;
                    Term t = ttbom.getTermTypeAbstractModel().getTerm();
                    retval = JUtils.getFileAndLine(t);
                }
            }
        }
        if (retval==null) {
            if (typeModel_!=null) {
                if (typeModel_ instanceof JavaTermTypeAbstractModel) {
                    JavaTermTypeAbstractModel termTypeModel = (JavaTermTypeAbstractModel)typeModel_;
                    Term t = termTypeModel.getTerm();
                    retval = JUtils.getFileAndLine(t);
                }
            }
        }
        if (retval==null) {
            // :((
            retval=FileAndLine.UNKNOWN;
        }
      }catch(TermWareException ex){
        ex.printStackTrace();
        retval=FileAndLine.UNKNOWN;  
      }
      return retval;
    }
    
    private JavaPackageModel  packageModel_=null;
    private JavaTypeModel     typeModel_=null;
    private JavaTopLevelBlockOwnerModel   topLevelBlockOwnerModel_=null;
    private JavaStatementModel       statementModel_=null;
    private JavaExpressionModel      expressionModel_=null;
    
    
    
}
