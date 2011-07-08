
package ua.gradsoft.javachecker.trace;

/**
 *When traced code throw this exception
 * @author rssh
 */
public class InvokedEvaluationException extends EvaluationException
{

    public InvokedEvaluationException(Exception ex)
    {
      super(3,"invoked evaluation exception",ex);  
    }
    
}
