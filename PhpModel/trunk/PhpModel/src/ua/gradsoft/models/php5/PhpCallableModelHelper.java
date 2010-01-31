
package ua.gradsoft.models.php5;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.termware.Term;

/**
 *Helper for callable model
 * @author rssh
 */
public class PhpCallableModelHelper {

    public static List<PhpParameterModel>  parseSParametersList(Term t, PhpCompileEnvironment pce)
    {
      List<PhpParameterModel> retval = new ArrayList<PhpParameterModel>();
      Term l = t.getSubtermAt(0);
      while(!l.isNil()) {
          Term ct = l.getSubtermAt(0);
          l=l.getSubtermAt(1);
          retval.add(new PhpParameterModel(ct,pce));
      }
      return retval;
    }

}
