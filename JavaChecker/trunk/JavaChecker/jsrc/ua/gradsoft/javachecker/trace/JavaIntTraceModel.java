
package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermIntegerLiteralExpressionModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Trace model for 'int'
 * @author rssh
 */
public class JavaIntTraceModel extends JavaPrimitiveTraceModel
{

    public JavaIntTraceModel(int value)
    {
      super(JavaPrimitiveTypeModel.INT);  
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

    public int getValue()
    { return value_; }

    public void setValue(int value)
    {
      value_=value;
      expression_=null;
    }


    private int value_;
    private JavaTermIntegerLiteralExpressionModel expression_=null;
}
