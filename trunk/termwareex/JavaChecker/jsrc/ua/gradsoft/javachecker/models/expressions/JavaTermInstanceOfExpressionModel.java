/*
 * JavaTermInstanceOfExpressionModel.java
 *
 * Created on понеділок, 5, лютого 2007, 22:41
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
       JavaTermExpressionModel subExpression=JavaTermExpressionModel.create(t.getSubtermAt(0),st,enclosedType);
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
    
    private JavaTermExpressionModel subExpression_;
    private Term                    typeTerm_;
    private JavaTypeModel           resolvedType_;
    
}
