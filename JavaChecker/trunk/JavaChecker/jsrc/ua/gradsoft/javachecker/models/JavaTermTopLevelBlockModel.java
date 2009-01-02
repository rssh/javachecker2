/*
 * JavaTermBlockModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for java block as term.
 * @author Ruslan Shevchenko
 */
public class JavaTermTopLevelBlockModel implements JavaTopLevelBlockModel
{

   /**
    * build top level block.
    *t must be a list of sequences. 
    */ 
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
       
    /**
     * Block( cons(StatementModel,....) )
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term retval=TermUtils.createNil();
        for(JavaStatementModel ts:getStatements()) {
            Term statementModelTerm = ts.getModelTerm();
            retval=TermUtils.createTerm("cons",statementModelTerm,retval);
        }
        retval = TermUtils.reverseListTerm(retval);
        retval = TermUtils.createTerm("Block",retval);
        return retval;
    }
   
    
    private void build(Term t) throws TermWareException
    {      
      JavaTermStatementModel prevS=null;  
      while(!t.isNil()) {
          Term statementTerm = t.getSubtermAt(0);          
          t=t.getSubtermAt(1);
          JavaTermStatementModel ts = new JavaTermStatementModel(this,statementTerm,null,prevS);
          statements_.add(ts);
          prevS=ts;
      }  
    }
   
    private JavaTermTopLevelBlockOwnerModel           owner_;
    private List<JavaStatementModel>  statements_  = new ArrayList<JavaStatementModel>();    
    
}
