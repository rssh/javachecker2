
package ua.gradsoft.models.php5;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.termware.Term;

/**
 *Model for entitoes, which can be called. (methods and functions)
 * @author rssh
 */
public interface PhpCallableModel extends PhpElementModel
{

    public String getName();

    public boolean isReturnByReference();

    public List<PhpParameterModel> getParameters();

    public PhpSCompoundStatementModel  getStatement();



}
