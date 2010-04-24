package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Expression for bitwise and
 * @author rssh
 */
public class PhpBitwiseAndExpressionModel extends  PhpBinaryExpressionModel
{

   public PhpBitwiseAndExpressionModel(Term t, PhpCompileEnvironment pce) throws TermWareException {
        super(t.getSubtermAt(0), t.getSubtermAt(2), PhpBinaryOperator.BITWISE_AND, pce);
    }


}
