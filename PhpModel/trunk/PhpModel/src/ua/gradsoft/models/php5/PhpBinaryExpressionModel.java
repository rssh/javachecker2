
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author rssh
 */
public abstract class PhpBinaryExpressionModel implements PhpExpressionModel
{

    public PhpBinaryExpressionModel(Term tfrs, Term tsnd, PhpBinaryOperator inOp, PhpCompileEnvironment pce)
                              throws TermWareException
    {
        frs = PhpExpressionModelHelper.create(tfrs, pce);
        snd = PhpExpressionModelHelper.create(tsnd, pce);
        op = inOp;
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

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[3];
        body[0] = frs.getTerm(pee);
        body[1] = PhpTermUtils.createString(op.getSymbols());
        body[2] = snd.getTerm(pee);
        return PhpTermUtils.createContextTerm(op.getTermName(), body, this);
    }


    public String getName()
    { return getOp().getName(); }

    public PhpBinaryOperator getOp()
    { return op; }

    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        throw new UnsupportedOperationException("Binary operation cn't be reference");
    }

    public boolean isReference(PhpEvalEnvironment php) {
        return false;
    }

    public String getIdentifierName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public boolean isIdentifier() {
        return false;
    }



    public PhpValueModel eval(PhpEvalEnvironment php) {
        return op.doOp(frs, snd, php);
    }


    protected PhpExpressionModel frs;
    protected PhpExpressionModel snd;
    protected PhpBinaryOperator  op;
}
