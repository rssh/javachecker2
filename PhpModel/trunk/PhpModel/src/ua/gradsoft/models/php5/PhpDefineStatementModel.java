
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *Models for PHP define statement model
 * @author rssh
 */
public class PhpDefineStatementModel extends PhpStatementModel
{

    public PhpDefineStatementModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
        name=t.getSubtermAt(0).getSubtermAt(0).getString();
        expression=PhpExpressionModelHelper.create(t.getSubtermAt(1), pce);
        if (t.getSubtermAt(2).isNil()) {
            caseInsensitiveExpression=null;
        } else {
            caseInsensitiveExpression=PhpExpressionModelHelper.create(t.getSubtermAt(2), pce);
        }
    }


    @Override
    public void eval(PhpEvalEnvironment env) {
        PhpValueModel v = expression.eval(env);
        if (env.getEvalState()!=EvalState.OK) {
            return;
        }
        boolean caseInsensitive=false;
        if (caseInsensitiveExpression!=null) {
            caseInsensitive = caseInsensitiveExpression.eval(env).getBoolean();
        }
        if (caseInsensitive) {
            env.getCaseInsensitiveConstants().put(name.toUpperCase(),v);
        } else {
            env.getCaseSensitiveConstants().put(name, v);
        }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[3];
        body[0] = PhpTermUtils.createIdentifier(name);
        body[1] = expression.getTerm(pee);
        if (caseInsensitiveExpression==null) {
            body[2] = PhpTermUtils.createNil();
        } else {
            body[2] = caseInsensitiveExpression.getTerm(pee);
        }
        Term retval = PhpTermUtils.getTermFactory().createTerm("DefineStatement",body);
        retval = TermHelper.setAttribute(retval, "ctx", PhpTermUtils.createJTerm(this));
        return retval;
    }





    private String name;
    private PhpExpressionModel expression;
    private PhpExpressionModel caseInsensitiveExpression;

}
