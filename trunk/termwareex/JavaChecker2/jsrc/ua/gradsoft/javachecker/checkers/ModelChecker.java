/*
 * ModelChecker.java
 *
 */

package ua.gradsoft.javachecker.checkers;

import java.util.Iterator;
import ua.gradsoft.javachecker.AbstractChecker;
import ua.gradsoft.javachecker.CheckerType;
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
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.util.TransformersStar;

/**
 *Checker which works over ModelTerm
 * @author RSSH
 */
public class ModelChecker extends AbstractChecker {
    
    /** Creates a new instance of ModelChecker */
    public ModelChecker(String name, String category, String description, Term rules, boolean enabled) throws TermWareException, ConfigException {
        super(name,category,description,enabled);
        ITermRewritingStrategy strategy = TermWare.getInstance().createStrategyByName("BottomTop");
        termSystem_ = new TermSystem(strategy,Main.getFacts(),TermWare.getInstance());
        if (rules.getName().equals("ruleset")) {
            for(int i=0; i<rules.getArity(); ++i) {
                Term current=rules.getSubtermAt(i);
                if (current.getName().equals("import")) {
                    if(current.getArity()==2) {
                        Term termPatternName=current.getSubtermAt(1);
                        if (!termPatternName.isString()&&!termPatternName.isAtom()) {
                            throw new AssertException("secong argument of import must be atom or string");
                        }
                        String patternName=termPatternName.getName();
                        TermSystem fromSys = TermWare.getInstance().getRoot().resolveSystem(current.getSubtermAt(0));
                        Iterator<ITermTransformer> it=fromSys.getStrategy().getStar().iterator(patternName);
                        while(it.hasNext()) {
                            ITermTransformer tr=it.next();
                            //System.out.println("adding normalizer"+tr.toString());
                            termSystem_.addNormalizer(patternName, tr);
                        }
                    }else{
                        throw new AssertException("import in checkers.def must have arity 2");
                    }
                }else{
                    termSystem_.addRule(current);
                }
            }
        }else{
            throw new ConfigException("ruleset term required");
        }
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
                throw new AssertException("Can't initalize model for "+tm.getName(),ex);
            }
            modelTermHolder.setValue(modelTerm);
        }
        Term toReduce = TermUtils.createTerm(getName(),modelTerm.termClone());
        if (Main.isDump()) {            
            modelTerm.println(System.out);
        }
        Term ret = termSystem_.reduce(toReduce);
        if (Main.isDump()) {
            System.out.println("!!!---!!!");
            ret.println(System.out);
        }
    }
    
    private TermSystem termSystem_;
}
