/*
 * JavaTermMethodCallExpressionModel.java
 *
 *
 * Copyright (c) 2006 - 2009 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.FileAndLine;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaExpressionModelHelper;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeArgumentsSubstitution;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for method call
 * @author Ruslan Shevchenko
 */
public class JavaTermMethodCallExpressionModel extends JavaTermExpressionModel /*implements JavaMethodCallExpressionModel*/
{
    
    public JavaTermMethodCallExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      //System.out.println("MethodCall:"+TermHelper.termToString(t));
      //!!!
      FileAndLine fl = JUtils.getFileAndLine(t);
      if (fl.getFname().endsWith("ORBUtilSystemException.java")) {
          if (fl.getLine()==5675) {
              System.err.println("!!!, t=" + TermHelper.termToString(t));
          }
      }
      //!!!
      Term objectOrType=t.getSubtermAt(0);
      subexpressions_=new LinkedList<JavaExpressionModel>();
      JavaExpressionModel e = JavaTermExpressionModel.create(t.getSubtermAt(0),st,enclosedType);
      subexpressions_.add(e);
      Term methodNameTerm=t.getSubtermAt(1);
      Term argumentsTerm=t.getSubtermAt(2);
      Term l=argumentsTerm.getSubtermAt(0);      
      while(!l.isNil()) {
          Term arg = l.getSubtermAt(0);
          e = JavaTermExpressionModel.create(arg,st,enclosedType);
          subexpressions_.add(e);
          l = l.getSubtermAt(1);
      }
    }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.METHOD_CALL;  
    }
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException
    {
      lazyInitMethodModel();
      
      JavaTypeModel retval= substitution_.substitute(methodModel_.getResultType());      
      String methodName = t_.getSubtermAt(1).getSubtermAt(0).getString();
      return retval;
    }
    
    public boolean isType()
    { return false; }
    
    public List<JavaExpressionModel>  getSubExpressions()
    {   
        return subexpressions_; 
    }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
        return JavaExpressionModelHelper.subExpressionsAreConstants(this);
    }
   
    
    public JavaMethodModel  getMethodModel() throws TermWareException, EntityNotFoundException
    {
      lazyInitMethodModel();
      return methodModel_;
    }
    
    /**
     * MethodCallModel(obj,identifier,arguments,methodModel,ctx)
     */
    public Term getModelTerm()  throws TermWareException, EntityNotFoundException
    {
      lazyInitMethodModel();
      Term obj = subexpressions_.get(0).getModelTerm();
      Term identifier = t_.getSubtermAt(1);
      Term args = TermUtils.createNil();
      for(JavaExpressionModel e: subexpressions_.subList(1,subexpressions_.size())) {
          Term arg = e.getModelTerm();
          args = TermUtils.createTerm("cons",arg,args);
      }
      args = TermUtils.reverseListTerm(args);
      Term mmt = TermUtils.createJTerm(methodModel_);
      Term tctx = TermUtils.createJTerm(this.createPlaceContext());
      Term retval = TermUtils.createTerm("MethodCallModel",obj,identifier,args,mmt,tctx);
      return retval;
    }
    
    private void lazyInitMethodModel() throws TermWareException, EntityNotFoundException
    {
      if (methodModel_==null)  {
          List<JavaTypeModel> argumentTypes=new ArrayList<JavaTypeModel>(subexpressions_.size()-1);
          for(JavaExpressionModel arg: subexpressions_.subList(1,subexpressions_.size())) {
              argumentTypes.add(arg.getType());
          }     
          String methodName = t_.getSubtermAt(1).getSubtermAt(0).getString();
          JavaTypeArgumentsSubstitution s = new JavaTypeArgumentsSubstitution();
          try {
             if (subexpressions_.get(0).getType()==null) {
                 System.err.println("!!!__:"+TermHelper.termToString(t_));                 
                 System.err.println("!!!M:"+TermHelper.termToString(subexpressions_.get(0).getModelTerm()));
             }
             
             methodModel_ = JavaResolver.resolveMethod(methodName,argumentTypes,s,subexpressions_.get(0).getType());
             substitution_=s;
          }catch(EntityNotFoundException ex){
              //System.err.println("!!!!_____:"+TermHelper.termToString(t_));
              ex.setFileAndLine(JUtils.getFileAndLine(t_));
              throw ex;
          }
      }
    }
    
    private JavaMethodModel methodModel_=null;    
    private JavaTypeArgumentsSubstitution substitution_=null;
    private List<JavaExpressionModel> subexpressions_;
    
    
}
