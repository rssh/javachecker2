
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for PHP statement
 * @author rssh
 */
public class PhpForStatementModel extends PhpStatementModel
{

    public static PhpForStatementModel create(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
      return new PhpForStatementModel(t,pce);
    }

    public PhpForStatementModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
       if (t.getSubtermAt(0).isNil()) {
           initExpression=null;
       } else {
           initExpression=PhpExpressionModelHelper.create(t.getSubtermAt(0), pce);
       }
       if (t.getSubtermAt(1).isNil()) {
           checkExpression=null;
       } else {
           checkExpression=PhpExpressionModelHelper.create(t.getSubtermAt(1), pce);
       }
       if (t.getSubtermAt(2).isNil()) {
           incrExpression=null;
       } else {
           checkExpression=PhpExpressionModelHelper.create(t.getSubtermAt(2), pce);
       }
       statement = PhpStatementModel.create(t.getSubtermAt(3), pce);
    }

    @Override
    public void compile(PhpCompileEnvironment env) {
        statement.compile(env);
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        PhpValueModel v1;
        if (initExpression!=null) {
           v1 = initExpression.eval(env);
           if (env.getEvalState()!=EvalState.OK) {
               return;
           }
        }
        boolean continueLoop=true;
        while(continueLoop) {
           if (checkExpression!=null) {
               continueLoop = checkExpression.eval(env).getBoolean();
               if (env.getEvalState()!=EvalState.OK) {
                   return;
               }
           }
           if (!continueLoop) {
               return;
           }
           if (statement!=null) {
               statement.eval(env);
               switch(env.getEvalState()) {
                   case BREAK:
                   {
                     int x = env.decBreakOrContinueDepth();
                     if (x<=0) {
                         env.setEvalState(EvalState.OK);
                     }
                     return;
                   }
                   case CONTINUE:
                   {
                       int x = env.decBreakOrContinueDepth();
                       if (x<=0) {
                           env.setEvalState(EvalState.OK);
                       }else{
                           return;
                       }
                   }
                   break;
                   case OK:
                       break;
                   default:
                       return;
               }
           }
           if (incrExpression!=null) {
               incrExpression.eval(env);
           }
        }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[4];
        if (initExpression!=null) {
            body[0]=initExpression.getTerm(pee);
        }else{
            body[0]=PhpTermUtils.createNil();
        }
        if (checkExpression!=null) {
            body[1]=checkExpression.getTerm(pee);
        }else{
            body[1]=PhpTermUtils.createNil();
        }
        if (incrExpression!=null) {
            body[2]=initExpression.getTerm(pee);
        }else{
            body[2]=PhpTermUtils.createNil();
        }
        if (statement!=null) {
            body[3]=statement.getTerm(pee);
        } else {
            body[3]=PhpTermUtils.createNil();
        }
        Term retval = PhpTermUtils.getTermFactory().createTerm("ForStatement", body);
        retval = TermHelper.setAttribute(retval, "ctx", PhpTermUtils.createJTerm(this));
        return retval;
    }





    private PhpExpressionModel initExpression;
    private PhpExpressionModel checkExpression;
    private PhpExpressionModel incrExpression;
    private PhpStatementModel  statement;

}
