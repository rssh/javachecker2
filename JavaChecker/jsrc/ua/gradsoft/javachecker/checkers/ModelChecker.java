/*
 * ModelChecker.java
 *
 */

package ua.gradsoft.javachecker.checkers;

import java.util.Iterator;
import ua.gradsoft.javachecker.checkers.AbstractTypeChecker;
import ua.gradsoft.javachecker.checkers.CheckerType;
import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.util.Holder;
import ua.gradsoft.termware.ITermRewritingStrategy;
import ua.gradsoft.termware.ITermTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Checker which works over ModelTerm
 * @author RSSH
 */
public class ModelChecker extends AbstractTypeChecker {
    
    /** Creates a new instance of ModelChecker */
    public ModelChecker(String name, String category, String description, Term rules, boolean enabled) throws TermWareException, ConfigException {
        super(name,category,description,enabled);
        ITermRewritingStrategy strategy = TermWare.getInstance().createStrategyByName("BottomTop");
        termSystem_ = new TermSystem(strategy,Main.getFacts(),TermWare.getInstance());
        addRuleset(termSystem_,rules);
        TermWare.getInstance().addSystem(getName(),termSystem_);
    }
    
    public CheckerType getCheckerType() {
        return CheckerType.MODEL_RULESET; }
    
    
    public void configure(JavaFacts facts) {
        /* do nothing */
    }
    
    
    
    public void run(JavaTermTypeAbstractModel tm, Holder<Term> astTermHolder, Holder<Term> modelTermHolder) throws TermWareException {
        Term modelTerm = modelTermHolder.getValue();
        if (modelTerm==null) {
            try {
                modelTerm=tm.getModelTerm();
            }catch(EntityNotFoundException ex){               
                throw new AssertException("Can't initalize model for "+tm.getName()+" at "+ex.getFileAndLine().getFname()+","+ex.getFileAndLine().getLine()+":"+ex.getMessage(),ex);
            }
            modelTermHolder.setValue(modelTerm);
        }
        Term toReduce = TermUtils.createTerm(getName(),modelTerm.termClone());
        if (Main.isDump()) {            
            modelTerm.println(System.out);
        }
       // termSystem_.setDebugMode(true);
       // termSystem_.setDebugEntity(FirstTopStrategy.class.getName());
        
        //TermSystem openCloseSystem = TermWare.getInstance().getRoot().resolveSystem("TrackOpenClose");
        //openCloseSystem.setDebugMode(true);
        //openCloseSystem.setDebugEntity("StrategyReductions");
        //openCloseSystem.setDebugEntity("All");
        //openCloseSystem.setDebugEntity("Reductions");
        
        
          Term ret = termSystem_.reduce(toReduce);
      
        if (Main.isDump()) {         
            ret.println(System.out);
        }
    }
    
    public boolean hasSecondPass()
    { return false; }
    
    public void runSecondPass(JavaTermTypeAbstractModel tm, Holder<Term> astTermHolder, Holder<Term> modelTermHolder) throws TermWareException {
        throw new AssertException("Model checkers must have not second pass");
    }
    
    
    private TermSystem termSystem_;
}
