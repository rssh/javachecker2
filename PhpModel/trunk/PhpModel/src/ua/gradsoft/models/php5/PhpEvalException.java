package ua.gradsoft.models.php5;

/**
 *
 * @author rssh
 */
public class PhpEvalException extends RuntimeException
{

  public PhpEvalException(int code, String message, Exception ex, PhpStatementModel statement, PhpEvalEnvironment env)
  { super(message,ex);
    this.code=code;
    this.statement=statement;
    this.env=env;
  }

  public PhpEvalException(String message, Exception ex)
  { this(-1,message,ex,null,null); }

  public PhpEvalException(String message)
  { this(-1,message,null,null,null); }

  public PhpEvalException(String message, PhpStatementModel statement, PhpEvalEnvironment env)
  {
    this(-1,message,null,null,null);  
  }

  public int getCode()
  { return code; }

  public PhpStatementModel getStatement()
  { return statement; }
  
  public PhpEvalEnvironment getEvalEnvironment()
  {
    return env;  
  }

  private int code;
  private PhpStatementModel statement;
  private PhpEvalEnvironment env;
}
