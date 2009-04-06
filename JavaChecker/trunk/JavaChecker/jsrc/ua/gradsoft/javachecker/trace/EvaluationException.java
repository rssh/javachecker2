
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
    code_=-1;
  }

  public EvaluationException(int code, String message)
  {
    super(message);
    code_=code;
  }

  public EvaluationException(String message, Exception ex)
  { super(message,ex); code_=-1; }

  public EvaluationException(int code, String message, Exception ex)
  { super(message,ex); code_=code; }

  public EvaluationException(Exception ex)
  { super("evaluation exception",ex); code_=-1; }

  public int getCode()
  { return code_; }

  private int code_;
}
