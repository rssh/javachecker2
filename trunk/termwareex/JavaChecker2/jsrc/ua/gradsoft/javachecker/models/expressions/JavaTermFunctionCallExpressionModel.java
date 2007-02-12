/*
 * JavaTermFunctionCallExpressionModel.java
 *
 * Created on середа, 7, лютого 2007, 19:07
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
 *FunctionCall
 * @author Ruslan Shevchenko
 */
public class JavaTermFunctionCallExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermFunctionCallExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      Term idTerm=t.getSubtermAt(0);
      Term argumentsTerm=t.getSubtermAt(1);
      Term l=argumentsTerm.getSubtermAt(0);
      arguments_=new LinkedList<JavaExpressionModel>();
      while(!l.isNil()) {
          Term arg = l.getSubtermAt(0);
          JavaTermExpressionModel e = JavaTermExpressionModel.create(t,st,enclosedType);
          arguments_.add(e);
      }
    }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.FUNCTION_CALL;  
    }
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException
    {
      lazyInitMethodModel();
      return methodModel_.getResultType();
    }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return arguments_; }
    
    public JavaMethodModel  getMethodModel() throws TermWareException, EntityNotFoundException
    {
      lazyInitMethodModel();
      return methodModel_;
    }
    
    private void lazyInitMethodModel() throws TermWareException, EntityNotFoundException
    {
      if (methodModel_==null)  {
          List<JavaTypeModel> argumentTypes=new ArrayList<JavaTypeModel>(arguments_.size());
          for(JavaExpressionModel arg: arguments_) {
              argumentTypes.add(arg.getType());
          }     
          String methodName = t_.getSubtermAt(0).getString();
          JavaTypeArgumentsSubstitution s = new JavaTypeArgumentsSubstitution();
          methodModel_ = JavaResolver.resolveMethod(methodName,argumentTypes,s,enclosedType_);
      }
    }
    
    
    private List<JavaExpressionModel> arguments_;
    private JavaMethodModel           methodModel_=null;
    
}
