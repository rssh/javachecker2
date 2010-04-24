
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Base mode for unary expressions
 * @author rssh
 */
public class PhpUnaryExpressionModel implements PhpExpressionModel
{

    public PhpUnaryExpressionModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
        this(PhpExpressionModelHelper.create(t.getSubtermAt(1), pce) ,PhpUnaryOperator.findOp(t.getSubtermAt(0).getString()));
    }


    public PhpUnaryExpressionModel(PhpExpressionModel frs, PhpUnaryOperator op)
    {
      this.frs=frs;
      this.op = op;
    }

    public PhpUnaryOperator getOp()
    { return op; }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        return op.doOp(frs, php);
    }

    public boolean isReference(PhpEvalEnvironment php) {
        return op==PhpUnaryOperator.REF;
    }

   public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        if (op==PhpUnaryOperator.REF) {
            return frs.getReferenceModel(php);
        }else{
            php.error("attempt to get reference in non-reference operatpr");
            throw new UnsupportedOperationException("attempt to get reference in non-reference operator");
            //return null;
        }
    }


    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        body[0] = PhpTermUtils.createString(op.getSymbols());
        body[1] = frs.getTerm(pee);
        return PhpTermUtils.createContextTerm(op.getTermName(), body, this);
    }



    protected PhpExpressionModel frs;
    protected PhpUnaryOperator op;
}
