/*
 * JavaTermConditionalAndExpressionModel.java
 *
 * Created on ��������, 5, ������ 2007, 20:35
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPrimitiveTypeModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *ConditionalAndExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermConditionalAndExpressionModel extends JavaTermExpressionModel
{

    public JavaTermConditionalAndExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      Term frs = t.getSubtermAt(0);      
      Term snd = t.getSubtermAt(1);      
      subExpressions_ = new LinkedList<JavaExpressionModel>();      
      subExpressions_.add(JavaTermExpressionModel.create(frs,st,enclosedType));
      subExpressions_.add(JavaTermExpressionModel.create(snd,st,enclosedType));
    }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.CONDITIONAL_AND;  
    }
    
    public JavaTypeModel getType()
    {
      return JavaPrimitiveTypeModel.BOOLEAN;  
    }
    
    public boolean isType()
    {
      return false;  
    }
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return subExpressions_; }
    
    private List<JavaExpressionModel> subExpressions_;
    
    
}
