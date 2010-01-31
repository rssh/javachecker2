
package ua.gradsoft.models.php5;

/**
 *
 * @author rssh
 */
public class PhpIntegerModel extends PhpPrimitiveValueModel
{

    public PhpIntegerModel(int v)
    {
      value=v;
    }

    public boolean getBoolean() {
        return value!=0;
    }

    public double getFloat() {
        return (double)value;
    }

    public int getInt() {
        return value;
    }

    public String getString(PhpEvalEnvironment pee) {
        return Integer.toString(value);
    }

    public PhpType getType() {
        return PhpType.INTEGER;
    }

    int value;
}
