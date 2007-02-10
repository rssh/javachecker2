/*
 * JavaTermRelationalExpressionModel.java
 *
 * Created on вівторок, 6, лютого 2007, 1:06
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
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *RelationalExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermRelationalExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermRelationalExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      Term frs = t.getSubtermAt(0); 
      Term op  = t.getSubtermAt(1);
      String s = null;
      if (op.isString()) {
          s=op.getString();
      }else if (op.isAtom()) {
          s=op.getName();
      }else{
          throw new AssertException("Invalid equality operator:"+TermHelper.termToString(op));
      }
      relationKind_=JavaRelationOperatorKind.create(s);
      Term snd = t.getSubtermAt(2);      
      subExpressions_ = new LinkedList<JavaExpressionModel>();      
      subExpressions_.add(JavaTermExpressionModel.create(frs,st,enclosedType));
      subExpressions_.add(JavaTermExpressionModel.create(snd,st,enclosedType));
    }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.RELATIONAL; 
    }
        
    public JavaRelationOperatorKind getRelationOperatorKind()
    {
      return relationKind_;  
    }
    
    public JavaTypeModel getType()
    {
      return JavaPrimitiveTypeModel.BOOLEAN;  
    }
    
    public boolean isType()
    { return false; }    
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return subExpressions_; }
    
    private List<JavaExpressionModel> subExpressions_;
    private JavaRelationOperatorKind  relationKind_;
    
    
    
}
