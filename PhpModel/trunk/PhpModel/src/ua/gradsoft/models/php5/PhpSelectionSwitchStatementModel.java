
package ua.gradsoft.models.php5;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for switch statement.
 * @author rssh
 */
public class PhpSelectionSwitchStatementModel extends PhpStatementModel
{

    public PhpSelectionSwitchStatementModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
        expression = PhpExpressionModelHelper.create(t.getSubtermAt(0), pce);
        caseStatements = new LinkedList<PhpLabeledStatementsModel>();
        Term l = t.getSubtermAt(1).getSubtermAt(0);
        while(!l.isNil()) {
            Term lt = l.getSubtermAt(0);
            l=l.getSubtermAt(1);
            caseStatements.add(new PhpLabeledStatementsModel(lt,pce));
        }
    }

    @Override
    public void compile(PhpCompileEnvironment env) {
        /* do nothing ? */
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        PhpValueModel v = expression.eval(env);
        if (env.getEvalState()!=EvalState.OK) return;
        for(PhpLabeledStatementsModel labeled: caseStatements) {
            PhpValueModel v = labeled.getExpression();

        }
    }


    private PhpExpressionModel expression;
    private List<PhpLabeledStatementsModel> caseStatements;

}
