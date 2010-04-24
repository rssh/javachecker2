
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
        return createConstantTerm(
                  TermWare.getInstance().getTermFactory().createBoolean(value)
                );
    }

    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        return new PhpDefaultReferenceModel(this);
    }

    public boolean isReference(PhpEvalEnvironment php) {
        return false;
    }

    public String getIdentifierName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isIdentifier() {
        return false;
    }



    public static PhpBooleanModel create(boolean v)
    {
      return v ? TRUE : FALSE;
    }


    public static final PhpBooleanModel TRUE = new PhpBooleanModel(true, true);
    public static final PhpBooleanModel FALSE = new PhpBooleanModel(false, true);

    private boolean value;
    
}
