/*
 * BTCompilationUnitChecker.java
 *
 * Created on April 11, 2007, 7:24 PM
 *
 */

package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.checkers.CheckerType;
import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.termware.ITermRewritingStrategy;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *Checker with apply ruleset, according to BottomTop strategy.
 * @author rssh
 */
public class BTCompilationUnitChecker extends AbstractCompilationUnitChecker
{
    
 
     
    /**
     * Creates a new instance of BTTypeChecker
     */
    public BTCompilationUnitChecker(String name,String category,String description,Term rules, boolean enabled) throws TermWareException, ConfigException {
        super(name,category,description,enabled);
        ITermRewritingStrategy strategy = TermWare.getInstance().createStrategyByName("BottomTop");
        termSystem_ = new TermSystem(strategy,Main.getFacts(),TermWare.getInstance());
        addRuleset(termSystem_,rules);
    }
    
    public CheckerType  getCheckerType() {
        return CheckerType.BT_COMPILATION_UNIT_RULESET; }
    
    public  void  configure(JavaFacts facts) {
        /* do nothing yet */
    }
    
    public  void run(Term compilationUnitAst) throws TermWareException {
        Term  toReduce = compilationUnitAst.termClone();
        termSystem_.reduce(toReduce);
    }
    
    private TermSystem  termSystem_;
    
    
}
