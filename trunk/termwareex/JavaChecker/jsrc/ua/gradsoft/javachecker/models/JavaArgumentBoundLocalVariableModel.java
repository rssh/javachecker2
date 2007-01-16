/*
 * JavaArgumentBoundLocalVariableModel.java
 *
 * Created on п'€тниц€, 12, с≥чн€ 2007, 19:51
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

/**
 *Local variable with type substitutions.
 * @author Ruslan Shevchenko
 */
public class JavaArgumentBoundLocalVariableModel implements JavaLocalVariableModel
{
    
    public JavaArgumentBoundLocalVariableModel(JavaLocalVariableModel origin,
                                               JavaArgumentBoundStatementModel statement
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
    
    public JavaTypeModel  getTypeModel()
    { 
        return statement_.getArgumentBoundTopLevelBlockModel().getSubstitution().substitute(origin_.getTypeModel());
    }
    
        
    public JavaStatementModel getStatement()
    { return statement_; }
    
    public boolean isForHead()
    { return origin_.isForHead(); }

    private JavaLocalVariableModel origin_;
    private JavaArgumentBoundStatementModel statement_;
    
}
