
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for Php throw statement
 * @author rssh
 */
public class PhpThrowStatementModel extends PhpStatementModel
{

    public static PhpThrowStatementModel create(Term t, PhpCompileEnvironment pce)
    {
       return  new PhpThrowStatementModel(t,pce); 
    }

    public PhpThrowStatementModel(Term t, PhpCompileEnvironment pce)
    {
       classInstantiation = PhpExpressionModelHelper.create(t,pce);
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        PhpValueModel v  = classInstantiation.eval(env);
        if (env.getEvalState()!=EvalState.OK) {
            return;
        }
        env.setThrowedException(v);
        env.setEvalState(EvalState.THROW);
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        body[0] = classInstantiation.getTerm(pee);
        body[1] = PhpTermUtils.createEndOfStatement();
        return PhpTermUtils.createContextTerm("ThrowStatement",body,this);
    }


    private PhpExpressionModel classInstantiation;

}
