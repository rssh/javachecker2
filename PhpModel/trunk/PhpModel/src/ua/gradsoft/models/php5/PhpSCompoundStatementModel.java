
package ua.gradsoft.models.php5;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Interface for compound statement
 * @author rssh
 */
public class PhpSCompoundStatementModel extends PhpStatementModel
{

    public static PhpSCompoundStatementModel create(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
      return new PhpSCompoundStatementModel(t, pce);     
    }

    public PhpSCompoundStatementModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
      Term c = t.getSubtermAt(0);
      statements = new LinkedList<PhpStatementModel>();
      while(!c.isNil()) {
          PhpStatementModel st = PhpStatementModel.create(c.getSubtermAt(0), pce);
          statements.add(st);
      }
    }


    @Override
    public void compile(PhpCompileEnvironment env) {
        for(PhpStatementModel st:statements) {
            st.compile(env);
        }
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        for(PhpStatementModel st:statements) {
            st.eval(env);
            if (env.getEvalState()!=EvalState.OK) {
                return;
            }
        }
    }


    //private Term t;
    private List<PhpStatementModel> statements;

}
