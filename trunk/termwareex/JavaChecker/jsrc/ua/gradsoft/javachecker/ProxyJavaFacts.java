/*
 * ProxyJavaFacts.java
 *
 * Created on четвер, 19, лютого 2004, 22:26
 */

package ua.gradsoft.javachecker;

import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.IEnv;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;


/**
 *Proxy facts wich provide facts access to JavaFacts singleton.
 * @author  Ruslan Shevchenko
 */
public class ProxyJavaFacts extends DefaultFacts
{
    
    /** Creates a new instance of ProxyJavaFacts */
    public ProxyJavaFacts(IEnv env) throws TermWareException
    {
        setEnv(env);      
    }
    
     public boolean violationDiscovered(String name,String message,Term partOfCode) throws TermWareException
    {
        return Main.getFacts().violationDiscovered(name, message, partOfCode);
    }
    
    public boolean isCheckEnabled(String name)
    {
        return Main.getFacts().isCheckEnabled(name);
    }
    
           
    
}
