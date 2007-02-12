/*
 * JavaTermClassLiteralExpressionModel.java
 *
 * Created on вівторок, 6, лютого 2007, 12:29
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
      return JavaResolver.resolveTypeModelByFullClassName("java.lang.Class");
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
        
    private Term                    typeTerm_;
    private JavaTypeModel           resolvedType_;
    
    
}
