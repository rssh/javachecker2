package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpReturnStatementModel extends PhpStatementModel
{

    public static PhpReturnStatementModel create(Term t, PhpCompileEnvironment pce)
    {
        return new PhpReturnStatementModel(t, pce);
    }

    public PhpReturnStatementModel(Term t, PhpCompileEnvironment pce)
    {
       if (t.getSubtermAt(0).isNil()) {
           expression=null;
       } else {
           expression = PhpExpressionModelHelper.create(t, pce);
       }
    }

    @Override
    public void compile(PhpCompileEnvironment env) {

    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        if (expression!=null) {
            PhpValueModel m = expression.eval(env);
            switch(env.getEvalState()) {
                case OK:
                    env.pushLastReturn(m);
                    break;
                case CONTINUE:
                case BREAK:
                    throw new PhpEvalException("continue or break outside loop");
                default:
                    return;
            }
        }else{
            env.pushLastReturn(PhpNullValueModel.INSTANCE);
        }
        env.setEvalState(EvalState.RETURN);
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        if (expression==null) {
            body[0]=PhpTermUtils.createNil();
        } else {
            body[0]=expression.getTerm(pee);
        }
        body[1]=PhpTermUtils.getTermFactory().createTerm("EndOfStatement", new Term[0]);
        Term retval = PhpTermUtils.getTermFactory().createTerm("ReturnStatement", body);
        retval=TermHelper.setAttribute(retval, "ctx", PhpTermUtils.createJTerm(this));
        return retval;
    }





    private PhpExpressionModel expression;
}
