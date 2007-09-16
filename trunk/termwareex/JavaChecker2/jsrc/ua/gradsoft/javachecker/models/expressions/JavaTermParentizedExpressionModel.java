/*
 * JavaTermParentizedExpressionModel.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.InvalidJavaTermException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaTermParentizedExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermParentizedExpressionModel(Term t, JavaTermStatementModel statement, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,statement,enclosedType); 
      if (t.getName().equals("Expression")||t.getName().equals("StatementExpression")) {
          subExpression_=JavaTermExpressionModel.create(t.getSubtermAt(0),statement,enclosedType);
      }else{
          throw new InvalidJavaTermException("Expexted Expression or StatementExpression ",t);
      }
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.EXPRESSION; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    { return subExpression_.getType(); }
    
    public boolean isType() throws TermWareException, EntityNotFoundException
    { return subExpression_.isType(); }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
        return subExpression_.isConstantExpression();
    }
   
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return Collections.singletonList(subExpression_); }
    
    
    /**
     * model term of subexpression
     */
    public Term getModelTerm()  throws TermWareException, EntityNotFoundException
    { return subExpression_.getModelTerm(); }
    
    
    private JavaExpressionModel subExpression_;

}
