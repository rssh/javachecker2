
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Constant declaration for php
 * @author rssh
 */
public class PhpConstantDeclarationModel extends PhpStatementModel
{

    public PhpConstantDeclarationModel(Term t, PhpCompileEnvironment pce)
                                                       throws TermWareException
    {
        if (t.getSubtermAt(0).isNil()) {
            visibility = PhpVisibility.PUBLIC;
        } else {
            String vn = t.getSubtermAt(0).getName();
            if (vn.equals("public")) {
                visibility = PhpVisibility.PUBLIC;
            } else if (vn.equals("private")) {
                visibility = PhpVisibility.PRIVATE;
            } else if (vn.equals("public")) {
                visibility = PhpVisibility.PROTECTED;
            } else {
                throw new AssertException("Invalid visibility: "+vn);
            }
        }
        name = t.getSubtermAt(1).getSubtermAt(0).getString();
        expression = PhpExpressionModelHelper.create(t, pce);
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        PhpValueModel v = expression.eval(env);
        if (env.getEvalState()!=EvalState.OK) {
            return;
        }

    }



    public String getName()
    { return name; }

    public PhpVisibility getVisibility()
    { return visibility; }

    public PhpExpressionModel getExpression()
    { return expression; }

    private String name;
    private PhpVisibility visibility;
    private PhpExpressionModel expression;

}
