package ua.gradsoft.models.php5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *expression for method call
 * @author rssh
 */
public abstract class PhpMethodCallExpressionModel implements PhpExpressionModel
{

    public static PhpMethodCallExpressionModel create(Term t, PhpCompileEnvironment pce)
    {
      Term frs = t.getSubtermAt(0);
      Term snd = t.getSubtermAt(1);
      if (frs.getName().equals("Identifier")) {
          return new PhpFunctionCallExpressionModel(frs,pce);
      }else if (frs.getName().equals("MemberSelector")) {
          PhpExpressionModel obj = PhpExpressionModelHelper.create(frs.getSubtermAt(0), pce);

          return new PhpObjectMethodCallExpressionModel(
                                                        frs.getSubtermAt(0),
                                                        frs.getSubtermAt(1),
                                                        snd,
                                                        pce);
      }else if (frs.getName().equals("ScopeResolution")) {
          return new PhpClassMethodCallExpressionModel(frs.getSubtermAt(0),
                                                       frs.getSubtermAt(1),
                                                       snd,
                                                       pce);
      // TODO: add anonomous function for closure
      }else{
          PhpExpressionModel efrs = PhpExpressionModelHelper.create(t, pce);
          return new PhpExpressionMethodCallExpressionModel(frs,snd,pce);
      }
    }

    public PhpReferenceModel getReferenceModel(PhpEvalEnvironment php) {
        throw new UnsupportedOperationException("MethodCallExpressionModel.getReferenceModel is unsupported");
    }

    public boolean isReference(PhpEvalEnvironment php) {
        return false;
    }

    public String getIdentifierName() {
        throw new UnsupportedOperationException("Not identifer");
    }

    public boolean isIdentifier() {
        return false;
    }



    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
       Term[] body = new Term[2];
       body[0] = getFirstSubterm(pee);
       body[1] = PhpTermUtils.createList(params, pee);
       return PhpTermUtils.createContextTerm("MethodCall", body, this);
    }

    public abstract Term getFirstSubterm(PhpEvalEnvironment pee) throws TermWareException;

    protected List<PhpExpressionModel> parseParams(Term l, PhpCompileEnvironment pce)
                                                               throws TermWareException
    {
       if (l.isNil()) {
           return Collections.emptyList();
       } else {
           List<PhpExpressionModel> retval = new ArrayList<PhpExpressionModel>();
           while(!l.isNil()) {
               Term c = l.getSubtermAt(0);
               l=l.getSubtermAt(1);
               PhpExpressionModel e = PhpExpressionModelHelper.create(c, pce);
               retval.add(e);
           }
           return retval;
       }
    }

    protected List<PhpValueModel> evalParams(PhpEvalEnvironment pee)
    {
        List<PhpValueModel> retval = new ArrayList<PhpValueModel>();
        for(PhpExpressionModel p: params) {
            PhpValueModel v = p.eval(pee);
            if (pee.getEvalState()!=EvalState.OK) {
                return retval;
            }
            retval.add(v);
        }
        return retval;
    }
    
    protected List<PhpExpressionModel>  params;
}
