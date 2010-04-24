
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for continue statement
 * @author rssh
 */
public class PhpContinueStatementModel extends PhpStatementModel
{
    
    public static PhpContinueStatementModel create(Term t, PhpCompileEnvironment pce) 
                                                                       throws TermWareException
    {
      return new PhpContinueStatementModel(t,pce);     
    }

    public PhpContinueStatementModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
       if (t.getSubtermAt(0).isNil()) {
           n=1;
       } else if (t.getSubtermAt(0).isInt()) {
           n=t.getSubtermAt(0).getInt();
       } else {
           throw new AssertException("Argument of continue statement must be int");
       }
    }


    @Override
    public void eval(PhpEvalEnvironment env) {
        if (n>0) {
            env.setBreakOrContinueDepth(n);
            env.setEvalState(EvalState.CONTINUE);
        }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[1];
        body[0] = PhpTermUtils.getTermFactory().createInt(n);
        Term retval = PhpTermUtils.getTermFactory().createTerm("ContinueStatement", body);
        retval = TermHelper.setAttribute(retval, "ctx", PhpTermUtils.createJTerm(this));
        return retval;
    }

    int n;
}
