/*
 * part of JavaChecker.
 */

package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermBooleanLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermIntegerLiteralExpressionModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 * Trace Model for boolean primitive
 */
public class JavaBooleanTraceModel extends JavaPrimitiveTraceModel
{

    public JavaBooleanTraceModel(boolean value)
    {
      super(JavaPrimitiveTypeModel.BOOLEAN);
      value_=value;
    }

    public JavaExpressionModel getExpressionModel()
    {
       if (expression_==null) {
         try {
            expression_ = new JavaTermBooleanLiteralExpressionModel(value_,null,null);
         }catch(TermWareException ex){
            throw new TermWareRuntimeException(ex);
         }
       }
       return expression_;
    }

    public boolean getValue()
    { return value_; }

    public void setValue(boolean value)
    {
      if (value_!=value) {
         expression_=null;
      }
      value_=value;
    }


    private boolean value_;
    private JavaTermBooleanLiteralExpressionModel expression_=null;


}
