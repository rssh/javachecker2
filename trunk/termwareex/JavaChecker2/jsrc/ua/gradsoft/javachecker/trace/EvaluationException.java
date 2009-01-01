
package ua.gradsoft.javachecker.trace;

/**
 *Exception durting evaluation in trace.
 * @author rssh
 */
public class EvaluationException extends Exception
{
  public EvaluationException(String message)  
  {
    super(message);  
  }
    
  public EvaluationException(String message, Exception ex)
  { super(message,ex); }

  public EvaluationException(Exception ex)
  { super("evaluation exception",ex); }

}
