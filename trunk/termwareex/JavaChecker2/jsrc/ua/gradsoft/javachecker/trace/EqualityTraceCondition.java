
package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.javachecker.models.expressions.JavaEqualityOperatorKind;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Trace condition, which appropriative to equality operatiron (== or !=)
 * for two expressions.
 * EQUALITY(first,second) || NEQUALITY(first,second)
 * where OP is a string.
 * @author rssh
 */
public class EqualityTraceCondition implements TraceCondition
{

    public EqualityTraceCondition(JavaEqualityOperatorKind kind, JavaExpressionModel first, JavaExpressionModel second)
    {
      kind_=kind;  
      first_=first;
      second_=second;
    }
    
    // todo - impelement more advanced varables determination.
    public void forse(JavaTraceContext traceContext) throws TermWareException
    {
      JavaExpressionKind kd; 
      JavaVariableModel firstVM = null;
      switch(first_.getKind()) {
          case IDENTIFIER:
          case NAME:
          case FIELD:              
          default:
              throw new AssertException("not implementd");  
      }
    }
    
    public Term  getTraceConditionTerm() throws TermWareException
    {
              throw new AssertException("not implementd");  
    }
    
    public JavaTermExpressionModel  getExpression() throws TermWareException
    {
              throw new AssertException("not implementd");         
    }
    
    public TraceCondition  invert() throws TermWareException
    {
              throw new AssertException("not implementd");                 
    }
    
    
    public TraceCondition  cloneTraceCondition() throws TermWareException
    {
              throw new AssertException("not implementd");                         
    }

    // TODO: implement (via eval)
    public TraceCondition  simplicify(JavaTraceContext context) throws TermWareException
    {
        return this;
    }
        
    
    public TraceConditionResult  eval(JavaTraceContext traceContext) throws TermWareException
    {
              throw new AssertException("not implementd");          
    }
    
    public ConditionalJavaTraceSet eliminateORs(ConditionalJavaTraceSet traces) {
        return traces;
    }
    
    private JavaEqualityOperatorKind  kind_;         
    private JavaExpressionModel first_;
    private JavaExpressionModel second_;
    
            
}
