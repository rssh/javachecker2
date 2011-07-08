/*
 * JavaTermMultiplicativeExpressionModel.java
 *
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
 *MultiplicativeExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermMultiplicativeExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermMultiplicativeExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
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
      multiplicativeKind_=JavaMultiplicativeOperatorKind.create(s);
      Term snd = t.getSubtermAt(2);      
      subExpressions_ = new LinkedList<JavaExpressionModel>();      
      subExpressions_.add(JavaTermExpressionModel.create(frs,st,enclosedType));
      subExpressions_.add(JavaTermExpressionModel.create(snd,st,enclosedType));
    }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.MULTIPLICATIVE;
    }
        
    public JavaMultiplicativeOperatorKind getMultiplicativeOperatorKind()
    {
      return multiplicativeKind_;  
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
     * MultiplicativeExpressionModel(x,y,op,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term x = subExpressions_.get(0).getModelTerm();  
      Term y = subExpressions_.get(1).getModelTerm();
      Term op = TermUtils.createString(multiplicativeKind_.getString());
      Term tctx = TermUtils.createJTerm(this.createPlaceContext());
      Term retval = TermUtils.createTerm("MultiplicativeExpressionModel",x,y,op,tctx);
      return retval;
    }
    
    
    private List<JavaExpressionModel> subExpressions_;
    private JavaMultiplicativeOperatorKind     multiplicativeKind_;            
           
}
