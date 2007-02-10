/*
 * JavaTermAndExpressionModel.java
 *
 * Created on понеділок, 5, лютого 2007, 22:05
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionHelper;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeModelHelper;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *AndExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermAndExpressionModel extends JavaTermExpressionModel
{
 
    
    public JavaTermAndExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      Term frs = t.getSubtermAt(0);      
      Term snd = t.getSubtermAt(1);      
      subExpressions_ = new LinkedList<JavaExpressionModel>();      
      subExpressions_.add(JavaTermExpressionModel.create(frs,st,enclosedType));
      subExpressions_.add(JavaTermExpressionModel.create(snd,st,enclosedType));
    }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.AND; 
    }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      JavaTypeModel x = subExpressions_.get(0).getType();
      if (JavaTypeModelHelper.isBoolean(x)) {
          return x;
      }else{
          JavaTypeModel y = subExpressions_.get(1).getType();
          return JavaExpressionHelper.resolveBinaryNumericPromotion(x,y);          
      }     
    }
    
    public boolean isType()
    { return false; }
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return subExpressions_; }
    
    private List<JavaExpressionModel> subExpressions_;

    
    
}
