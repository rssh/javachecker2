
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
    
    public PhpMethodModel(Term t, PhpClassOrInterfaceDeclarationModel owner, PhpCompileEnvironment pce)
                                                throws TermWareException
    {
      if (t.getName().equals("MemberFuctionDeclaration")) {
          parseAttributes(t.getSubtermAt(0));
          returnByReference = t.getSubtermAt(1).getBoolean();
          Term identifier = t.getSubtermAt(2);
          name = identifier.getSubtermAt(0).getString();
          parameters = PhpCallableModelHelper.parseSParametersList(t.getSubtermAt(3),pce);
          statement = PhpSCompoundStatementModel.create(t.getSubtermAt(4), pce);
      } else if (t.getName().equals("MemberAbstractFunctionDeclaration")) {
          parseAttributes(t.getSubtermAt(0));
          returnByReference = t.getSubtermAt(1).getBoolean();
          Term identifier = t.getSubtermAt(2);
          name = identifier.getSubtermAt(0).getString();
          parameters = PhpCallableModelHelper.parseSParametersList(t.getSubtermAt(3), pce);
          statement = null;
      } else if (t.getName().equals("InterfaceMethodDeclaration")) {
          parseAttributes(t.getSubtermAt(0));
          returnByReference = t.getSubtermAt(1).getBoolean();
          Term identifier = t.getSubtermAt(2);
          name = identifier.getSubtermAt(0).getString();
          parameters = PhpCallableModelHelper.parseSParameterList(t.getSubtermAt(3), pce);
          if (t.getSubtermAt(4).getBoolean()) {
              statement = PhpSCompoundStatementModel.create(t.getSubtermAt(4), pce);
          } else {
              statement = null;
          }
      } else {
          throw new InvalidPhpTermExpression("MemberFunctionDeclaration or MemberAbstractFunctionDeclaration or InterfaceMethodDeclaration exprected, have:"+TermHelper.termToString(t),t);
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

    public PhpClassOrInterfaceDeclarationModel getClassOrInterfaceDeclarationModel()
    {
       return classDeclarationModel;
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        int n = (classDeclarationModel.isInterface()) ? 6 : 5;
        Term body[] = new Term[n];
        body[0]=generateAttributesTerm();
        body[1]=PhpTermUtils.createBoolean(returnByReference);
        body[2]=PhpTermUtils.createIdentifier(getName());
        body[3]=PhpTermUtils.createList(parameters,pee);
        if (isAbstract()) {
          body[4]=PhpTermUtils.createNil();
        } else {
          body[4]=statement.getTerm(pee);
        }
        Term retval;
        if (classDeclarationModel.isClass()) {
            if (isAbstract()) {
                retval = PhpTermUtils.createContextTerm("MemberAbstractFunctionDeclaration",
                                                        body, this);
            } else {
                retval = PhpTermUtils.createContextTerm("MemberFunctionDeclaration", body, this);
            }
        }else{
            body[5]=PhpTermUtils.createEndOfStatement();
            retval=PhpTermUtils.createContextTerm("InterfaceMethodDeclaration", body, this);
        }
        return retval;
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


    public PhpValueModel eval(PhpEvalEnvironment pee, PhpValueModel thisValue, List<PhpValueModel> paramValues)
    {
      if (paramValues.size()>parameters.size()) {
          pee.error("wrong number of arguments in call of "+name);
          return PhpNullValueModel.INSTANCE;
      }
      PhpFlatVarContext ctx =  pee.pushNewContext();
      for(int i=0; i<parameters.size(); ++i) {
        PhpParameterModel pm = parameters.get(i);
        if (i < paramValues.size()) {
          if (pm.isByReference()) {
            ctx.bind(pm.getName(), paramValues.get(i).copyByReference(pee));
          } else {
            ctx.bind(pm.getName(), paramValues.get(i).copyByValue(pee));
          }
          if (pm.getTypeHint()!=null) {
            if (!checkTypeHint(pm.getTypeHint(),paramValues.get(i))) {
                pee.error("does not conform typeHint in method"+getName()+" parameter "+pm.getName());
            }
          }
        } else {
          if (pm.getInitExpression()!=null) {
              PhpValueModel v = pm.getInitExpression().eval(pee);
              if (pee.getEvalState()!=EvalState.OK) {
                  pee.popContext();
                  return PhpNullValueModel.INSTANCE;
              }
              ctx.bind(pm.getName(), v);
          } else {
             pee.error("wrong number of arguments in call of "+name);
             pee.popContext();
             return PhpNullValueModel.INSTANCE;
          }
        }
      }
      ctx.bind("this", thisValue);
      pee.pushCurrentClassDeclaration(this);
      statement.eval(pee);
      if (pee.getEvalState()==EvalState.RETURN) {
          pee.setEvalState(EvalState.OK);
      }
      php.popCurrentClassDeclaration();
      pee.popNewContext();
      return pee.popLastReturn();
    }

    private boolean checkTypeHint(String hint, PhpValueModel v)
    {
      return true; 
    }

    private String name;
    private PhpVisibility visibility;
    private boolean  finalFlag;
    private boolean  staticFlag;
    private List<PhpParameterModel> parameters;
    private boolean returnByReference;
    private PhpSCompoundStatementModel statement;
    private PhpClassOrInterfaceDeclarationModel classDeclarationModel;



}
