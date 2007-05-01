/*
 * JavaTermAllocationExpressionModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.models.JavaTypeArgumentBoundTypeModel;
import ua.gradsoft.javachecker.models.JavaArrayTypeModel;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model of AllocationExpression
 * @author Ruslan Shevchenko
 */
public class JavaTermAllocationExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermAllocationExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
       typeTerm_=t.getSubtermAt(0);       
       typeArgumentsTerm_ = t.getSubtermAt(1);
       nReferences_=0;
       subExpressions_=new LinkedList<JavaExpressionModel>();
       resolvedType_=null;
       Term arrayDimsAndInitsOrArgumentsTerm = t.getSubtermAt(2);
       if(arrayDimsAndInitsOrArgumentsTerm.getName().equals("Arguments")) {
           getArgumentsSubexpressions(arrayDimsAndInitsOrArgumentsTerm,st,enclosedType);
       }else{
           getArrayDimsAndInits(arrayDimsAndInitsOrArgumentsTerm,st,enclosedType);
       }           
       resolvedType_=null;
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.ALLOCATION_EXPRESSION;
    }
    
    public JavaTypeModel getType() throws TermWareException, EntityNotFoundException
    {
      return getAllocatedType();
    }
    
    public boolean isType()
    { return false; }
    
    public JavaTypeModel getAllocatedType() throws TermWareException, EntityNotFoundException
    {
      if (resolvedType_==null) {       
          JavaPlaceContext ctx = createPlaceContext();
          try {
            resolvedType_=JavaResolver.resolveTypeTerm(typeTerm_,ctx);
          }catch(EntityNotFoundException ex){
              ex.setFileAndLine(JUtils.getFileAndLine(t_));
              throw ex;
          }
          if (!typeArgumentsTerm_.isNil()) {
              resolvedType_=new JavaTypeArgumentBoundTypeModel(resolvedType_,typeArgumentsTerm_,enclosedType_,null,statement_);
          }
          int nReferences=nReferences_;
          while(nReferences>0) {
              resolvedType_=new JavaArrayTypeModel(resolvedType_);
              --nReferences;
          }
      } 
      return resolvedType_;
    }
    
    public List<JavaExpressionModel> getSubExpressions()
    {
        return subExpressions_;
    }
    
    
    /**
     * AllocationExpressionModel(TypeRef,(arguments*),ctx)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
        JavaTypeModel allocatedType = getAllocatedType();
        Term allocatedTypeTerm = TermUtils.createTerm("TypeRef",allocatedType.getShortNameAsTerm(),TermUtils.createJTerm(allocatedType));
        List<JavaExpressionModel> arguments = getSubExpressions();
        Term argumentsTerm=TermUtils.createNil();
        for(JavaExpressionModel e: arguments) {
            Term argumentTerm = e.getModelTerm();
            argumentsTerm = TermUtils.createTerm("cons",argumentTerm,argumentsTerm);
        }
        argumentsTerm = TermUtils.reverseListTerm(argumentsTerm);
        Term tctx = TermUtils.createJTerm(this.createPlaceContext());
        Term retval = TermUtils.createTerm("AllocationExpressionModel",allocatedTypeTerm,argumentsTerm,tctx);
        return retval;
    }
    
    private void getArgumentsSubexpressions(Term arguments,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       Term l=arguments.getSubtermAt(0);
       while(!l.isNil()) {
           Term ct = l.getSubtermAt(0);
           l=l.getSubtermAt(1);
           JavaTermExpressionModel e = JavaTermExpressionModel.create(ct,st,enclosedType);
           subExpressions_.add(e);
       }
    }
    
    private void getArrayDimsAndInits(Term arrayDimsAndInits, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
      Term arrayDims = arrayDimsAndInits.getSubtermAt(0);
      Term l = arrayDims.getSubtermAt(0);
      while(!l.isNil()) {
          Term ct = l.getSubtermAt(0);
          l=l.getSubtermAt(1);
          ++nReferences_;
          if (ct.getArity()>0) {
              Term exprTerm = ct.getSubtermAt(0);
              JavaExpressionModel e = JavaTermExpressionModel.create(exprTerm,st,enclosedType);
              subExpressions_.add(e);
          }
      }
    }
    
    private List<JavaExpressionModel> subExpressions_;
    private Term                    typeTerm_;
    private Term                    typeArgumentsTerm_;
    private int                     nReferences_=0;
    private JavaTypeModel           resolvedType_;
    
}
