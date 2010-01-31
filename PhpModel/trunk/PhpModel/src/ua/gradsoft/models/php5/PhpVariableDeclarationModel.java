
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *VariableDeclaration
 * @author rssh
 */
public class PhpVariableDeclarationModel implements PhpElementModel
{

    public PhpVariableDeclarationModel(Term t, PhpCompileEnvironment pce)
    {
       name = t.getSubtermAt(0).getSubtermAt(0).getString();
       if (t.getSubtermAt(1).isNil()) {
           initExpression=null;
       } else {
           initExpression=PhpExpressionModelHelper.create(t, pce);
       }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
       Term body[]= new Term[2];
       body[0] = PhpTermUtils.createIdentifier(name);
       body[1] = initExpression==null ? PhpTermUtils.createNil() : initExpression.getTerm(pee);
       return PhpTermUtils.createTermWithContext("VariableDecl",body,this);
    }

    public String getName()
    { return name; }

    public PhpExpressionModel getInitExpression()
    { return initExpression; }


    private String name;
    private PhpExpressionModel initExpression;
}
