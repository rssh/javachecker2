
package ua.gradsoft.models.php5;

/**
 *
 * @author rssh
 */
public interface  PhpExpressionModel extends PhpElementModel
{

    public PhpValueModel eval(PhpEvalEnvironment php);

    public boolean isReference(PhpEvalEnvironment php);
    
    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php);

    public boolean isIdentifier();

    public String  getIdentifierName();

}
