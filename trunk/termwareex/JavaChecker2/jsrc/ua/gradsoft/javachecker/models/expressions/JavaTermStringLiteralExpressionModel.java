/*
 * JavaTermStringLiteralExpressionModel.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine.
 * http://www.gradsoft.ua
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaLiteralModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.parsers.java5.ParserHelper;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *StringLiteral
 * @author Ruslan Shevchenko
 */
public class JavaTermStringLiteralExpressionModel extends JavaTermExpressionModel implements JavaLiteralModel, JavaObjectConstantExpressionModel
{
    
   public JavaTermStringLiteralExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.STRING_LITERAL;
    }
    
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return JavaResolver.resolveTypeModelByFullClassName("java.lang.String");
    }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
        return Collections.emptyList();
    }
    
    /**
     *return Literal String. (I. e. uninterpreted, with quotes and escapes inside)
     */
    public String getString() throws TermWareException
    { return ParserHelper.codeStringLiteral(t_.getSubtermAt(0).getString()); }
                        
    /**
     * StringLiteral(String)
     */
    public Term getModelTerm()
    { return t_; }
    
    public boolean isConstantExpression() 
    {
        return true;
    }
    
    
    /**
     * return lexical translation of string inside literal
     */
    public Object getConstant() throws TermWareException
    {
      return t_.getSubtermAt(0).getString();  
    }
    
    
    
}
