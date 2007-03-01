/*
 * JavaTermPreincrementExpressionModel.java
 *
 * Created on вівторок, 6, лютого 2007, 3:11
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
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *PreincrementExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermPreincrementExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermPreincrementExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);
       subexpression_ =JavaTermExpressionModel.create(t.getSubtermAt(0),st,enclosedType);
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.PREINCREMENT;
    }
    
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
    
    /**
     * PreincrementExpressionModel(x,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term x = subexpression_.getModelTerm();
      Term ctx = TermUtils.createJTerm(this.createPlaceContext());
      Term retval = TermUtils.createTerm("PreincrementExpression",x,ctx);
      return retval;
    }
    
    
    private JavaTermExpressionModel subexpression_;
    
    
    
}
