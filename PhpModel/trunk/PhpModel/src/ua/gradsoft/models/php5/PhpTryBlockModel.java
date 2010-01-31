package ua.gradsoft.models.php5;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of try block
 * @author rssh
 */
public class PhpTryBlockModel extends PhpStatementModel
{

    
    public static PhpTryBlockModel create(Term t, PhpCompileEnvironment pce)
                                                   throws TermWareException
    { return new PhpTryBlockModel(t,pce); }

    public PhpTryBlockModel(Term t, PhpCompileEnvironment pce)
                                                   throws TermWareException
    {
       internalStatement = new PhpSCompoundStatementModel(t.getSubtermAt(0),pce);
       Term l = t.getSubtermAt(1);
       catchBlocks = new LinkedList<PhpCatchBlockModel>();
       while(!l.isNil()) {
           Term ct = l.getSubtermAt(0);
           l = l.getSubtermAt(1);
           catchBlocks.add(new PhpCatchBlockModel(ct));
       }
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        internalStatement.eval(env);
        if (env.getEvalState()==EvalState.THROW) {
            for(PhpCathBlockModel cb: catchBlocks) {
                if (match(env.getThrowedException(),cb.getException())) {
                    env.setEvalState(EvalState.OK);
                    cb.getStatement().eval(env);
                    return;
                }
            }
        }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        throw new UnsupportedOperationException("Not supported yet.");
    }



    private PhpSCompoundStatementModel internalStatement;
    private List<PhpCatchBlockModel> catchBlocks;

}
