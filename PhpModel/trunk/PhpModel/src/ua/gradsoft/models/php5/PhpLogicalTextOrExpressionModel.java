
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;

/**
 *
 * @author rssh
 */
public class PhpLogicalTextOrExpressionModel implements PhpExpressionModel
{

    public PhpValueModel eval(PhpEvalEnvironment pee) {
        PhpValueModel r1 = frs.eval(pee);
        if (snd==null) {
            return r1;
        }
        if (r1.getBoolean()) {
            return PhpBooleanModel.TRUE;
        }
        PhpValueModel r2 = snd.eval(pee);
        return (r2.getBoolean() ? PhpBooleanModel.TRUE : PhpBooleanModel.FALSE);
    }

    public PhpExpressionModel getFrs() {
        return frs;
    }

    public void setFrs(PhpExpressionModel frs) {
        this.frs = frs;
    }

    public PhpExpressionModel getSnd() {
        return snd;
    }

    public void setSnd(PhpExpressionModel snd) {
        this.snd = snd;
    }

    public Term getTerm() {
        return term;
    }

    public void setTerm(Term t) {
        this.term = t;
    }

    private Term term;
    private PhpExpressionModel frs;
    private PhpExpressionModel snd;

}
