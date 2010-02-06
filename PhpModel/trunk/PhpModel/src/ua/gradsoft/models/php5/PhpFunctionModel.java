package ua.gradsoft.models.php5;

import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for PHP function
 * @author rssh
 */
public class PhpFunctionModel extends PhpStatementModel implements PhpCallableModel
{

   public static PhpFunctionModel create(Term t, PhpCompileEnvironment pce)
                                                 throws TermWareException
   { return new PhpFunctionModel(t,pce); }

   public PhpFunctionModel(Term t, PhpCompileEnvironment pce)
                                                throws TermWareException
    {
      if (t.getName().equals("MemberFuctionDeclaration")) {
          // t.getSubtermAt(0) - actually we have no attributes.
          if (t.getSubtermAt(0).isNil()) {
             returnByReference = false;
          } else if (t.getSubtermAt(1).isBoolean()) {
              returnByReference = t.getSubtermAt(1).getBoolean();
          } else if (t.getSubtermAt(1).isAtom()) {
              returnByReference = true;
          }
          Term identifier = t.getSubtermAt(2);
          name = identifier.getSubtermAt(0).getString();
          parameters = PhpCallableModelHelper.parseSParametersList(t.getSubtermAt(3), pce);
          statement = PhpSCompoundStatementModel.create(t.getSubtermAt(4), pce);
      } else {
          throw new AssertException("MemberFunctionDeclaration exprected, have:"+TermHelper.termToString(t));
      }
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        env.getFunctions().put(name, this);
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[5];
        body[0] = PhpTermUtils.getTermFactory().createTerm("SMemberFunctionAttributes",
                                                           PhpTermUtils.createNil()
                                                          );
        body[1] = PhpTermUtils.createBoolean(returnByReference);
        body[2] = PhpTermUtils.createIdentifier(name);
        Term parmList = PhpTermUtils.createList(getParameters(), pee);
        body[3] = PhpTermUtils.getTermFactory().createTerm("SParameterList",parmList);
        body[4] = statement.getTerm(pee);
        return PhpTermUtils.createContextTerm("MemberFunctionDeclaration", body, this);
    }



    public String getName() {
        return name;
    }

    public List<PhpParameterModel> getParameters() {
        return parameters;
    }

    public PhpSCompoundStatementModel getStatement() {
        return statement;
    }

    public boolean isReturnByReference() {
        return returnByReference;
    }


    private String name;
    private List<PhpParameterModel> parameters;
    private boolean returnByReference;
    private PhpSCompoundStatementModel statement;
    //private PhpClassDeclarationModel classDeclarationModel;


}
