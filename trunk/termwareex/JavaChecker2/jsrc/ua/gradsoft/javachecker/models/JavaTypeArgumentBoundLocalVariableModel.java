/*
 * JavaTypeArgumentBoundLocalVariableModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Local variable with type substitutions.
 * @author Ruslan Shevchenko
 */
public class JavaTypeArgumentBoundLocalVariableModel implements JavaLocalVariableModel
{
    
    public JavaTypeArgumentBoundLocalVariableModel(JavaLocalVariableModel origin,
                                               JavaTypeArgumentBoundStatementModel statement
                                               )
    {
      origin_=origin;  
      statement_=statement;     
    }
    
    public String getName()
    {
      return origin_.getName();  
    }
    
    public JavaVariableKind  getKind()
    {
      return origin_.getKind();  
    }
    
    public JavaTypeModel  getTypeModel() throws TermWareException, EntityNotFoundException
    { 
        return statement_.getArgumentBoundTopLevelBlockModel().getSubstitution().substitute(origin_.getTypeModel());
    }
        
        
    public JavaStatementModel getStatement()
    { return statement_; }
    
    public JavaExpressionModel  getInitExpressionModel()
    {
      JavaExpressionModel e = origin_.getInitExpressionModel();
      return (e==null) ? null : new JavaTypeArgumentBoundExpressionModel(e,statement_);
    }
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap() throws TermWareException
    {
       return origin_.getAnnotationsMap(); 
    }

    
    public JavaModifiersModel getModifiersModel()
    { return origin_.getModifiersModel(); }
    
    /**
     * TypeArgumentBoundLocalVariable(origin,substitution)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
       Term x = origin_.getModelTerm();
       Term y = TermUtils.createJTerm(statement_.getArgumentBoundTopLevelBlockModel().getSubstitution());
       Term retval = TermUtils.createTerm("TypeArgumentBoundLocalVariableModel",x,y);
       return retval;
    }
    
    public boolean isForHead()
    { return origin_.isForHead(); }

    private JavaLocalVariableModel origin_;
    private JavaTypeArgumentBoundStatementModel statement_;
    
}
