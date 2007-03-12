/*
 * ModelChecker.java
 *
 */

package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.AbstractChecker;
import ua.gradsoft.javachecker.CheckerType;
import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.javachecker.util.Holder;
import ua.gradsoft.termware.ITermRewritingStrategy;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Checker which works over ModelTerm 
 * @author RSSH
 */
public class ModelChecker extends AbstractChecker
{
    
    /** Creates a new instance of ModelChecker */
    public ModelChecker(String name, String category, String description, Term rules, boolean enabled) throws TermWareException, ConfigException
    {
        super(name,category,description,enabled);
        ITermRewritingStrategy strategy = TermWare.getInstance().createStrategyByName("BottomTop");
        termSystem_ = new TermSystem(strategy,Main.getFacts(),TermWare.getInstance());
        if (rules.getName().equals("ruleset")) {
            for(int i=0; i<rules.getArity(); ++i) {
                termSystem_.addRule(rules.getSubtermAt(i));
            }
        }else{
            throw new ConfigException("ruleset term required");
        }        
    }
    
    public CheckerType getCheckerType()
    { return CheckerType.MODEL_RULESET; }
    
    
    public void configure(JavaFacts facts)
    {
        /* do nothing */
    }
    
    public void run(JavaTermTypeAbstractModel tm, Holder<Term> astTermHolder, Holder<Term> modelTermHolder) throws TermWareException
    {        
        Term modelTerm = modelTermHolder.getValue();
        if (modelTerm==null) {
          try {  
            modelTerm=tm.getModelTerm();
          }catch(EntityNotFoundException ex){
            throw new AssertException("Can't initalize model for "+tm.getName(),ex);    
          }
          modelTermHolder.setValue(modelTerm);
        }
        termSystem_.reduce(modelTerm.termClone());
    }
    
    private TermSystem termSystem_;
}
