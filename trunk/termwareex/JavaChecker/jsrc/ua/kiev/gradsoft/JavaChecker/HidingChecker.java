/*
 * HidingChecker.java
 *
 * Created on четвер, 26, лютого 2004, 2:42
 */

package ua.kiev.gradsoft.JavaChecker;

import java.util.*;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Check for hiding
 * @author  Ruslan Shevchenko
 */
public class HidingChecker {
    
    /** Creates a new instance of HidingChecker */
    public HidingChecker() throws TermWareException
    {
        facts_=new HidingCheckFacts(Main.getFacts().getEnv(), NOT_SET);
        ITermRewritingStrategy strategy=TermWareSingleton.createStrategyByName("BottomUp");
        sys_=new ITermSystem(strategy,facts_,facts_.getEnv()); 
        sys_.addRule("java_variable_declarator($name,$initializer) [| isTargetName($name) |] -> PROBLEM // hidingDiscovered($name)");
    }
    
    public synchronized boolean check(ITerm t, Set setOfNames,int type) throws TermWareException
    {
      facts_.setHideType(type);
      facts_.setTargetNames(setOfNames);
      ITerm r=sys_.reduce(t);
      return facts_.getNHides()==0;
    }
    
    public final static int HIDING_OF_FORMAL_PARAMETER = 0;
    public final static int HIDING_OF_CLASS_FIELD = 1;
    public final static int NOT_SET = 1;
    
    
    private HidingCheckFacts facts_;
    private ITermSystem sys_;
    
}
