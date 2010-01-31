
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;

/**
 *Model for Expression
 * @author rssh
 */
public class PhpExpressionStatementModel extends PhpStatementModel
{


    public static PhpExpressionStatementModel create(Term t, PhpCompileEnvironment pce)
    {
      return new PhpExpressionStatementModel(t,pce);
    }

    public PhpExpressionStatementModel(Term t, PhpCompileEnvironment pce) {
        term=t;
        expression=PhpExpressionModel.create(t.getSubtermAt(0),pce);
    }




    @Override
    public void compile(PhpCompileEnvironment env) {
        /* do nothing */
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        PhpValueModel r = expression.eval(env);
        /* do nothing with r */
    }

    private PhpExpressionModel expression;
    private Term               term;
}
