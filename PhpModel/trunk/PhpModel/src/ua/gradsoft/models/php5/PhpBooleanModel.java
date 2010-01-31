
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for boolean
 * @author rssh
 */
public class PhpBooleanModel extends PhpPrimitiveValueModel
{

    protected PhpBooleanModel(boolean value, boolean isCostant)
    {
      this.value=value;  
    }

    public boolean getBoolean() {
        return value;
    }

    public double getFloat() {
        return (double)getInt();
    }

    public int getInt() {
        return (value ? 1 : 0);
    }

    public String getString(PhpEvalEnvironment pee) {
        return (value ? "1" : "");
    }

    public PhpType getType() {
        return PhpType.BOOLEAN;
    }


    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        return TermWare.getInstance().getTermFactory().createBoolean(value);
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




    public static final PhpBooleanModel TRUE = new PhpBooleanModel(true, true);
    public static final PhpBooleanModel FALSE = new PhpBooleanModel(false, true);

    private boolean value;
    private boolean isConstant;
}
