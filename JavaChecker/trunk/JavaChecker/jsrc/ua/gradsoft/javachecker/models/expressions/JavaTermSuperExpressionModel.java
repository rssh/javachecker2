/*
 * JavaTermSuperExpressionModel.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.InvalidJavaTermException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Super(x) = x.super
 * @author Ruslan Shevchenko
 */
public class JavaTermSuperExpressionModel extends JavaTermExpressionModel
{

    public JavaTermSuperExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      subexpression_=JavaTermExpressionModel.create(t.getSubtermAt(0),st,enclosedType);
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.SUPER; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    { 
      try {
        return subexpression_.getType().getSuperClass(); 
      }catch(NotSupportedException ex){
          throw new InvalidJavaTermException("super call of type without superclassing",t_);
      }
    }
    
    public boolean isType()
    { return false; }
    
    /**
     * SuperModel(x,ctx)
     */
    public Term getModelTerm() throws  TermWareException, EntityNotFoundException
    {
      Term x = subexpression_.getModelTerm();
      Term ctx = TermUtils.createJTerm(createPlaceContext());
      Term retval = TermUtils.createTerm("SuperModel",x,ctx);
      return retval;
    }
    
    public List<JavaExpressionModel> getSubExpressions()
    { return Collections.singletonList(subexpression_); }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
        return subexpression_.isConstantExpression();
    }
    
    
    private JavaExpressionModel  subexpression_;
    
    
}
