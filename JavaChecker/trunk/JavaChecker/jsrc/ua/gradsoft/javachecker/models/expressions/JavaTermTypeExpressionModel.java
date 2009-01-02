/*
 * JavaTermTypeExpressionModel.java
 *
 */

package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;

/**
 *Expression, which means type
 */
public abstract class JavaTermTypeExpressionModel extends JavaTermExpressionModel
{
    
    /** Creates a new instance of JavaTermTypeExpressionModel */
    public JavaTermTypeExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) 
    {
      super(t,st,enclosedType);  
    }
    
    public boolean isType()
    { return true; }
    
}
