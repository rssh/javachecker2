/*
 * ClassReachabilityChecker.java
 *
 * Created on October 2, 2007, 6:29 PM
 *
 */

package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.termware.TermWareException;

/**
 *TODO: implement.
 * @author rssh
 */
public class ClassReachabilityChecker implements JavaTypeModelProcessor
{
    
    
    public void configure(JavaFacts facts) throws ConfigException, TermWareException
    {}
    
    public void process(JavaTermTypeAbstractModel typeModel,JavaFacts facts) throws TermWareException, EntityNotFoundException
    {
        if (isReachable(typeModel,facts)) {
           // markDepended(typeModel);
        }else{
            /* do nothing */
        }
    }

    public boolean hasSecondPass()
    {
        return true;
    }
    
    public void processSecondPass(JavaTermTypeAbstractModel typeModel,JavaFacts facts) throws TermWareException, EntityNotFoundException
    {
        /*
       Term t = typeModel.getAttribute(this.getClass().getName());
       if (t.isBoolean()) {
           boolean v = t.getBoolean();
           if (!b) {               
               // unreachable.              
               facts.violationDiscovered("UnreachableClass","Class is unreachable",typeModel.getASTTerm());
           }
       }
         */
    }
    
    private boolean isReachable(JavaTermTypeAbstractModel typeModel,JavaFacts facts)
    {
    //    facts.getStringConfigValue("")
        return false;
    }
    
    
}
