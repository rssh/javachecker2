
package ua.gradsoft.javachecker.trace;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.termware.TermWareException;

/**
 *Set of conditional traces.
 * (main part of our analysis)
 * @author rssh
 */
public class ConditionalJavaTraceSet 
{

    public ConditionalJavaTraceSet()
    { contexts_ = new ArrayList<ConditionalJavaTraceContext>(); }    
    
    public void split(TraceCondition traceCondition) throws TermWareException
    {
        TraceCondition yesCondition = traceCondition;
        TraceCondition noCondition = traceCondition.invert();
        List<ConditionalJavaTraceContext> newContexts = new ArrayList<ConditionalJavaTraceContext>();
        for(ConditionalJavaTraceContext ctx : contexts_ ) {
            ConditionalJavaTraceContext yesCtx = ctx.cloneConditionalJavaTraceContext();
            yesCtx.addCondition(yesCondition);
            ConditionalJavaTraceContext noCtx = ctx;
            noCtx.addCondition(noCondition);
            if (yesCtx.isCanBePossible()) {
              newContexts.add(yesCtx);
            }
            if (noCtx.isCanBePossible()) {
              newContexts.add(noCtx);
            }
        }        
        contexts_ = newContexts;
    }
     
    public void add(ConditionalJavaTraceContext ctx)
    {
        contexts_.add(ctx);
    }
    
    public void addAll(ConditionalJavaTraceSet traceSets)
    {
       contexts_.addAll(traceSets.getContexts());
    }
    
    public List<ConditionalJavaTraceContext>  getContexts()
    { return contexts_; }
    
    private List<ConditionalJavaTraceContext> contexts_;
}
