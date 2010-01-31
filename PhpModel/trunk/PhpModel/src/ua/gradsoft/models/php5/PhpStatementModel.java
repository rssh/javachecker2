package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;


public abstract class PhpStatementModel implements PhpElementModel
{

  public static PhpStatementModel create(Term t, PhpCompileEnvironment env)
                                                  throws TermWareException
  {
   PhpStatementModel retval=null;
   if (t.getName().equals("GotoLabeledStatement")) {
     retval = PhpGotoLabeledStatementModel.create(t,env);
   } else if (t.getName().equals("ClassDeclaration")) {
     retval = PhpClassDeclarationModel.create(t,env);
   } else if (t.getName().equals("InterfaceDeclaration")) {
     retval = PhpInterfaceDeclarationModel.create(t,env);
   } else if (t.getName().equals("ExpressionStatement")) {
     retval = PhpExpressionStatementModel.create(t,env);
   } else if (t.getName().equals("SCompoundStatement")) {
     retval = PhpSCompoundStatementModel.create(t,env);
   } else if (t.getName().equals("SelectionIfStatement")) {
     retval = PhpSelectionIfStatementModel.create(t,env);
   } else if (t.getName().equals("SelectionSwitchStatement")) {
     retval = PhpSelectionSwitchStatementModel.create(t,env);
   //} else if (t.getName().equals("IterationStatement")) {
   //  return PhpIterationStatementModel.create(t,env);
   } else if (t.getName().equals("WhileStatement")) {
     retval = PhpWhileStatementModel.create(t,env);
   } else if (t.getName().equals("DoStatement")) {
     retval = PhpDoStatementModel.create(t,env);
   } else if (t.getName().equals("ForStatement")) {
     retval = PhpForStatementModel.create(t,env);
   } else if (t.getName().equals("ForEachStatement")) {
     retval = PhpForEachStatementModel.create(t,env);
   //} else if (t.getName().equals("JumpStatement")) {
   } else if (t.getName().equals("ContinueStatement")) {
     retval = PhpContinueStatementModel.create(t,env);
   } else if (t.getName().equals("BreakStatement")) {
     retval = PhpBreakStatementModel.create(t,env);
   } else if (t.getName().equals("RetiurnStatement")) {
     retval = PhpReturnStatementModel.create(t,env);
   } else if (t.getName().equals("GotoStatement")) {
     retval = PhpGotoStatementModel.create(t,env);
   } else if (t.getName().equals("IncludeStatement")) {
     retval = PhpIncludeStatementModel.create(t,env);
   } else if (t.getName().equals("EchoStatement")) {
     retval = PhpEchoStatementModel.create(t,env);
   } else if (t.getName().equals("DefineStatement")) {
     retval = PhpDefineStatementModel.create(t,env);
   //} else if (t.getName().equals("MemberDeclaration")) {
   //  return PhpMemberDeclarationStatementModel.create(t,env);
   } else if (t.getName().equals("MemberFunctionDeclaration")) {
       retval = PhpFunctionModel.create(t,env);
   } else if (t.getName().equals("MemberConstantDeclaration")) {
       retval = PhpConstantDeclarationModel.create(t,env);
   } else if (t.getName().equals("MemberVariablesDeclaration")) {
       retval = PhpVariablesDeclarationModel.create(t,env);
   } else if (t.getName().equals("MemberGlobalDeclarations")) {
       retval = PhpGlobalsDeclarationModel.create(t,env);
   } else if (t.getName().equals("ThrowStatement")) {
     retval = PhpThrowStatementModel.create(t,env);
   } else if (t.getName().equals("TryBlock")) {
     retval = PhpTryBlockModel.create(t,env);
   } else if (t.getName().equals("NamespaceStatement")) {
     retval = PhpNamespaceStatementModel.create(t,env);
   } else if (t.getName().equals("SUseStatement")) {
     retval = PhpUseStatementModel.create(t,env);
   } else if (t.getName().equals("DeclareStatement")) {
     retval = PhpDeclareStatementModel.create(t,env);
   } else if (t.getName().equals("EndOfStatement")) {
     retval = PhpEndOfStatementModel.create(t,env);
   } else {
     throw new AssertException("Invalid name for statement:"+t.getName());
   }
   retval.setFileAndLine(t);
   return retval;
  }


  public abstract void eval(PhpEvalEnvironment env);

  protected void setFileAndLine(Term t) throws AssertException
  {
     line = TermHelper.getAttribute(t, "line").getInt();
     file = TermHelper.getAttribute(t, "file").getString();
  }

  public int getLine() { return line; }
  public String getFile() { return file; }

  private int line;
  private String file;

}
