/*
 * JavaTermClassLiteralExpressionModel.java
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
import ua.gradsoft.javachecker.models.JavaTypeArgumentBoundTypeModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *ClassLiteral
 *I. e. Type().class
 * @author Ruslan Shevchenko
 */
public class JavaTermClassLiteralExpressionModel extends JavaTermExpressionModel
{

    public JavaTermClassLiteralExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
       typeTerm_=t.getSubtermAt(0);
       resolvedType_=null;
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.CLASS_LITERAL;
    }
    
    public JavaTypeModel getType() throws EntityNotFoundException, TermWareException
    {
      JavaTypeModel classType = JavaResolver.resolveTypeModelByFullClassName("java.lang.Class");
      JavaTypeModel resolvedType = getClassLiteralType();
      return new JavaTypeArgumentBoundTypeModel(classType,Collections.singletonList(resolvedType),classType);
    }
    
    public boolean isType()
    { return false; }
    
    public JavaTypeModel getClassLiteralType() throws TermWareException, EntityNotFoundException
    {
      if (resolvedType_==null) {       
          JavaPlaceContext ctx = createPlaceContext();
          resolvedType_=JavaResolver.resolveTypeTerm(typeTerm_,ctx);
      } 
      return resolvedType_;
    }
    
    public List<JavaExpressionModel> getSubExpressions()
    {
        return Collections.emptyList();
    }
    
    public boolean isConstantExpression()
    { return true; }
    
    /**
     * ClassLiteralModel(TypeRef,tctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        Term typeRefTerm = TermUtils.createTerm("TypeRef",typeTerm_,TermUtils.createJTerm(getClassLiteralType()));
        Term tctx = TermUtils.createJTerm(createPlaceContext());
        Term retval = TermUtils.createTerm("ClassLiteralModel",typeRefTerm,tctx);
        return retval;
    }
        
    private Term                    typeTerm_;
    private JavaTypeModel           resolvedType_;
    
    
}
