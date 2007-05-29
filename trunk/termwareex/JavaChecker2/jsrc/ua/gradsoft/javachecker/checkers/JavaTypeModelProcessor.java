/*
 * JavaTypeModelProcessor.java
 *
 */

package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.termware.TermWareException;

/**
 *This is interface, which must be implemented by classes, plugged into JavaChecker as class checkers.
 */
public interface JavaTypeModelProcessor 
{

    public void configure(JavaFacts facts) throws ConfigException, TermWareException;
    
    public void process(JavaTermTypeAbstractModel typeModel,JavaFacts facts) throws TermWareException, EntityNotFoundException;
    
}
