/*
 * BTChecker.java
 *
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.AbstractChecker;
import ua.gradsoft.javachecker.CheckerType;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.util.Holder;
import ua.gradsoft.termware.ITermRewritingStrategy;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author RSSH
 */
public class BTChecker extends AbstractChecker {
    
    /** Creates a new instance of BTChecker */
    public BTChecker(String name,String category,String description,Term rules, boolean enabled) throws TermWareException {
        super(name,category,description,enabled);
        ITermRewritingStrategy strategy = TermWare.getInstance().createStrategyByName("BottomTop");
        termSystem_ = new TermSystem(strategy,Main.getFacts(),TermWare.getInstance());
        if (rules.getName().equals("ruleset")) {
            for(int i=0; i<rules.getArity(); ++i) {
                termSystem_.addRule(rules.getSubtermAt(i));
            }
        }
    }
    
    public CheckerType  getCheckerType() {
        return CheckerType.BT_RULESET; }
    
    public  void  configure(JavaFacts facts) {
        /* do nothing yet */
    }
    
    public  void run(JavaTermTypeAbstractModel tm, Holder<Term> astTermHolder, Holder<Term> modelTermHolder) throws TermWareException {
        if (astTermHolder.getValue()==null) {            
            if (!tm.hasASTTerm()) {
                return;
            }         
            astTermHolder.setValue(tm.getASTTerm());
        }
        Term  toReduce = TermUtils.createTerm(getName(),astTermHolder.getValue().termClone());
        termSystem_.reduce(toReduce);
    }
    
    private TermSystem  termSystem_;
}
