package ua.gradsoft.javachecker.trace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.termware.TermWareException;

/**
 *Trace model, whith additional knowledge about trace condition,
 * which we know must be true.
 * (i. e. in next code block
 * <pre> 
 * if (a==b) {
 *   a=a*2;
 *   c=b;
 * }
 * </pre>
 * during evaluation of  <code> a=a*2 </code> whe know, that condition a==b
 *  is forsed to true.
 * @author rssh
 */
public class ConditionalJavaTraceContext {

    public ConditionalJavaTraceContext(JavaTraceContext traceContext) {
        traceContext_ = traceContext;
        invalidated_ = new LinkedList<JavaVariableModel>();
        traceConditions_ = new LinkedList<TraceCondition>();
        notCanBePossible_ = false;
    }

    private ConditionalJavaTraceContext(JavaTraceContext traceContext, List<TraceCondition> traceConditions, List<JavaVariableModel> invalidated) {
        traceContext_ = traceContext;
        traceConditions_ = traceConditions;
        invalidated_ = invalidated;
        notCanBePossible_ = false;
    }

    public ConditionalJavaTraceContext cloneConditionalJavaTraceContext() throws TermWareException {
        JavaTraceContext newTraceContext = traceContext_.cloneTraceContext();
        List<TraceCondition> newTraceConditions = new ArrayList<TraceCondition>();
        for (TraceCondition tc : traceConditions_) {
            newTraceConditions.add(tc.cloneTraceCondition());
        }
        List newInvalidated = new ArrayList();
        newInvalidated.addAll(invalidated_);
        return new ConditionalJavaTraceContext(newTraceContext, newTraceConditions, newInvalidated);
    }

    public void addCondition(TraceCondition traceCondition) throws TermWareException {
        TraceConditionResult r = traceCondition.eval(traceContext_);
        switch (r) {
            case TRUE:
                return;
            case FALSE:
                notCanBePossible_ = true;
            default:
                try {
                    traceCondition.forse(traceContext_);
                    traceConditions_.add(traceCondition);
                } catch (ImpossibleForsingOfTraceCondition ex) {
                    // do nothing.
                    ;
                }
        }
    }

    public boolean isCanBePossible() {
        return !notCanBePossible_;
    }
    private JavaTraceContext traceContext_;
    private List<JavaVariableModel> invalidated_;
    private List<TraceCondition> traceConditions_;
    private boolean notCanBePossible_;
}
