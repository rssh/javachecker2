
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for PHP Conditional Expression
 * @author rssh
 */
public class PhpConditionalExpressionModel implements PhpExpressionModel
{

    public PhpConditionalExpressionModel(Term t, PhpCompileEnvironment pce)
                                                     throws TermWareException
    {
        Term tcondition = t.getSubtermAt(0);
        Term tIfTrue = t.getSubtermAt(1);
        Term tIfFalse = t.getSubtermAt(2);
        condition = PhpExpressionModelHelper.create(tcondition, pce);
        ifTrue = PhpExpressionModelHelper.create(tIfTrue, pce);
        ifFalse = PhpExpressionModelHelper.create(tIfFalse, pce);
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        PhpValueModel m = condition.eval(php);
        if (php.getEvalState()==EvalState.OK) {
            if (m.getBoolean()) {
                return ifTrue.eval(php);
            } else {
                return ifFalse.eval(php);
            }
        }else{
            return PhpNullValueModel.INSTANCE;
        }
    }

    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isReference(PhpEvalEnvironment php) {
        return false;
    }

    public String getIdentifierName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isIdentifier() {
        return false;
    }



    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[3];
        body[0] = condition.getTerm(pee);
        body[1] = ifTrue.getTerm(pee);
        body[2] = ifFalse.getTerm(pee);
        return PhpTermUtils.createContextTerm("ConditionalExpression", body, this);
    }


    private PhpExpressionModel condition;
    private PhpExpressionModel ifTrue;
    private PhpExpressionModel ifFalse;


}
