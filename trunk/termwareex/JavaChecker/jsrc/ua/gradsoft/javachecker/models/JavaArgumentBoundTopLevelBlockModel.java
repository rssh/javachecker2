/*
 * JavaArgumentBoundBlockModel.java
 *
 * Created on п'€тниц€, 12, с≥чн€ 2007, 1:57
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for block with type-variables argument bound
 * @author Ruslan Shevchenko
 */
public class JavaArgumentBoundTopLevelBlockModel implements JavaTopLevelBlockModel
{
    
    public JavaArgumentBoundTopLevelBlockModel(JavaArgumentBoundTopLevelBlockOwnerModel owner,
                                               JavaTopLevelBlockModel origin,
                                               JavaTypeArgumentsSubstitution substitution)            
    {
      owner_=owner;
      origin_=origin;
      substitution_=substitution;
    }
    
    public JavaArgumentBoundTopLevelBlockOwnerModel  getOwnerModel()
    { return owner_; }
    
        
    /**
     * return list of statements in block.
     */
    public List<JavaStatementModel> getStatements()
    {
      if (statements_==null) {  
        List<JavaStatementModel> retval=new ArrayList<JavaStatementModel>();       
        JavaArgumentBoundStatementModel prevR=null;        
        for(JavaStatementModel s: origin_.getStatements()) {
            JavaArgumentBoundStatementModel r=new JavaArgumentBoundStatementModel(this,s,null,prevR);
            retval.add(r);            
            prevR=r;
        }
        statements_=retval;
      }
      return statements_;
    }
    
    public JavaTopLevelBlockModel getOrigin()
    { return origin_; }
    
    public JavaTypeArgumentsSubstitution getSubstitution()
    { return substitution_; }
    
    
    /**
     * TypeArgumentBoundTopLevelBlock(origin,ctx)
     */
    public Term getModelTerm() throws TermWareException
    {
        Term blockModelTerm = origin_.getModelTerm();
        JavaPlaceContext ctx = JavaPlaceContextFactory.createNewTopLevelBlockOwnerContext(owner_);
        Term tctx=TermUtils.createJTerm(ctx);
        Term retval = TermUtils.createTerm("TypeArgumentBoundTopLevelBlock",blockModelTerm,tctx);
        return retval;
    }
    
    private JavaArgumentBoundTopLevelBlockOwnerModel owner_;
    private JavaTopLevelBlockModel origin_;
    private List<JavaStatementModel> statements_=null;
    private JavaTypeArgumentsSubstitution substitution_;
}
