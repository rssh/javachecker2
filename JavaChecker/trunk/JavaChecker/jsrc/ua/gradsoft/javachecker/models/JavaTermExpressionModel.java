/*
 * JavaTermExpressionModel.java
 *
 *
 * Copyright (c) 2006, 2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.models.expressions.JavaTermAdditiveExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermAllocationExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermAndExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermAnnotationExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermAnnotationMemberValueArrayInitializerExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermArrayIndexExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermArrayInitializerExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermAssigmentExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermBooleanLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermCastExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermCharacterLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermClassLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermConditionalAndExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermConditionalExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermConditionalOrExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermEqualityExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermExclusiveOrExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermFieldExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermFloatingPointLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermFunctionCallExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermIdentifierExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermInclusiveOrExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermInnerAllocationExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermInstanceOfExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermIntegerLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermMethodCallExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermMultiplicativeExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermNameExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermNullLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermParentizedExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermPostfixExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermPredecrementExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermPreincrementExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermRelationalExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermShiftExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermSpecializedMethodCallExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermStringLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermSuperExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermSuperPrefixExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermThisExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermThisPrefixExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermUnaryExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Expression, based on term
 * @author Ruslan Shevchenko
 */
public abstract class JavaTermExpressionModel implements JavaExpressionModel
{
    public static JavaTermExpressionModel create(Term t, JavaTermStatementModel statement, JavaTypeModel enclosedType) throws TermWareException
    {
     return create(t,statement,enclosedType,null,null);   
    }
            
    
    public static JavaTermExpressionModel create(Term t, JavaTermStatementModel statement, JavaTypeModel enclosedType, JavaAnnotationInstanceModel enclosedAnnotation, String enclosedAnnotationElement) throws TermWareException
    {
      //System.out.println("create expession for "+TermHelper.termToString(t));
      if (t.getName().equals("Expression")||t.getName().equals("StatementExpression")) {
          if (t.getArity()==1) {
            return new JavaTermParentizedExpressionModel(t,statement,enclosedType);
          }else if (t.getArity()==2){
              return new JavaTermPostfixExpressionModel(t,statement,enclosedType);
          }else if (t.getArity()==3){
              return new JavaTermAssigmentExpressionModel(t,statement,enclosedType);
          }else{
              throw new AssertException("Arity of Expression must be 1 or 3:"+TermHelper.termToString(t));
          }
      }else if(t.getName().equals("ConditionalExpression")) {
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if (t.getArity()==3) {
              return new JavaTermConditionalExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("Invalid expression term",t);
          }
      }else if(t.getName().equals("ConditionalOrExpression")) {
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if(t.getArity()==2) {
              return new JavaTermConditionalOrExpressionModel(t,statement,enclosedType);
          }else{
              throw new AssertException("arity of ConditionalOrExpression must be 2");
          }    
      }else if(t.getName().equals("ConditionalAndExpression")) {
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if(t.getArity()==2){
              return new JavaTermConditionalAndExpressionModel(t,statement,enclosedType);
          }else{
              throw new AssertException("arity of ConditionalAndExpression must be 2");
          }
      }else if(t.getName().equals("InclusiveOrExpression")) {
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if(t.getArity()==2){
              return new JavaTermInclusiveOrExpressionModel(t,statement,enclosedType);
          }else{
              throw new AssertException("arity of InclusiveOrExpression must be 2 in "+TermHelper.termToString(t));
          }
      }else if(t.getName().equals("ExclusiveOrExpression")) {
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if(t.getArity()==2){
              return new JavaTermExclusiveOrExpressionModel(t,statement,enclosedType);
          }else{
              throw new AssertException("arity of ExclusiveOrExpression must be 2 in "+TermHelper.termToString(t));
          }       
      }else if(t.getName().equals("AndExpression")) {
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if(t.getArity()==2){
              return new JavaTermAndExpressionModel(t,statement,enclosedType);
          }else{
              throw new AssertException("arity of AndExpression must be 2 in "+TermHelper.termToString(t));
          }    
      }else if(t.getName().equals("EqualityExpression")) {
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if(t.getArity()==3) {
              return new JavaTermEqualityExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of EqualityExpression must be 3",t);
          }
      }else if(t.getName().equals("InstanceOfExpression")){
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if(t.getArity()==2) {
              return new JavaTermInstanceOfExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of InstanceOfExpression must be 3",t);
          }  
      }else if(t.getName().equals("RelationalExpression")){
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if (t.getArity()==3) {
              return new JavaTermRelationalExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of RelationalExpression must be 3",t);
          }
      }else if(t.getName().equals("ShiftExpression")){
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if (t.getArity()==3) {
              return new JavaTermShiftExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of ShiftExpression must be 3",t);
          }
      }else if(t.getName().equals("AdditiveExpression")){
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if (t.getArity()==3) {
              return new JavaTermAdditiveExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of AdditiveExpression must be 3",t);
          }          
      }else if(t.getName().equals("MultiplicativeExpression")){          
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if (t.getArity()==3) {
              return new JavaTermMultiplicativeExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of MultiplicativeExpression must be 3",t);
          }  
      }else if(t.getName().equals("UnaryExpression")) {
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if (t.getArity()==2) {
              return new JavaTermUnaryExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of UnaryExpression must be 2",t);
          }        
      }else if(t.getName().equals("UnaryExpressionNotPlusMinus")) {
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if (t.getArity()==2) {
              return new JavaTermUnaryExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of UnaryExpression must be 2",t);
          }      
      }else if(t.getName().equals("PreIncrementExpression")){
          return new JavaTermPreincrementExpressionModel(t,statement,enclosedType);
      }else if(t.getName().equals("PreDecrementExpression")){
          return new JavaTermPredecrementExpressionModel(t,statement,enclosedType);
      }else if(t.getName().equals("PostfixExpression")){
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if(t.getArity()==2){
              return new JavaTermPostfixExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of PostifxExpression must be 2",t);
          }
      }else if(t.getName().equals("CastExpression")) {
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else if(t.getArity()==2){
              return new JavaTermCastExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of PostifxExpression must be 2",t);
          }     
      }else if(t.getName().equals("IntegerLiteral")){
          if (t.getArity()==1) {
              return new JavaTermIntegerLiteralExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of IntegerLiteral must be 1",t);
          }
      }else if(t.getName().equals("FloatingPointLiteral")){
          if (t.getArity()==1) {
              return new JavaTermFloatingPointLiteralExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of FloatingPointLiteral must be 1",t);
          }   
      }else if(t.getName().equals("CharacterLiteral")){
          if (t.getArity()==1) {
              return new JavaTermCharacterLiteralExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of CharacterLiteral must be 1",t);
          }             
      }else if(t.getName().equals("StringLiteral")){
          if (t.getArity()==1) {
              return new JavaTermStringLiteralExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of StringLiteral must be 1",t);
          }     
      }else if(t.getName().equals("BooleanLiteral")){
          if (t.getArity()==1) {
              return new JavaTermBooleanLiteralExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of StringLiteral must be 1",t);
          }               
      }else if(t.getName().equals("NullLiteral")){
          if (t.getArity()==0) {
              return new JavaTermNullLiteralExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of NullLiteral must be 0",t);
          }                         
      }else if(t.getName().equals("PrimaryExpression")){
          if (t.getArity()==1) {
              return create(t.getSubtermAt(0),statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("Invalid primary expression term",t);
          }
      }else if(t.getName().equals("ClassLiteral")){
          if (t.getArity()==1) {
              return new JavaTermClassLiteralExpressionModel(t,statement,enclosedType);
          }else{
            throw new InvalidJavaTermException("arity of ClassLiteral must be 1",t);  
          }
      }else if(t.getName().equals("AllocationExpression")) {
          return new JavaTermAllocationExpressionModel(t,statement,enclosedType);
      }else if (t.getName().equals("Identifier")) {
          return new JavaTermIdentifierExpressionModel(t,statement,enclosedType);
      }else if (t.getName().equals("FunctionCall")) {
          if (t.getArity()==2) {
              return new JavaTermFunctionCallExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of FunctionCall must be 2",t);  
          }
      }else if(t.getName().equals("This")){
          if (t.getArity()==1) {
              return new JavaTermThisExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of This must be 1",t);  
          }          
      }else if(t.getName().equals("InnerAllocation")) {
          if (t.getArity()==2) {
              return new JavaTermInnerAllocationExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of InnerAllocation must be 2",t);  
          }
      }else if(t.getName().equals("Super")){
          if (t.getArity()==1) {
              return new JavaTermSuperExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of Super must be 1",t);  
          }  
      }else if(t.getName().equals("SuperPrefix")) {
          if (t.getArity()==0) {
              return new JavaTermSuperPrefixExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of SuperPrefix must be 0",t);  
          }
      }else if(t.getName().equals("MethodCall")){
          if (t.getArity()==3) {
              return new JavaTermMethodCallExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of MethodCall must be 3",t);  
          }    
      }else if(t.getName().equals("SpecializedMethodCall")) {
          if (t.getArity()==4) {
              return new JavaTermSpecializedMethodCallExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of SpecializedMethodCall must be 4",t);  
          }        
      }else if(t.getName().equals("Field")) {
          if (t.getArity()==2) {
              return new JavaTermFieldExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of Field must be 2",t);  
          }
     // }else if (t.getName().equals("StaticField")) {
     //     if (t.getArity()==2){
     //         return new JavaTermStaticFieldExpressionModel(t,statement,enclosedType);
     //     }else{
     //         throw new InvalidJavaTermException("arity of StaticField must be 2",t);  
     //     }
      }else if (t.getName().equals("ArrayIndex")) {
          if (t.getArity()==2) {
              return new JavaTermArrayIndexExpressionModel(t,statement,enclosedType);
          }else{              
              throw new InvalidJavaTermException("arity of ArrayIndex must be 2",t);  
          }
      }else if (t.getName().equals("ArrayInitializer")) {
          if (t.getArity()==1) {
              return new JavaTermArrayInitializerExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of ArrayIndex must be 2",t); 
          }
      }else if (t.getName().equals("Name")) {
          if (t.getArity()==1) {
              return new JavaTermNameExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("arity of Name must be 1",t);  
          }
      }else if(t.isAtom()) {
          if (t.getName().equals("this")) {
              return new JavaTermThisPrefixExpressionModel(t,statement,enclosedType);
          }else if(t.getName().equals("super")){
              return new JavaTermSuperPrefixExpressionModel(t,statement,enclosedType);
          }else{
              throw new InvalidJavaTermException("Invalid expression term",t);
          }  
      }else if(t.getName().equals("Annotation")){
          if (enclosedAnnotation!=null) {
              JavaAnnotationInstanceModel instanceModel = new JavaTermAnnotationInstanceModel(t,ElementType.ANNOTATION_TYPE,enclosedType);
              return new JavaTermAnnotationExpressionModel(t,instanceModel,enclosedType,enclosedAnnotation);
          }else{
              throw new InvalidJavaTermException("Annotation expression allowed only inside annotation",t);
          }
      }else if(t.getName().equals("MemberValueArrayInitializer")) {
          if (enclosedAnnotation==null) {
              throw new InvalidJavaTermException("AnnotationMemberValueArrayInitializer allowed only inside annotation",t);    
          }
          return new JavaTermAnnotationMemberValueArrayInitializerExpressionModel(t,enclosedType,enclosedAnnotation,enclosedAnnotationElement);
      }else if(t.getName().equals("MemberValue")) {
          return create(t.getSubtermAt(0),statement,  enclosedType, enclosedAnnotation, enclosedAnnotationElement);
      }else{
          throw new InvalidJavaTermException("Invalid expression term",t);
      }
    }
    
    
    
    protected JavaTermExpressionModel(Term t, JavaTermStatementModel statement, JavaTypeModel enclosedType)
    {
      t_=t;
      statement_=statement;
      enclosedType_=enclosedType;
    }
    
    protected JavaPlaceContext createPlaceContext()
    {
       JavaPlaceContext retval = new JavaPlaceContext();
       retval.setExpressionModel(this);
       return retval;
    }
    
    public JavaStatementModel  getStatementModel()
    { return statement_; }
    
    public JavaTermStatementModel  getTermStatementModel()
    { return statement_; }
    
    public JavaTypeModel  getEnclosedType()
    { return enclosedType_; }
    
    public Term getTerm()
    { return t_; }
    
    protected Term t_;
    protected JavaTermStatementModel statement_;
    protected JavaTypeModel  enclosedType_;
    
}
