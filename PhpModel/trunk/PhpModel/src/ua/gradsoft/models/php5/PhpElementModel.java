
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Element of PHP model
 * @author rssh
 */
public interface PhpElementModel {

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException;

    public int  getLine();

    public String getFile();

}
