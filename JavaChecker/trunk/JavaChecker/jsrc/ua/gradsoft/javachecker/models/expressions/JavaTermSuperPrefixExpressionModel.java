/*
 * JavaTermSuperPrefixExpressionModel.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *SuperExpression 
 * @author Ruslan Shevchenko
 */
public class JavaTermSuperPrefixExpressionModel extends JavaTermExpressionModel
{

   public JavaTermSuperPrefixExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.SUPER_PREFIX;
    }
    
    public boolean isType()
    { return false; }
    
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return enclosedType_.getSuperClass();
    }
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
        return Collections.emptyList();
    }
    
    public boolean isConstantExpression()
    {
        return true;
    }
    
    /**
     * SuperPrefixModel(ctx)
     */
    public Term getModelTerm() throws TermWareException
    {
      Term ctx = TermUtils.createJTerm(createPlaceContext());
      Term retval = TermUtils.createTerm("SuperPrefixExpressionModel",ctx);
      return retval;
    }
    
    
}
