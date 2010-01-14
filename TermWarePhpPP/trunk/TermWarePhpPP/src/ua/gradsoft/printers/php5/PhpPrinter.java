
package ua.gradsoft.printers.php5;

import java.io.PrintWriter;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareSymbols;
import ua.gradsoft.termware.exceptions.RuntimeAssertException;
import ua.gradsoft.termware.printers.AbstractPrinter;

/**
 *Printer for PHP
 * @author rssh
 */
public class PhpPrinter extends AbstractPrinter
{

    public PhpPrinter(PrintWriter out, String outTag) {
        super(out, outTag);
    }

    public void writeTerm(Term t) throws TermWareException {
        writeTerm(t,0);
    }

    public void writeTerm(Term t, int level) {
        if (t.getName().equals("SPhpPage")) {
            printSPhpPage(t, level);
        }else if (t.getName().equals("SHtmlBlocks")){
            printSHtmlBlocks(t,level);
        }else if (t.getName().equals("HtmlBlock")){
            printHtmlBlock(t,level);
        }else if (t.getName().equals("ClassDeclaration")){
            printClassDeclaration(t,level);
        }else if (t.getName().equals("InterfaceDeclaration")){
            printInterfaceDeclaration(t,level);
        }else if (t.getName().equals("Identifier")){
            printIdentifier(t,level);
        }else if (t.getName().equals("EchoStatement")){
            printEchoStatement(t,level);
        }else if (t.getName().equals("ExpressionStatement")){
            printExpressionStatement(t,level);
        }else if (t.getName().equals("LogicalTextOrExpression")){
            printLogicalTextOrExpression(t,level);
        }else if (t.getName().equals("LogicalTextXorExpression")){
            printLogicalTextXorExpression(t,level);
        }else if (t.getName().equals("LogicalTextAndExpression")){
            printLogicalTextAndExpression(t,level);
        }else if (t.getName().equals("AssignmentExpression")){
            printAssignmentExpression(t,level);
        }else if (t.getName().equals("ConditionalExpression")){
            printConditionalExpression(t,level);
        }else if (t.getName().equals("Logical_Or_Expression")){
            printLogical_Or_Expression(t,level);
        }else if (t.getName().equals("Logical_And_Expression")){
            printLogical_And_Expression(t,level);
        }else if (t.getName().equals("BitwiseOrExpression")){
            printBitwiseOrExpression(t,level);
        }else if (t.getName().equals("BitwiseXorExpression")){
            printBitwiseXorExpression(t,level);
        }else if (t.getName().equals("BitwiseAndExpression")){
            printBitwiseAndExpression(t,level);
        }else if (t.getName().equals("EqualityExpression")){
            printEqualityExpression(t,level);
        }else if (t.getName().equals("RelationalExpression")){
            printRelationalExpression(t,level);
        }else if (t.getName().equals("ShiftExpression")){
            printShiftExpression(t,level);
        }else if (t.getName().equals("AdditiveExpression")){
            printAdditiveExpression(t,level);
        }else if (t.getName().equals("MultiplicativeExpression")){
            printMultiplicativeExpression(t,level);
        }else if (t.getName().equals("CastExpression")){
            printCastExpression(t,level);
        }else if (t.getName().equals("UnaryExpression")){
            printUnaryExpression(t,level);
        }else if (t.getName().equals("PrefixIncDecExpression")){
            printPrefixIncDecExpression(t,level);
        }else if (t.getName().equals("PostfixIncDecExpression")){
            printPostfixIncDecExpression(t,level);
        }else if (t.getName().equals("InstanceOfExpression")){
            printInstanceOfExpression(t,level);
        }else if (t.getName().equals("CurlyBrackets")){
            printCurlyBracketsExpression(t,level);
        }else if (t.getName().equals("Reference")){
            printReferenceExpression(t,level);
        }else if (t.getName().equals("IgnoreErrors")){
            printIgnoreErrorsExpression(t,level);
        }else if (t.getName().equals("InBracesExpression")){
            printInBracesExpression(t,level);
        }else if (t.getName().equals("AllocationExpression")){
            printAllocationExpression(t,level);
        }else if (t.getName().equals("CloneExpression")){
            printCloneExpression(t,level);
        }else if (t.getName().equals("EndOfStatement")){
            printEndOfStatement(t,level);
        }else if (t.getName().equals("EmbeddedHtml")) {
            printEmbeddedHtml(t,level);
        }else if (t.getName().equals("MethodCall")){
            printMethodCall(t,level);
        }else if (t.getName().equals("MemberSelector")){
            printMemberSelector(t,level);
        }else if (t.getName().equals("ScopeResolution")){
            printScopeResolution(t,level);
        }else if (t.getName().equals("ArrayIndex")){
            printArrayIndex(t,level);
        }else if (t.getName().equals("MemberDeclaration")){
            printMemberDeclaration(t,level);
        }else if (t.getName().equals("MemberFunctionDeclaration")){
            printMemberFunctionDeclaration(t,level);
        }else if (t.getName().equals("MemberVariablesDeclaration")){
            printMemberVariablesDeclaration(t,level);
        }else if (t.getName().equals("MemberGlobalsDeclaration")){
            printMemberGlobalsDeclaration(t,level);
        }else if (t.getName().equals("MemberConstantDeclaration")){
            printMemberConstantDeclaration(t,level);
        }else if (t.getName().equals("SCompoundStatement")) {
            printCompoundStatement(t,level);
        }else if (t.getName().equals("SArgumentExpressionList")){
            printSArgumentExpressionList(t,level);
        }else if (t.getName().equals("SelectionIfStatement")){
            printSelectionIfStatement(t,level);
        }else if (t.getName().equals("SelectionSwitchStatement")){
            printSelectionSwitchStatement(t,level);
        }else if (t.getName().equals("SCaseStatements")){
            printSCaseStatements(t,level);
        }else if (t.getName().equals("LabeledStatements")){
            printLabeledStatements(t,level);
        }else if (t.getName().equals("BreakStatement")){
            printBreakStatement(t,level);
        }else if (t.getName().equals("ContinueStatement")){
            printContinueStatement(t,level);
        }else if (t.getName().equals("ReturnStatement")){
            printReturnStatement(t,level);
        }else if (t.getName().equals("DoubleStringLiteral")){
            printDoubleStringLiteral(t,level);
        }else if (t.getName().equals("SingleStringLiteral")){
            printSingleStringLiteral(t,level);
        }else if (t.getName().equals("ForEachStatement")){
            printForEachStatement(t,level);
        }else if (t.getName().equals("WhileStatement")){
            printWhileStatement(t,level);
        }else if (t.getName().equals("DoStatement")){
            printDoStatement(t,level);
        }else if (t.getName().equals("ForStatement")){
            printForStatement(t,level);
        }else if (t.getName().equals("IncludeStatement")){
            printIncludeStatement(t,level);
        }else if (t.getName().equals("DefineStatement")){
            printDefineStatement(t,level);
        }else if (t.getName().equals("ThrowStatement")){
            printThrowStatement(t,level);
        }else if (t.getName().equals("TryBlock")) {
            printTryBlock(t,level);
        }else if (t.getName().equals("GotoStatement")){
            printGotoStatement(t,level);
        }else if (t.getName().equals("GotoLabeledStatement")){
            printGotoLabeledStatement(t,level);
        }else if (t.getName().equals("SArray")){
            printSArray(t,level);
        }else if (t.getName().equals("Variable")){
            printVariable(t,level);
        }else if (t.getName().equals("Parameter")){
            printParameter(t,level);
        }else if (t.getName().equals("MemberAbstractFunctionDeclaration")){
            printMemberAbstractFunctionDeclaration(t,level);
        }else if (t.getName().equals("InterfaceMethodDeclaration")){
            printInterfaceMethodDeclaration(t,level);
        }else if (t.getName().equals("HtmlBlocks")) {
            printHtmlBlocks(t,level);
        }else if (t.getName().equals("HtmlBlock")) {
            printHtmlBlock(t,level);
        }else if (t.getName().equals("Invisible")) {
            printInvisible(t,level);
        }else{
            t.print(out_);
        }
    }

    public void printSPhpPage(Term t, int level){
        Term htmlBlocks = t.getSubtermAt(0);
        Term statementsOrNil = t.getSubtermAt(1);
        writeTerm(htmlBlocks,level+1);
        if (!statementsOrNil.isNil()) {
            writeIdent(level);
            out_.print("<?php ");
            Term curr = statementsOrNil;
            while(!curr.isNil() && curr.getName().equals("cons")) {
                Term statement = curr.getSubtermAt(0);               
                writeTerm(statement,level);
                curr=curr.getSubtermAt(1);
            }
            if (!curr.isNil()) {
             // not-list, some strange. let-s print one.
             curr.println(out_);
            }
        }    
    }

    public void printSHtmlBlocks(Term t, int level)
    {
       printList(t.getSubtermAt(0),level); 
    }
    
    public void printHtmlBlock(Term t, int level)
    {
      if (t.getSubtermAt(0).isString()) {
        out_.print(t.getSubtermAt(0).getString());
      } else {
        writeTerm(t.getSubtermAt(0),level);
      }
    }

    public void printEmbeddedHtml(Term t, int level)
    {
        out_.println("?>");
        Term l=t.getSubtermAt(0);
        while(!l.isNil()) {
            Term c=l.getSubtermAt(0);
            if (c.isString()) {
                out_.print(c.getString());
            }else{
                c.println(out_);
            }
            l=l.getSubtermAt(1);
        }
        Term et = t.getSubtermAt(1);
        if (et.isNil()) {
            /* do noting */
        }else if (et.isAtom() && et.getName().equals("PhpBegin")) {
            out_.print("<?php ");
        }else{
            // erro: print as is
            t.println(out_);
        }
    }

    public void printEndOfStatement(Term t, int level)
    {
      if (t.getArity()==0) {
          out_.println(";");
          writeIdent(level);
      }else if (t.getArity()==1) {
          writeTerm(t.getSubtermAt(0),level);
      }else{
            // erro: print as is
            t.println(out_);          
      }
    }

    public void printEchoStatement(Term t, int level)
    {
      Term at = t.getSubtermAt(0);
      if (at.isAtom()) {
          out_.print(at.getName());
          out_.print(" ");
      }else{
          throw new RuntimeAssertException("first argument of echi statement must be atom");
      }
      writeTerm(t.getSubtermAt(1),level);
      printEndOfStatement(t.getSubtermAt(2),level);
    }

    public void printDefineStatement(Term t, int level)
    {
      out_.print("define(");
      writeTerm(t.getSubtermAt(0),level+1);
      out_.print(", ");
      writeTerm(t.getSubtermAt(1),level+1);
      out_.print(")");
      writeTerm(t.getSubtermAt(2),level+1);
    }

    public void printExpressionStatement(Term t, int level)
    {
     if (t.getArity()==2) {
      writeTerm(t.getSubtermAt(0),level);  
      writeTerm(t.getSubtermAt(1),level);  
     } else {
      t.print(out_);
     }
    }

    public void printSArgumentExpressionList(Term t, int level)
    {
     Term list = t.getSubtermAt(0);
     while(!list.isNil()) {
         writeTerm(list.getSubtermAt(0),level);
         list=list.getSubtermAt(1);
         if (!list.isNil()) {
           out_.print(", ");
         }
     }
    }

    public void printInterfaceDeclaration(Term t, int level)
    {
        writeNextLine(level);
        Term name = t.getSubtermAt(0);
        Term extendsList = t.getSubtermAt(1);
        Term members = t.getSubtermAt(2).getSubtermAt(0);
        out_.print("interface ");
        printIdentifier(name,level);
        if (!extendsList.isNil()) {
            Term l = extendsList;
            while(!l.isNil()) {
                Term identifier = l.getSubtermAt(0);
                l=l.getSubtermAt(1);
                printIdentifier(identifier,level);
                if (!l.isNil()) {
                    out_.print(", ");
                }
            }
        }
        out_.print(" {");
        writeNextLine(level+1);
        while(!members.isNil()) {
            Term c = members.getSubtermAt(0);
            writeTerm(c,level+1);
            writeNextLine(level+1);
            members=members.getSubtermAt(1);
        }
        writeNextLine(level);
        out_.print("}");
        writeNextLine(level);
    }

    public void printClassDeclaration(Term t, int level)
    {
      Term property=t.getSubtermAt(0);
      Term className=t.getSubtermAt(1);
      Term superName=t.getSubtermAt(2);
      Term implementsList=t.getSubtermAt(3);
      Term sclassMembers=t.getSubtermAt(4);
      if (!property.isNil()) {
          out_.print(property.getName());
          out_.print(" ");
      }
      out_.print("class ");
      printIdentifier(className,level);
      if (!superName.isNil()) {
        out_.print(" extends ");
        printIdentifier(superName,level);
      }
      if (!implementsList.isNil()) {
         out_.print(" implements ");
         Term curr=implementsList;
         if (curr.getArity()==2 && curr.getName().equals("cons")) {
           while(!curr.isNil()) {
             printIdentifier(curr.getSubtermAt(0),level);
             curr=curr.getSubtermAt(1);
             if (!curr.isNil()) {
                 out_.print(", ");
             }
           }
         }else{
           writeTerm(implementsList,level+1);
         }
      }
      out_.print("{");
      writeNextLine(level);
      Term members = sclassMembers.getSubtermAt(0);
      if (!members.isNil()) {
        if (members.getArity()==2 && members.getName().equals("cons")) {
          while(!members.isNil()) {
            Term member=members.getSubtermAt(0);
            members=members.getSubtermAt(1);
            writeTerm(member,level+1);
          }
        }else{
          writeTerm(members,level+1);
        }
      }
      writeIdent(level);
      out_.print("}");
      writeNextLine(level);
    }

    public void printMemberDeclaration(Term t, int level)
    {
       if (t.getArity()==1) {
           writeTerm(t.getSubtermAt(0),level);
       }else{
           // yet not implemented.
           t.println(out_);
       }
    }

    public void printMemberFunctionDeclaration(Term t, int level)
    {
      Term attributes = t.getSubtermAt(0);
      Term isReference = t.getSubtermAt(1);
      Term name = t.getSubtermAt(2);
      Term params = t.getSubtermAt(3);
      Term compoundStatement = t.getSubtermAt(4);
      printMemberFunctionAttributes(attributes,level);
      out_.print(" function ");
      if (!isReference.isNil()) {
          out_.print("&");
      }
      printIdentifier(name,level);
      out_.print("(");
      if (!params.isNil()) {
          Term curr = params.getSubtermAt(0);
          if (curr.getNameIndex().equals(TermWareSymbols.CONS_INDEX)) {
            while(!curr.isNil()) {
              writeTerm(curr.getSubtermAt(0),level);
              curr=curr.getSubtermAt(1);
              if (!curr.isNil()) {
                  out_.print(", ");
              }
            }
          }else{
              // some strange, let's print
             curr.print(out_);
          }
      }
      out_.print(")");
      writeTerm(compoundStatement, level);
      writeNextLine(level);
    }

    public void printMemberAbstractFunctionDeclaration(Term t, int level)
    {
      out_.print("abstract ");
      Term attributes = t.getSubtermAt(0);
      if (!attributes.isNil()) {
         printMemberFunctionAttributes(attributes,level);
      }
      out_.print(" function ");
      if (t.getSubtermAt(1).getBoolean()) {
          out_.print("&");
      }
      writeTerm(t.getSubtermAt(2),level);
      out_.print("(");
      Term params = t.getSubtermAt(3);
      if (params.getName().equals("SParameterList")) {
          params=params.getSubtermAt(0);
      }
      if (!params.isNil()) {
          while(!params.isNil()) {
            Term param = params.getSubtermAt(0);
            params=params.getSubtermAt(1);
            writeTerm(param,level);
            if (!params.isNil()) {
                out_.print(", ");
            }
          }
          
      }
      out_.print(")");
      writeTerm(t.getSubtermAt(4),level);
    }

    public void printInterfaceMethodDeclaration(Term t, int level)
    {
      out_.print("function ");  
      boolean isRef = t.getSubtermAt(0).getBoolean();
      if (isRef) {
          out_.print("&");
      }
      printIdentifier(t.getSubtermAt(1),level+1);
      out_.print("(");
      Term params=t.getSubtermAt(2);
      if (!params.isNil()) {
          if (params.getName().equals("SParameterList")) {
              params=params.getSubtermAt(0);
          }
          while(!params.isNil()) {              
              writeTerm(params.getSubtermAt(0),level+1);
              params=params.getSubtermAt(1);
              if (!params.isNil()) {
                  out_.print(", ");
              }
          }
      }
      out_.print(") ");
      boolean emptyBody = t.getSubtermAt(3).getBoolean();
      if (emptyBody) {
          out_.print("{ }");
      }
      writeTerm(t.getSubtermAt(4),level);
    }


    public void printMemberVariablesDeclaration(Term t, int level)
    {
      Term attributes = t.getSubtermAt(0);
      printAttributes(attributes,level);
      Term varDecls = t.getSubtermAt(1);
      while(!varDecls.isNil()) {
          Term varDecl = varDecls.getSubtermAt(0);
          varDecls=varDecls.getSubtermAt(1);
          if (!varDecls.isNil()) {
              out_.print(", ");
          }
          Term identifier = varDecl.getSubtermAt(0);
          out_.print(" $");
          if (identifier.getArity()==1) {
            if (identifier.getSubtermAt(0).isString()) {
              out_.print(identifier.getSubtermAt(0).getString());
            }else{
              out_.print("IDENTIFIER(");
              identifier.print(out_);
              out_.print(")");
            }
          }else{
            out_.print("IDENTIFIER(");
            identifier.print(out_);
            out_.print(")");
          }
          if (varDecl.getArity()>1) {
           if (!varDecl.getSubtermAt(1).isNil()) {
              out_.print("=");
              writeTerm(varDecl.getSubtermAt(1),level);
           }
          }
      }
      writeTerm(t.getSubtermAt(2),level);
      writeNextLine(level);
    }

    public void printMemberGlobalsDeclaration(Term t, int level)
    {
      out_.print("global ");
      Term l=t.getSubtermAt(0);
      while(!l.isNil()) {
          Term c = l.getSubtermAt(0);
          writeTerm(c,level+1);
          l=l.getSubtermAt(1);
          if (!l.isNil()) {
              out_.print(", ");
          }
      }
      writeTerm(t.getSubtermAt(1),level+1);
    }

    public void printMemberConstantDeclaration(Term t, int level)
    {
      if (!t.getSubtermAt(0).isNil()) {
          writeTerm(t.getSubtermAt(0),level+1);
          out_.print(" ");
      }
      out_.print("const ");
      writeTerm(t.getSubtermAt(1),level+1);
      out_.print(" = ");
      writeTerm(t.getSubtermAt(2),level+1);
      writeTerm(t.getSubtermAt(3),level+1);
    }


    public void printIdentifier(Term t, int level)
    {
      if (t.getArity()==1 && t.getName().equals("Identifier")) {
        Term s = t.getSubtermAt(0);
        if (s.isString()) {
          out_.print(s.getString());
        } else {
          t.print(out_);
        }
      } else {
        out_.print("IDENTIFIER(");
        t.print(out_);
        out_.print(")");
      }
    }

    public void printDoubleStringLiteral(Term t, int level)
    {
      Term l = t.getSubtermAt(0);
      out_.print("\"");
      while(!l.isNil()) {
          Term c=l.getSubtermAt(0);
          l=l.getSubtermAt(1);
          if (c.getName().equals("string")) {
              out_.print(c.getSubtermAt(0).getString());
          }else if (c.getName().equals("var")){
              out_.print("${");
              out_.print(c.getSubtermAt(0).getString());
              out_.print("}");
          }
      }
      out_.print("\"");
    }

    public void printSingleStringLiteral(Term t, int level)
    {
        Term l = t.getSubtermAt(0);
        out_.print("'");
        if (l.isString()) {
          out_.print(l.getString());
        } else {
          l.print(out_);
        }
        out_.print("'");
    }

    public void printMemberFunctionAttributes(Term t, int level)
    {
        printAttributes(t,level);
    }

    private void printAttributes(Term t, int level)
    {
        if (t.getArity()==1) {
          Term list = t.getSubtermAt(0);
          while(!list.isNil()) {
            writeTerm(list.getSubtermAt(0),level);
            list=list.getSubtermAt(1);
            if (!list.isNil()) {
                out_.print(", ");
            }
          }
        }else{
          out_.print("ATTRIBUTES(");  
          t.print(out_);
          out_.print(")");  
        }
    }

    public void printMethodCall(Term t, int level)
    {
       writeTerm(t.getSubtermAt(0),level);
       out_.print("(");
       writeTerm(t.getSubtermAt(1),level);
       out_.print(")");
    }

    public void printScopeResolution(Term t, int level)
    {
        writeTerm(t.getSubtermAt(0),level);
        out_.print("::");
        writeTerm(t.getSubtermAt(1),level);
    }

    public void printMemberSelector(Term t, int level)
    {
        writeTerm(t.getSubtermAt(0),level);
        out_.print("->");
        writeTerm(t.getSubtermAt(1),level);
    }

    public void printArrayIndex(Term t, int level)
    {
        writeTerm(t.getSubtermAt(0),level);
        out_.print("[");
        if (t.getArity()>1){
          writeTerm(t.getSubtermAt(1),level);
        }
        out_.print("]");
    }

    public void printAllocationExpression(Term t, int level)
    {
        out_.print("new ");
        writeTerm(t.getSubtermAt(0),level);
        if (t.getArity()>1) {
            Term args = t.getSubtermAt(1);
            if (!args.isNil()) {
                out_.print("(");
                writeTerm(args,level);
                out_.print(")");
            }
        }
    }

    
    public void printList(Term t, int level)
    {
        Term curr = t;
        while(!curr.isNil()) {
            writeTerm(curr.getSubtermAt(0),level);
            curr=curr.getSubtermAt(1);
        }
    }

    public void printVariable(Term t, int level)
    {
        if (t.getArity()==2) {
            int nDollars = t.getSubtermAt(0).getInt();
            String varname = t.getSubtermAt(1).getString();
            for(int i=0; i<nDollars;++i){
                out_.print("$");
            }
            out_.print(varname);
        } else {
            // impossible.
            out_.print(t);
        }
    }

    public void printSArray(Term t, int level)
    {
      out_.print("array(");
      Term l = t.getSubtermAt(0);
      while(!l.isNil()) {
          if (!l.getName().equals("cons") || l.getArity()!=2) {
             l.print(out_);
             break;
          }
          Term element = l.getSubtermAt(0);
          l=l.getSubtermAt(1);
          if (element.getName().equals("ArrayKeyValuePair")){
              writeTerm(element.getSubtermAt(0),level+1);
              out_.print(" => ");
              writeTerm(element.getSubtermAt(1),level+1);
          }else{
              writeTerm(element,level+1);
          }
          if (!l.isNil()) {
              out_.print(", ");
          }
      }
      out_.print(")");
    }

    public void printCompoundStatement(Term t, int level)
    {
      out_.println("{");
      writeIdent(level+1);
      if (t.getArity()==0) {
       out_.print("COMPOUND_STATEMENT_ERROR:");
       t.print(out_);
      }else{
       Term l = t.getSubtermAt(0);
       while(!l.isNil()) {
          writeTerm(l.getSubtermAt(0),level+1);
          writeNextLine(level+1);
          l=l.getSubtermAt(1);
       }
      }
      out_.println("}");
      writeIdent(level);
    }

    public void printSelectionIfStatement(Term t, int level)
    {
      out_.print("if (");
      writeTerm(t.getSubtermAt(0),level);
      out_.print(")");
      writeNextLine(level+1);
      writeTerm(t.getSubtermAt(1),level+1);
      if (!t.getSubtermAt(2).isNil()) {
          writeNextLine(level);
          Term l = t.getSubtermAt(2);
          while(!l.isNil()) {
              out_.print("elseif ");
              Term elseif = l.getSubtermAt(0);
              l=l.getSubtermAt(1);
              if (elseif.getArity()==2) {
                out_.print("(");
                writeTerm(elseif.getSubtermAt(0), level);
                out_.print(") ");
                writeNextLine(level+1);
                writeTerm(elseif.getSubtermAt(1), level+1);
              }else{
                  elseif.println(out_);
              }
          }
      }
      if (!t.getSubtermAt(3).isNil()) {
          writeNextLine(level);
          out_.print(" else ");
          writeTerm(t.getSubtermAt(3), level+1);
      }
      writeNextLine(level);
    }

    public void printSelectionSwitchStatement(Term t, int level) {
        out_.print("switch(");
        writeTerm(t.getSubtermAt(0),level);
        out_.print(") ");
        writeTerm(t.getSubtermAt(1),level);
    }

    public void printSCaseStatements(Term t, int level) {
        out_.print("{ ");
        out_.println();
        Term c = t.getSubtermAt(0);
        while(!c.isNil()) {
          writeTerm(c.getSubtermAt(0),level+1);
          c=c.getSubtermAt(1);
        }
        out_.print("}");
        out_.println();
    }

    public void printLabeledStatements(Term t, int level) {
        Term frs = t.getSubtermAt(0);
        if (frs.isAtom() && frs.getName().equals("default")) {
            out_.print("default:");
        }else{
            out_.print("case ");
            writeTerm(frs,level);
            out_.print(":");
        }
        Term c = t.getSubtermAt(1);
        while(!c.isNil()) {
          writeTerm(c.getSubtermAt(0),level+1);
          c=c.getSubtermAt(1);
        }
    }

    public void printBreakStatement(Term t, int level)
    {
      out_.print("break");
      if (!t.getSubtermAt(0).isNil()) {
          out_.print(" ");
          out_.print(t.getSubtermAt(0).getInt());
      }
      out_.print(" ");
      writeTerm(t.getSubtermAt(1),level);
    }

    public void printContinueStatement(Term t, int level)
    {
      out_.print("continue");
      if (!t.getSubtermAt(0).isNil()) {
          out_.print(" ");
          out_.print(t.getSubtermAt(0).getInt());
      }
      out_.print(" ");
      writeTerm(t.getSubtermAt(1),level);
    }

    public void printReturnStatement(Term t, int level)
    {
      out_.print("return");
      if (!t.getSubtermAt(0).isNil()) {
          out_.print(" ");
          writeTerm(t.getSubtermAt(0),level);
      }
      out_.print(" ");
      writeTerm(t.getSubtermAt(1),level);
    }

    public void printIncludeStatement(Term t, int level)
    {
        boolean isRef = t.getSubtermAt(0).getBoolean();
        if (isRef) {
            out_.print("@");
        }
        out_.print(t.getSubtermAt(1).getName());
        out_.print("(");
        writeTerm(t.getSubtermAt(2),level);
        out_.print(")");
        writeTerm(t.getSubtermAt(3),level);
    }
    
    public void printLogicalTextOrExpression(Term t,int level) {
        printBinaryExpression("or",t.getSubtermAt(0),t.getSubtermAt(1),level);
    }

    public void printLogicalTextXorExpression(Term t,int level) {
        printBinaryExpression("xor",t.getSubtermAt(0),t.getSubtermAt(1),level);
    }

    public void printLogicalTextAndExpression(Term t,int level) {
        printBinaryExpression("and",t.getSubtermAt(0),t.getSubtermAt(1),level);
    }


    public void printAssignmentExpression(Term t,int level)
    {
        if (t.getArity()==1) {
            writeTerm(t.getSubtermAt(0),level);
        }else if (t.getArity()==3) {          
            String op = t.getSubtermAt(1).getSubtermAt(0).getString();
            printBinaryExpression(op,t.getSubtermAt(0),t.getSubtermAt(2),level);
        }else{
            t.println(out_);
        }
    }

    public void printConditionalExpression(Term t, int level)
    {
        if (t.getArity()==1) {
            writeTerm(t.getSubtermAt(0),level);
        }else if (t.getArity()==3){
            writeTerm(t.getSubtermAt(0),level);
            out_.print(" ? ");
            writeTerm(t.getSubtermAt(1),level);
            out_.print(" : ");
            writeTerm(t.getSubtermAt(2),level);
        }else{
            t.println(out_);
        }
    }

    public void printLogical_Or_Expression(Term t,int level) {
        printBinaryExpression("||",t.getSubtermAt(0),t.getSubtermAt(1),level);
    }

    public void printLogical_And_Expression(Term t,int level) {
        printBinaryExpression("&&",t.getSubtermAt(0),t.getSubtermAt(1),level);
    }

    public void printBitwiseOrExpression(Term t,int level) {
        printBinaryExpression("|",t.getSubtermAt(0),t.getSubtermAt(1),level);
    }

    public void printBitwiseXorExpression(Term t,int level) {
        printBinaryExpression("^",t.getSubtermAt(0),t.getSubtermAt(1),level);
    }

    public void printBitwiseAndExpression(Term t,int level) {
        printBinaryExpression("&",t.getSubtermAt(0),t.getSubtermAt(1),level);
    }

    public void printEqualityExpression(Term t,int level) {
        printBinaryExpression(t.getSubtermAt(1).getString(),t.getSubtermAt(0),t.getSubtermAt(2),level);
    }

    public void printRelationalExpression(Term t,int level) {
        printBinaryExpression(t.getSubtermAt(1).getString(),t.getSubtermAt(0),t.getSubtermAt(2),level);
    }

    public void printShiftExpression(Term t,int level) {
        printBinaryExpression(t.getSubtermAt(1).getString(),t.getSubtermAt(0),t.getSubtermAt(2),level);
    }

    public void printAdditiveExpression(Term t,int level) {
        printBinaryExpression(t.getSubtermAt(1).getString(),t.getSubtermAt(0),t.getSubtermAt(2),level);
    }

    public void printMultiplicativeExpression(Term t,int level) {
        printBinaryExpression(t.getSubtermAt(1).getString(),t.getSubtermAt(0),t.getSubtermAt(2),level);
    }


    private void printBinaryExpression(String op, Term frs,Term snd,int level)
    {
      writeTerm(frs,level);
      out_.print(" ");
      out_.print(op);
      out_.print(" ");
      writeTerm(snd,level);
    }

    public void printCastExpression(Term t,int level) {
        if (t.getArity()==1) {
            writeTerm(t.getSubtermAt(0),level);
        }else if(t.getArity()==2){
           out_.print("(");
           out_.print(t.getSubtermAt(0).getName());
           out_.print(")");
           writeTerm(t.getSubtermAt(1),level);
        }else{
           t.println(out_);
        }
    }

    public void printUnaryExpression(Term t,int level) {
        if (t.getArity()==1) {
            writeTerm(t.getSubtermAt(0),level);
        }else if(t.getArity()==2){
           out_.print(t.getSubtermAt(0).getString());
           out_.print(" ");
           writeTerm(t.getSubtermAt(1),level);
        }else{
           t.println(out_);
        }
    }

    public void printPrefixIncDecExpression(Term t,int level) {
        if (t.getArity()==1) {
            writeTerm(t.getSubtermAt(0),level);
        }else if(t.getArity()==2){
           out_.print(t.getSubtermAt(0).getString());
           out_.print(" ");
           writeTerm(t.getSubtermAt(1),level);
        }else{
           t.println(out_);
        }
    }

    public void printPostfixIncDecExpression(Term t,int level) {
        if (t.getArity()==1) {
            writeTerm(t.getSubtermAt(0),level);
        }else if(t.getArity()==2){
           writeTerm(t.getSubtermAt(0),level);
           out_.print(" ");
           out_.print(t.getSubtermAt(1).getString());
        }else{
           t.println(out_);
        }
    }

    public void printInstanceOfExpression(Term t,int level) {
        if (t.getArity()==1) {
            writeTerm(t.getSubtermAt(0),level);
        }else if(t.getArity()==2){
           writeTerm(t.getSubtermAt(0),level);
           out_.print(" intstanceof ");
           writeTerm(t.getSubtermAt(1),level);
        }else{
           t.println(out_);
        }
    }

    public void printCurlyBracketsExpression(Term t,int level) {
        if (t.getArity()==1) {
            out_.print("{");
            writeTerm(t.getSubtermAt(0),level);
            out_.print("}");
        }else if(t.getArity()==2){
           writeTerm(t.getSubtermAt(0),level);
           out_.print("{");
           writeTerm(t.getSubtermAt(1),level);
           out_.print("}");
        }else{
           t.println(out_);
        }
    }

    public void printReferenceExpression(Term t,int level) {
        if (t.getArity()==1) {
            out_.print("&");
            writeTerm(t.getSubtermAt(0),level);
        }else{
           t.println(out_);
        }
    }

    public void printIgnoreErrorsExpression(Term t,int level) {
        if (t.getArity()==1) {
            out_.print("@");
            writeTerm(t.getSubtermAt(0),level);
        }else{
           t.println(out_);
        }
    }


    public void printInBracesExpression(Term t,int level) {
        if (t.getArity()==1) {
            out_.print("(");
            writeTerm(t.getSubtermAt(0),level);
            out_.print(")");
        }else{
           t.println(out_);
        }
    }

    public void printCloneExpression(Term t, int level) {
        out_.print("clone ");
        writeTerm(t.getSubtermAt(0),level+1);
    }
   
    public void printForEachStatement(Term t, int level)
    {
       out_.print("foreach( ");
       Term expr = t.getSubtermAt(0);
       if (!expr.isNil()) {
           writeTerm(expr,level);
       }
       out_.print(" as ");
       Term key = t.getSubtermAt(1);
       Term value = t.getSubtermAt(2);
       boolean isRef = t.getSubtermAt(3).getBoolean();
       Term statement = t.getSubtermAt(4);
       if (!key.isNil()) {
           writeTerm(key, level);
           out_.print("=>");
       }
       if (isRef){
           out_.print("&");
       }
       writeTerm(value,level);
       out_.print(")");
       writeNextLine(level+1);
       writeTerm(statement,level+1);       
    }

    public void printWhileStatement(Term t, int level)
    {
      out_.print("while(");
      writeTerm(t.getSubtermAt(0),level);
      out_.print(") ");
      writeTerm(t.getSubtermAt(1),level);
    }

    public void printDoStatement(Term t, int level)
    {
      out_.print("do ");
      writeTerm(t.getSubtermAt(0),level);
      out_.print(" while ( ");
      writeTerm(t.getSubtermAt(1),level);
      out_.print(" )");
      writeTerm(t.getSubtermAt(2),level);
    }

    public void printForStatement(Term t, int level)
    {
      out_.print("for(");
      if (!t.getSubtermAt(0).isNil()) {
         writeTerm(t.getSubtermAt(0),level);
      }
      out_.print(";");
      if (!t.getSubtermAt(1).isNil()) {
         writeTerm(t.getSubtermAt(1),level);
      }
      out_.print(";");
      if (!t.getSubtermAt(2).isNil()) {
         writeTerm(t.getSubtermAt(2),level);
      }
      out_.print(")");
      writeTerm(t.getSubtermAt(3),level);
    }

    public void printGotoLabeledStatement(Term t, int level)
    {
      out_.print(t.getSubtermAt(0).getString());
      out_.print(": ");
      writeTerm(t.getSubtermAt(1),level);
    }

    public void printGotoStatement(Term t, int level)
    {
      out_.print("goto ") ;
      out_.print(t.getSubtermAt(0).getString());
      writeTerm(t.getSubtermAt(1),level);
    }

    public void printThrowStatement(Term t, int level)
    {
      out_.print("throw ") ;
      writeTerm(t.getSubtermAt(0),level);
      out_.print(" ") ;
      writeTerm(t.getSubtermAt(1),level);
    }

    public void printTryBlock(Term t, int level)
    {
      out_.print("try ");
      writeTerm(t.getSubtermAt(0),level);
      Term l = t.getSubtermAt(1);
      while(!l.isNil()) {
          Term catchBlock=l.getSubtermAt(0);
          l=l.getSubtermAt(1);
          out_.print("catch(");
          writeTerm(catchBlock.getSubtermAt(0),level);
          out_.print(" ");
          writeTerm(catchBlock.getSubtermAt(1),level);
          out_.print(")");
          writeTerm(catchBlock.getSubtermAt(2),level);
      }
    }

    public void printParameter(Term t, int level)
    {
      if (!t.getSubtermAt(0).isNil()) {
          writeTerm(t.getSubtermAt(0),level+1);
          out_.print(" ");
      }
      if (t.getSubtermAt(1).getBoolean()) {
          out_.print("&");
      }
      writeTerm(t.getSubtermAt(2),level+1);
      if (!t.getSubtermAt(3).isNil()) {
          out_.print(" = ");
          writeTerm(t.getSubtermAt(3),level+1);
      }
    }

    public void printHtmlBlocks(Term t, int level)
    {
     for(int i=0; i<t.getArity();++i) {
         writeTerm(t.getSubtermAt(i),level+1);
     }
    }


    public void printInvisible(Term t, int level)
    {
     /* do nothing */
    }

    public void flush()
    {
       out_.flush();
    }

    private void writeIdent(int level)
    {
        for(int i=0; i<level; ++i) {
            out_.print(' ');
        }
    }
    
    private void writeNextLine(int level)
    {
        out_.println();
        writeIdent(level);
    }

}
