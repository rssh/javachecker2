/*
 * JavaTermStatementModel.java
 *
 * Created on п'€тниц€, 12, с≥чн€ 2007, 1:43
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for java statement.
 * @author Ruslan Shevchenko
 */
public class JavaTermStatementModel implements JavaStatementModel
{
    
    public JavaTermStatementModel(JavaTermTopLevelBlockModel blockModel,Term t,JavaTermStatementModel parent,JavaTermStatementModel previous) throws TermWareException
    {
      blockModel_=blockModel;  
      t_=t;  
      parent_=parent;
      previous_=previous;
      build(t);
    }
    
    
    
    private void build(Term t) throws TermWareException
    {
       if (t.getName().equals("LabeledStatement")) {
           kind_=JavaStatementKind.LABELED_STATEMENT;    
           childs_=new LinkedList<JavaStatementModel>();
           childs_.add(new JavaTermStatementModel(blockModel_,t.getSubtermAt(1),this,null));
       }else if(t.getName().equals("AssertStatement")) {
           kind_=JavaStatementKind.ASSERT_STATEMENT;    
           childs_=Collections.emptyList();      
           extractAnonimousTypes(t.getSubtermAt(0));
           if (t.getArity() > 1) {
               extractAnonimousTypes(t.getSubtermAt(0));
           }
       }else if(t.getName().equals("Block")){
           kind_=JavaStatementKind.BLOCK;
           childs_=new LinkedList<JavaStatementModel>();
           Term cur=t.getSubtermAt(0);
           JavaTermStatementModel prevS=null;
           while(!cur.isNil()) {
               Term st=cur.getSubtermAt(0);
               cur=cur.getSubtermAt(1);
               JavaTermStatementModel s=new JavaTermStatementModel(blockModel_,st,this,prevS);
               childs_.add(s);
               prevS=s;
           }           
       }else if(t.getName().equals("EmptyStatement")){
           kind_=JavaStatementKind.EMPTY_STATEMENT;
           childs_=Collections.emptyList();           
       }else if(t.getName().equals("StatementExpressionStatement")){
           kind_=JavaStatementKind.STATEMENT_EXPRESSION_STATEMENT;
           childs_=Collections.emptyList();
           extractAnonimousTypes(t.getSubtermAt(0));
       }else if(t.getName().equals("SwitchStatement")){
           kind_=JavaStatementKind.SWITCH_STATEMENT;
           childs_=new LinkedList<JavaStatementModel>();
           //System.err.println("!!!Switch:"+TermHelper.termToString(t));
           extractAnonimousTypes(t.getSubtermAt(0));
           Term switchStatementLabelBlockList = t.getSubtermAt(1); 
           JavaTermStatementModel prevS=null;
           while(!switchStatementLabelBlockList.isNil()) {
             Term switchStatementLabelBlock=switchStatementLabelBlockList.getSubtermAt(0);
             switchStatementLabelBlockList=switchStatementLabelBlockList.getSubtermAt(1);
             Term blockStatementList = switchStatementLabelBlock.getSubtermAt(1);
             Term newBlock=TermWare.getInstance().getTermFactory().createTerm("Block",blockStatementList);
             JavaTermStatementModel s = new JavaTermStatementModel(blockModel_,newBlock,this,prevS);
             childs_.add(s);               
             prevS=s;
           }            
       }else if(t.getName().equals("IfStatement")){
           kind_=JavaStatementKind.IF_STATEMENT;
           extractAnonimousTypes(t.getSubtermAt(0));
           childs_=new LinkedList<JavaStatementModel>();
           Term stTrue = t.getSubtermAt(1);
           JavaTermStatementModel stm=new JavaTermStatementModel(blockModel_,stTrue,this,null);
           childs_.add(stm);
           if (t.getArity()==3) {
               Term stFalse = t.getSubtermAt(2);
               JavaTermStatementModel stm1=new JavaTermStatementModel(blockModel_,stFalse,this,stm);
               childs_.add(stm1);
           }
           
       }else if(t.getName().equals("WhileStatement")){
           kind_=JavaStatementKind.WHILE_STATEMENT;
           extractAnonimousTypes(t.getSubtermAt(0));
           childs_=new LinkedList<JavaStatementModel>();
           JavaTermStatementModel stm=new JavaTermStatementModel(blockModel_,t.getSubtermAt(1),this,null);           
           childs_.add(stm);           
       }else if(t.getName().equals("DoStatement")){
           kind_=JavaStatementKind.DO_STATEMENT;
           childs_=new LinkedList<JavaStatementModel>();
           JavaTermStatementModel stm=new JavaTermStatementModel(blockModel_,t.getSubtermAt(0),this,null);                      
           extractAnonimousTypes(t.getSubtermAt(1));
       }else if(t.getName().equals("ForStatement")){
           kind_=JavaStatementKind.FOR_STATEMENT;
           Term loopHead=t.getSubtermAt(0);
           extractLocalVariableAndAnonimousTypesFromLoopHead(loopHead);          
           Term st=t.getSubtermAt(1);
       }else if(t.getName().equals("BreakStatement")){
           kind_=JavaStatementKind.BREAK_STATEMENT;
           childs_=Collections.emptyList();
       }else if(t.getName().equals("ContinueStatement")){
           kind_=JavaStatementKind.CONTINUE_STATEMENT;
           childs_=Collections.emptyList();
       }else if(t.getName().equals("ReturnStatement")){
           kind_=JavaStatementKind.RETURN_STATEMENT;
           childs_=Collections.emptyList();
           if (t.getArity()>0) {
               extractAnonimousTypes(t.getSubtermAt(0));
           }
       }else if(t.getName().equals("ThrowStatement")){
           kind_=JavaStatementKind.THROW_STATEMENT;
           childs_=Collections.emptyList();
           extractAnonimousTypes(t.getSubtermAt(0));
       }else if(t.getName().equals("SynchronizedStatement")){
           kind_=JavaStatementKind.SYNCHRONIZED_STATEMENT;
           childs_=new LinkedList<JavaStatementModel>();
           JavaTermStatementModel prevS=null;
           Term blockTerm = t.getSubtermAt(1);
           Term l=blockTerm.getSubtermAt(0);
           while(!l.isNil()) {
               Term ct=l.getSubtermAt(0);
               l=l.getSubtermAt(1);
               JavaTermStatementModel s = new JavaTermStatementModel(blockModel_,ct,this,prevS);
               childs_.add(s);
               prevS=s;
           }
       }else if(t.getName().equals("TryStatement")){
           kind_=JavaStatementKind.TRY_STATEMENT;
           childs_=new LinkedList<JavaStatementModel>();
           JavaTermStatementModel block = new JavaTermStatementModel(blockModel_,t.getSubtermAt(0),this,null);
           childs_.add(block);
           JavaTermStatementModel catchSeq = new JavaTermStatementModel(blockModel_,t.getSubtermAt(1),this,block);
           if (t.getArity()>2) {
               JavaTermStatementModel fb=new JavaTermStatementModel(blockModel_,t.getSubtermAt(2),this,catchSeq);
               childs_.add(fb);
           }
       }else if(t.getName().equals("CatchSequence")){
           kind_=JavaStatementKind.CATCH_SEQUENCE;
           childs_=new LinkedList<JavaStatementModel>();
           JavaTermStatementModel prevS=null;
           Term l=t.getSubtermAt(0);
           while(!l.isNil()) {
               Term ct=l.getSubtermAt(0);
               l=l.getSubtermAt(1);
               JavaTermStatementModel s = new JavaTermStatementModel(blockModel_,ct,this,prevS);
               childs_.add(s);
               prevS=s;
           }
       }else if(t.getName().equals("Catch")){
           kind_=JavaStatementKind.CATCH;
           childs_=new LinkedList<JavaStatementModel>();
           extractLocalVariableFromFormalParameter(t.getSubtermAt(0));
           JavaTermStatementModel s = new JavaTermStatementModel(blockModel_,t.getSubtermAt(1),this,null);
           childs_.add(s);
       }else if(t.getName().equals("LocalVariableDeclaration")){
           kind_=JavaStatementKind.LOCAL_VARIABLE_DECLARATION;
           childs_=Collections.emptyList();
           extractLocalVariablesAndAnonimousTypesFromLocalVariableDeclaration(t);           
       }else if(t.getName().equals("ClassOrInterfaceDeclaration")){
           kind_=JavaStatementKind.CLASS_OR_INTERFACE_DECLARATION;
           childs_=Collections.emptyList();
           JavaTermClassOrInterfaceModel iTypeModel = new JavaTermClassOrInterfaceModel(0,t,blockModel_.getOwnerModel().getTypeModel().getPackageModel()); 
           addLocalType(iTypeModel);
       }else{
           throw new AssertException("Invalid Statement:"+TermHelper.termToString(t));
       }
    }
    
    public JavaStatementKind       getKind()
    {
      return kind_;  
    }
    
    
    public JavaTopLevelBlockModel  getTopLevelBlockModel()
    {
      return blockModel_;  
    }
    
    public JavaTermTopLevelBlockModel  getTermTopLevelBlockModel()
    { return blockModel_; }
    
    public JavaStatementModel getParentStatementModel()
    { return parent_; }
    
    
    public JavaStatementModel getPreviousStatementModel()
    { return previous_; }
    
    public List<JavaStatementModel> getChildStatements()
    {
      return childs_;  
    }
    
    public List<JavaLocalVariableModel> getLocalVariables()
    { return localVariables_; }
    
    /**
     * return localType, defined in this statement, or null if this statement
     *is not defintioon fo local type
     */
    public JavaTypeModel getLocalType()
    {      
      return localType_;
    }
    
    /**
     * StatementModel(term,context)
     */
    public Term getModelTerm() throws TermWareException
    {        
      JavaPlaceContext ctx = JavaPlaceContextFactory.createNewStatementContext(this);
      Term tctx=TermUtils.createJTerm(ctx);
      Term retval = TermUtils.createTerm("StatementModel",t_,tctx);
      return retval;
    }
    
    private void extractLocalVariableAndAnonimousTypesFromLoopHead(Term loopHead) throws TermWareException
    {
        if (loopHead.getName().equals("TraditionalForLoopHead")){
           Term initTerm=loopHead.getSubtermAt(0);
           if (initTerm.isNil()) {
               return;
           }else{
               if (initTerm.getName().equals("LocalVariableDeclaration")) {
                   extractLocalVariablesAndAnonimousTypesFromLocalVariableDeclaration(initTerm);
               }else{
                   return;
               }
           }
        }else if(loopHead.getName().equals("ForEachLoopHead")){
           Term typeTerm = loopHead.getSubtermAt(0);
           Term identifierTerm = loopHead.getSubtermAt(1);
           String name=identifierTerm.getName();
           localVariables_.add(new JavaTermLocalVariableModel(name,JavaLocalVariableKind.FOR_EACH_LOOP_HEAD,typeTerm,loopHead.getSubtermAt(2), this));
           Term exprTerm = loopHead.getSubtermAt(2);
           extractAnonimousTypes(exprTerm);
        }else{
            throw new AssertException("Invalid loop head:"+TermHelper.termToString(loopHead));
        }
    }
    
    private void extractLocalVariablesAndAnonimousTypesFromLocalVariableDeclaration(Term dcl) throws TermWareException
    {
       Term typeTerm = dcl.getSubtermAt(1);
       Term dclList=dcl.getSubtermAt(2);
       while(!dclList.isNil()) {
           Term vdcl=dclList.getSubtermAt(0);
           dclList=dclList.getSubtermAt(1);
           Term identifier=null;
           Term refTypeTerm=typeTerm;
           Term vid=vdcl.getSubtermAt(0);
           if (vid.getName().equals("VariableDeclaratorId")) {
               identifier=vid.getSubtermAt(0);
               if (vid.getArity()>1) {
                   int nReferences=vid.getSubtermAt(1).getInt();
                   if(nReferences>0) {
                       Term it=TermWare.getInstance().getTermFactory().createInt(nReferences);
                       refTypeTerm=TermWare.getInstance().getTermFactory().createTerm("ReferenceType",it,typeTerm);
                   }                   
               }
           }else if (vid.getName().equals("Identifier")) {
               identifier=vid;
           }else{
               throw new AssertException("Invalid variabl declarator:"+TermHelper.termToString(vid));
           }
           Term initTerm=TermWare.getInstance().getTermFactory().createNIL();
           if (vdcl.getArity()>1) {
               initTerm=vdcl.getSubtermAt(1);
               extractAnonimousTypes(initTerm);
           }
           String name=identifier.getSubtermAt(0).getString();
           localVariables_.add(new JavaTermLocalVariableModel(name,JavaLocalVariableKind.LOCAL_VARIABLE_DCL,refTypeTerm,initTerm,this));
       }
    }
    
    private void extractLocalVariableFromFormalParameter(Term fp) throws TermWareException
    {
        Term typeTerm = fp.getSubtermAt(1);
        Term vid = fp.getSubtermAt(2);
        Term refTypeTerm=typeTerm;
        Term init = TermWare.getInstance().getTermFactory().createNil();
        Term idTerm;
        if (vid.getName().equals("Identifier")) {
            idTerm=vid;
        }else if(vid.getName().equals("VariableDeclaratorId")) {
            idTerm=vid.getSubtermAt(0);
            if (vid.getArity()>1) {
               int nReferences=vid.getSubtermAt(1).getInt();
               if(nReferences>0) {
                       Term it=TermWare.getInstance().getTermFactory().createInt(nReferences);
                       refTypeTerm=TermWare.getInstance().getTermFactory().createTerm("ReferenceType",it,typeTerm);
               }                   
            }            
        }else{
            throw new AssertException("Invalid VariableDeclaratorId:"+TermHelper.termToString(vid));
        }
        String name=idTerm.getSubtermAt(0).getString();
        localVariables_.add(new JavaTermLocalVariableModel(name,JavaLocalVariableKind.CATCH_EXCEPTION,refTypeTerm,init,this));        
    }
    
    private void extractAnonimousTypes(Term expr) throws TermWareException
    {
      if (expr.isComplexTerm()) {
          if (expr.getName().equals("AllocationExpression")) {
              Term acb = expr.getSubtermAt(3);
              if (!acb.isNil()) {
                  JavaTermAnonimousTypeModel tm = new JavaTermAnonimousTypeModel(this,expr);
                  // tm added to parent in constructor. so - do nothing.
              }
          }else{
             // run on subterms. 
             // possible in fututr: optimize, by knowning expression model.
             for(int i=0; i< expr.getArity(); ++i) {
                 extractAnonimousTypes(expr.getSubtermAt(i));
             }
          }
      }
    }
    
    private void addLocalType(JavaTermClassOrInterfaceModel localType)
    {
      localType_=localType;      
      localType.setIsLocal(this);
    }
      
    private JavaStatementKind kind_;
    private Term              t_;
    private JavaTermTopLevelBlockModel blockModel_;
    private JavaTermStatementModel parent_=null;
    private JavaTermStatementModel previous_=null;
    
    private List<JavaStatementModel> childs_;
    private List<JavaLocalVariableModel> localVariables_=new LinkedList<JavaLocalVariableModel>();
    private JavaTypeModel localType_=null;
    
}
