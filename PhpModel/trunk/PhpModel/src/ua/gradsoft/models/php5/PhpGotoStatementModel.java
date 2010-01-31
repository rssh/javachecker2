package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for goto statement
 * @author rssh
 */
public class PhpGotoStatementModel extends PhpStatementModel
{

    public PhpGotoStatementModel(Term t, PhpCompileEnvironment pce)
    {
      identifier = t.getSubtermAt(0).getString();
    }

    @Override
    public void compile(PhpCompileEnvironment env) {
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
       env.setGotoIdentifier(identifier); 
       env.setEvalState(EvalState.GOTO);
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        body[0] = PhpTermUtils.getTermFactory().createString(identifier);
        body[1] = PhpTermUtils.getTermFactory().createTerm("EndOfStatement",new Term[0]);
        Term retval = PhpTermUtils.getTermFactory().createTerm("GotoStatement",body);
        retval = TermHelper.setAttribute(retval, "ctx", PhpTermUtils.createJTerm(this));
        return retval;
    }
    
    



    private String identifier;
}
