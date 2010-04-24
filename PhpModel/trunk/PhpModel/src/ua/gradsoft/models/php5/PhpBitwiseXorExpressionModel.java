
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *expression model for bitwise xor
 * @author rssh
 */
public class PhpBitwiseXorExpressionModel extends PhpBinaryExpressionModel
{

    public PhpBitwiseXorExpressionModel(Term t, PhpCompileEnvironment pce) throws TermWareException {
        super(t.getSubtermAt(0), t.getSubtermAt(2), PhpBinaryOperator.BITWISE_XOR, pce);
    }


}
