
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpVariableModel implements PhpElementModel
{

    public PhpVariableModel(Term t, PhpCompileEnvironment pee)
    {
      nDollars = t.getSubtermAt(0).getInt();
      identifier = t.getSubtermAt(1).getString();
    }


    public String getLastVarname(PhpEvalEnvironment pee)
    {
       if (nDollars==1) {
           return identifier;
       } else {
           String s = identifier;
           do {
               s=pee.evalVariable(s).toString();
               --nDollars;
           } while (nDollars > 1);
           return s;
       }
    }

    @Override
    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        body[0] = PhpTermUtils.createInt(nDollars);
        body[1] = PhpTermUtils.createString(identifier);
        return PhpTermUtils.createContextTerm("Value", body, this);
    }



    private int    nDollars;
    private String identifier;
}
