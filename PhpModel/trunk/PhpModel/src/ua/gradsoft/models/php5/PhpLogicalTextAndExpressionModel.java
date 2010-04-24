
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for logical and
 * @author rssh
 */
public class PhpLogicalTextAndExpressionModel extends PhpBinaryExpressionModel
{

    public PhpLogicalTextAndExpressionModel(Term t, PhpCompileEnvironment pce)
                              throws TermWareException
    {
      super(t.getSubtermAt(0),t.getSubtermAt(2),PhpBinaryOperator.LOGICAL_AND,pce);
    }


    @Override
    public String getName()
    { return "LogicalTextAndExpression"; }


}
