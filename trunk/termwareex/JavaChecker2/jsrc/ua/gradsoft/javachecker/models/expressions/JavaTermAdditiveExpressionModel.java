/*
 * JavaTermAdditiveExpressionModel.java
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
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.trace.JavaTraceContext;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *AdditiveExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermAdditiveExpressionModel extends JavaTermExpressionModel
{
    public JavaTermAdditiveExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
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
      additiveKind_=JavaAdditiveOperatorKind.create(s);
      Term snd = t.getSubtermAt(2);      
      subExpressions_ = new LinkedList<JavaExpressionModel>();      
      subExpressions_.add(JavaTermExpressionModel.create(frs,st,enclosedType));
      subExpressions_.add(JavaTermExpressionModel.create(snd,st,enclosedType));
    }
    
    
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.ADDITIVE;
    }
        
    public JavaAdditiveOperatorKind getAdditiveOperatorKind()
    {
      return additiveKind_;  
    }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      JavaTypeModel x = subExpressions_.get(0).getType();
      JavaTypeModel y = subExpressions_.get(1).getType();
      if (x.getFullName().equals("java.lang.String")) {
          return x;
      }
      return JavaExpressionHelper.resolveBinaryNumericPromotion(x,y);          
    }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return subExpressions_; }
    
    /**
     * AdditiveExpressionModel(x,y,op,ctx);
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term frs=subExpressions_.get(0).getModelTerm();
        Term snd=subExpressions_.get(1).getModelTerm();
        Term kindTerm = TermUtils.createString(additiveKind_.getString());
        Term ctx = TermUtils.createJTerm(createPlaceContext());
        return TermUtils.createTerm("AdditiveExpressionModel",frs,snd,kindTerm,ctx);
    }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
        for(JavaExpressionModel expr: subExpressions_) {
            if (!expr.isConstantExpression()) {
                return false;
            }
        }
        return true;
    }
    
    public JavaExpressionModel eval(JavaTraceContext trace) throws TermWareException
    {        
        /*
        JavaExpressionModel frs=subExpressions_.get(0);
        JavaExpressionModel snd=subExpressions_.get(1);
        frs=frs.eval(trace);        
        snd=snd.eval(trace);
        if (  (frs instanceof JavaObjectConstantExpressionModel)
            &&(snd instanceof JabaObjectConstantExpressionModel)
                ) {
            JavaObjectConstantExpressionModel ofrs = (JavaObjectConstantExpressionModel)frs;
            JavaObjectConstantExpressionModel osnd = (JavaObjectConstantExpressionModel)snd;
            Object cfrs = ofrs.getConstant();
            Object csnd = osnd.getConstant();
            if (cfrs.)
        }
         */
        throw new AssertException("Not implemented");
    }
    
    
    private List<JavaExpressionModel> subExpressions_;
    private JavaAdditiveOperatorKind     additiveKind_;            
    
   
}
