package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermIntegerLiteralExpressionModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Trace Model for long
 * @author rssh
 */
public class JavaLongTraceModel extends JavaPrimitiveTraceModel
{

    public JavaLongTraceModel(long value)
    {
      super(JavaPrimitiveTypeModel.LONG);
      value_=value;
    }

    public JavaExpressionModel getExpressionModel()
    {
       if (expression_==null) {
         try {
            expression_ = new JavaTermIntegerLiteralExpressionModel(value_,null,null);
         }catch(TermWareException ex){
            throw new TermWareRuntimeException(ex);
         }
       }
       return expression_;
    }

    public long getValue()
    { return value_; }

    public void setValue(long value)
    {
      value_=value;
      expression_=null;
    }

    private long value_;
    private JavaTermIntegerLiteralExpressionModel expression_=null;

}
