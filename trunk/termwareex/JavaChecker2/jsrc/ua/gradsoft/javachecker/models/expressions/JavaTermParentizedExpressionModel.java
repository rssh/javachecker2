/*
 * JavaTermParentizedExpressionModel.java
 *
 * Created on понеділок, 5, лютого 2007, 6:16
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
 *
 * @author Ruslan Shevchenko
 */
public class JavaTermParentizedExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermParentizedExpressionModel(Term t, JavaTermStatementModel statement, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,statement,enclosedType); 
      if (t.getName().equals("Expression")) {
          subExpression_=JavaTermExpressionModel.create(t.getSubtermAt(0),statement,enclosedType);
      }
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.EXPRESSION; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    { return subExpression_.getType(); }
    
    public boolean isType() throws TermWareException, EntityNotFoundException
    { return subExpression_.isType(); }
    
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return Collections.singletonList(subExpression_); }
    
    private JavaExpressionModel subExpression_;

}
