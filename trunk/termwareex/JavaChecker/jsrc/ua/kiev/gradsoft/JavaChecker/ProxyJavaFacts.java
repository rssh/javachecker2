/*
 * ProxyJavaFacts.java
 *
 * Created on четвер, 19, лютого 2004, 22:26
 */

package ua.kiev.gradsoft.JavaChecker;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;
import ua.kiev.gradsoft.TermWare.facts.*;

/**
 *Proxy facts wich provide facts access to JavaFacts singleton.
 * @author  Ruslan Shevchenko
 */
public class ProxyJavaFacts extends DefaultFacts
{
    
    /** Creates a new instance of ProxyJavaFacts */
    public ProxyJavaFacts(IEnv env) throws TermWareException
    {
        super(env);
    }
    
    public boolean isCheckEmptyCatchClauses()
    { return Main.getFacts().isCheckEmptyCatchClauses(); }
    
    public boolean equalsWithoutHashcodeDiscovered(ITerm classnameTerm) throws TermWareException
    { return Main.getFacts().equalsWithoutHashcodeDiscovered(classnameTerm); }
    
    public boolean hashcodeWithoudEqualsDiscovered(ITerm classnameTerm) throws TermWareException
    { return Main.getFacts().hashcodeWithoudEqualsDiscovered(classnameTerm); }
    
    public boolean synchronizeViolationDiscovered(ITerm varTerm,ITerm synchronizerTerm) throws TermWareException
    { return Main.getFacts().synchronizeViolationDiscovered(varTerm,synchronizerTerm); }
    
    
    
}
