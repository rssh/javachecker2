
package ua.gradsoft.models.php5;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;

/**
 *Model for varibales declaration
 * @author rssh
 */
public class PhpVariablesDeclarationModel extends PhpStatementModel
{

    public PhpVariablesDeclarationModel(Term t, PhpCompileEnvironment pce)
    {
       parseAttributes(t.getSubtermAt(0));
       Term vars = t.getSubtermAt(1);
       declarations = new LinkedList<PhpVariableDeclarationModel>();
       while(!vars.isNil()) {
           Term ct = vars.getSubtermAt(0);
           vars=vars.getSubtermAt(1);
           declarations.put(new PhpVariableDeclarationModel(ct,pce));
       }
    }
    
    
    
    private PhpVisibility visibility;
    private boolean       staticFlag;
    private List<PhpVariableDeclarationModel> declarations;
}
