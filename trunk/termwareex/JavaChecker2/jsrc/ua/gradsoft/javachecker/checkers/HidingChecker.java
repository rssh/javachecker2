/*
 * HidingChecker.java
 *
 * Created on четвер, 26, лютого 2004, 2:42
 */

package ua.gradsoft.javachecker.checkers;

import java.util.Set;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.termware.ITermRewritingStrategy;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;


/**
 *Check for hiding
 * @author  Ruslan Shevchenko
 */
public class HidingChecker {
    
    /** Creates a new instance of HidingChecker */
    public HidingChecker() throws TermWareException
    {
        facts_=new HidingCheckFacts(Main.getFacts().getEnv(), NOT_SET);
        ITermRewritingStrategy strategy=TermWare.getInstance().createStrategyByName("BottomTop");
        sys_=new TermSystem(strategy,facts_,TermWare.getInstance()); 
        sys_.addRule("java_variable_declarator($name,$initializer) [ isTargetName($name) ] -> PROBLEM [ hidingDiscovered($name) ]");
    }
    
    public synchronized boolean check(Term t, Set<String> setOfNames,int type) throws TermWareException
    {
      facts_.setHideType(type);
      facts_.setTargetNames(setOfNames);
      Term r=sys_.reduce(t);
      return facts_.getNHides()==0;
    }
    
    public final static int HIDING_OF_FORMAL_PARAMETER = 0;
    public final static int HIDING_OF_CLASS_FIELD = 1;
    public final static int NOT_SET = 1;
    
    
    private HidingCheckFacts facts_;
    private TermSystem sys_;
    
}
