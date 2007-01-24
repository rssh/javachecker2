/*
 * ASTTransformers.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5;

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
    
    public class ASTFacts extends DefaultFacts
    {
        public ASTFacts() throws TermWareException
        {super(); }
        
        public boolean isIdentifier(Term t)
        {
          return t.getName().equals("Identifier");
        }

        public boolean isName(Term t)
        {
          return t.getName().equals("Name");  
        }
        
        public void append(TransformationContext ctx,Term result,Term n,Term x) throws TermWareException
        {
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
        BTStrategy strategy=new BTStrategy();
        //FirstTopStrategy strategy=new FirstTopStrategy();
        IFacts  facts=new ASTFacts();
        
        simplifier_=new TermSystem(strategy,facts,TermWare.getInstance());
        //simplifier_.setDebugEntity("All");
        //simplifier_.setDebugMode(true);
        
        simplifier_.addRule("Expression($x) -> $x");
        simplifier_.addRule("ConditionalExpression($x) -> $x");
        simplifier_.addRule("ConditionalOrExpression($x) -> $x");
        simplifier_.addRule("ConditionalAndExpression($x) -> $x");
        simplifier_.addRule("InclusiveOrExpression($x) -> $x");
        simplifier_.addRule("ExclusiveOrExpression($x) -> $x");
        simplifier_.addRule("AndExpression($x) -> $x");
        simplifier_.addRule("EqualityExpression($x) -> $x");
        simplifier_.addRule("InstanceOfExpression($x) -> $x");
        simplifier_.addRule("RelationalExpression($x) -> $x");
        simplifier_.addRule("ShiftExpression($x) -> $x");
        simplifier_.addRule("AdditiveExpression($x) -> $x");
        simplifier_.addRule("MultiplicativeExpression($x) -> $x");
        simplifier_.addRule("UnaryExpression($x) -> $x");
        simplifier_.addRule("UnaryExpressionNotPlusMinus($x) -> $x");
        simplifier_.addRule("PostfixExpression($x) -> $x");
        simplifier_.addRule("PrimaryExpression($x) -> $x");
        //simplifier_.addRule("PrimaryPrefix($x) -> $x");
        //simplifier_.addRule("PrimarySuffix($x) -> $x");
        simplifier_.addRule("Literal($x) -> $x");
        
        simplifier_.addRule("Statement($x) -> $x");
        simplifier_.addRule("BlockStatement($x) -> $x");
        
        simplifier_.addRule("Type($x) -> $x");
        simplifier_.addRule("PrimitiveType($x)->$x");
        simplifier_.addRule("ClassOrInterfaceType($x) -> $x");
        simplifier_.addRule("VariableInitializer($x) -> $x");
        simplifier_.addRule("ArgumentList($x) -> $x");
        
        simplifier_.addRule("ExplicitConstructorInvocation($x) -> $x");

        simplifier_.addRule("ExplicitConstructorInvocation($x) -> $x");

        simplifier_.addRule("IdentifierOrFunctionCall($x,$y) -> FunctionCall($x,$y)");
        simplifier_.addRule("IdentifierOrFunctionCall($x) -> $x");
               
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(ThisSelector(),$y)) -> PrimaryExpression(This($x),$y)");
        simplifier_.addRule("PrimaryExpression($x,ThisSelector()) -> PrimaryExpression(This($x))");

        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(AllocationSelector($y),$z)) -> PrimaryExpression(Allocation($x,$y),$z)");
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(AllocationSelector($y))) -> PrimaryExpression(Allocation($x,$y))");
       
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(MethodCallSelector($y,$z),$w)) -> PrimaryExpression(MethodCall($x,$y,$z),$w)");
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(MethodCallSelector($y,$z))) -> PrimaryExpression(MethodCall($x,$y,$z))");
        
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(SpecializedMethodCallSelector($ta,$y,$z),$w)) -> PrimaryExpression(SpecializedMethodCall($x,$ta,$y,$z),$w)");
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(SpecializedMethodCallSelector($ta,$y,$z))) -> PrimaryExpression(SpecializedMethodCall($x,$ta,$y,$z))");
       
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(FieldSelector($y),$w)) -> PrimaryExpression(Field($x,$y),$w)");
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(FieldSelector($y))) -> PrimaryExpression(Field($x,$y))");
        
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(SpecializedFieldSelector($ta,$y),$w)) -> PrimaryExpression(SpecializedField($x,$ta,$y),$w)");
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(SpecializedFieldSelector($ta,$y))) -> PrimaryExpression(SpecializedField($x,$ta,$y))");       

        simplifier_.addRule("MemberSelector($x,$y,$z) [ isNil($x) ] -> MethodCallSelector($y,$z) !-> SpecializedMethodCallSelector($x,$y,$z)");        
        simplifier_.addRule("MemberSelector($x,$y) [ isNil($x) ] -> FieldSelector($y) !-> SpecializedFieldSelector($x,$y)"); 
        
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(ArrayIndexSelector($y),$w)) -> PrimaryExpression(ArrayIndex($x,$y),$w)");
        simplifier_.addRule("PrimaryExpression($x,PrimarySuffix(ArrayIndexSelector($y))) -> PrimaryExpression(ArrayIndex($x,$y))");                        
        
        simplifier_.addRule("Field($x,$y) [isIdentifier($x) && isIdentifier($y)] -> Name($x,$y)");
        simplifier_.addRule("Field($x,$y) [isName($x) && isIdentifier($y) ] -> $z [ append($z,$x,$y) ] ");
                
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
            ||t.getName().equals("PrimaryExpression")
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
    public Term simplify(Term t) throws TermWareException {
        if (!initialized_) {
            init();
        }
        return simplifier_.reduce(t);
    }
    
    
    public Term insertEmptyTypeParametersExtendsAndImplementLists(Term t) throws TermWareException {
        //System.out.println("transform:"+TermHelper.termToString(t));
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
                    t.setSubtermAt(i,insertEmptyTypeParametersExtendsAndImplementLists(st));
                }
            }
        }
        return t;
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
    
    TermSystem simplifier_=null;
    boolean initialized_=false;
    
}
