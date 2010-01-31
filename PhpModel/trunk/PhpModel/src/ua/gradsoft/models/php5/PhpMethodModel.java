
package ua.gradsoft.models.php5;

import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model of Php Method
 * @author rssh
 */
public class PhpMethodModel implements PhpCallableModel
{
    
    public PhpMethodModel(Term t, PhpClassDeclarationModel owner, PhpCompileEnvironment pce)
                                                throws TermWareException
    {
      if (t.getName().equals("MemberFuctionDeclaration")) {
          parseAttributes(t.getSubtermAt(0));
          returnByReference = (!t.getSubtermAt(1).isNil());
          Term identifier = t.getSubtermAt(2);
          name = identifier.getSubtermAt(0).getString();
          parseSParameterList(t.getSubtermAt(3));
          statement = PhpSCompoundStatementModel.create(t.getSubtermAt(4), pce);
      } else if (t.getName().equals("MemberAbstractFunctionDeclaration")) {
          parseAttributes(t.getSubtermAt(0));
          returnByReference = t.getSubtermAt(1).getBoolean();
          Term identifier = t.getSubtermAt(2);
          name = identifier.getSubtermAt(0).getString();
          parseSParameterList(t.getSubtermAt(3));
          statement = null;         
      } else {
          throw new AssertException("MemberFunctionDeclaration or MemberAbstractFunctionDeclaratio exprected, have:"+TermHelper.termToString(t));
      } 
    }


    public String getName() {
        return name;
    }

    public boolean isAbstract()
    {
        return statement==null;
    }

    public List<PhpParameterModel> getParameters() {
        return parameters;
    }

    public PhpSCompoundStatementModel getStatement() {
        return statement;
    }

    public boolean isFinal()
    {
        return finalFlag;
    }

    public boolean isStatic()
    {
        return staticFlag;
    }


    public boolean isReturnByReference() {
        return returnByReference;
    }

    public PhpClassDeclarationModel getClassDeclarationModel()
    {
       return classDeclarationModel;
    }



    private void parseAttributes(Term t) throws TermWareException
    {
      finalFlag=false;
      staticFlag=false;
      visibility = PhpVisibility.PUBLIC;
      Term l = t.getSubtermAt(0);
      while(!l.isNil()) {
          Term attr = l.getSubtermAt(0);
          l=l.getSubtermAt(1);
          if (attr.getName().equals("final")) {
              finalFlag=true;
          }else if (attr.getName().equals("static")) {
              staticFlag=true;
          } else if (attr.getName().equals("public")) {
              visibility = PhpVisibility.PUBLIC;
          } else if (attr.getName().equals("protected")) {
              visibility = PhpVisibility.PROTECTED;
          } else if (attr.getName().equals("private")) {
              visibility = PhpVisibility.PRIVATE;
          } else {
              throw new AssertException("Invalid member function attribute:"+t.getName());
          }
      }
    }


    private String name;
    private PhpVisibility visibility;
    private boolean  finalFlag;
    private boolean  staticFlag;
    private List<PhpParameterModel> parameters;
    private boolean returnByReference;
    private PhpSCompoundStatementModel statement;
    private PhpClassDeclarationModel classDeclarationModel;



}
