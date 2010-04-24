
package ua.gradsoft.models.php5;

/**
 *
 * @author rssh
 */
public interface PhpReferenceModel extends PhpValueModel
{

    public void  set(PhpValueModel value);

    /**
     * set this reference to be proxy
     * @param ref
     */
    //public void  setReference(PhpReferenceModel ref);

}
