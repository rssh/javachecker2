
package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermConditionalOrExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 * OR(first,second)
 */
public class OrTraceCondition implements TraceCondition
{
    
    public OrTraceCondition(TraceCondition first, TraceCondition second)
    {
      first_=first;
      second_=second;
    }
    
    public Term getTraceConditionTerm() throws TermWareException
    {
        Term t1 = first_.getTraceConditionTerm();
        Term t2 = second_.getTraceConditionTerm();
        return TermWare.getInstance().getTermFactory().createTerm("OR",t1,t2);
    }
    
    public JavaTermExpressionModel  getExpression() throws TermWareException
    {
       JavaTermExpressionModel m1 = first_.getExpression();
       JavaTermExpressionModel m2 = second_.getExpression();
       Term t1 = m1.getTerm();
       Term t2 = m2.getTerm();
       TermFactory tf = TermWare.getInstance().getTermFactory();
       Term t = tf.createTerm("ConditionalOr", t1, t2);       
       JavaTermStatementModel stm = first_.getExpression().getTermStatementModel();
       JavaTypeModel tm = first_.getExpression().getEnclosedType();       
       return new JavaTermConditionalOrExpressionModel(t,stm,tm);
    }
    
        
    
    public TraceCondition cloneTraceCondition() throws TermWareException
    {
      TraceCondition newFirst = first_.cloneTraceCondition();
      TraceCondition newSecond = second_.cloneTraceCondition();
      return new OrTraceCondition(newFirst,newSecond);
    }

    /**
     * invert
     * @return  AND(x.invert, y.invert)
     * @throws ua.gradsoft.termware.TermWareException
     */
    public TraceCondition  invert() throws TermWareException
    {
        TraceCondition ifirst = first_.invert();
        TraceCondition isecond = second_.invert();
        return new AndTraceCondition(ifirst,isecond);        
    }
    
    
    public TraceCondition  simplicify(JavaTraceContext context) throws TermWareException
    {
       TraceCondition sfirst = first_.simplicify(context); 
       TraceConditionResult frs = sfirst.eval(context);
       if (frs == TraceConditionResult.TRUE) {
          JavaTermStatementModel stm = first_.getExpression().getTermStatementModel();
          JavaTypeModel tm = first_.getExpression().getEnclosedType();                  
          return new TrueTraceCondition(stm,tm);
       }       
       TraceCondition ssecond = second_.simplicify(context);
       TraceConditionResult snd = ssecond.eval(context);
       if (snd == TraceConditionResult.TRUE) {
          JavaTermStatementModel stm = second_.getExpression().getTermStatementModel();
          JavaTypeModel tm = second_.getExpression().getEnclosedType();                  
          return new TrueTraceCondition(stm,tm);           
       }
       switch(frs) {
           case FALSE:
               if (snd==TraceConditionResult.FALSE) {
                 JavaTermStatementModel stm = first_.getExpression().getTermStatementModel();
                 JavaTypeModel tm = first_.getExpression().getEnclosedType();                  
                 return new FalseTraceCondition(stm,tm);                   
               }
               return ssecond;
           case UNKNOWN:
               if (snd==TraceConditionResult.FALSE) {
                   return sfirst;
               }else{
                   return new OrTraceCondition(sfirst,ssecond);
               }               
       }
       throw new AssertException("Impossibe: not all cases checked.");
    }

    
    public TraceConditionResult  eval(JavaTraceContext traceContext) throws TermWareException
    {
       TraceConditionResult frs = first_.eval(traceContext);
       if (frs == TraceConditionResult.TRUE) {
           return frs;
       }
       TraceConditionResult snd = second_.eval(traceContext);
       if (snd == TraceConditionResult.TRUE) {
           return snd;
       }
       switch(frs) {
           case FALSE: return snd;
           case UNKNOWN: return TraceConditionResult.UNKNOWN;           
       }
       throw new AssertException("Impossibe: not all cases checked.");
    }
    
    
                            
    public TraceCondition getFirst()
    {
        return first_;
    }

    public TraceCondition getSecond()
    { return second_; }

    public void forse(JavaTraceContext traceContext) throws TermWareException
    {
        throw new AssertException("forse before eliminate-ors is called");
    }
    
    public ConditionalJavaTraceSet eliminateORs(ConditionalJavaTraceSet traceContexts) throws TermWareException
    {
        throw new AssertException("Not implemented!");
    }
    
    
    private TraceCondition first_;
    private TraceCondition second_;
        
}
