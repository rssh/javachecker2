/*
 * JavaTermCharacterLiteralExpressionModel.java
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
import ua.gradsoft.javachecker.models.JavaLiteralModel;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *CharacterLiteral
 * @author Ruslan Shevchenko
 */
public class JavaTermCharacterLiteralExpressionModel extends JavaTermExpressionModel implements JavaLiteralModel, JavaObjectConstantExpressionModel
{
    
   public JavaTermCharacterLiteralExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.CHARACTER_LITERAL;
    }
    
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return JavaPrimitiveTypeModel.CHAR;
    }
    
    public boolean isType()
    { return false; }
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
        return Collections.emptyList();
    }
    
    /**
     * CharacterLiteral(string)
     */
    public Term getModelTerm()
    { return getTerm(); }
    
    public String getString()
    { return t_.getSubtermAt(0).getString(); }
                
    public Object getConstant() throws TermWareException
    {
        return evalCharacterLiteral(getString());
    }
    
    
    static char evalCharacterLiteral(String s) throws TermWareException
    {      
      return JavaTermStringLiteralExpressionModel.evalStringLiteral(s,'\'',"Character").charAt(0);
    }
              
      
    
    
}
