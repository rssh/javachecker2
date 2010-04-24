
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for shift expression
 * @author rssh
 */
public class PhpShiftExpressionModel extends PhpBinaryExpressionModel
{

    public PhpShiftExpressionModel(Term t, PhpCompileEnvironment pce) throws TermWareException {
        super(t.getSubtermAt(0), t.getSubtermAt(2), PhpBinaryOperator.findOp(t.getSubtermAt(1).getString()), pce);
    }


}
