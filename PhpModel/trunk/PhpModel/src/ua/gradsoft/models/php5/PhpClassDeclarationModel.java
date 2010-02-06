
package ua.gradsoft.models.php5;

import java.util.HashMap;
import java.util.Map;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for class
 * @author rssh
 */
public class PhpClassDeclarationModel extends PhpStatementModel
{

    public PhpClassDeclarationModel(Term t, PhpCompileEnvironment e) throws TermWareException
    {
      if(t.getName().equals("ClassDeclaration")) {
          Term idTerm = t.getSubtermAt(1);
          term=t;
          name=idTerm.getSubtermAt(0).getString();
      }else{
        throw new AssertException("ClassDeclaration exprected");
      }
      Term sclassMembers = t.getSubtermAt(3);
      Term ct = sclassMembers.getSubtermAt(0);
      while(!ct.isNil()) {
          Term mb = ct.getSubtermAt(0);
          ct=ct.getSubtermAt(1);
          addMember(mb,e);
      }
    }

    public String getName()
    { return name; }

    @Override
    public void eval(PhpEvalEnvironment env) {
        env.addClassDeclarationModel(this);
    }

    private void addMember(Term md, PhpCompileEnvironment pce) throws TermWareException
    {
      if (md.getName().equals("MemberFunctionDeclaration")) {
          addMemberFunctionDeclaration(md,pce);
      } else if (md.getName().equals("MemberConstantDeclaration")) {
          addMemberConstantDeclaration(md,pce);
      } else if (md.getName().equals("MemberVariablesDeclaration")) {
          addMemberVariablesDeclaration(md,pce);
      } else if (md.getName().equals("MemberGlobalsDeclaration")) {
          addMemberGlobalsDeclaration(md,pce);
      } else if (md.getName().equals("MemberAbstractFunctionDeclaration")) {
          addMemberAbstractFunctionDeclaration(md,pce);
      } else {
          throw new AssertException("Invalid class member:"+TermHelper.termToString(md));
      }
    }

    private void addMemberFunctionDeclaration(Term md, PhpCompileEnvironment pce) throws TermWareException
    {
      PhpMethodModel m = new PhpMethodModel(md,this,pce);
      if (methods==null) {
          methods=new HashMap<String,PhpMethodModel>();
      }
      methods.put(m.getName(), m);
    }



    private String name;
    private Term term;
    private Map<String,PhpValueModel> constants;
    private Map<String,PhpMethodModel>  methods;

}
