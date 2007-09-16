/*
 * JavaTermFloatingPointLiteralExpressionModel.java
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
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for floating point literal expression
 * @author Ruslan Shevchenko
 */
public class JavaTermFloatingPointLiteralExpressionModel extends JavaTermExpressionModel implements JavaLiteralModel, JavaObjectConstantExpressionModel
{

   public JavaTermFloatingPointLiteralExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.FLOATING_POINT_LITERAL;
    }
    
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      Term ct = t_.getSubtermAt(0);
      if (ct.isFloat()) {
          return JavaPrimitiveTypeModel.FLOAT;
      }else if(ct.isDouble()){
          return JavaPrimitiveTypeModel.DOUBLE;
      }else if(ct.isString()){
         return JavaExpressionHelper.getFloatingPointLiteralType(t_.getSubtermAt(0).getString());
      }else{
          throw new AssertException("invalid kind of FloatingPointLiteral:"+ct.getPrimaryType1());
      }
    }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
        return Collections.emptyList();
    }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
      return true;
    }
    
    public Object getConstant() throws TermWareException, EntityNotFoundException
    {
       Term ct = t_.getSubtermAt(0);
       if (ct.isDouble()) {
           return ct.getDouble();
       }else if (ct.isFloat()) {
           return ct.getFloat();
       }else{
           throw new AssertException("Impossible type for FloatingPointLiteral:"+ct);
       }
    }
    
    /**
     * FloatingPointLiteral(Double|Float)
     */
    public Term getModelTerm()
    { return getTerm(); }
    
    public String getString()
    { 
      Term ct = t_.getSubtermAt(0);
      if (ct.isDouble()) {
          return Double.toString(ct.getDouble());
      }else if (ct.isFloat()) {
          return Float.toString(ct.getFloat())+'F';
      }else{
          return TermHelper.termToString(ct);
      }
    }
                
    
    
}
