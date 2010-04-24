

package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Expression for LogicalAnd
 * @author rssh
 */
public class PhpLogical_And_ExpressionModel extends PhpBinaryExpressionModel
{

    public PhpLogical_And_ExpressionModel(Term t, PhpCompileEnvironment pce) throws TermWareException {
        super(t.getSubtermAt(0), t.getSubtermAt(2), PhpBinaryOperator.LOGICAL_AND, pce);
    }



}
