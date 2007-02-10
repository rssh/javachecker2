/*
 * JavaTermIntegerLiteralExpressionModel.java
 *
 * Created on вівторок, 6, лютого 2007, 4:02
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionHelper;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaLiteralModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *IntegerLiteral
 * @author Ruslan Shevchenko
 */
public class JavaTermIntegerLiteralExpressionModel extends JavaTermExpressionModel implements JavaLiteralModel
{
    
   public JavaTermIntegerLiteralExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.INTEGER_LITERAL;
    }
    
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return JavaExpressionHelper.getIntegerLiteralType(t_.getSubtermAt(0).getString());
    }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
        return Collections.emptyList();
    }
    
    public String getString()
    { return t_.getSubtermAt(0).getString(); }
                
    
}
