package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.models.expressions.JavaTermCastExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermIntegerLiteralExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Trace model for 'short' primitives
 * @author rssh
 */
public class JavaShortTraceModel extends JavaPrimitiveTraceModel
{

    public JavaShortTraceModel(short value)
    {
      super(JavaPrimitiveTypeModel.SHORT);
      value_=value;
    }

    public JavaExpressionModel getExpressionModel()
    {
       if (expression_==null) {
         try {
            JavaTermExpressionModel intLiteral = new JavaTermIntegerLiteralExpressionModel(value_,null,null);
            Term atomTerm = TermUtils.createAtom("short");
            Term castTerm = TermUtils.createTerm("CastExpression",atomTerm,intLiteral.getTerm());
            expression_ = new JavaTermCastExpressionModel(castTerm,null,null);
         }catch(TermWareException ex){
            throw new TermWareRuntimeException(ex);
         }
       }
       return expression_;
    }

    public short getValue()
    { return value_; }

    public void setValue(short value)
    {
      value_=value;
      expression_=null;
    }


    private short value_;
    private JavaTermCastExpressionModel expression_=null;


}
