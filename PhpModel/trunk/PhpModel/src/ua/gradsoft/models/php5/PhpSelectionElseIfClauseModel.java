
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpSelectionElseIfClauseModel
{

    public PhpSelectionElseIfClauseModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
        expression = PhpExpressionModelHelper.create(t.getSubtermAt(0), pce);
        ifTrueStatement = PhpStatementModel.create(t.getSubtermAt(1), pce);
    }


    public void compile(PhpCompileEnvironment env) {
        ifTrueStatement.compile(env);
    }

    public PhpExpressionModel getCondition()
    { return expression; }

    public PhpStatementModel  getStatement()
    { return ifTrueStatement; }


    private PhpExpressionModel expression;
    private PhpStatementModel ifTrueStatement;
}
