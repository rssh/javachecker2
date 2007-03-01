/*
 * JavaTermTypeExpressionModel.java
 *
 * Created on 20 Февраль 2007 г., 2:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;

/**
 *Expression, which means type
 * @author RSSH
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
