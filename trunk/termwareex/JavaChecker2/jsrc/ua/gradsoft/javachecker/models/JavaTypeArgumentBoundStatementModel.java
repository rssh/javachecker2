/*
 * JavaTypeArgumentBoundStatementModel.java
 *
 * Created on п'€тниц€, 12, с≥чн€ 2007, 2:11
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaTypeArgumentBoundStatementModel implements JavaStatementModel
{
    
    public JavaTypeArgumentBoundStatementModel(JavaTypeArgumentBoundTopLevelBlockModel blockModel,
                                           JavaStatementModel origin,
                                           JavaTypeArgumentBoundStatementModel parent,
                                           JavaTypeArgumentBoundStatementModel previous
            )
    {
      blockModel_=blockModel;
      origin_=origin;
      substitutedParent_=parent;
      substitutedPrevious_=previous;
    }

        
    public JavaStatementKind  getKind()
    { return origin_.getKind(); }
    
     public JavaTopLevelBlockModel  getTopLevelBlockModel()
     {
         return blockModel_;
     }
     
     public JavaTypeArgumentBoundTopLevelBlockModel getArgumentBoundTopLevelBlockModel()
     {
        return blockModel_; 
     }
    
     public JavaStatementModel getParentStatementModel()
     {
         return substitutedParent_;
     }
    
    
    public JavaStatementModel getPreviousStatementModel()
    {
        return substitutedPrevious_;
    }
    
    public List<JavaStatementModel> getChildStatements()
    {
        if (substitutedChilds_==null) {
            substitutedChilds_=new ArrayList<JavaStatementModel>();
            JavaTypeArgumentBoundStatementModel prevS=null;
            for(JavaStatementModel s : origin_.getChildStatements()) {
                JavaTypeArgumentBoundStatementModel as = new JavaTypeArgumentBoundStatementModel(blockModel_,s,this,prevS);
                substitutedChilds_.add(as);                
                prevS=as;
            }
        }
        return substitutedChilds_;
    }
    
    /**
     *get local variables, defined in this statement.
     *
     */
    public List<JavaLocalVariableModel> getLocalVariables()
    { 
      List<JavaLocalVariableModel> localVariables = origin_.getLocalVariables();
      List<JavaLocalVariableModel> retval = new LinkedList<JavaLocalVariableModel>();
      for(JavaLocalVariableModel v: localVariables) {
          JavaTypeArgumentBoundLocalVariableModel av = new JavaTypeArgumentBoundLocalVariableModel(v,this);
          retval.add(av);
      }
      return retval;
    }
        
    /**
     *get local type, defined in this statements.
     */
    public JavaTypeModel getLocalType()  throws TermWareException
    {
      return blockModel_.getSubstitution().substitute(origin_.getLocalType());  
    }
                
     
    public JavaStatementModel  getOrigin()
    {
      return origin_;  
    }

    public List<JavaExpressionModel>  getExpressions()
    {
      List<JavaExpressionModel> oel = origin_.getExpressions();
      List<JavaExpressionModel> retval = new ArrayList<JavaExpressionModel>(oel.size());
      for(JavaExpressionModel oe:oel) {
          JavaExpressionModel e = new JavaTypeArgumentBoundExpressionModel(oe,this);
          retval.add(e);
      }
      return retval;
    }
    
    /**
     * TypeArgumentBoundStatementModel(originModel,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException 
    {
        Term originModelTerm = origin_.getModelTerm();
        JavaPlaceContext ctx = JavaPlaceContextFactory.createNewStatementContext(this);
        Term tctx=TermUtils.createJTerm(ctx);
        Term retval=TermUtils.createTerm("TypeArgumentBoundStatementModel",originModelTerm,tctx);
        return retval;
    }
    
    private JavaTypeArgumentBoundTopLevelBlockModel blockModel_;
    private JavaStatementModel          origin_;
    private JavaStatementModel          substitutedParent_=null;
    private JavaStatementModel          substitutedPrevious_=null;
    private List<JavaStatementModel> substitutedChilds_=null;
    
}
