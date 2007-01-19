/*
 * JavaArgumentBoundStatementModel.java
 *
 * Created on п'€тниц€, 12, с≥чн€ 2007, 2:11
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaArgumentBoundStatementModel implements JavaStatementModel
{
    
    public JavaArgumentBoundStatementModel(JavaArgumentBoundTopLevelBlockModel blockModel,
                                           JavaStatementModel origin,
                                           JavaArgumentBoundStatementModel parent,
                                           JavaArgumentBoundStatementModel previous
            )
    {
      blockModel_=blockModel;
      origin_=origin;
      substitutedParent_=parent;
      substitutedPrevious_=previous;
    }

        
    public JavaStatementKind  getKind()
    { return origin_.getKind(); }
    
     public JavaTopLevelBlockModel  getTopLevelBlockModel()
     {
         return blockModel_;
     }
     
     public JavaArgumentBoundTopLevelBlockModel getArgumentBoundTopLevelBlockModel()
     {
        return blockModel_; 
     }
    
     public JavaStatementModel getParentStatementModel()
     {
         return substitutedParent_;
     }
    
    
    public JavaStatementModel getPreviousStatementModel()
    {
        return substitutedPrevious_;
    }
    
    public List<JavaStatementModel> getChildStatements()
    {
        if (substitutedChilds_==null) {
            substitutedChilds_=new ArrayList<JavaStatementModel>();
            JavaArgumentBoundStatementModel prevS=null;
            for(JavaStatementModel s : origin_.getChildStatements()) {
                JavaArgumentBoundStatementModel as = new JavaArgumentBoundStatementModel(blockModel_,s,this,prevS);
                substitutedChilds_.add(as);                
                prevS=as;
            }
        }
        return substitutedChilds_;
    }
    
    /**
     *get local variables, defined in this statement.
     *
     */
    public List<JavaLocalVariableModel> getLocalVariables()
    { 
      List<JavaLocalVariableModel> localVariables = origin_.getLocalVariables();
      List<JavaLocalVariableModel> retval = new LinkedList<JavaLocalVariableModel>();
      for(JavaLocalVariableModel v: localVariables) {
          JavaArgumentBoundLocalVariableModel av = new JavaArgumentBoundLocalVariableModel(v,this);
          retval.add(av);
      }
      return retval;
    }
        
    /**
     *get local type, defined in this statements.
     */
    public JavaTypeModel getLocalType()  throws TermWareException
    {
      return blockModel_.getSubstitution().substitute(origin_.getLocalType());  
    }
            
     
    public JavaStatementModel  getOrigin()
    {
      return origin_;  
    }
    
    
    private JavaArgumentBoundTopLevelBlockModel blockModel_;
    private JavaStatementModel          origin_;
    private JavaStatementModel          substitutedParent_=null;
    private JavaStatementModel          substitutedPrevious_=null;
    private List<JavaStatementModel> substitutedChilds_=null;
    
}
