/*
 * EmptyCatchClauseChecker.java
 *
 * Created on середа, 18, лютого 2004, 13:25
 */

package ua.kiev.gradsoft.JavaChecker;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;


/**
 *Checker for Overloaded Equals
 * @author  Ruslan Shevchenko
 */
public class OverloadedEqualsChecker {
    
    public OverloadedEqualsChecker(JavaFacts facts) throws TermWareException
    {
        sys_=TermWareSingleton.getRoot().resolveSystem("OverloadedEquals"); 
        if (facts.isDebugMode()) {
          sys_.setDebugMode(true);
          sys_.setDebugEntity("All");
        }
    }
    
    
    public void checkCompilationUnit(ITerm compilationUnit) throws TermWareException
    {
      if (Main.showFiles()) {
         System.out.println("check file:"+TermHelper.getAttribute(JUtils.findMarkedIdentifier(compilationUnit), "file").getString());
      }
      ITerm t=sys_.reduce(compilationUnit);
    }

   
    private ITermSystem sys_;
  
}
