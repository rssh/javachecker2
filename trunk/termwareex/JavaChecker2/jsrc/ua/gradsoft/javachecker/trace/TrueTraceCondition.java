
package ua.gradsoft.javachecker.trace;

import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.models.expressions.JavaTermBooleanLiteralExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *True trace condition
 * @author rssh
 */
public class TrueTraceCondition implements TraceCondition
{

    
    public TrueTraceCondition(JavaTermStatementModel statement,JavaTypeModel enclosedType)
    {
      statement_=statement;
      enclosedType_=enclosedType;
    }
    
    public JavaTermExpressionModel getExpression() throws TermWareException
    {
        Term s = TermUtils.createString("true");
        Term retval = TermUtils.createTerm("BooleanLiteral", s);        
        return new JavaTermBooleanLiteralExpressionModel(retval,statement_,enclosedType_);
    }
    
    public Term getTraceConditionTerm()
    {
        return TermUtils.createBoolean(true);
    }
           
    
    /* immutable -- so we do nothing */
    public TraceCondition cloneTraceCondition()
    {
      return this;
    }
    
    public TraceConditionResult eval(JavaTraceContext traceContext)
    {
      return TraceConditionResult.TRUE;  
    }
    
    public void forse(JavaTraceContext traceContext)
    {
       /* do nothing */
    }
    
    public TraceCondition simplicify(JavaTraceContext ctx)
    {
       return this; 
    }
    
    public TraceCondition invert()
    {
       return new FalseTraceCondition(statement_,enclosedType_); 
    }
        
    public ConditionalJavaTraceSet eliminateORs(ConditionalJavaTraceSet traces)
    {
      return traces;  
    }
    
    
    private JavaTermStatementModel statement_;
    private JavaTypeModel          enclosedType_;
    
    
}
