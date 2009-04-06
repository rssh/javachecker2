/*
 * JavaTermBooleanLiteralExpressionModel.java
 *
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
import ua.gradsoft.javachecker.models.JavaLiteralModel;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *BooleanLiteral
 * @author Ruslan Shevchenko
 */
public class JavaTermBooleanLiteralExpressionModel  extends JavaTermExpressionModel implements JavaLiteralModel, JavaObjectConstantExpressionModel
{

   public JavaTermBooleanLiteralExpressionModel(boolean b,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       this(TermUtils.createTerm("BooleanLiteral",
               (b ? TermUtils.createString("true"): TermUtils.createString("false"))),
               st,enclosedType);
    }

    
   public JavaTermBooleanLiteralExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.BOOLEAN_LITERAL;
    }    
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return JavaPrimitiveTypeModel.BOOLEAN;
    }
    
    public boolean isType()
    { return false; }
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
        return Collections.emptyList();
    }
    
    /**
     * BooleanLiteral(<string>)
     */
    public Term getModelTerm()
    {
        return t_;
    }
    
    public boolean isConstantExpression()
    {
        return true;
    }
    
    public Object getConstant()
    {
        return new Boolean(getBoolean());
    }

    public String getString()
    { return t_.getSubtermAt(0).getString(); }
    
    public boolean getBoolean()
    {
        return getString().equals("true");
    }

    public static JavaExpressionModel getFalseExpression()
    {
      if (falseExpressionModel_==null) {
        try {
          falseExpressionModel_ = new JavaTermBooleanLiteralExpressionModel(
                    TermUtils.createTerm("BooleanLiteral",TermUtils.createString("false")),
                    null,null
                  );
        }catch(TermWareException e){
            throw new TermWareRuntimeException(e);
        }
      }
      return falseExpressionModel_;
    }

    public static JavaExpressionModel getTrueExpression()
    {
      if (trueExpressionModel_==null) {
        try {
          trueExpressionModel_ = new JavaTermBooleanLiteralExpressionModel(
                    TermUtils.createTerm("BooleanLiteral",TermUtils.createString("true")),
                    null,null
                  );
        }catch(TermWareException e){
            throw new TermWareRuntimeException(e);
        }
      }
      return trueExpressionModel_;
    }



    private static JavaExpressionModel falseExpressionModel_;
    private static JavaExpressionModel trueExpressionModel_;
}
