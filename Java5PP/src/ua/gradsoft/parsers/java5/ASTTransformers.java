/*
 * ASTTransformers.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5;

import java.util.LinkedList;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.IFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.strategies.BTStrategy;

/**
 *This class holds systems, which transform AST tree adter invoking jjt parser and befor
 *return AST term to caller.
 *
 * @author Ruslan Shevchenko
 */
public class ASTTransformers {
    
    /**
     * Creates a new instance of ASTTransformers
     */
    public ASTTransformers() {
    }
    
    public class ASTFacts extends DefaultFacts {
        public ASTFacts() throws TermWareException {
            super(); }
        
        public boolean isIdentifier(Term t) {
            return t.getName().equals("Identifier");
        }
        
        public boolean isName(Term t) {
            return t.getName().equals("Name");
        }
        
        public void append(TransformationContext ctx,Term result,Term n,Term x) throws TermWareException {
            Term[] newBody = new Term[n.getArity()+1];
            for(int i=0; i<n.getArity(); ++i){
                newBody[i]=n.getSubtermAt(i);
            }
            newBody[n.getArity()]=x;
            Term retval = n.createSame(newBody);
            ctx.getCurrentSubstitution().put(result,retval);
        }
        
    }
    
    void init() throws TermWareException {
        BTStrategy strategyBefore=new BTStrategy();
        //FirstTopStrategy strategy=new FirstTopStrategy();
        IFacts  facts=new ASTFacts();
        
        simplifierBefore_=new TermSystem(strategyBefore,facts,TermWare.getInstance());
        
        //simplifier_.setDebugEntity("All");
        //simplifier_.setDebugMode(true);
        
        simplifierBefore_.addRule("ResultType($x) -> $x");
        
        simplifierBefore_.addRule("Expression($x) -> $x");
        simplifierBefore_.addRule("ConditionalExpression($x)  -> $x");
        simplifierBefore_.addRule("ConditionalOrExpression($x) -> $x");
        simplifierBefore_.addRule("ConditionalAndExpression($x) -> $x");
        simplifierBefore_.addRule("InclusiveOrExpression($x) -> $x");
        simplifierBefore_.addRule("ExclusiveOrExpression($x) -> $x");
        simplifierBefore_.addRule("AndExpression($x) -> $x");
        simplifierBefore_.addRule("EqualityExpression($x) -> $x");
        simplifierBefore_.addRule("InstanceOfExpression($x) -> $x");
        simplifierBefore_.addRule("RelationalExpression($x) -> $x");
        simplifierBefore_.addRule("ShiftExpression($x) -> $x");
        simplifierBefore_.addRule("AdditiveExpression($x) -> $x");
        simplifierBefore_.addRule("MultiplicativeExpression($x) -> $x");
        simplifierBefore_.addRule("UnaryExpression($x) -> $x");
        simplifierBefore_.addRule("UnaryExpressionNotPlusMinus($x) -> $x");
        simplifierBefore_.addRule("PostfixExpression($x) -> $x");
        simplifierBefore_.addRule("PrimaryExpression($x) -> $x");
        //simplifier_.addRule("PrimaryPrefix($x) -> $x");
        //simplifier_.addRule("PrimarySuffix($x) -> $x");
        simplifierBefore_.addRule("Literal($x) -> $x");
         
         
        simplifierBefore_.addRule("Statement($x) -> $x");
        simplifierBefore_.addRule("BlockStatement($x) -> $x");
         
        simplifierBefore_.addRule("Type($x) -> $x");
        simplifierBefore_.addRule("PrimitiveType($x)->$x");
        simplifierBefore_.addRule("ClassOrInterfaceType($x) -> $x");
        simplifierBefore_.addRule("ReferenceType(0,$x) -> $x");
        simplifierBefore_.addRule("VariableInitializer($x) -> $x");
        //simplifierBefore_.addRule("ArgumentList($x) -> $x");
         
        simplifierBefore_.addRule("AssignmentOperator($x) -> $x");
         
         
        simplifierBefore_.addRule("ExplicitConstructorInvocation($x) -> $x");
         
        simplifierBefore_.addRule("ExplicitConstructorInvocation($x) -> $x");
         
        simplifierBefore_.addRule("IdentifierOrFunctionCall($x,$y) -> FunctionCall($x,$y)");
        simplifierBefore_.addRule("IdentifierOrFunctionCall($x) -> $x");
         
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(ThisSelector(),$y)) -> PrimaryExpression(This($x),$y)");
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(ThisSelector())) -> PrimaryExpression(This($x))");
        
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(SuperSelector(),$y)) -> PrimaryExpression(Super($x),$y)");
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(SuperSelector())) -> PrimaryExpression(Super($x))");
        
         
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(AllocationSelector($y),$z)) -> PrimaryExpression(InnerAllocation($x,$y),$z)");
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(AllocationSelector($y))) -> PrimaryExpression(InnerAllocation($x,$y))");
         
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(MethodCallSelector($y,$z),$w)) -> PrimaryExpression(MethodCall($x,$y,$z),$w)");
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(MethodCallSelector($y,$z))) -> PrimaryExpression(MethodCall($x,$y,$z))");
         
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(SpecializedMethodCallSelector($ta,$y,$z),$w)) -> PrimaryExpression(SpecializedMethodCall($x,$ta,$y,$z),$w)");
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(SpecializedMethodCallSelector($ta,$y,$z))) -> PrimaryExpression(SpecializedMethodCall($x,$ta,$y,$z))");
         
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(FieldSelector($y),$w)) -> PrimaryExpression(Field($x,$y),$w)");
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(FieldSelector($y))) -> PrimaryExpression(Field($x,$y))");
         
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(SpecializedFieldSelector($ta,$y),$w)) -> PrimaryExpression(SpecializedField($x,$ta,$y),$w)");
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(SpecializedFieldSelector($ta,$y))) -> PrimaryExpression(SpecializedField($x,$ta,$y))");
         
        simplifierBefore_.addRule("MemberSelector($x,$y,$z) [ isNil($x) ] -> MethodCallSelector($y,$z) !-> SpecializedMethodCallSelector($x,$y,$z)");
        simplifierBefore_.addRule("MemberSelector($x,$y) [ isNil($x) ] -> FieldSelector($y) !-> SpecializedFieldSelector($x,$y)");
         
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(ArrayIndexSelector($y),$w)) -> PrimaryExpression(ArrayIndex($x,$y),$w)");
        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(ArrayIndexSelector($y))) -> PrimaryExpression(ArrayIndex($x,$y))");
         
        simplifierBefore_.addRule("Field($x,$y) [isIdentifier($x) && isIdentifier($y)] -> Name($x,$y)");
        simplifierBefore_.addRule("Field($x,$y) [isName($x) && isIdentifier($y) ] -> $z [ append($z,$x,$y) ] ");        

        simplifierBefore_.addRule("PrimaryExpression($x,PrimarySuffix(SuperSelector($args))) -> ExplicitSuperConstructorInvocation($x,$args)");
        simplifierBefore_.addRule("StatementExpressionStatement(ExplicitSuperConstructorInvocation($x,$y)) -> ExplicitSuperConstructorInvocation($x,$y)");
        simplifierBefore_.addRule("Expression(ExplicitSuperConstructorInvocation($x,$y)) -> ExplicitSuperConstructorInvocation($x,$y)");
        simplifierBefore_.addRule("StatementExpression(ExplicitSuperConstructorInvocation($x,$y)) -> ExplicitSuperConstructorInvocation($x,$y)");


        simplifierBefore_.addRule("MethodDeclaration($tp,$rt,MethodDeclarator($x,$y,$z),$th,$bl) -> MethodDeclaration($tp,ReferenceType($x,$rt),MethodDeclarator($y,$z),$th,$bl)");


        
        BTStrategy strategyAfter=new BTStrategy();
        simplifierAfter_=new TermSystem(strategyAfter,facts,TermWare.getInstance());
        
       // simplifierAfter_.setDebugMode(true);
       // simplifierAfter_.setDebugEntity("All");
        
        simplifierAfter_.addRule("ArgumentList($x) -> $x");
        simplifierAfter_.addRule("Arguments() -> Arguments([])");
        
        
        //simplifierAfter_.addRule("Arguments([ArgumentList($x)]) -> Arguments($x)");
        //simplifierAfter_.addRule("Arguments(ArgumentList($x)) -> Arguments($x)");

         
        simplifierAfter_.addRule("ConditionalAndExpression([$x:$y]) -> ConditionalAndExpression($x,ConditionalAndExpression($y))");
        simplifierAfter_.addRule("ConditionalAndExpression($x,[$y:$z]) -> ConditionalAndExpression($x,ConditionalAndExpression($y,$z))");
        simplifierAfter_.addRule("ConditionalAndExpression([]) -> [] ");
        simplifierAfter_.addRule("ConditionalAndExpression($x,[]) -> $x");
         
        simplifierAfter_.addRule("ConditionalOrExpression([$x:$y]) -> ConditionalOrExpression($x,ConditionalOrExpression($y))");
        simplifierAfter_.addRule("ConditionalOrExpression($x,[$y:$z]) -> ConditionalOrExpression($x,ConditionalOrExpression($y,$z))");
        simplifierAfter_.addRule("ConditionalOrExpression([]) -> []");
        simplifierAfter_.addRule("ConditionalOrExpression($x,[]) -> $x");
         
        simplifierAfter_.addRule("InclusiveOrExpression([$x:$y]) -> InclusiveOrExpression($x,$y)");
        simplifierAfter_.addRule("InclusiveOrExpression($x,[$y:$z]) -> InclusiveOrExpression($x,InclusiveOrExpression($y,$z))");
        simplifierAfter_.addRule("InclusiveOrExpression($x,[]) -> $x");
         
        simplifierAfter_.addRule("ExclusiveOrExpression([$x:$y]) -> ExclusiveOrExpression($x,$y)");
        simplifierAfter_.addRule("ExclusiveOrExpression($x,[$y:$z]) -> ExclusiveOrExpression($x,ExclusiveOrExpression($y,$z))");
        simplifierAfter_.addRule("ExclusiveOrExpression($x,[]) -> $x");
         
        simplifierAfter_.addRule("AndExpression([$x:$y]) -> AndExpression($x,$y)");
        simplifierAfter_.addRule("AndExpression($x,[$y:$z]) -> AndExpression($x,AndExpression($y,$z))");
        simplifierAfter_.addRule("AndExpression($x,[]) -> $x");
         
         
        simplifierAfter_.addRule("EqualityExpression($x,[$y:$z]) -> EqualityExpression(EqualityExpression($x,$y),$z)");
        simplifierAfter_.addRule("EqualityExpression($x,[]) -> $x");
        simplifierAfter_.addRule("EqualityExpression($x,EqualityExpressionOperand($op,$y)) -> EqualityExpression($x,$op,$y)");
         
        simplifierAfter_.addRule("RelationalExpression($x,[$y:$z]) -> RelationalExpression(RelationalExpression($x,$y),$z)");
        simplifierAfter_.addRule("RelationalExpression($x,[]) -> $x");
        simplifierAfter_.addRule("RelationalExpression($x,RelationalExpressionSuffix($op,$y)) -> RelationalExpression($x,$op,$y)");
         
        simplifierAfter_.addRule("ShiftExpression([$x:$y]) -> ShiftExpression($x,$y)");
        simplifierAfter_.addRule("ShiftExpression($x,[$y:$z]) -> ShiftExpression(ShiftExpression($x,$y),$z)");
        simplifierAfter_.addRule("ShiftExpression($x,[]) -> $x");
        simplifierAfter_.addRule("ShiftExpression($x,ShiftExpressionOperand($op,$y)) -> ShiftExpression($x,$op,$y)");
         
         
        simplifierAfter_.addRule("AdditiveExpression($x,[$y:$z]) -> AdditiveExpression(AdditiveExpression($x,$y),$z)");
        simplifierAfter_.addRule("AdditiveExpression($x,AdditiveOperand($op,$y)) -> AdditiveExpression($x,$op,$y)");
        simplifierAfter_.addRule("AdditiveExpression($x,[]) -> $x");
         
        simplifierAfter_.addRule("MultiplicativeExpression($x,[$y:$z])->MultiplicativeExpression(MultiplicativeExpression($x,$y),$z)");
        simplifierAfter_.addRule("MultiplicativeExpression($x,MultiplicativeOperand($op,$y)) -> MultiplicativeExpression($x,$op,$y)");
        simplifierAfter_.addRule("MultiplicativeExpression($x,[]) -> $x");

        simplifierAfter_.addRule("FormalParameterModifiers($x,$y) -> Modifiers($x,$y)");
                 
        initialized_=true;
    }
    
    public static Term javaTermArgsAsList(Term t,boolean deep) throws TermWareException {
        Term list=TermWare.getInstance().getTermFactory().createNIL();
        for(int i=t.getArity(); i>0; --i) {
            Term ct=t.getSubtermAt(i-1);
            if (deep) {
                ct=transformSeqToList(ct);
            }
            list=TermWare.getInstance().getTermFactory().createTerm("cons",ct,list);
        }
        t=TermWare.getInstance().getTermFactory().createTerm(t.getName(),list);
        return t;
    }
    
    public static Term javaTermArgsAsList(Term t, int startFrom, boolean deep) throws TermWareException {
        Term list=TermWare.getInstance().getTermFactory().createNIL();
        for(int i=t.getArity(); i>startFrom; --i) {
            Term ct=t.getSubtermAt(i-1);
            if (deep) {
                ct=transformSeqToList(ct);
            }
            if (!ct.isNil()) {
                list=TermWare.getInstance().getTermFactory().createTerm("cons",ct,list);
            }
        }
        Term[] newBody=new Term[startFrom+1];
        for(int i=0; i<startFrom; ++i) {
            newBody[i]=t.getSubtermAt(i);
        }
        newBody[startFrom]=list;
        t=TermWare.getInstance().getTermFactory().createTerm(t.getName(),newBody);
        return t;
    }
    
    
    public static Term transformSeqToList(Term t) throws TermWareException {
        Term retval=null;
        if (t.isComplexTerm()) {
            if (t.getName().equals("Name")) {
                retval=javaTermArgsAsList(t,0,false);
            }else if (t.getName().equals("NameList")
            ||t.getName().equals("FormalParameters")
            ||t.getName().equals("ClassOrInterfaceType")
            ||t.getName().equals("ClassOrInterfaceBody")
            ||t.getName().equals("EnumBody")
            ||t.getName().equals("ExtendsList")
            ||t.getName().equals("ImplementsList")
            ||t.getName().equals("Block")
            ||t.getName().equals("ArgumentList")
            ||t.getName().equals("TypeParameters")
            ||t.getName().equals("TypeArguments")
            ||t.getName().equals("TypeBound")
            ||t.getName().equals("StatementExpressionList")
            ||t.getName().equals("ArrayDims")
            ||t.getName().equals("ArrayInitializer")
            ||t.getName().equals("CatchSequence")
            ||t.getName().equals("AnnotationTypeBody")           
            ||t.getName().equals("MemberValuePairs")           
            ||t.getName().equals("MemberValueArrayInitializer")           
            ) {
                retval=javaTermArgsAsList(t,0,true);
            }else if (t.getName().equals("FieldDeclaration")
            ||t.getName().equals("SwitchStatement")
            ||t.getName().equals("SwitchStatementLabelBlock")
            ||t.getName().equals("ConditionalOrExpression")
            ||t.getName().equals("ConditionalAndExpression")
            ||t.getName().equals("InclusiveOrExpression")
            ||t.getName().equals("ExclusiveOrExpression")
            ||t.getName().equals("AndExpression")
            ||t.getName().equals("EqualityExpression")
            ||t.getName().equals("RelationalExpression")
            ||t.getName().equals("ShiftExpression")
            ||t.getName().equals("AdditiveExpression")
            ||t.getName().equals("MultiplicativeExpression")
            ||t.getName().equals("Modifiers")
            ||t.getName().equals("FormalParameterModifiers")
            // ||t.getName().equals("PrimaryExpression")
            ) {
                retval=javaTermArgsAsList(t,1,true);
                Term frs=transformSeqToList(t.getSubtermAt(0));
                retval.setSubtermAt(0,frs);
            }else if (t.getName().equals("LocalVariableDeclaration")){
                t.setSubtermAt(0,transformSeqToList(t.getSubtermAt(0)));
                t.setSubtermAt(1,transformSeqToList(t.getSubtermAt(1)));
                retval=javaTermArgsAsList(t,2,true);
            }else if (t.getName().equals("ConstructorDeclaration")) {
                for(int i=0; i<5; ++i) {
                    t.setSubtermAt(i,transformSeqToList(t.getSubtermAt(i)));
                }
                retval=javaTermArgsAsList(t,5,true);
            }else{
                for(int i=0; i<t.getArity(); ++i) {
                    t.setSubtermAt(i,transformSeqToList(t.getSubtermAt(i)));
                }
                retval=t;
            }
        }else{
            retval=t;
        }
        return retval;
    }
    
    /**
     * simplify AST Tree by erasing intermediate nodes, which are not used.
     *(for example ConditionalOrExpresson without OR statement).
     *Childs of intermediate nodes became the childs of parent of such intermediate
     *node.
     */
    public Term simplifyBefore(Term t) throws TermWareException {
        if (!initialized_) {
            init();
        }
        return simplifierBefore_.reduce(t);
    }
    
    public Term simplifyAfter(Term t) throws TermWareException {
        return simplifierAfter_.reduce(t);
    }
    
    
    public Term insertEmptyTypeParametersExtendsAndImplementLists(Term x) throws TermWareException {
        //System.out.println("[!}transform:"+TermHelper.termToString(t));
        
        /*
        if (true) {
            return x;
        }
         **/
        
        LinkedList<Term> termsToSet = new LinkedList<Term>();
        LinkedList<Term> termsToTransform = new LinkedList<Term>();
        LinkedList<Integer> ints = new LinkedList<Integer>();
        
        termsToTransform.add(x);
        
        Term retval=null;
        Term t=x;
        
        while(!termsToTransform.isEmpty()) {
            t = termsToTransform.removeFirst();            
            if (t.isComplexTerm()) {
                if (t.getName().equals("ClassOrInterfaceDeclaration")) {
                    //System.out.println("transform1:"+TermHelper.termToString(t));
                    Term[] newDefBody=new Term[6];
                    int nUsed=0;
                    for(int i=0; i<t.getArity();++i) {
                        Term st=t.getSubtermAt(i);
                        if (st.getName().equals("class") || st.getName().equals("interface")) {
                            newDefBody[0]=st;
                            ++nUsed;
                        }else if(st.getName().equals("Identifier")) {
                            newDefBody[1]=st;
                            ++nUsed;
                        }else if(st.getName().equals("TypeParameters")){
                            newDefBody[2]=st;
                            ++nUsed;
                        }else if(st.getName().equals("ExtendsList")) {
                            newDefBody[3]=st;
                            ++nUsed;
                        }else if(st.getName().equals("ImplementsList")) {
                            newDefBody[4]=st;
                            ++nUsed;
                        }else if(st.getName().equals("ClassOrInterfaceBody") || st.getName().equals("ClasOrInterfaceBodyDeclaration")) {
                            newDefBody[5]=insertEmptyTypeParametersExtendsAndImplementLists(st);
                            ++nUsed;
                        }else{
                            throw new AssertException("Unknown subtern of ClassOrInterfaceDeclaration:"+st.getName());
                        }
                    }
                    if (nUsed<6) {
                        if (newDefBody[2]==null) {
                            newDefBody[2]=TermWare.getInstance().getTermFactory().createNil();
                        }
                        if (newDefBody[3]==null) {
                            newDefBody[3]=TermWare.getInstance().getTermFactory().createTerm("ExtendsList",
                                    TermWare.getInstance().getTermFactory().createNil());
                        }
                        if (newDefBody[4]==null){
                            newDefBody[4]=TermWare.getInstance().getTermFactory().createTerm("ImplementsList",
                                    TermWare.getInstance().getTermFactory().createNil());
                        }
                        if (newDefBody[5]==null) {
                            newDefBody[5]=TermWare.getInstance().getTermFactory().createTerm("ClassOrInterfaceBody",
                                    TermWare.getInstance().getTermFactory().createNil());
                        }
                        Term newTerm=TermWare.getInstance().getTermFactory().createTerm("ClassOrInterfaceDeclaration",newDefBody);
                        newTerm=TermHelper.copyAttributes(newTerm,t);
                        t=newTerm;
                    }
                    Term t4=insertEmptyTypeParametersExtendsAndImplementLists(t.getSubtermAt(4));
                    if (t4!=newDefBody[4]) {
                        t.setSubtermAt(4,t4);
                    }
                }else if(t.getName().equals("EnumDeclaration")) {
                    // check implements list
                    Term t0 = t.getSubtermAt(0);
                    Term t1=t.getSubtermAt(1);
                    if (t1.getName().equals("EnumBody")) {
                        // insert empty implements list.
                        Term nilImplements = TermWare.getInstance().getTermFactory().createNIL();
                        Term[] newBody = new Term[3];
                        newBody[0]=t0;
                        newBody[1]=nilImplements;
                        newBody[2]=t1;
                        t=TermWare.getInstance().getTermFactory().createComplexTerm("EnumDeclaration",newBody);
                    }else{
                        // this is implements list
                        // so, this is full enum term, do nothing
                    }
                }else{
                    for(int i=0; i<t.getArity();++i) {
                        Term st=t.getSubtermAt(i);
                        termsToSet.addLast(t);
                        termsToTransform.addLast(st);
                        ints.addLast(i);
                    }
                }
            }
            if (retval==null) {
                retval=t;
            }else  if (!termsToSet.isEmpty())  {            
                Term svT=termsToSet.removeFirst();
                int index=ints.removeFirst();                
                svT.setSubtermAt(index,t);            
            }                       
        }
        return retval; /* really unreachable, but compiler does not known about this */
    }
    
    
    /**
     * erase pointers to parents, to free memory from intermediate nodes.
     *by default enabled, can be disabled.
     */
    Term eraseJjtParents(Term t) throws TermWareException {
        for(int i=0; i<t.getArity(); ++i) {
            Term st=t.getSubtermAt(i);
            if (st instanceof JavaNode) {
                ((JavaNode)st).jjtSetParent(null);
            }
            eraseJjtParents(st);
        }
        return t;
    }
    
    private static final Term[] EMPTY_TERM_ARRAY=new Term[0];
    
    TermSystem simplifierAfter_=null;
    TermSystem simplifierBefore_=null;
    boolean initialized_=false;
    
}
