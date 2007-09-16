/*
 * JavaTermConditionalExpressionModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaNullTypeModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *ConditionelExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermConditionalExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermConditionalExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      condition_=JavaTermExpressionModel.create(t.getSubtermAt(0),st,enclosedType);
      ifTrue_=JavaTermExpressionModel.create(t.getSubtermAt(1),st,enclosedType);
      ifFalse_=JavaTermExpressionModel.create(t.getSubtermAt(2),st,enclosedType);
    }

    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.CONDITIONAL; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
       if (ifTrue_.getType().equals(JavaNullTypeModel.INSTANCE)) {
           return ifFalse_.getType();
       }else{
           return ifTrue_.getType();
       }
    }
    
    public boolean isType()
    { return false; }
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
       List<JavaExpressionModel> retval = new LinkedList<JavaExpressionModel>(); 
       retval.add(condition_);
       retval.add(ifTrue_);
       retval.add(ifFalse_);
       return retval;
    }
    
    /**
     * ConditionalExpressionModel(condition,ifTrue,ifFalse,tctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term x = condition_.getModelTerm();
      Term y = ifTrue_.getModelTerm();
      Term z = ifFalse_.getModelTerm();
      Term tctx = TermUtils.createJTerm(createPlaceContext());
      Term retval = TermUtils.createTerm("ConditionalExpressionModel",x,y,z,tctx);
      return retval;
    }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
      return condition_.isConstantExpression() && ifTrue_.isConstantExpression() && ifFalse_.isConstantExpression();
    }
    
    
    private JavaTermExpressionModel condition_;
    private JavaTermExpressionModel ifTrue_;
    private JavaTermExpressionModel ifFalse_;    
}
