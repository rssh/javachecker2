package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpGotoLabeledStatementModel extends PhpStatementModel
{


    public static PhpGotoLabeledStatementModel create(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
      return new PhpGotoLabeledStatementModel(t,pce);
    }


    public PhpGotoLabeledStatementModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
      identifier_ = t.getSubtermAt(0).getString();
      statement_ = PhpStatementModel.create(t.getSubtermAt(1), pce);
    }

    @Override
    public void compile(PhpCompileEnvironment env) {
        //env.markIdentifier(identifier,this);
    }


    @Override
    public void eval(PhpEvalEnvironment env) {
        statement_.eval(env);
    }


    private String identifier_;
    private PhpStatementModel statement_;

}
