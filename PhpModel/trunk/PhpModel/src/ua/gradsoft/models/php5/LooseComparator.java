
package ua.gradsoft.models.php5;

import java.util.Comparator;

/**
 *Loose comparator
 * @author rssh
 */
public class LooseComparator implements Comparator<PhpValueModel>
{

    public LooseComparator(PhpEvalEnvironment env)
    {
        evalEnvironment=env;
    }

    public PhpEvalEnvironment getEvalEnvironment() {
        return evalEnvironment;
    }

    public void setEvalEnvironment(PhpEvalEnvironment evalEnvironment) {
        this.evalEnvironment = evalEnvironment;
    }

    public int compare(PhpValueModel x, PhpValueModel y) {
        return PhpValueHelper.looseComparison(x, y, evalEnvironment);
    }


    private PhpEvalEnvironment evalEnvironment;
}
