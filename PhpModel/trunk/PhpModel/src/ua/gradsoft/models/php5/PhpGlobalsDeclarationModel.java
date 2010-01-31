
package ua.gradsoft.models.php5;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for globals declaration
 * @author rssh
 */
public class PhpGlobalsDeclarationModel extends PhpStatementModel
{


    public static PhpGlobalsDeclarationModel create(Term t, PhpCompileEnvironment pce)
                                                                 throws TermWareException
    {
      return new PhpGlobalsDeclarationModel(t,pce);
    }

    public PhpGlobalsDeclarationModel(Term t, PhpCompileEnvironment pce)
                                                   throws TermWareException
    {
        Term l = t.getSubtermAt(0);
        variables = new LinkedList<PhpVariableModel>();
        while(!l.isNil()) {
            Term ct = l.getSubtermAt(0);
            l=l.getSubtermAt(1);
            variables.add(new PhpVariableModel(ct,pce));
        }
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        for(PhpVariableModel v: variables) {
            String s = v.getLastVarName(env);
            env.addGlobalReference(s);
        }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[2];
        body[0] = PhpTermUtils.createList(variables);
        body[1] = PhpTermUtils.createEndOfStatement();
        return PhpTermUtils.createContextTerm("MemberGlobalsDeclaration",body,this);
    }



    private List<PhpVariableModel>  variables;
}
