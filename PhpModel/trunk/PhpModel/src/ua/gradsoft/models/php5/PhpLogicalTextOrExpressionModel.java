
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author rssh
 */
public class PhpLogicalTextOrExpressionModel extends PhpBinaryExpressionModel
{

    public PhpLogicalTextOrExpressionModel(Term t, PhpCompileEnvironment pce)
                              throws TermWareException
    {
      super(t,pce);
    }

    public PhpValueModel eval(PhpEvalEnvironment pee) {
        return op.doOp(frs, snd, pee);
    }


}
