
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for PHP additive expressions.
 * @author rssh
 */
public class PhpAdditiveExpressionModel extends PhpBinaryExpressionModel
{


    public PhpAdditiveExpressionModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
      super(t.getSubtermAt(0),t.getSubtermAt(2), PhpBinaryOperator.findOp(t.getSubtermAt(1).getString()) , pce);
      if (op==null) {
          throw new InvalidPhpTermExpression("Unknown additive operatpr: "+t.getSubtermAt(1),t);
      }
    }


}
