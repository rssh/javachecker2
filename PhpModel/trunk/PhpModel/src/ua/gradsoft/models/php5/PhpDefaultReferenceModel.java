
package ua.gradsoft.models.php5;

/**
 *Reference to PHP value
 * @author rssh
 */
public class PhpDefaultReferenceModel extends PhpReferenceModel
{

    public PhpDefaultReferenceModel(PhpValueModel value)
    {
      super(value);  
    }

    @Override
    public void set(PhpValueModel value) {
        origin=value;
    }

}
