/*
 * JavaTermSuperExpressionModel.java
 *
 * Created on середа, 7, лютого 2007, 21:08
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.InvalidJavaTermException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Super(x) = x.super
 * @author Ruslan Shevchenko
 */
public class JavaTermSuperExpressionModel extends JavaTermExpressionModel
{

    public JavaTermSuperExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      subexpression_=JavaTermExpressionModel.create(t.getSubtermAt(0),st,enclosedType);
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.SUPER; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    { try {
        return subexpression_.getType().getSuperClass(); 
      }catch(NotSupportedException ex){
          throw new InvalidJavaTermException("super call of type without superclassing",t_);
      }
    }
    
    public boolean isType()
    { return false; }
    
    
    public List<JavaExpressionModel> getSubExpressions()
    { return Collections.singletonList(subexpression_); }
    
    private JavaExpressionModel  subexpression_;
    
    
}
