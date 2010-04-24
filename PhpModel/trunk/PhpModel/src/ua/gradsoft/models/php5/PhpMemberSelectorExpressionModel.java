
package ua.gradsoft.models.php5;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Mdel for member selector
 * @author rssh
 */
public class PhpMemberSelectorExpressionModel implements PhpExpressionModel
{

    public PhpMemberSelectorExpressionModel(Term t, PhpCompileEnvironment pce)
    {
        frs = PhpExpressionModelHelper.create(t.getSubtermAt(0), pce);
        snd = PhpExpressionModelHelper.create(t.getSubtermAt(1), pce);
        cachedConstantName=null;
    }

    public PhpValueModel eval(PhpEvalEnvironment php) {
        PhpValueModel vfrs = frs.eval(php);
        PhpObjectModel ofrs = vfrs.getObject(php);
        PhpValueModel vsnd = snd.eval(php);
        PhpValueModel retval = ofrs.getMemberVariables().get(vsnd.getString(php));
        if (retval==null) {
            PhpMethodModel getMethod =
                    ofrs.getClassDeclaration().getMethods().get("__get");
            if (getMethod!=null) {
                List<PhpValueModel> getArgs = Collections.singletonList(vsnd);
                retval = getMethod.eval(php, vfrs, getArgs);
            }
        }
        if (retval==null) {
            php.warning("no such field:"+vsnd.getString(php));
            retval=PhpNullValueModel.INSTANCE;
        }
        return retval;
    }



    public String getIdentifierName() {
        throw new UnsupportedOperationException("Not supported.");
    }

    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        return new PhpMemberSelectorReferenceModel(this,php);
    }

    public boolean isIdentifier() {
        return false;
    }

    public boolean isReference(PhpEvalEnvironment php) {
        return true;
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        body[0] = frs.getTerm(pee);
        body[1] = snd.getTerm(pee);
        return PhpTermUtils.createContextTerm("MemberSelector", body, this);
    }


    public PhpExpressionModel  getFrs()
    { return frs; }

    public PhpExpressionModel  getSnd()
    { return snd; }

    private PhpExpressionModel       frs;
    private PhpExpressionModel       snd;
 
}
