/*
 * JavaTermFloatingPointLiteralExpressionModel.java
 *
 * Created on вівторок, 6, лютого 2007, 4:53
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
 *
 * @author Ruslan Shevchenko
 */
public class JavaTermFloatingPointLiteralExpressionModel extends JavaTermExpressionModel implements JavaLiteralModel
{

   public JavaTermFloatingPointLiteralExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.FLOATING_POINT_LITERAL;
    }
    
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return JavaExpressionHelper.getFloatingPointLiteralType(t_.getSubtermAt(0).getString());
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
