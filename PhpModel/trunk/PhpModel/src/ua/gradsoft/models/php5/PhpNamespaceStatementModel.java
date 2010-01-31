package ua.gradsoft.models.php5;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Namespace statement model
 * @author rssh
 */
public class PhpNamespaceStatementModel extends PhpStatementModel
{

    public static PhpNamespaceStatementModel create(Term t, PhpCompileEnvironment pce)
    {
       return new  PhpNamespaceStatementModel(t,pce);
    }

    public PhpNamespaceStatementModel(Term t, PhpCompileEnvironment pce)
    {
      currentNamespace = new LinkedList<String>();
      Term l = t.getSubtermAt(0).getSubtermAt(0);
      while(!l.isNil()) {
          currentNamespace.add(l.getSubtermAt(0).getSubtermAt(0).getString());
          l=l.getSubtermAt(1);
      }
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        env.setCurrentNamespace(currentNamespace);
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    private List<String>  currentNamespace;

}
