
package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaNullTypeModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.models.expressions.JavaTermNullLiteralExpressionModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Trace Model for 'null' object.
 * @author rssh
 */
public class JavaTraceNullModel implements JavaTraceObjectModel
{

    public JavaTraceNullModel() {}

    public JavaTraceNullModel(JavaTermExpressionModel nullLiteral)
    {
        nullExpression_=nullLiteral;
    }

    public JavaExpressionModel getExpressionModel()
    {
        if (nullExpression_==null) {
          try {
            nullExpression_= new JavaTermNullLiteralExpressionModel(
                                   TermUtils.createTerm("NullLiteral"),null,null);
          }catch(TermWareException ex){
              throw new TermWareRuntimeException(ex);
          }
        }
        return nullExpression_;
    }

    public JavaTraceObjectModel getField(String name) throws TermWareException, EntityNotFoundException, EvaluationException {
        throw new InvokedEvaluationException(new NullPointerException());
    }

    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException {
        return JavaNullTypeModel.INSTANCE;
    }

    public boolean hasFields() throws TermWareException, EntityNotFoundException {
        return false;
    }

    public void setField(String name, JavaTraceObjectModel value) throws TermWareException, EntityNotFoundException, EvaluationException {
        throw new InvokedEvaluationException(new NullPointerException());
    }

    private JavaTermExpressionModel nullExpression_=null;

}
