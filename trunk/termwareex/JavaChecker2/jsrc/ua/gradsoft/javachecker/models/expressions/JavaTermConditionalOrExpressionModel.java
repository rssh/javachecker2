/*
 * JavaTermConditionalOrExpressionModel.java
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
import ua.gradsoft.javachecker.models.JavaExpressionModelHelper;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for ConditionalOr
 * @author Ruslan Shevchenko
 */
public class JavaTermConditionalOrExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermConditionalOrExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      Term frs = t.getSubtermAt(0);      
      Term snd = t.getSubtermAt(1);      
      subExpressions_ = new LinkedList<JavaExpressionModel>();      
      subExpressions_.add(JavaTermExpressionModel.create(frs,st,enclosedType));
      subExpressions_.add(JavaTermExpressionModel.create(snd,st,enclosedType));
    }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.CONDITIONAL_OR;  
    }
    
    public JavaTypeModel getType()
    {
      return JavaPrimitiveTypeModel.BOOLEAN;  
    }
    
    public boolean isType()
    { return false; }
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return subExpressions_; }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
        return JavaExpressionModelHelper.subExpressionsAreConstants(this);
    }
    
    /**
     * ConditionalOrExpressionModel(x,y,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
       Term x = subExpressions_.get(0).getModelTerm();
       Term y = subExpressions_.get(1).getModelTerm();
       Term tctx = TermUtils.createJTerm(createPlaceContext());
       Term retval = TermUtils.createTerm("ConditionalOrExpressionModel",x,y,tctx);
       return retval;
    }
    
    
    private List<JavaExpressionModel> subExpressions_;
}
