
package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Interface for trace conditions.
 * @author rssh
 */
public interface TraceCondition {

    Term  getTraceConditionTerm() throws TermWareException;
    
    JavaTermExpressionModel  getExpression() throws TermWareException;
    
    TraceCondition  invert() throws TermWareException;
    
    TraceCondition  cloneTraceCondition() throws TermWareException;

    TraceCondition  simplicify(JavaTraceContext context) throws TermWareException;
    
    /**
     * try to eval condition in given context.
     */
     TraceConditionResult  eval(JavaTraceContext traceContext) throws TermWareException;
    
    /**     
     * forse traceContext to be true.
     * i. e. let we know that this condition is true, then we can deduce some facts
     * about traceContext and set ther (i.e. in target traceContext) appropriateve variables.
     * for example, if we know that (a=b) and in traceContext variables for a is 
     * known -- than we can assign a to b.
     * @param traceContext  
     * @throws ua.gradsoft.termware.TermWareException
     */
    void forse(JavaTraceContext traceContext) throws TermWareException, ImpossibleForsingOfTraceCondition;
    
        
}
