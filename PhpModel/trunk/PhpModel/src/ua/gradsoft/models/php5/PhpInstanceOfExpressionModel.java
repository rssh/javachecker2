
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpInstanceOfExpressionModel extends PhpBinaryExpressionModel
{

    public PhpInstanceOfExpressionModel(Term t, PhpCompileEnvironment pce) throws TermWareException {
        super(t.getSubtermAt(0),t.getSubtermAt(1),PhpBinaryOperator.INSTANCE_OF,pce);
    }

    @Override
    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        body[0] = frs.getTerm(pee);
        body[1] = snd.getTerm(pee);
        return PhpTermUtils.createContextTerm("InstanceOfExpression", body, this);
    }



}
