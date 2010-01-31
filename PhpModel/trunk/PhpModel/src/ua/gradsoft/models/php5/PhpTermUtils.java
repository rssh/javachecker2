
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *Static class with utilities for work with terms
 * @author rssh
 */
public class PhpTermUtils {

    public static TermFactory getTermFactory()
    {
      return TermWare.getInstance().getTermFactory();  
    }

    public static Term createNil()
    {
      return TermWare.getInstance().getTermFactory().createNil();  
    }

    public static Term createIdentifier(String name) throws TermWareException
    {
        return getTermFactory().createTerm("Identifier",name);
    }

    public static Term createJTerm(Object o) throws TermWareException
    {
        return getTermFactory().createJTerm(o);
    }

}
