
package ua.gradsoft.models.php5;

import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

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

    public PhpValueModel copyByReference(PhpEvalEnvironment pee) {
        if (isConstant) {
            throw new PhpEvalException("Can't get constant by reference");
        } else {
            return new PhpDefaultReferenceModel(this);
        }
    }

    public PhpValueModel copyByValue(PhpEvalEnvironment pee) {
        // we are immutable
        return this;
    }

    protected Term createConstantTerm(Term v) throws TermWareException
    {
        Term[] body = new Term[1];
        body[0] = v;
        return PhpTermUtils.createContextTerm("Constant", body, this);
    }

    protected boolean isConstant = false;

}
