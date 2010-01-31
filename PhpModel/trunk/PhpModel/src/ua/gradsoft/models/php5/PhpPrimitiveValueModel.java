
package ua.gradsoft.models.php5;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author rssh
 */
public abstract class PhpPrimitiveValueModel implements PhpValueModel
{

    public PhpArrayModel getArray(PhpEvalEnvironment pee) {
        PhpArrayModel retval = new PhpArrayModel(pee);
        retval.put(new PhpIntegerModel(0),this);
        return retval;
    }


    public PhpObjectModel getObject(PhpEvalEnvironment pee) {
        PhpClassDeclarationModel stdClass = pee.findClassDeclarationModel("stdClass_");
        Map<String,PhpValueModel> vmap = new TreeMap<String,PhpValueModel>();
        vmap.put("scalar",this);
        return new PhpObjectModel(stdClass,vmap);
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        return this;
    }




}
