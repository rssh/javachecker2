
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for declare statement.
 */
public class PhpDeclareStatementModel extends PhpStatementModel
{

    public PhpDeclareStatementModel(Term t, PhpCompileEnvironment pce)
                                                throws TermWareException
    {
      Term assignment = t.getSubtermAt(0);
      if (!assignment.getName().equals("AssignmentExpression")) {
          throw new AssertException("AssignmentExpression expected, have:"+TermHelper.termToString(assignment));
      }
      Term propertyTerm = assignment.getSubtermAt(0);
      if (!propertyTerm.getName().equals("Constant")) {
          throw new AssertException("Constant expected, have:"+TermHelper.termToString(propertyTerm));
      }
      declaredProperty = propertyTerm.getSubtermAt(0).getSubtermAt(0).getString();
      declaredExpression = PhpExpressionModelHelper.create(assignment.getSubtermAt(2), pce);
      statement = PhpStatementModel.create(t.getSubtermAt(1), pce);
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        if (declaredProperty.equals("ticks")) {
            PhpValueModel v = declaredExpression.eval(env);
            env.setTick(v.getInt());
            statement.eval(env);
            env.unsetTick();
        }else{
            throw new PhpEvalException("Unknown declare constant:"+declaredProperty,this,env);
        }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        Term[] asgnmBody = new Term[3];
        Term pId = PhpTermUtils.createIdentifier(declaredProperty);
        Term assEq = PhpTermUtils.createTerm("AssignmentOperator",PhpTermUtils.createString("="));
        asgnmBody[0]=pId;
        asgnmBody[1]=assEq;
        asgnmBody[2]=declaredExpression.getTerm(pee);
        body[0]=PhpTermUtils.createTerm("AssignmentExpression", asgnmBody);
        body[1]=statement.getTerm(pee);
        return PhpTermUtils.createContextTerm("DeclareStatement", body, this);
    }



    private String declaredProperty;
    private PhpExpressionModel declaredExpression;
    private PhpStatementModel statement;
}
