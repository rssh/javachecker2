
package ua.gradsoft.models.php5;

/**
 *
 * @author rssh
 */
public class PhpArrayElementReferenceModel extends PhpReferenceModel
{

    public PhpArrayElementReferenceModel(PhpValueModel key, PhpArrayModel array)
    {
        this.key=key;
        this.array=array;
    }

    @Override
    public void set(PhpValueModel value) {
        array.put(key, value);
    }


    PhpValueModel key;
    PhpArrayModel array;

}
