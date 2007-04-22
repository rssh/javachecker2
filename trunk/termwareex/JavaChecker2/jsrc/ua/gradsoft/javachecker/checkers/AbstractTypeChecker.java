/*
 * AbstractTypeChecker.java
 *
 * Created on April 11, 2007, 6:55 PM
 *
 */

package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.javachecker.util.Holder;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Checker for type definition
 * @author rssh
 */
public abstract class AbstractTypeChecker extends AbstractChecker
{
    
    public AbstractTypeChecker(String name, 
                    String category,
                    String description,                                    
                    boolean  enabled)
    {
       super(name,category,description,enabled); 
    }
    
    public abstract void run(JavaTermTypeAbstractModel tm, Holder<Term> astTerm, Holder<Term> modelTerm) throws TermWareException;
    
    
}
