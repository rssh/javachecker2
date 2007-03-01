/*
 * JavaTermAssigmentExpressionModel.java
 *
 * Created on понеділок, 5, лютого 2007, 15:03
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of assigment expression
 * @author Ruslan Shevchenko
 */
public class JavaTermAssigmentExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermAssigmentExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      super(t,st,enclosedType);
      leftPart_=JavaTermExpressionModel.create(t.getSubtermAt(0),st,enclosedType);      
      assigment_ = JavaAssigmentOperatorKind.create(t.getSubtermAt(1));
      rightPart_=JavaTermExpressionModel.create(t.getSubtermAt(2),st,enclosedType);
    }
    
    public JavaExpressionKind  getKind()
    { return JavaExpressionKind.ASSIGMENT; }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    { return leftPart_.getType(); }
    
    public boolean isType()
    { return false; }
    
    public List<JavaExpressionModel>  getSubExpressions()
    {
        List<JavaExpressionModel> retval = new LinkedList<JavaExpressionModel>();
        retval.add(leftPart_);
        retval.add(rightPart_);
        return retval;
    }
    
    /**
     * AssigmentExpressionModel(left,right,op,ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      Term x=leftPart_.getModelTerm();
      Term y=rightPart_.getModelTerm();
      Term op=TermUtils.createString(assigment_.getString());
      Term tctx = TermUtils.createJTerm(createPlaceContext());
      Term retval = TermUtils.createTerm("AssigmentExpressionModel",x,y,op,tctx);
      return retval;
    }

    
    private JavaTermExpressionModel         leftPart_;
    private JavaTermExpressionModel         rightPart_;
    private JavaAssigmentOperatorKind       assigment_;
    
}
