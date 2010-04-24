
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpPostfixIncDecExpressionModel extends PhpUnaryExpressionModel
{

    public PhpPostfixIncDecExpressionModel(Term t, PhpCompileEnvironment pce)
                                              throws TermWareException
    {
      super(PhpExpressionModelHelper.create(t.getSubtermAt(0), pce),
            PhpUnaryOperator.findOp(t.getSubtermAt(0).getString()));
    }


    @Override
    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term [] body = new Term[2];
        body[0] = frs.getTerm(pee);
        body[1] = PhpTermUtils.createString(op.getSymbols());
        return PhpTermUtils.createContextTerm("PostficIncDecExpression", body, this);
    }


}
