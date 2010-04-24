package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpDoStatementModel extends PhpStatementModel
{

    public static PhpDoStatementModel create(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
      return new PhpDoStatementModel(t,pce);
    }

    protected PhpDoStatementModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
       statement=PhpStatementModel.create(t.getSubtermAt(0),pce);
       expression=PhpExpressionModelHelper.create(t.getSubtermAt(1), pce);
    }

 

    @Override
    public void eval(PhpEvalEnvironment env) {
        boolean inLoop=true;
        do {
            statement.eval(env);
            switch(env.getEvalState()) {
                case OK:
                    break;
                case BREAK:
                {
                    int dp = env.decBreakOrContinueDepth();
                    if (dp<=0) {
                        env.setEvalState(EvalState.OK);
                        inLoop=false;
                    }else{
                        return;
                    }
                    break;
                }
                case CONTINUE:
                {
                    int dp = env.decBreakOrContinueDepth();
                    if (dp<=0) {
                        env.setEvalState(EvalState.OK);
                    }else{
                        return;
                    }
                }
                    break;
                default:
                    return;
            }
            if (!inLoop) {
                break;
            }
            inLoop = expression.eval(env).getBoolean();
        }while(inLoop);
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[3];
        body[0]=statement.getTerm(pee);
        body[1]=expression.getTerm(pee);
        body[2]=PhpTermUtils.createEndOfStatement();
        return PhpTermUtils.createContextTerm("DoStatement", body, this);
    }



    private PhpExpressionModel expression;
    private PhpStatementModel statement;
}
