
package ua.gradsoft.models.php5;

import java.util.Map;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpForEachStatementModel extends PhpStatementModel
{

    public PhpForEachStatementModel(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
        if (t.getSubtermAt(0).isNil()) {
            expression=null;
        } else {
            expression = PhpExpressionModelHelper.create(t, pce);
        }
        if (t.getSubtermAt(1).isNil()) {
            withKey=false;
            keyVariableName=null;
        } else {
            withKey=true;
            keyVariableName=t.getSubtermAt(1).getSubtermAt(1).getString();
        }
        valueVariableName=t.getSubtermAt(2).getSubtermAt(1).getString();
        statement=PhpStatementModel.create(t.getSubtermAt(3), pce);
    }

    @Override
    public void compile(PhpCompileEnvironment env) {
        statement.compile(env);
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        PhpArrayModel arr = expression.eval(env).getArray(env);
        for(Map.Entry<PhpValueModel,PhpValueModel> e:arr.getMap().entrySet()){
           if (withKey) {
               env.getLocals().bind(keyVariableName, e.getKey());
           }
           if (byReference) {
               env.bindByReference(valueVariableName,e.getValue());
           } else {
               env.bindByValue(valueVariableName,e.getValue());
           }
           statement.eval(env);
           switch(env.getEvalState()) {
               case OK:
                   break;
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
               default:
                   return;
           }
        }
    }



    private PhpExpressionModel expression;
    boolean withKey;
    private String  keyVariableName;
    private String  valueVariableName;
    private boolean byReference;
    private PhpStatementModel  statement;
}
