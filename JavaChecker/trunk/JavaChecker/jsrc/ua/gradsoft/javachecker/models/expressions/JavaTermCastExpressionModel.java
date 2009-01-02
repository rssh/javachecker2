/*
 * JavaTermCastExpressionModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *CastExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermCastExpressionModel extends JavaTermExpressionModel
{

    public JavaTermCastExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);
       subExpression_=JavaTermExpressionModel.create(t.getSubtermAt(1),st,enclosedType);
       typeTerm_=t.getSubtermAt(0);
       resolvedType_=null;
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.CAST;
    }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return getCastType();      
    }
    
    public boolean isType()
    { return false; }
    
    public JavaTypeModel getCastType() throws TermWareException, EntityNotFoundException
    {
      if (resolvedType_==null) {       
          JavaPlaceContext ctx = createPlaceContext();
          resolvedType_=JavaResolver.resolveTypeTerm(typeTerm_,ctx);
      } 
      return resolvedType_;
    }
    
    public List<JavaExpressionModel> getSubExpressions()
    {
        JavaExpressionModel e = subExpression_;
        return Collections.singletonList(e);
    }
    
    /**
     * CastExpressionModel(TypeRef,subExpression,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term typeRefTerm=TermUtils.createTerm("TypeRef",typeTerm_,TermUtils.createJTerm(getCastType()));
        Term x = subExpression_.getModelTerm();
        Term tctx = TermUtils.createJTerm(createPlaceContext());
        Term retval = TermUtils.createTerm("CastExpressionModel",typeRefTerm,x,tctx);
        return retval;
    }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    { return subExpression_.isConstantExpression(); }
    
    private JavaTermExpressionModel subExpression_;
    private Term                    typeTerm_;
    private JavaTypeModel           resolvedType_;
    
    
}
