package ua.gradsoft.models.php5;

/**
 *
 * @author rssh
 */
public class PhpEvalException extends RuntimeException
{

  public PhpEvalException(int code, String message, Exception ex)
  { super(message,ex);
    this.code=code;
  }

  public PhpEvalException(String message, Exception ex)
  { this(-1,message,ex); }

  public PhpEvalException(String message)
  { this(-1,message,null); }

  public int getCode()
  { return code; }

  private int code;
}
