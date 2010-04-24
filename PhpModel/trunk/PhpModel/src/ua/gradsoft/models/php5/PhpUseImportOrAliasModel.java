
package ua.gradsoft.models.php5;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpUseImportOrAliasModel implements PhpElementModel
{

    public PhpUseImportOrAliasModel(Term t, PhpCompileEnvironment pce)
    {
     Term sQualifiedName = t.getSubtermAt(0);
     Term l = sQualifiedName.getSubtermAt(0);
     qualifiedName = new LinkedList<String>();
     while(! l.isNil() ) {
         Term ct = l.getSubtermAt(0);
         l=l.getSubtermAt(1);
         qualifiedName.add(ct.getSubtermAt(0).getString());
     }
     if (t.getSubtermAt(1).isNil()) {
         alias=null;
     }else{
         alias=t.getSubtermAt(1).getSubtermAt(0).getString();
     }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    private List<String> qualifiedName;
    private String alias;
}
