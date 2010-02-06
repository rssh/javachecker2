
package ua.gradsoft.models.php5;

import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermHelper;
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

    public static Term createList(List<PhpElementModel> l, PhpEvalEnvironment pee) throws TermWareException
    {
        TermFactory tf = getTermFactory();
        Term retval = tf.createNil();
        for(PhpElementModel e: l) {
            retval = tf.createTerm("cons",e.getTerm(pee),retval);
        }
        retval=TermHelper.reverseList(TermWare.getInstance(), retval);
        return retval;
    }

    public static Term createContextTerm(String name, Term[] body, PhpElementModel e) throws TermWareException
    {
        Term retval = getTermFactory().createTerm(name,body);
        retval=TermHelper.setAttribute(retval, "ctx", createJTerm(e));
        return retval;
    }

    public static Term createBoolean(boolean value)
    {
        return getTermFactory().createBoolean(value);
    }

}
