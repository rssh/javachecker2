/*
 * JavaTermThisExpressionModel.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * http://www.gradsoft.ua
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaExpressionModelHelper;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *ThisExpression (i.e. x.this)
 *@see JavaTermThisPrefixExpressionModel
 * @author Ruslan Shevchenko
 */
public class JavaTermThisExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermThisExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      subexpression_=JavaTermExpressionModel.create(t.getSubtermAt(0),st,enclosedType);
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.THIS; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    { return subexpression_.getType(); }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel> getSubExpressions()
    { return Collections.singletonList(subexpression_); }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
        return JavaExpressionModelHelper.subExpressionsAreConstants(this);
    }

    
    
    /**
     * ThisModel(x,ctx)
     */
    public Term getModelTerm() throws  TermWareException, EntityNotFoundException
    {
      Term x = subexpression_.getModelTerm();
      Term ctx = TermUtils.createJTerm(this.createPlaceContext());
      Term retval = TermUtils.createTerm("ThisModel",x,ctx);
      return retval;
    }
    
    
    private JavaExpressionModel  subexpression_;
    
}
