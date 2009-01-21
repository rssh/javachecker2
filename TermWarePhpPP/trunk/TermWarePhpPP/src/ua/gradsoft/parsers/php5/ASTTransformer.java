
package ua.gradsoft.parsers.php5;

import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 *Transfromer for received PHP scripts.
 * @author rssh
 */
public class ASTTransformer {


    public ASTTransformer() throws TermWareException
    { init(); }

    public class ASTFacts extends DefaultFacts
    {
        public ASTFacts() throws TermWareException {
            super();
        }
        
    }

    private void init() throws TermWareException
    {
        ASTFacts astFacts = new ASTFacts();
        FirstTopStrategy strategy1 = new FirstTopStrategy();
        FirstTopStrategy strategy2 = new FirstTopStrategy();

        afterToList_ = new TermSystem(strategy1,astFacts);
        beforeToList_ = new TermSystem(strategy2,astFacts);
    }

    public Term transform(Term x) throws TermWareException
    {
      x=beforeToList_.reduce(x);
      x=transformSeqToList(x);
      x=afterToList_.reduce(x);
      return x;
    }

    public Term phpTermArgsAsList(Term t, int startFrom, boolean deep) throws TermWareException
    {
        Term list = TermWare.getInstance().getTermFactory().createNil();
        for(int i=t.getArity(); i>startFrom; --i) {
            Term ct = t.getSubtermAt(i-1);
            if (deep) {
                ct=transformSeqToList(ct);
            }
            if (!ct.isNil()) {
                list=TermWare.getInstance().getTermFactory().createTerm("cons",ct,list);
            }
        }
        Term[] newBody = new Term[startFrom+1];
        for(int i=0; i<startFrom; ++i) {
            newBody[i]=t.getSubtermAt(i);
        }
        newBody[startFrom]=list;
        t=TermWare.getInstance().getTermFactory().createTerm(t.getName(),newBody);
        return t;
    }

    public Term transformSeqToList(Term t) throws TermWareException
    {
      if (t.isComplexTerm()) {
          if (t.getName().equals("HtmlBlocks")) {
              t=phpTermArgsAsList(t,0,true);
          }
      }  
      return t;
    }

    private TermSystem  afterToList_;
    private TermSystem  beforeToList_;


}
