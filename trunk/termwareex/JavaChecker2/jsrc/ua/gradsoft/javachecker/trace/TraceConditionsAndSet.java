
package ua.gradsoft.javachecker.trace;

import java.util.List;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *set of trace confitoon.
 * @author rssh
 */
public class TraceConditionsAndSet implements TraceCondition
{

        
    public TraceConditionsAndSet(List<TraceCondition> traceConditions)
    { traceConditions_=traceConditions; }
    
    public Term  getTraceConditionTerm() throws TermWareException
    {
       if (traceConditions_.size()==0) {
           // it's true
           return TermUtils.createBoolean(true);
       } else if (traceConditions_.size() == 1) {
           return traceConditions_.get(0).getTraceConditionTerm();
       } else {
           Term last = traceConditions_.get(traceConditions_.size()-1).getTraceConditionTerm();
           for(int i=traceConditions_.size()-1; i>0; --i) {
               Term t = traceConditions_.get(i-1).getTraceConditionTerm();
               last = TermUtils.createTerm("AND", t,last);
           }
           return last;
       }
    }
    
    public JavaTermExpressionModel  getExpression() throws TermWareException
    {
        throw new AssertException("Not implemented");
    }
    
    public TraceCondition  invert() throws TermWareException
    {
        throw new AssertException("Not implemented");
    }
    
    public TraceCondition  cloneTraceCondition() throws TermWareException
    {
        throw new AssertException("Not implemented");
    }

    public TraceCondition  simplicify(JavaTraceContext context) throws TermWareException
    {
        throw new AssertException("Not implemented");
    }
    
    public TraceConditionResult  eval(JavaTraceContext traceContext) throws TermWareException
    {
        throw new AssertException("Not implemented");        
    }
    
    public void forse(JavaTraceContext traceContext) throws TermWareException, ImpossibleForsingOfTraceCondition
    {
        throw new AssertException("Not implemented");                
    }

    public ConditionalJavaTraceSet eliminateORs(ConditionalJavaTraceSet traceContexts) throws TermWareException
    {
        throw new AssertException("Not implemented");                        
    }
    
   
    
    List<TraceCondition>  traceConditions_;

}
