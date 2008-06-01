
package ua.gradsoft.javachecker.models;

import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 * Throwed on invalid expression.
 */
public class InvalidJavaExpressionModelException extends InvalidJavaTermException
{

    public InvalidJavaExpressionModelException(String message, JavaExpressionModel expr)
    {
      super(message,getTerm(expr));  
    }
    
    private static Term getTerm(JavaExpressionModel expr)
    {
        if (expr instanceof JavaTermExpressionModel){
           JavaTermExpressionModel te = (JavaTermExpressionModel)expr;
           return te.getTerm();
        }else{
            try {
               // we does not know
               return expr.getModelTerm();
            } catch(TermWareException ex){
               return TermUtils.createString(expr.toString()); 
            } catch(EntityNotFoundException ex){
                return TermUtils.createString(expr.toString());
            }
        }               
    }
    
}
