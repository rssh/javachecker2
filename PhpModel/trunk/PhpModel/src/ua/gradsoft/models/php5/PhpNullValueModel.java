
package ua.gradsoft.models.php5;

import java.util.Map;
import java.util.TreeMap;

/**
 *Valie model for NULL constant
 * @author rssh
 */
public class PhpNullValueModel implements PhpValueModel
{

    public PhpArrayModel getArray(PhpEvalEnvironment pee) {
        return new PhpArrayModel(pee);
    }

    public boolean getBoolean() {
        return false;
    }

    public double getFloat() {
        return 0;
    }

    public int getInt() {
        return 0;
    }

    public PhpObjectModel getObject(PhpEvalEnvironment pee) {
        Map<String,PhpValueModel> newMap = new TreeMap<String,PhpValueModel>();
        PhpClassDeclarationModel stdClass = pee.findClassDeclarationModel("stdClass");
        return new PhpObjectModel(stdClass,newMap);
    }

    public String getString(PhpEvalEnvironment pee) {
        return "";
    }

    public PhpType getType() {
        return PhpType.NULL;
    }

    public PhpValueModel copyByReference(PhpEvalEnvironment pee) {
        return this;
    }

    public PhpValueModel copyByValue(PhpEvalEnvironment pee) {
        return this;
    }


    public static final PhpNullValueModel INSTANCE = new PhpNullValueModel();
}
