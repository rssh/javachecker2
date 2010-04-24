package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Expression for closure call
 * @author rssh
 */
public class PhpClosureCallExpressionModel extends PhpMethodCallExpressionModel
{

    

    @Override
    public Term getFirstSubterm(PhpEvalEnvironment pee) throws TermWareException {
        return fun.getTerm(pee);
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        fun.eval(php);
        return php.popLastReturn();
    }


    protected PhpFunctionModel fun;
}
