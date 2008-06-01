/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermInstanceOfExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Trace condition for instance-of
 * @author rssh
 */
public class InstanceOfTraceCondition implements TraceCondition
{
    
    public InstanceOfTraceCondition(JavaTermInstanceOfExpressionModel expr)
    {
     expr_=expr;   
    }
    
    
    public Term  getTraceConditionTerm() throws TermWareException
    {
       throw new AssertException("Not implemented");
    }
    
    public JavaTermExpressionModel  getExpression() throws TermWareException
    {
        return expr_;
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
    
    private JavaTermInstanceOfExpressionModel expr_;

}
