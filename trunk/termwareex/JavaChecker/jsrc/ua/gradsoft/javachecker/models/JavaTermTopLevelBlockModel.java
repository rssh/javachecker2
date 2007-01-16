/*
 * JavaTermBlockModel.java
 *
 * Created on �'������, 12, ���� 2007, 1:22
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for java block as term.
 * @author Ruslan Shevchenko
 */
public class JavaTermTopLevelBlockModel implements JavaTopLevelBlockModel
{
    
   public JavaTermTopLevelBlockModel(JavaTermTopLevelBlockOwnerModel owner, Term t) throws TermWareException
   {
     owner_=owner;      
     build(t);
   }
      
                   
    public JavaTopLevelBlockOwnerModel  getOwnerModel()
    { return owner_; }
    
    public JavaTermTopLevelBlockOwnerModel  getOwnerTermModel()
    { return owner_; }
    
    
    /**
     * return list of statements in block.
     */
    public List<JavaStatementModel> getStatements()
    {
      return statements_;   
    }
       
    private void build(Term t) throws TermWareException
    {
      //System.err.println("build top level block:"+TermHelper.termToString(t));
      JavaTermStatementModel prevS=null;  
      while(!t.isNil()) {
          Term statementTerm = t.getSubtermAt(0);
          //System.err.println("Statement term:"+TermHelper.termToString(statementTerm));
          t=t.getSubtermAt(1);
          JavaTermStatementModel ts = new JavaTermStatementModel(this,statementTerm,null,prevS);
          statements_.add(ts);
          prevS=ts;
      }  
    }
   
    private JavaTermTopLevelBlockOwnerModel           owner_;
    private List<JavaStatementModel>  statements_  = new LinkedList<JavaStatementModel>();    
    
}
