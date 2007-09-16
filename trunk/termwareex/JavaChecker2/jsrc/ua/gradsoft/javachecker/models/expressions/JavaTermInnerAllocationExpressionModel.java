/*
 * JavaTermInnerAllocationExpressionModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaTypeArgumentBoundTypeModel;
import ua.gradsoft.javachecker.models.JavaArrayTypeModel;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaExpressionModelHelper;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Allocation of inner class.
 * @author Ruslan Shevchenko
 */
public class JavaTermInnerAllocationExpressionModel extends JavaTermExpressionModel
{
    
    public JavaTermInnerAllocationExpressionModel(Term t,JavaTermStatementModel st,JavaTypeModel enclosedType) throws TermWareException
    {
       super(t,st,enclosedType);       
       ownerTypeTerm_=t.getSubtermAt(0);       
       Term at=t.getSubtermAt(1);
       typeTerm_=at.getSubtermAt(0);       
       typeArgumentsTerm_ = at.getSubtermAt(1);
       nReferences_=0;
       subExpressions_=new LinkedList<JavaExpressionModel>();
       resolvedType_=null;
       Term arrayDimsAndInitsOrArgumentsTerm = at.getSubtermAt(2);
       if(arrayDimsAndInitsOrArgumentsTerm.getName().equals("Arguments")) {
           getArgumentsSubexpressions(arrayDimsAndInitsOrArgumentsTerm,st,enclosedType);
       }else{
           getArrayDimsAndInits(arrayDimsAndInitsOrArgumentsTerm,st,enclosedType);
       }           
       resolvedType_=null;
    }
    
    public JavaExpressionKind getKind()
    {
      return JavaExpressionKind.INNER_ALLOCATION;
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
          resolvedOwnerType_=JavaResolver.resolveTypeTerm(typeTerm_,ctx);
          resolvedType_=JavaResolver.resolveTypeToModel(typeTerm_,resolvedOwnerType_);
          if (!typeArgumentsTerm_.isNil()) {
              resolvedType_=new JavaTypeArgumentBoundTypeModel(resolvedType_,typeArgumentsTerm_,enclosedType_,null,statement_);
          }
          int nReferences=nReferences_;
          while(nReferences>0) {
              resolvedType_=new JavaArrayTypeModel(resolvedType_,null);
          }
      } 
      return resolvedType_;
    }
    
    public List<JavaExpressionModel> getSubExpressions()
    {
        return subExpressions_;
    }
    
    public boolean isConstantExpression() throws TermWareException, EntityNotFoundException
    {
        return JavaExpressionModelHelper.subExpressionsAreConstants(this);
    }
    
    /**
     *
     * InnerAllocationExpressionModel(ownerType,type,$expression-list,$ctx)
     *
     *where type and ownerType are TypeRefs.
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
    {
      JavaTypeModel allocatedType = this.getAllocatedType();  
      Term t0 = TermUtils.createTerm("TypeRef",ownerTypeTerm_,TermUtils.createJTerm(resolvedOwnerType_));
      Term t1 = TermUtils.createTerm("TypeRef",typeTerm_,TermUtils.createJTerm(resolvedType_));
      Term tas = TermUtils.createNil();
      for(JavaExpressionModel e:subExpressions_) {
          Term ta = e.getModelTerm();
          tas=TermUtils.createTerm("cons",ta,tas);
      }
      tas = TermUtils.reverseListTerm(tas);
      Term tctx = TermUtils.createJTerm(createPlaceContext());
      Term retval = TermUtils.createTerm("InnerAllocationExpressionModel",t0,t1,tas,tctx);
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
    private Term                    ownerTypeTerm_;    
    private Term                    typeArgumentsTerm_;
    private int                     nReferences_=0;
    private JavaTypeModel           resolvedType_=null;
    private JavaTypeModel           resolvedOwnerType_=null;
    
    
}
