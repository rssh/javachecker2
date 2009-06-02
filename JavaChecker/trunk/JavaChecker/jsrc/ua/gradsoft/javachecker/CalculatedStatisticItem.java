
package ua.gradsoft.javachecker;

import java.util.Set;
import java.util.TreeSet;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *Statistic item, which is calculated (from counters or
 *  other items).
 */
public class CalculatedStatisticItem extends StatisticItem
{

    public CalculatedStatisticItem(String name, String category, String description, Term expression, boolean enabled, Statistics statistics) {
        super(name, category, description, enabled, true,statistics);
        expression_=expression;
        values_=new Double[StatisticScope.values().length];
        Term showTerm = TermHelper.getAttribute(expression, "show");
        if(!showTerm.isNil()) {
            if (showTerm.isBoolean()) {
                setShow(showTerm.getBoolean());
            }
        }
    }

    public double getValue(StatisticScope scope) throws TermWareException
    {
        Double d = values_[scope.ordinal()];
        if (d==null) {
            d=evalExpression(scope);
        }
        return d;
    }

    public double endScope(StatisticScope scope) throws TermWareException
    {
      return evalExpression(scope);
    }

    protected double evalExpression(StatisticScope scope) throws TermWareException
    {
       TermSystem ts = owner_.getTermSystem();
       Term expressionWithScope=translateExpression(expression_,scope);
       Term r = ts.reduce(expressionWithScope);
       return r.getAsDouble(ts.getInstance());
    }

    public Term translateExpression(Term expression, StatisticScope scope) throws TermWareException
    {
      if (expression.isAtom()) {
          TermFactory tf = TermWare.getInstance().getTermFactory();
          return tf.createTerm("V", tf.createString(expression.getName()),
                                    tf.createInt(scope.ordinal())
                  );
      }else if (expression.isComplexTerm()) {
          Term[] newBody = new Term[expression.getArity()];
          for(int i=0; i<expression.getArity(); ++i) {
              newBody[i]=translateExpression(expression.getSubtermAt(i),scope);
          }
          return expression.createSame(newBody);
      }else{
          return expression;
      }
    }


    public Set<String> getDependencies()
    {
      Set<String> dependencies = new TreeSet<String>();
      fillDependencies(dependencies,expression_);
      return dependencies;
    }

    private void fillDependencies(Set<String> dependencies, Term t)
    {
      if (t.isComplexTerm()) {
          for(int i=0; i<t.getArity(); ++i) {
              fillDependencies(dependencies, t.getSubtermAt(i));
          }
      } else if (t.isAtom()) {
          dependencies.add(t.getName());
      } else {
          /* do nothing */
          ;
      }
    }

    private Term expression_;
    private Double[] values_;
}
