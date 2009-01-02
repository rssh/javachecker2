/*
 * AbstractCompilationUnitChecker.java
 *
 * $Id: AbstractCompilationUnitChecker.java,v 1.1 2007-04-22 00:53:43 rssh Exp $
 */

package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.util.Holder;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Checker accross compilation unit
 * @author rssh
 */
public abstract class AbstractCompilationUnitChecker extends AbstractChecker
{
  
     public AbstractCompilationUnitChecker(String name, 
                    String category,
                    String description,                                    
                    boolean  enabled)
    {
       super(name,category,description,enabled); 
    }
    
    public abstract void run(Term astTerm) throws TermWareException;
    
  
}
