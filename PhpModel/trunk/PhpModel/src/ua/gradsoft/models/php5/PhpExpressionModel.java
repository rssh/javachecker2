
package ua.gradsoft.models.php5;

/**
 *
 * @author rssh
 */
public interface  PhpExpressionModel extends PhpElementModel
{

    public PhpValueModel eval(PhpEvalEnvironment php);

}
