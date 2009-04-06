
package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.CheckerType;
import ua.gradsoft.javachecker.ConfigException;
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
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Ruleset with First-Top strategy
 * @author rssh
 */
public class FTTypeChecker  extends AbstractTypeChecker
{

    /**
     * Creates a new instance of BTTypeChecker
     */
    public FTTypeChecker(String name,String category,String description,Term rules, boolean enabled) throws TermWareException, ConfigException {
        super(name,category,description,enabled);
        ITermRewritingStrategy strategy = TermWare.getInstance().createStrategyByName("FirstTop");
        termSystem_ = new TermSystem(strategy,Main.getFacts(),TermWare.getInstance());
        addRuleset(termSystem_,rules);
    }

    public CheckerType  getCheckerType() {
        return CheckerType.FT_TYPE_RULESET; }

    public  void  configure(JavaFacts facts) {
        /* do nothing yet */
    }

    public boolean hasSecondPass()
    { return false; }

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

    public  void runSecondPass(JavaTermTypeAbstractModel tm, Holder<Term> astTermHolder, Holder<Term> modelTermHolder) throws TermWareException {
       throw new AssertException("Second pass is not enabled in FTChecker");
    }

    private TermSystem  termSystem_;


}
