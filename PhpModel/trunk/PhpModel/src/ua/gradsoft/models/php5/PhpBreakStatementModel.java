
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for break statement
 * @author rssh
 */
public class PhpBreakStatementModel extends PhpStatementModel
{

   public static PhpBreakStatementModel create(Term t, PhpCompileEnvironment pce) 
                                                                       throws TermWareException
    {
      return new PhpBreakStatementModel(t,pce);     
    }

    public PhpBreakStatementModel(Term t, PhpCompileEnvironment pce) throws TermWareException
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
            env.setEvalState(EvalState.BREAK);
        }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[1];
        body[0] = PhpTermUtils.getTermFactory().createInt(n);
        Term retval = PhpTermUtils.getTermFactory().createTerm("BreakStatement", body);
        retval = TermHelper.setAttribute(retval, "ctx", PhpTermUtils.createJTerm(this));
        return retval;
    }

    int n;

}
