/*
 * JavaTypeModelOnePassProcessor.java
 *
 */

package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Type processor, which do only one pass.
 * @author rssh
 */
public abstract class JavaTypeModelOnePassProcessor implements JavaTypeModelProcessor
{
    
    public boolean hasSecondPass()
    { return false; }
    
    public void processSecondPass(JavaTermTypeAbstractModel typeModel,JavaFacts facts) throws TermWareException, EntityNotFoundException
    {
      throw new AssertException("Second pass is not allowed for "+this.getClass().getName());    
    }

    
}
