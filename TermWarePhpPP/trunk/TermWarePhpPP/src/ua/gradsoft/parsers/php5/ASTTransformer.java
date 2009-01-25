
package ua.gradsoft.parsers.php5;

import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.strategies.FirstTopStrategy;

/**
 *Transfromer for received PHP scripts.
 * @author rssh
 */
public class ASTTransformer {


    public ASTTransformer() throws TermWareException
    { init(); }

    public class ASTFacts extends DefaultFacts
    {
        public ASTFacts() throws TermWareException {
            super();
        }
        
    }

    private void init() throws TermWareException
    {
        ASTFacts astFacts = new ASTFacts();
        FirstTopStrategy strategy1 = new FirstTopStrategy();
        FirstTopStrategy strategy2 = new FirstTopStrategy();

        afterToList_ = new TermSystem(strategy1,astFacts);
        beforeToList_ = new TermSystem(strategy2,astFacts);


        beforeToList_.addRule("Statement($x)->$x");
        beforeToList_.addRule("Expression($x)->$x");
        beforeToList_.addRule("LogicalTextOrExpression($x)->$x");
        beforeToList_.addRule("LogicalTextXorExpression($x)->$x");
        beforeToList_.addRule("LogicalTextAndExpression($x)->$x");
        beforeToList_.addRule("AssignmentExpression($x)->$x");
        beforeToList_.addRule("ConditionalExpression($x)->$x");
        beforeToList_.addRule("Logical_Or_Expression($x)->$x");
        beforeToList_.addRule("Logical_And_Expression($x)->$x");
        beforeToList_.addRule("BitwiseOrExpression($x)->$x");
        beforeToList_.addRule("BitwiseXorExpression($x)->$x");
        beforeToList_.addRule("BitwiseAndExpression($x)->$x");
        beforeToList_.addRule("EqualityExpression($x)->$x");
        beforeToList_.addRule("RelationalExpression($x)->$x");
        beforeToList_.addRule("ShiftExpression($x)->$x");
        beforeToList_.addRule("AdditiveExpression($x)->$x");
        beforeToList_.addRule("MultiplicativeExpression($x)->$x");
        beforeToList_.addRule("CastExpression($x)->$x");
        beforeToList_.addRule("UnaryExpression($x)->$x");
        beforeToList_.addRule("PrefixIncDecExpression($x)->$x");
        beforeToList_.addRule("PostfixIncDecExpression($x)->$x");
        beforeToList_.addRule("InstanceOfExpression($x)->$x");
        beforeToList_.addRule("PostfixExpression($x)->$x");
        beforeToList_.addRule("PrimaryExpression($x)->$x");
        beforeToList_.addRule("Constant($x)->$x");
        beforeToList_.addRule("ClassInstantiation($x)->$x");

        beforeToList_.addRule("HtmlBlocks..($x) -> SHtmlBlocks($x)");
        beforeToList_.addRule("PhpPage..($x) -> SPhpPage(car($x),cdr($x))");
        beforeToList_.addRule("car([$x:$y])->$x");
        beforeToList_.addRule("cdr([$x:$y])->$y");
        beforeToList_.addRule("cdr([])->[]");
        beforeToList_.addRule("ArgumentExpressionList..($x) -> SArgumentExpressionList($x)");

        beforeToList_.addRule("String($x)->$x");

        beforeToList_.addRule("ClassMembers..($x)->SClassMembers($x)");
        beforeToList_.addRule("MemberFunctionAttributes..($x)->SMemberFunctionAttributes($x)");
        beforeToList_.addRule("Visibility($x)->$x");
        beforeToList_.addRule("CompoundStatement..($x) -> SCompoundStatement($x)");
        beforeToList_.addRule("ParameterList..($x) -> SParameterList($x)");
        beforeToList_.addRule("MemberVariableAttributes..($x) -> SMemberVariableAttributes($x)");

        beforeToList_.addRule("InterfaceMembers..($x)->SInterfaceMembers($x)");
        beforeToList_.addRule("InterfaceMember($x) -> $x");

        beforeToList_.addRule("SelectionElseIfClauses..($x) -> SSelectionElseIfClauses($x)");
        beforeToList_.addRule("SSelectionElseIfClauses($x) -> $x");
        beforeToList_.addRule("SelectionStatement($x) -> $x");

        beforeToList_.addRule("VariableDeclSeq..($x) -> $x");

        beforeToList_.addRule("PostfixExpression..($x) -> SPostfixExpression($x) ");
        beforeToList_.addRule("SPostfixExpression(cons($x,$y)) -> PostfixSuffix0($x,$y) ");
        beforeToList_.addRule("PostfixSuffix0($x,[$y:$z]) -> PostfixSuffix0(PostfixSuffix0($x,$y),$z)");
        beforeToList_.addRule("PostfixSuffix0($x,[]) -> $x");
        beforeToList_.addRule("PostfixSuffix0($x,PostfixExpressionMethodCallSuffix($y)) -> MethodCall($x,$y)");
        beforeToList_.addRule("PostfixSuffix0($x,PostfixExpressionMemberSelectorSuffix($y)) -> MemberSelector($x,$y)");
        beforeToList_.addRule("PostfixSuffix0($x,PostfixExpressionScopeResolutionSuffix($y)) -> ScopeResolution($x,$y)");
        beforeToList_.addRule("PostfixSuffix0($x,PostfixExpressionArrayIndexSuffix($y)) -> ArrayIndex($x,$y)");
        beforeToList_.addRule("PostfixSuffix0($x,PostfixExpressionCurlyBracketsSuffix($y)) -> CurlyBrackets($x,$y)");

        beforeToList_.addRule("IterationStatement($x) -> $x");

        beforeToList_.addRule("AdditiveExpression($x,$o1,AdditiveExpression($y,$o2,$z)) -> AdditiveExpression(AdditiveExpression($x,$o1,$y),$o2,$z)");
        beforeToList_.addRule("MultiplicativeExpression($x,$o1,MultiplicativeExpression($y,$o2,$z)) -> MultiplicativeExpression(MultiplicativeExpression($x,$o1,$y),$o2,$z)");

        beforeToList_.addRule("IncDecSuffix..($x) -> $x");

        beforeToList_.addRule("PostfixIncDecExpression0($x,cons($y,$z)) -> PostfixIncDecExpression0(PostfixIncDecExpression($x,$y),$z)");
        beforeToList_.addRule("PostfixIncDecExpression0($x,[]) -> $x");
        beforeToList_.addRule("PostfixIncDecExpression0($x) -> $x");

        beforeToList_.addRule("PrimaryExpression(ref,$x) -> Reference($x)");
        beforeToList_.addRule("PrimaryExpression(IgnoreErrors,$x) -> IgnoreErrors($x)");

        beforeToList_.addRule("JumpStatement($x) -> $x");
        
        beforeToList_.addRule("Array..($x) -> SArray($x)");
        beforeToList_.addRule("ArrayKeyOrValue($x) -> ($x)");
        beforeToList_.addRule("ArrayKeyOrValue($x,$y) -> ArrayKeyValuePair($x,$y)");
       
        beforeToList_.addRule("VarList..($x) -> $x");
        beforeToList_.addRule("CatchBlocks..($x) -> $x");

    }

    public Term transform(Term x) throws TermWareException
    {
      x=beforeToList_.reduce(x);
      //x=transformSeqToList(x);
      x=afterToList_.reduce(x);
      return x;
    }

    public Term phpTermArgsAsList(Term t, int startFrom, boolean deep) throws TermWareException
    {
        Term list = TermWare.getInstance().getTermFactory().createNil();
        for(int i=t.getArity(); i>startFrom; --i) {
            Term ct = t.getSubtermAt(i-1);
            if (deep) {
                ct=transformSeqToList(ct);
            }
            if (!ct.isNil()) {
                list=TermWare.getInstance().getTermFactory().createTerm("cons",ct,list);
            }
        }
        Term[] newBody = new Term[startFrom+1];
        for(int i=0; i<startFrom; ++i) {
            newBody[i]=t.getSubtermAt(i);
        }
        newBody[startFrom]=list;
        t=TermWare.getInstance().getTermFactory().createTerm(t.getName(),newBody);
        return t;
    }

    public Term transformSeqToList(Term t) throws TermWareException
    {
      if (t.isComplexTerm()) {
          if (t.getName().equals("PhpPage")) {
              t=phpTermArgsAsList(t,0,true);
          }else{
              for(int i=0; i<t.getArity(); ++i) {
                  Term ct = t.getSubtermAt(i);
                  ct=transformSeqToList(ct);
                  t.setSubtermAt(i, ct);
              }
          }
      }  
      return t;
    }

    private TermSystem  afterToList_;
    private TermSystem  beforeToList_;


}
