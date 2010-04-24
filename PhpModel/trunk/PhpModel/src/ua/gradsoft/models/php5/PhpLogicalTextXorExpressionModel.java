
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Module for php text xor expression
 * @author rssh
 */
public class PhpLogicalTextXorExpressionModel extends PhpBinaryExpressionModel
{

    public PhpLogicalTextXorExpressionModel(Term t, PhpCompileEnvironment pce)
                              throws TermWareException
    {
      super(t,pce);
    }

    public PhpValueModel eval(PhpEvalEnvironment pee) {
        PhpValueModel r1 = frs.eval(pee);
        if (snd==null) {
            return r1;
        }
        PhpValueModel r2 = snd.eval(pee);
        if (r1.getBoolean()) {
            if (r2.getBoolean()) {
              return PhpBooleanModel.FALSE;
            } else {
              return PhpBooleanModel.TRUE;
            }
        } else {
            if (r2.getBoolean()) {
                return PhpBooleanModel.TRUE;
            }else{
                return PhpBooleanModel.FALSE;
            }
        }
    }

    public String getName()
    { return "LogicalTextXorExpression"; }


}
