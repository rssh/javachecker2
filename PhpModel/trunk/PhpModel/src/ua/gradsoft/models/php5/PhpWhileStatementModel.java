
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for while statement
 */
public class PhpWhileStatementModel extends PhpStatementModel
{

    protected PhpWhileStatementModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
       expression = PhpExpressionModelHelper.create(t.getSubtermAt(0) , pce);
       statement = PhpStatementModel.create(t.getSubtermAt(1), pce);
    }

    public static PhpWhileStatementModel create(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
      return new PhpWhileStatementModel(t,pce);
    }

    @Override
    public void compile(PhpCompileEnvironment env) {
       statement.compile(env);
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        boolean cr = true;
        while(cr) {
            PhpValueModel v = expression.eval(env);
            if (env.getEvalState()!=EvalState.OK) {
                return;
            }
            if (!v.getBoolean()) {
                return;
            }
            statement.eval(env);
            switch(env.getEvalState()) {
                case BREAK:
                {
                    int breakDepth = env.decBreakOrContinueDepth();
                    if (breakDepth>0) {
                        return;
                    } else if (breakDepth==0) {
                        env.setEvalState(EvalState.OK);
                        return;
                    } else {
                        throw new PhpEvalException("breakDepth < 0");
                    }
                }
                case CONTINUE:
                    if (env.decBreakOrContinueDepth()>0) {
                        return;
                    }else{
                        env.setEvalState(EvalState.OK);
                    }
                    break;
                case OK:
                    break;
                default:
                    return;
            }
        }
    }

    public PhpExpressionModel getExpression()
    {
      return expression;
    }

    private PhpExpressionModel expression;
    private PhpStatementModel  statement;

}
