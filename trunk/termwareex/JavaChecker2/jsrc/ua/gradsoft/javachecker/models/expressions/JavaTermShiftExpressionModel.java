/*
 * JavaTermShiftExpressionModel.java
 *
 *
 * Copyright (c) 2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionHelper;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaExpressionModelHelper;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *ShiftExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermShiftExpressionModel extends JavaTermExpressionModel
{
    public JavaTermShiftExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      Term frs = t.getSubtermAt(0); 
      Term op  = t.getSubtermAt(1);
      String s = null;
      if (op.isString()) {
          s=op.getString();
      }else if (op.isAtom()) {
          s=op.getName();
      }else{
          throw new AssertException("Invalid equality operator:"+TermHelper.termToString(op));
      }
      shiftKind_=JavaShiftOperatorKind.create(s);
      Term snd = t.getSubtermAt(2);      
      subExpressions_ = new LinkedList<JavaExpressionModel>();      
      subExpressions_.add(JavaTermExpressionModel.create(frs,st,enclosedType));
      subExpressions_.add(JavaTermExpressionModel.create(snd,st,enclosedType));
    }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.SHIFT; 
    }
        
    public JavaShiftOperatorKind getShiftOperatorKind()
    {
      return shiftKind_;  
    }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      JavaTypeModel x = subExpressions_.get(0).getType();
      JavaTypeModel y = subExpressions_.get(1).getType();
      return JavaExpressionHelper.resolveBinaryNumericPromotion(x,y);          
    }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return subExpressions_; }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
        return JavaExpressionModelHelper.subExpressionsAreConstants(this);
    }
    
    
    /**
     * ShiftExpressionModel(x,y,op,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term x = subExpressions_.get(0).getModelTerm();
        Term y = subExpressions_.get(1).getModelTerm();
        Term op = TermUtils.createString(shiftKind_.getString());
        Term ctx = TermUtils.createJTerm(createPlaceContext());
        Term retval = TermUtils.createTerm("ShiftExpressionModel",x,y,op,ctx);
        return retval;
    }
        
    
    private List<JavaExpressionModel> subExpressions_;
    private JavaShiftOperatorKind  shiftKind_;            
    
}
