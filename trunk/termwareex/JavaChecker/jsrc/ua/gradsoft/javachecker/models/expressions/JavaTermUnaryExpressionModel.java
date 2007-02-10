/*
 * JavaTermUnaryExpressionModel.java
 *
 * Created on вівторок, 6, лютого 2007, 2:43
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *UnaryExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermUnaryExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermUnaryExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);
       subexpression_ =JavaTermExpressionModel.create(t.getSubtermAt(1),st,enclosedType);
       String operator = t.getSubtermAt(0).getString();
       operatorKind_ = JavaUnaryOperatorKind.create(operator);
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.UNARY;
    }
    
    public JavaUnaryOperatorKind getUnaryOperatorKind()
    { return operatorKind_; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return subexpression_.getType(); 
    }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
        JavaExpressionModel e = subexpression_;
        return Collections.singletonList(e);
    }
            
    private JavaTermExpressionModel subexpression_;
    private JavaUnaryOperatorKind   operatorKind_;
}
