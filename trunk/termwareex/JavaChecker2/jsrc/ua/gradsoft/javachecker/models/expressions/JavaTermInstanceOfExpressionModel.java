/*
 * JavaTermInstanceOfExpressionModel.java
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
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Expression for InstanceOf
 * @author Ruslan Shevchenko
 */
public class JavaTermInstanceOfExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermInstanceOfExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);
       subExpression_=JavaTermExpressionModel.create(t.getSubtermAt(0),st,enclosedType);
       typeTerm_=t.getSubtermAt(1);
       resolvedType_=null;
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.INSTANCEOF;
    }
    
    public JavaTypeModel getType()
    {
      return JavaPrimitiveTypeModel.BOOLEAN;  
    }
    
    public boolean isType()
    { return false; }
    
    
    public JavaTypeModel getInstanceOfType() throws TermWareException, EntityNotFoundException
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
    
    public JavaTermExpressionModel  getSubexpression()
    {
        return subExpression_;
    }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
        return subExpression_.isConstantExpression();
    }
    
    
    /**
     * InstanceOfExpressionModel(typeRef,expression,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        JavaTypeModel instanceOfType = getInstanceOfType();
        Term typeRef = TermUtils.createTerm("TypeRef",typeTerm_,TermUtils.createJTerm(instanceOfType));
        Term at = subExpression_.getModelTerm();        
        Term tctx = TermUtils.createJTerm(this.createPlaceContext());
        Term retval = TermUtils.createTerm("InstanceOfExpressionModel",typeRef,at,tctx);
        return retval;
    }
    
    
    private JavaTermExpressionModel subExpression_;
    private Term                    typeTerm_;
    private JavaTypeModel           resolvedType_;
    
}
