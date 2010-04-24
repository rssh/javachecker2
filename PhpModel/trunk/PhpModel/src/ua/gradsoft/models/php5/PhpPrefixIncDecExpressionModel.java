
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Prefix IncDecExpression
 * @author rssh
 */
public class PhpPrefixIncDecExpressionModel extends PhpUnaryExpressionModel
{

    public PhpPrefixIncDecExpressionModel(Term t, PhpCompileEnvironment pce) throws TermWareException {
        super(t, pce);
    }

}
