/*
 */

package ua.gradsoft.javachecker.trace;

/**
 *Condition with bound trace.
 * @author rssh
 */
public class BoundTraceCondition  
{

    public BoundTraceCondition(TraceCondition traceCondition, JavaTraceContext traceContext)
    {
       traceCondition_=traceCondition;
       traceContext_=traceContext;
    }
    
    public TraceCondition getTraceCondioton()
    {
      return traceCondition_;  
    }
    
    public JavaTraceContext getTraceContext()
    {
      return traceContext_;  
    }
    
    private JavaTraceContext traceContext_;
    private TraceCondition traceCondition_;
}
