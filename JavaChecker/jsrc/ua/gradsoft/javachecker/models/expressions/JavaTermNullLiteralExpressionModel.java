/*
 * JavaTermNullLiteralExpressionModel.java
 *
 *
 * Copyright (c) 2006-2009 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaLiteralModel;
import ua.gradsoft.javachecker.models.JavaNullTypeModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *NullLiteral
 * @author Ruslan Shevchenko
 */
public class JavaTermNullLiteralExpressionModel extends JavaTermExpressionModel implements JavaLiteralModel, JavaObjectConstantExpressionModel
{

   public JavaTermNullLiteralExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.NULL_LITERAL;
    }
    
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return JavaNullTypeModel.INSTANCE;
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
    
    
    public Object getConstant()
    { return null; }
    
    /**
     * NullLiteral()
     */
    public Term getModelTerm()
    {
      return t_;  
    }
    
    public String getString()
    { return "null"; }
                

    public static JavaTermNullLiteralExpressionModel  getNull()
    {
      if (null_==null) {
          try {
              null_ = new JavaTermNullLiteralExpressionModel(
                        TermUtils.createTerm("NullLiteral"), null, null
                      );
          }catch(TermWareException ex){
              throw new TermWareRuntimeException(ex);
          }
      }
      return null;    
    }

    private static JavaTermNullLiteralExpressionModel null_=null;
}
