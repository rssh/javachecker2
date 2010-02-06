
package ua.gradsoft.models.php5;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpUseStatementModel extends PhpStatementModel
{

    public PhpUseStatementModel(Term t, PhpCompileEnvironment pce)
    {
       importsOrAliases = new LinkedList<PhpUseImportOrAliasModel>();
       Term l = t.getSubtermAt(0);
       while(!l.isNil()) {
           Term ct = new PhpUseImportorAliasModel(l.getSubtermAt(0),pce);
           l=l.getSubtermAt(1);
       }
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        for(PhpUseImportOrAliasModel importOrAlias: importOrAliases) {
            importOrAlias.eval(env);
        }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        body[0] = PhpTermUtils.createList(importsOrAliases, pee);
        body[1] = PhpTermUtils.createEndOfStamement();
        return PhpTermUtils.createContextTerm("UseStatement",body,this);
    }



    private List<PhpUseImportOrAliasModel> importsOrAliases;
}
