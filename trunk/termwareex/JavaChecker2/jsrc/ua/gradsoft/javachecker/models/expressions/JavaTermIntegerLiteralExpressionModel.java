/*
 * JavaTermIntegerLiteralExpressionModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionHelper;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaLiteralModel;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *IntegerLiteral
 * @author Ruslan Shevchenko
 */
public class JavaTermIntegerLiteralExpressionModel extends JavaTermExpressionModel implements JavaLiteralModel, JavaObjectConstantExpressionModel
{
    
   public JavaTermIntegerLiteralExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.INTEGER_LITERAL;
    }
    
    
    public Object getConstant() throws TermWareException
    {        
      Object retval=null; 
      Term ct = t_.getSubtermAt(0);
      if (ct.isLong()) {
          retval = ct.getLong();
      }else {
          retval = ct.getString();
      }
      return retval;
    }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      if (t_.getSubtermAt(0).isLong()) {
          return JavaPrimitiveTypeModel.LONG;
      } else {
          return JavaPrimitiveTypeModel.INT;
      } 
    }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
        return Collections.emptyList();
    }
    
    public boolean isConstantExpression()
    {
        return true;
    }
    
    
    /**
     * IntegerLiteral(Long|Int)
     */
    public Term getModelTerm()
    { return getTerm(); }
    
    
    public String getString()
    { 
        if (t_.getSubtermAt(0).isLong()) {
            return Long.toString(t_.getSubtermAt(0).getLong())+"L";
        }else{
            return Integer.toString(t_.getSubtermAt(0).getInt());
        }
    }
    
    
                
    
}
