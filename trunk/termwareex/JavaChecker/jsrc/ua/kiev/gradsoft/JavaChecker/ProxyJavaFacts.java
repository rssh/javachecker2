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
    
     public boolean violationDiscovered(String name,String message,ITerm partOfCode) throws TermWareException
    {
        return Main.getFacts().violationDiscovered(name, message, partOfCode);
    }
    
    public boolean isCheckEnabled(String name)
    {
        return Main.getFacts().isCheckEnabled(name);
    }
    
    
    public boolean synchronizeViolationDiscovered(ITerm varTerm,ITerm synchronizerTerm) throws TermWareException
    { return violationDiscovered("SynchronizeViolations","violation of synchronization for variable "+TermHelper.termToString(varTerm.getSubtermAt(0)),synchronizerTerm); }
    
    
    
}
