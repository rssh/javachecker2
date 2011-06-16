/*
 * ASTTransformers.java
 *
 * Copyright (c) 2008 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.dot;

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
        
        public boolean isId(Term t) {
            return t.getName().equals("id");
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

        simplifierBefore_.addRule("stmt($x)  ->  $x");
        simplifierBefore_.addRule("portLocation($x)  ->  $x");
        simplifierBefore_.addRule("portAngle($x)  ->  $x");

        
        simplifierBefore_.addRule("nodeOrEdgeStmt($node,edgeStmtTail($rhs,$attrlist))  ->  dotEdges(cons($node,$rhs),$attrlist)");
        simplifierBefore_.addRule("nodeOrEdgeStmt($node,edgeStmtTail($rhs))  ->  dotEdges(cons($node,$rhs),NIL)");
        
        simplifierBefore_.addRule("edgeRHS($op,$node) -> cons($node,NIL)");
        simplifierBefore_.addRule("edgeRHS($op,$node,$rhs) -> cons($node,$rhs)");
        

        simplifierBefore_.addRule("nodeOrEdgeStmt($node,attrList..($args))  ->  dotNode($node,attrList..($args))");
        simplifierBefore_.addRule("nodeOrEdgeStmt($node)  ->  dotNode($node,NIL)");
        
        simplifierBefore_.addRule("subgraphOrEdgeSubgraphStmt($subgraph,edgeStmtTail($rhs,$attrlist)) -> dotEdges(cons($subgraph,$rhs),$attrlist)");
        simplifierBefore_.addRule("subgraphOrEdgeSubgraphStmt($subgraph,edgeStmtTail($rhs)) -> dotEdges(cons($subgraph,$rhs),NIL)");
        simplifierBefore_.addRule("subgraphOrEdgeSubgraphStmt($subgraph) -> $subgraph");

        simplifierBefore_.addRule("dotCompoundRecord($x) -> $x");
        simplifierBefore_.addRule("dotRecordElement($x) -> $x");

        
        
        BTStrategy strategyAfter=new BTStrategy();
        simplifierAfter_=new TermSystem(strategyAfter,facts,TermWare.getInstance());
        
       // simplifierAfter_.setDebugMode(true);
       // simplifierAfter_.setDebugEntity("All");
        
        simplifierAfter_.addRule("dotStmtList($x) -> $x");
        simplifierAfter_.addRule("attrList($x) -> $x");
        simplifierAfter_.addRule("attrAssignmentsList($x) -> $x");
        
        
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
            if (t.getName().equals("attrList")
                ||t.getName().equals("attrAssignmentsList")
                ||t.getName().equals("dotStmtList")
                ||t.getName().equals("dotRecord")
               ) {
                retval=javaTermArgsAsList(t,0,true);
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
    
    
    /**
     * erase pointers to parents, to free memory from intermediate nodes.
     *by default enabled, can be disabled.
     */
    Term eraseJjtParents(Term t) throws TermWareException {
        for(int i=0; i<t.getArity(); ++i) {
            Term st=t.getSubtermAt(i);
            if (st instanceof DotNode) {
                ((DotNode)st).jjtSetParent(null);
            }
            eraseJjtParents(st);
        }
        return t;
    }
    
    TermSystem simplifierAfter_=null;
    TermSystem simplifierBefore_=null;
    boolean initialized_=false;
    
}
