
package ua.gradsoft.models.php5;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Labeled statements: just holder for case labed and list of statements.
 * @author rssh
 */
public class PhpLabeledStatementsModel {

    
    public PhpLabeledStatementsModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
      expression = PhpExpressionModelHelper.create(t.getSubtermAt(0), pce);
      statements = new LinkedList<PhpStatementModel>();
      Term c = t.getSubtermAt(1);
      while(!c.isNil()) {
          Term ct = c.getSubtermAt(0);
          c=c.getSubtermAt(1);
          statements.add(PhpStatementModel.create(ct, pce));
      }
    }

    public PhpExpressionModel getExpression()
    { return expression; }

    public List<PhpStatementModel> getStatements()
    { return statements; }

    private PhpExpressionModel expression;
    private List<PhpStatementModel>  statements;
}
