
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class InvalidPhpTermExpression extends TermWareException
{
  public InvalidPhpTermExpression(String message, Term where)
  {
    super(message);
    this.where = where;
  }

  public Term getWhere()
  { return where; }

  private Term where;
}
