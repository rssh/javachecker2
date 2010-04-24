package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpFloatModel extends PhpPrimitiveValueModel
{

    public PhpFloatModel(double v)
    {
      value=v;
    }

    public boolean getBoolean() {
        return value!=0;
    }

    public double getFloat() {
        return value;
    }

    public int getInt() {
        return (int)value;
    }

    public String getString(PhpEvalEnvironment pee) {
        return Double.toString(value);
    }

    public PhpType getType() {
        return PhpType.FLOAT;
    }

    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        throw new UnsupportedOperationException();
    }

    public boolean isReference(PhpEvalEnvironment php) {
        return false;
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        return createConstantTerm(
                  PhpTermUtils.createDouble(value)
               );
    }

 

    private double value;
}
