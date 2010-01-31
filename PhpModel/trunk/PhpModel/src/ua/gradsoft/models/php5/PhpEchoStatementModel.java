
package ua.gradsoft.models.php5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author rssh
 */
public class PhpEchoStatementModel extends PhpStatementModel
{

    public static PhpEchoStatementModel create (Term t, PhpCompileEnvironment pce)
                                           throws TermWareException

    {
        return new PhpEchoStatementModel(t,pce);
    }


    public PhpEchoStatementModel(Term t, PhpCompileEnvironment pce)
                                           throws TermWareException
    {
       Term kt = t.getSubtermAt(0);
       Term l = t.getSubtermAt(1).getSubtermAt(0);
       if (kt.getName().equals("echo")) {
           kind=Kind.ECHO;
       }else if (kt.getName().equals("print")) {
           kind=Kind.PRINT;
       }else{
           throw new AssertException("echo or print required, instead:"+TermHelper.termToString(t));
       }
       expressions=new ArrayList<PhpExpressionModel>();
       while(!l.isNil()) {
           Term ct = l.getSubtermAt(0);
           l=l.getSubtermAt(1);
           PhpExpressionModel expr = PhpExpressionModelHelper.create(ct, pce);
           expressions.add(expr);
       }
    }

    @Override
    public void compile(PhpCompileEnvironment env) {
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        for(PhpExpressionModel e: expressions) {
            String s = e.eval(env).getString(env);
            try {
              env.getIO().getOutWriter().write(s);
            }catch(IOException ex){
                throw new PhpEvalException("Exception in echo statement",ex);
            }
        }
    }

    public enum Kind
    {
      ECHO, PRINT
    };

    public Kind getKind()
    { return kind; }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body=new Term[3];
        body[0] = PhpTermUtils.createAtom((kind==Kind.ECHO) ? "echo" : "print");
        body[1] = PhpTermUtils.createList(expressions);
        body[2] = PhpTermUtils.createEndOfExpression();
        Term retval = PhpTermUtils.getTermFactory().createTerm("EchoStatement",body);
        retval = TermHelper.setAttribute(retval, "ctx", PhpTermUtils.createJTerm(this));
        return retval;
    }



    private Kind kind;

    private List<PhpExpressionModel> expressions;
}
