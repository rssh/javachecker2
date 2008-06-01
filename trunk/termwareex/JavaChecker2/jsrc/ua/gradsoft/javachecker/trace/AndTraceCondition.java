package ua.gradsoft.javachecker.trace;

import java.util.Stack;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermAndExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermConditionalAndExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *TODO: implement
 * @author rssh
 */
public class AndTraceCondition implements TraceCondition
{

    public AndTraceCondition(TraceCondition first, TraceCondition second)
    {
      first_=first;
      second_=second;
    }
    
    public JavaTermExpressionModel  getExpression() throws TermWareException
    {
       JavaTermExpressionModel m1 = first_.getExpression();
       JavaTermExpressionModel m2 = second_.getExpression();
       Term t1 = m1.getTerm();
       Term t2 = m2.getTerm();
       TermFactory tf = TermWare.getInstance().getTermFactory();
       Term t = tf.createTerm("ConditionalAnd", t1, t2);       
       JavaTermStatementModel stm = first_.getExpression().getTermStatementModel();
       JavaTypeModel tm = first_.getExpression().getEnclosedType();       
       return new JavaTermConditionalAndExpressionModel(t,stm,tm);
    }
    
    public Term getTraceConditionTerm() throws TermWareException
    {
        Term t1 = first_.getTraceConditionTerm();
        Term t2 = second_.getTraceConditionTerm();
        return TermWare.getInstance().getTermFactory().createTerm("logical_and",t1,t2);
    }
    
    public TraceCondition cloneTraceCondition() throws TermWareException
    {
        TraceCondition newFirst = first_.cloneTraceCondition();
        TraceCondition newSecond = second_.cloneTraceCondition();
        return new AndTraceCondition(newFirst,newSecond);
    }
    
    public TraceCondition  simplicify(JavaTraceContext traceContext) throws TermWareException
    {
        TraceConditionResult frs = first_.eval(traceContext);
        if (frs==TraceConditionResult.FALSE) {
            return new FalseTraceCondition(traceContext.where().getTermStatementModel(),traceContext.where().getTypeModel());
        }
        TraceConditionResult snd = second_.eval(traceContext);
        if (snd==TraceConditionResult.FALSE) {
            return new FalseTraceCondition(traceContext.where().getTermStatementModel(),traceContext.where().getTypeModel());
        }
        if (frs==TraceConditionResult.TRUE) {
            return second_.simplicify(traceContext);
        }
        if (snd==TraceConditionResult.TRUE) {
            return first_.simplicify(traceContext);
        }
        TraceCondition newFirst = first_.simplicify(traceContext);
        TraceCondition newSecond = first_.simplicify(traceContext);
        if (newFirst!=first_ || newSecond!=second_) {
            return new AndTraceCondition(newFirst,newSecond);
        }
        return this;
    }
    
    public TraceConditionResult  eval(JavaTraceContext traceContext) throws TermWareException
    {
        TraceConditionResult frs = first_.eval(traceContext);
        if (frs==TraceConditionResult.FALSE) {
            return frs;
        }
        TraceConditionResult snd = second_.eval(traceContext);
        if (snd==TraceConditionResult.FALSE) {
            return snd;
        }
        if (frs==TraceConditionResult.UNKNOWN || snd==TraceConditionResult.UNKNOWN) {
            return TraceConditionResult.UNKNOWN;
        }
        // now frs and snd was evaluated to true
        return TraceConditionResult.TRUE;
    }
    
    
    
    public void forse(JavaTraceContext context) throws TermWareException, ImpossibleForsingOfTraceCondition
    {      
      first_.forse(context);            
      second_.forse(context);
    }
    
    
    public TraceCondition invert() throws TermWareException
    {
        return new OrTraceCondition(first_.invert(),second_.invert());
    }
    
    public ConditionalJavaTraceSet eliminateORs(ConditionalJavaTraceSet traceContexts) throws TermWareException
    {
       ConditionalJavaTraceSet s1 = first_.eliminateORs(traceContexts);
       ConditionalJavaTraceSet s2 = second_.eliminateORs(traceContexts);
       return s2;
    }
    
    
    private TraceCondition first_;
    private TraceCondition second_;
}
