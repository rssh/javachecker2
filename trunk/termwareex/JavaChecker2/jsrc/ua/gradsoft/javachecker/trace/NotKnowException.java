
package ua.gradsoft.javachecker.trace;

/**
 *Exception is throwed, when we do some action, but we have no
 * information: what to do with such action.
 * @author rssh
 */
public class NotKnowException extends EvaluationException
{
  public NotKnowException()
  {
    super("not know");  
  }
}
