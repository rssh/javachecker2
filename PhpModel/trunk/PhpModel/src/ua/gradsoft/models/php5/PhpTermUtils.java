
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

    public static Term createStringList(List<String> l) throws TermWareException
    {
        TermFactory tf = getTermFactory();
        Term retval = tf.createNil();
        for(String s: l) {
           retval = tf.createTerm("cons",tf.createString(s),retval); 
        }
        retval=TermHelper.reverseList(TermWare.getInstance(), retval);
        return retval;
    }

    public static Term createList(List<? extends PhpElementModel> l, PhpEvalEnvironment pee) throws TermWareException
    {
        TermFactory tf = getTermFactory();
        Term retval = tf.createNil();
        for(PhpElementModel e: l) {
            retval = tf.createTerm("cons",e.getTerm(pee),retval);
        }
        retval=TermHelper.reverseList(TermWare.getInstance(), retval);
        return retval;
    }

    public static Term createList(List<Term> l) throws TermWareException
    {
        TermFactory tf = getTermFactory();
        Term retval = tf.createNil();
        for(Term ct: l) {
            retval = tf.createTerm("cons",ct,retval);
        }
        retval=TermHelper.reverseList(TermWare.getInstance(), retval);
        return retval;
    }


    public static Term createString(String value)
    {
        return getTermFactory().createString(value);
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

    public static Term createInt(int value)
    {
        return getTermFactory().createInt(value);
    }

    public static Term createDouble(double value)
    {
        return getTermFactory().createDouble(value);
    }

    public static Term createAtom(String name)
    {
        return getTermFactory().createAtom(name);
    }

    public static Term createEndOfStatement() throws TermWareException
    {
        return getTermFactory().createTerm("EndOfStatement",new Term[0]);
    }

    public static Term createEndOfStatement(String body) throws TermWareException
    {
        return getTermFactory().createTerm("EndOfStatement",
                                             getTermFactory().createString(body));
    }

    public static Term createTerm(String name, Term t1) throws TermWareException
    {
        return getTermFactory().createTerm(name,t1);
    }

    public static Term createTerm(String name, Term t1, Term t2) throws TermWareException
    {
        return getTermFactory().createTerm(name,t1,t2);
    }

    public static Term createTerm(String name, Term[] body) throws TermWareException
    {
        return getTermFactory().createTerm(name, body);
    }


}
