
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public abstract class PhpPrimitiveReferenceModel implements PhpReferenceModel {

    protected PhpPrimitiveReferenceModel(PhpValueModel origin)
    {
      this.origin = origin;  
    }

    public PhpArrayModel getArray(PhpEvalEnvironment pee) {
        return origin.getArray(pee);
    }

    public boolean getBoolean() {
        return origin.getBoolean();
    }

    public double getFloat() {
        return origin.getFloat();
    }

    public int getInt() {
        return origin.getInt();
    }

    public PhpObjectModel getObject(PhpEvalEnvironment pee) {
        return origin.getObject(pee);
    }

    public String getString(PhpEvalEnvironment pee) {
        return origin.getString(pee);
    }

    public PhpType getType() {
        return origin.getType();
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        return origin.eval(php);
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        return origin.getTerm(pee);
    }

    public PhpValueModel copyByReference(PhpEvalEnvironment pee) {
        return origin.copyByReference(pee);
    }

    public PhpValueModel copyByValue(PhpEvalEnvironment pee) {
        return origin.copyByValue(pee);
    }

    public abstract void  set(PhpValueModel value);

    protected PhpValueModel origin;
}
