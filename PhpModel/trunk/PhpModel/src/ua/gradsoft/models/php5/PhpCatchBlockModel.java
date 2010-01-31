
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for catch block
 */
public class PhpCatchBlockModel implements PhpElementModel
{

    public PhpCatchBlockModel(Term t, PhpCompileEnvironment pce)
                                                       throws TermWareException
    {
      exceptionClassName = t.getSubtermAt(0).getSubtermAt(0).getString();
      exceptionVariable = new PhpVariableModel(t.getSubtermAt(1),pce);
      statement = PhpStatementModel.create(t.getSubtermAt(2), pce);
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[3];
        body[0]=PhpTermUtils.createIdentifier(exceptionClassName);
        body[1]=exceptionVariable.getTerm(pee);
        body[2]=statement.getTerm(pee);
        return PhpTermUtils.createContextTerm("CatchBlock",body,this);
    }





    public PhpStatementModel getStatement()
    { return statement; }
    
    public String getExceptionClassName()
    { return exceptionClassName; }
    
    public PhpVariableModel getExceptionVariable()
    { return exceptionVariable; }

    private PhpStatementModel statement;
    private String            exceptionClassName;
    private PhpVariableModel  exceptionVariable;
}
