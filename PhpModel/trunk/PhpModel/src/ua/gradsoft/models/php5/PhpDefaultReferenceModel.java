
package ua.gradsoft.models.php5;

/**
 *Reference to PHP value
 * @author rssh
 */
public class PhpDefaultReferenceModel extends PhpPrimitiveReferenceModel
{

    public PhpDefaultReferenceModel(PhpValueModel value)
    {
      super(value);  
    }

    @Override
    public void set(PhpValueModel value) {
        origin=value;
    }

    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        return this;
    }

    public boolean isReference(PhpEvalEnvironment php) {
       return true;
    }

    public String getIdentifierName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isIdentifier() {
        return false;
    }



}
