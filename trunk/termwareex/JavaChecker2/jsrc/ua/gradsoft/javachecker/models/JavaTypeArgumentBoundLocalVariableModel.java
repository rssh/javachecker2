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
import ua.gradsoft.javachecker.attributes.AttributedEntity;
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
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException
    { 
        return statement_.getArgumentBoundTopLevelBlockModel().getSubstitution().substitute(origin_.getType());
    }
        
    public JavaTypeModel  getOwnerType()
    { return origin_.getOwnerType(); }

    public JavaTopLevelBlockOwnerModel getTopLevelBlockOwner()
    { return origin_.getTopLevelBlockOwner(); }
    
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

    
    public JavaModifiersModel getModifiers()
    { return origin_.getModifiers(); }
    
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

    public Term getAttribute(String name) throws TermWareException {
        return origin_.getAttribute(name);
    }

    public void setAttribute(String name, Term value) throws TermWareException {
        origin_.setAttribute(name,value);
    }

    public AttributedEntity getChildAttributes(String childName)
    {
        return null;
    }
    
    private JavaLocalVariableModel origin_;
    private JavaTypeArgumentBoundStatementModel statement_;
    
}
