
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *Php parameter model
 * @author rssh
 */
public class PhpParameterModel implements PhpElementModel
{

    /**
     * create parametert model
     * @param t  - must be 'Parameter' term
     * @param pce - compile environment
     */
    public PhpParameterModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
       if (t.getSubtermAt(0).isNil()) {
           typeHint=null;
       } else {
           typeHint=t.getSubtermAt(0).getSubtermAt(0).getString();
       }
       byReference = t.getSubtermAt(1).getBoolean();
       Term varTerm = t.getSubtermAt(2);
       name=varTerm.getSubtermAt(1).getSubtermAt(0).getString();
       if (t.getSubtermAt(3).isNil()) {
           initExpression=null;
       }else{
           initExpression=PhpExpressionModelHelper.create(t.getSubtermAt(3), pce);
       }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[4];
        if (typeHint==null) {
            body[0] = PhpTermUtils.createNil();
        } else {
            body[0] = PhpTermUtils.createIdentifier(typeHint);
        }
        body[1] = PhpTermUtils.createIdentifier(name);
        body[2] = PhpTermUtils.createBoolean(byReference);
        if (initExpression==null) {
            body[3] = PhpTermUtils.createNil();
        } else {
            body[3] = initExpression.getTerm(pee);
        }
        Term retval = PhpTermUtils.getTermFactory().createComplexTerm("Parameter", body);
        retval = TermHelper.setAttribute(retval, "ctx", PhpTermUtils.createJTerm(this));
        return retval;
    }
    
    
    
    
    /**
     * @return name of parameter
     */
    public String getName()
    {
      return name;  
    }
    
    /**
     * @return type hint. May be null.
     */
    public String getTypeHint()
    { return typeHint; }
    
    
    /**
     * @return true if parameter is passed by reference
     */
    public boolean isByReference()
    {
      return byReference;  
    }

    /**
     * @return init expression is set, otherwise null.
     */
    public PhpExpressionModel  getInitExpression()
    {
      return initExpression;
    }

    private String typeHint;
    private String name;
    private boolean byReference;
    private PhpExpressionModel initExpression;

}
