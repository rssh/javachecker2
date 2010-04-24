
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author rssh
 */
public class PhpAssignmentExpressionModel implements PhpExpressionModel
{

    public PhpAssignmentExpressionModel(Term t, PhpCompileEnvironment pce)
            throws TermWareException
    {
       frs = PhpExpressionModelHelper.create(t.getSubtermAt(0), pce);
       op = PhpAssignmentOperator.find(t.getSubtermAt(1).getString());
       if (op==null) {
           throw new AssertException("Invalid assignment operator");
       }
       snd = PhpExpressionModelHelper.create(t.getSubtermAt(2), pce);
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        if (frs.isReference(php)) {
            return op.doOp(frs, snd, php);
        }else{
            throw new PhpEvalException(
                    -1, "first agrument of assignemt expression is not reference",
                    null,null,php);
        }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[3];
        Term[] opBody = new Term[1];
        opBody[0] = PhpTermUtils.createString(op.getOpString());
        body[0] = frs.getTerm(pee);
        body[1] = PhpTermUtils.createTerm("AssigmentOperator", opBody);
        body[2] = snd.getTerm(pee);
        return PhpTermUtils.createContextTerm("AssignmentExpression", body, frs);
    }

    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        throw new PhpEvalException("assigment expression can't be reference");
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




    private PhpExpressionModel frs;
    private PhpExpressionModel snd;
    private PhpAssignmentOperator op;

}
