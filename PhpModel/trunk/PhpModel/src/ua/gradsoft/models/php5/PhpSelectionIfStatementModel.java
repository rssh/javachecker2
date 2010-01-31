
package ua.gradsoft.models.php5;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for php 'If' statement
 * @author rssh
 */
public class PhpSelectionIfStatementModel extends PhpStatementModel
{

    
    public PhpSelectionIfStatementModel(Term t, PhpCompileEnvironment env) throws TermWareException
    {
       expression=PhpExpressionModelHelper.create(t.getSubtermAt(0), env);
       if (!t.getSubtermAt(1).isNil()) {
           trueStatement = PhpStatementModel.create(t.getSubtermAt(1), env);
       } else {
           trueStatement = null;
       }
       if (t.getSubtermAt(2).isNil()) {
           elseIfClauses = Collections.emptyList();
       }else{
           elseIfClauses = new LinkedList<PhpSelectionElseIfClauseModel>();
           Term c = t.getSubtermAt(2).getSubtermAt(0);
           while(!c.isNil()) {
               PhpSelectionElseIfClauseModel cm = new PhpSelectionElseIfClauseModel(c.getSubtermAt(0),env);
               elseIfClauses.add(cm);
               c=c.getSubtermAt(1);
           }       
       }
       if (t.getSubtermAt(3).isNil()) {
           elseClause=null;
       }else{
           elseClause = PhpStatementModel.create(t.getSubtermAt(1), env);
       }
    }


    @Override
    public void compile(PhpCompileEnvironment env) {
        trueStatement.compile(env);
        for(PhpSelectionElseIfClauseModel ei:elseIfClauses) {
            ei.compile(env);
        }
        if (elseClause!=null) {
           elseClause.compile(env);
        }
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        PhpValueModel v = expression.eval(env);
        if (env.getEvalState()!=EvalState.OK) {
            return;
        }
        if (v.getBoolean()) {
            if (trueStatement!=null) {
               trueStatement.eval(env);
            }
        }else{
            for(PhpSelectionElseIfClauseModel ei:elseIfClauses) {
                boolean b = ei.getCondition().eval(env).getBoolean();
                if (env.getEvalState()!=EvalState.OK) {
                    return;
                }
                if (b) {
                    if (ei.getStatement()!=null) {
                      ei.getStatement().eval(env);
                    }
                    return;
                }
            }
            if (elseClause!=null) {
               elseClause.eval(env);
            }
        }
    }


    private PhpExpressionModel expression;
    private PhpStatementModel  trueStatement;
    private List<PhpSelectionElseIfClauseModel> elseIfClauses;
    private PhpStatementModel  elseClause;

}
