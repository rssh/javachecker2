/*
 * JavaTermMethodCallExpressionModel.java
 *
 * Created on середа, 7, лютого 2007, 21:26
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeArgumentsSubstitution;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaTermMethodCallExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermMethodCallExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      Term objectOrType=t.getSubtermAt(0);
      subexpressions_=new LinkedList<JavaExpressionModel>();
      JavaExpressionModel e = JavaTermExpressionModel.create(t,st,enclosedType);
      subexpressions_.add(e);
      Term methodNameTerm=t.getSubtermAt(1);
      Term argumentsTerm=t.getSubtermAt(2);
      Term l=argumentsTerm.getSubtermAt(0);      
      while(!l.isNil()) {
          Term arg = l.getSubtermAt(0);
          e = JavaTermExpressionModel.create(t,st,enclosedType);
          subexpressions_.add(e);
      }
    }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.METHOD_CALL;  
    }
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException
    {
      lazyInitMethodModel();
      return methodModel_.getResultType();
    }
    
    public boolean isType()
    { return false; }
    
    public List<JavaExpressionModel>  getSubExpressions()
    {   
        return subexpressions_; 
    }
    
    public JavaMethodModel  getMethodModel() throws TermWareException, EntityNotFoundException
    {
      lazyInitMethodModel();
      return methodModel_;
    }
    
    
    private void lazyInitMethodModel() throws TermWareException, EntityNotFoundException
    {
      if (methodModel_==null)  {
          List<JavaTypeModel> argumentTypes=new ArrayList<JavaTypeModel>(subexpressions_.size()-1);
          for(JavaExpressionModel arg: subexpressions_.subList(1,subexpressions_.size())) {
              argumentTypes.add(arg.getType());
          }     
          String methodName = t_.getSubtermAt(1).getString();
          JavaTypeArgumentsSubstitution s = new JavaTypeArgumentsSubstitution();
          methodModel_ = JavaResolver.resolveMethod(methodName,argumentTypes,s,subexpressions_.get(0).getType());
      }
    }
    
    private JavaMethodModel methodModel_=null;    
    private List<JavaExpressionModel> subexpressions_;
    
    
}
