
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for relation expression
 * @author rssh
 */
public class PhpRelationalExpressionModel extends PhpBinaryExpressionModel
{

    public PhpRelationalExpressionModel(Term t, PhpCompileEnvironment pce) throws TermWareException {
        super(t.getSubtermAt(0), t.getSubtermAt(2), PhpBinaryOperator.findOp(t.getSubtermAt(1).getString()), pce);
    }

}
