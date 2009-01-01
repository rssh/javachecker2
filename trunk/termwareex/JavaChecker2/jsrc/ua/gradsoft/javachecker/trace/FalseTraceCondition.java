
package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaStatementModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.models.expressions.JavaTermBooleanLiteralExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *trace condition for false.
 * (immutable singleton constant)
 * @author rssh
 */
public class FalseTraceCondition implements TraceCondition
{

    public FalseTraceCondition(JavaTermStatementModel statement,JavaTypeModel enclosedType)
    {
      statement_=statement;
      enclosedType_=enclosedType;
    }
    
    public JavaTermExpressionModel getExpression() throws TermWareException
    {
        Term s = TermUtils.createString("false");
        Term retval = TermUtils.createTerm("BooleanLiteral", s);        
        return new JavaTermBooleanLiteralExpressionModel(retval,statement_,enclosedType_);
    }
    
    public Term getTraceConditionTerm()
    {
        return TermUtils.createBoolean(false);
    }
           
    
    /* immutable -- so we do nothing */
    public TraceCondition cloneTraceCondition()
    {
      return this;
    }
    
    public TraceConditionResult eval(JavaTraceContext traceContext)
    {
      return TraceConditionResult.FALSE;  
    }
    
    public void forse(JavaTraceContext traceContext) throws ImpossibleForsingOfTraceCondition
    {
        throw new ImpossibleForsingOfTraceCondition();
    }
    
    public TraceCondition simplicify(JavaTraceContext ctx)
    {
       return this; 
    }
    
    public TraceCondition invert()
    {
       return new TrueTraceCondition(statement_,enclosedType_); 
    }
    
    
    private JavaTermStatementModel statement_;
    private JavaTypeModel          enclosedType_;
    
}
