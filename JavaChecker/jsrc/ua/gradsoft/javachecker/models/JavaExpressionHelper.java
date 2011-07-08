/*
 * JavaExpressionHelper.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Helper class for Java Expressions
 * @author Ruslan Shevchenko
 */
public class JavaExpressionHelper {
    
    private JavaExpressionHelper(){}
    
    /**
     * return type of expression (
     *
     */
    public static JavaTypeModel resolveExpressionType(Term expr, JavaPlaceContext context) throws EntityNotFoundException, TermWareException {
        if (expr.getName().equals("Expression"))  {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else if (expr.getArity()==2) {
                JavaTypeModel frs = resolveExpressionType(expr.getSubtermAt(0),context);
                // we can just return frs.
                return frs;
            }else if (expr.getArity()==3) {
                JavaTypeModel frs = resolveExpressionType(expr.getSubtermAt(2),context);
                return frs;
            }else{
                throw new AssertException("arity of expression must be 1 or 2:"+TermHelper.termToString(expr));
            }
        }else if (expr.getName().equals("ConditionalExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                return resolveExpressionType(expr.getSubtermAt(1),context);
            }
        }else if (expr.getName().equals("ConditionalOrExpression")) {
            if (expr.getArity()>1) {
                // we know that this is boolean without knowing types of subexpression
                return JavaPrimitiveTypeModel.BOOLEAN;
            }else{
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }
        }else if (expr.getName().equals("ConditionalAndExpression")) {
            if (expr.getArity()>1) {
                return JavaPrimitiveTypeModel.BOOLEAN;
            }else{
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }
        }else if (expr.getName().equals("InclusiveOrExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else if (expr.getArity()==2) {
                JavaTypeModel t0 = resolveExpressionType(expr.getSubtermAt(0),context);
                if (JavaTypeModelHelper.isBoolean(t0)) {
                    return t0;
                }else{
                    JavaTypeModel t1 = resolveExpressionType(expr.getSubtermAt(2),context);
                    return resolveBinaryNumericPromotion(t0,t1);
                }
            }else{
                throw new InvalidJavaTermException("arity of InclusiveOrExpression must be 1 or 2",expr);
            }
        }else if (expr.getName().equals("ExclusiveOrExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else if (expr.getArity()==2) {
                JavaTypeModel t0 = resolveExpressionType(expr.getSubtermAt(0),context);
                if (JavaTypeModelHelper.isBoolean(t0)) return t0;
                JavaTypeModel t1 = resolveExpressionType(expr.getSubtermAt(2),context);
                return resolveBinaryNumericPromotion(t0,t1);
            }else{
                throw new InvalidJavaTermException("arity of ExclusiveOrExpression must be 1 or 2",expr);
            }
        }else if (expr.getName().equals("AndExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else if (expr.getArity()==2) {
                JavaTypeModel t0 = resolveExpressionType(expr.getSubtermAt(0),context);
                if (JavaTypeModelHelper.isBoolean(t0)) return t0;
                JavaTypeModel t1 = resolveExpressionType(expr.getSubtermAt(2),context);
                return resolveBinaryNumericPromotion(t0,t1);
            }else{
                throw new InvalidJavaTermException("arity of AndExpression must be 1 or 2",expr);
            }
        }else if (expr.getName().equals("EqualityExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                return JavaPrimitiveTypeModel.BOOLEAN;
            }
        }else if (expr.getName().equals("InstanceOfExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                return JavaPrimitiveTypeModel.BOOLEAN;
            }
        }else if (expr.getName().equals("RelationalExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                return JavaPrimitiveTypeModel.BOOLEAN;
            }
        }else if (expr.getName().equals("ShiftExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }
        }else if (expr.getName().equals("AdditiveExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                JavaTypeModel t0 = resolveExpressionType(expr.getSubtermAt(0),context);
                if (JavaTypeModelHelper.same(t0,JavaResolver.resolveTypeModelByFullClassName("java.lang.String"))) {
                    return JavaResolver.resolveTypeModelByFullClassName("java.lang.String");
                }else{
                    JavaTypeModel t1 = resolveExpressionType(expr.getSubtermAt(2),context);
                    return resolveBinaryNumericPromotion(t0,t1);
                }
            }
        }else if (expr.getName().equals("MultiplicativeExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                JavaTypeModel t0 = resolveExpressionType(expr.getSubtermAt(0),context);
                JavaTypeModel t1 = resolveExpressionType(expr.getSubtermAt(2),context);
                return resolveBinaryNumericPromotion(t0,t1);
            }
        }else if (expr.getName().equals("UnaryExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                return resolveExpressionType(expr.getSubtermAt(1),context);
            }
        }else if (expr.getName().equals("PreIncrementExpression")) {
            return resolveExpressionType(expr.getSubtermAt(0),context);
        }else if (expr.getName().equals("PreDecrementExpression")) {
            return resolveExpressionType(expr.getSubtermAt(0),context);
        }else if (expr.getName().equals("UnaryExpressionNotPlusMinus")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                return resolveExpressionType(expr.getSubtermAt(1),context);
            }
        }else if (expr.getName().equals("CastExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                return JavaResolver.resolveTypeTerm(expr.getSubtermAt(0),context);
            }
        }else if (expr.getName().equals("PostfixExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                return resolveExpressionType(expr.getSubtermAt(1),context);
            }
        }else if (expr.getName().equals("PrimaryExpression")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                throw new InvalidJavaTermException("arity of PrimaryExpression must be 1",expr);
            }
        }else if (expr.getName().equals("Literal")) {
            if (expr.getArity()==1) {
                return resolveExpressionType(expr.getSubtermAt(0),context);
            }else{
                throw new InvalidJavaTermException("arity of Literal must be 1",expr);
            }
        }else if (expr.getName().equals("BooleanLiteral")) {
            return JavaPrimitiveTypeModel.BOOLEAN;
        }else if (expr.getName().equals("IntegerLiteral")) {
            Term ct  = expr.getSubtermAt(0);
            if (ct.isLong()) {
                return JavaPrimitiveTypeModel.LONG;
            }else{
                return JavaPrimitiveTypeModel.INT;
            }        
        }else if (expr.getName().equals("FloatingPointLiteral")) {
            if (expr.getSubtermAt(0).isFloat()) {
                return JavaPrimitiveTypeModel.FLOAT;
            }else{
                return JavaPrimitiveTypeModel.DOUBLE;   
            }            
        }else if (expr.getName().equals("CharacterLiteral")){
            return JavaPrimitiveTypeModel.CHAR;
        }else if (expr.getName().equals("StringLiteral")) {
            return JavaResolver.resolveTypeModelByFullClassName("java.lang.String");
        }else if (expr.getName().equals("NullLiteral")) {
            return JavaNullTypeModel.INSTANCE;
        }else if (expr.getName().equals("this") && expr.isAtom()) {
            if (context.getTypeModel()!=null) {
                return context.getTypeModel();
            }else{
                throw new AssertException("'this' is not applicable in context");
            }
        }else if (expr.getName().equals("Super")) {
            if (context.getTypeModel()!=null) {
                 JavaTypeModel superTypeModel=context.getTypeModel().getSuperClass();
                 if (superTypeModel==null) {
                     //TODO: change to InvalidJavaTerm
                    throw new AssertException("'super' is not applicable in context");
                 }
                 JavaPlaceContext newContext = JavaPlaceContextFactory.createNewTypeContext(superTypeModel);
                 return resolveExpressionType(expr.getSubtermAt(0),newContext);
            }else{
                throw new AssertException("'super' is not applicable in context");
            }
        }else if (expr.getName().equals("AllocationExpression")) {
            Term t = getTypeTermFromAllocationExpression(expr);
            JavaTypeModel tm = JavaResolver.resolveTypeTerm(t,context);
            return tm;
        }else if (expr.getName().equals("ClassLiteral")) {
            return JavaResolver.resolveTypeModelByFullClassName("java.lang.Class");
        }else if (expr.getName().equals("FunctionCall")) {
            List<JavaTypeModel> argTypes = resolveArgumentTypes(expr.getSubtermAt(1),context);
            JavaTypeArgumentsSubstitution s = new JavaTypeArgumentsSubstitution();
            JavaTypeModel where = context.getTypeModel();           
            JavaMethodModel method = JavaResolver.resolveMethod(expr.getSubtermAt(0).getString(),argTypes,s,where);
            return s.substitute(method.getResultType());
        }else if (expr.getName().equals("Identifier")) {
            // check for variable.
            String name = expr.getSubtermAt(0).getString();
            JavaVariableModel v = JavaResolver.resolveVariableByName(name,context);
            return v.getType();
        }else if (expr.getName().equals("This")) {
            JavaTypeModel tm = JavaResolver.resolveTypeTerm(expr.getSubtermAt(0),context);
            return tm;
        }else if (expr.getName().equals("Allocation")) {
            Term t = getTypeTermFromAllocation(expr);
            JavaTypeModel tm = JavaResolver.resolveTypeTerm(t,context);
            return tm;
        }else if (expr.getName().equals("MethodCall")) {
            Term classToCall = expr.getSubtermAt(0);
            JavaTypeModel calledModel = null;
            if (classToCall.getName().equals("Name")) {
                calledModel = resolveNameExpressionType(classToCall,true,context);
            }else{
                calledModel = resolveExpressionType(classToCall,context);
            }
            String methodName = expr.getSubtermAt(1).getSubtermAt(0).getString();
            List<JavaTypeModel> argTypes=resolveArgumentTypes(expr.getSubtermAt(2),context);
            JavaTypeArgumentsSubstitution s = new JavaTypeArgumentsSubstitution();
            JavaMethodModel mm = JavaResolver.resolveMethod(methodName,argTypes,s,calledModel);
            return s.substitute(mm.getResultType());
        }else if (expr.getName().equals("SpecializedMethodCall")) {
            Term classToCall = expr.getSubtermAt(0);
            JavaTypeModel calledModel = null;
            if (classToCall.getName().equals("Name")) {
                calledModel = resolveNameExpressionType(classToCall,true,context);
            }else{
                calledModel = resolveExpressionType(classToCall,context);
            }
            //calledModel = new JavaArgumentBoundTypeModel(calledModel,expr.getSubtermAt(1));
            String methodName = expr.getSubtermAt(2).getSubtermAt(0).getString();
            List<JavaTypeModel> argTypes=resolveArgumentTypes(expr.getSubtermAt(3),context);
            JavaTypeArgumentsSubstitution s = new JavaTypeArgumentsSubstitution();
            JavaMethodModel m = JavaResolver.resolveMethod(methodName,argTypes,s,calledModel);
            JavaTypeModel tm = m.getResultType();
            return s.substitute(tm);
        }else if (expr.getName().equals("Field")) {
            Term owner=expr.getSubtermAt(0);
            JavaTypeModel ownerModel=null;
            if (owner.getName().equals("Name")) {
                ownerModel = resolveNameExpressionType(owner,true,context);
            }else{
                ownerModel = resolveExpressionType(owner,context);
            }
            JavaMemberVariableModel mv = JavaResolver.resolveMemberVariableByName(expr.getSubtermAt(1).getSubtermAt(0).getString(),ownerModel);
            return mv.getType();
        }else if (expr.getName().equals("SpecializedField")) {
            Term owner=expr.getSubtermAt(0);
            JavaTypeModel ownerModel=null;
            if (owner.getName().equals("Name")) {
                ownerModel = resolveNameExpressionType(owner,true,context);
            }else{
                ownerModel = resolveExpressionType(owner,context);
            }
            //ownerModel = new JavaArgumentBoundTypeModel(ownerModel,expr.getSubtermAt(1));
            JavaMemberVariableModel mv = JavaResolver.resolveMemberVariableByName(expr.getSubtermAt(2).getSubtermAt(0).getString(),ownerModel);
            return mv.getType();
        }else if (expr.getName().equals("ArrayIndex")) {
            Term arrayExpression = expr.getSubtermAt(0);
            JavaTypeModel arrayType = resolveExpressionType(arrayExpression,context);
            if (!arrayType.isArray()) {
                throw new InvalidJavaTermException("argument of ArrayIndex must be array",expr);
            }else{             
                return arrayType.getReferencedType();
            }
        }else if (expr.getName().equals("Name")) {
            return resolveNameExpressionType(expr,false,context);
        }else{
            throw new InvalidJavaTermException("Invalid expression",expr);
        }
    }
    
    
    static public JavaTypeModel getIntegerLiteralType(String s) {
        if (s.endsWith("L")||s.endsWith("l")) {
            return JavaPrimitiveTypeModel.LONG;
        }else{
            return JavaPrimitiveTypeModel.INT;
        }
    }
    
    static public JavaTypeModel getFloatingPointLiteralType(String s) {
        if (s.endsWith("f")||s.endsWith("F"))  {
            return JavaPrimitiveTypeModel.FLOAT;
        }else{
            return JavaPrimitiveTypeModel.DOUBLE;
        }
    }
    
    
    static public JavaTypeModel resolveBinaryNumericPromotion(JavaTypeModel t1, JavaTypeModel t2) {
        MethodMatchingConversions cn = new MethodMatchingConversions();
        JavaTypeModel x = JavaTypeModelHelper.unboxingConversion(t1,cn);
        JavaTypeModel y = JavaTypeModelHelper.unboxingConversion(t2,cn);
        if (x.equals(JavaPrimitiveTypeModel.SHORT)) {
            return y;
        }else if (x.equals(JavaPrimitiveTypeModel.INT)) {
            if (y.equals(JavaPrimitiveTypeModel.SHORT)) {
                return x;
            }else{
                return y;
            }
        }else if (x.equals(JavaPrimitiveTypeModel.LONG)) {
            if (y.equals(JavaPrimitiveTypeModel.SHORT)) {
                return x;
            }else if (y.equals(JavaPrimitiveTypeModel.INT)) {
                return x;
            }else{
                return y;
            }
        }else if (x.equals(JavaPrimitiveTypeModel.FLOAT)) {
            if (y.equals(JavaPrimitiveTypeModel.DOUBLE)) {
                return y;
            }else{
                return x;
            }
        }else{
            return y;
        }
    }
    
    
    
    static JavaTypeModel resolveNameExpressionType(Term t, boolean allowsTypes,JavaPlaceContext ctx) throws TermWareException, EntityNotFoundException {
        Term list=t.getSubtermAt(0);
        String firstName=list.getSubtermAt(0).getSubtermAt(0).getString();
        try{
            JavaVariableModel v = JavaResolver.resolveVariableByName(firstName,ctx);
            // then this can be subfield.
            return resolveFieldInName(v,list.getSubtermAt(1));
        }catch(EntityNotFoundException ex){
            /* ignore, try other possibilities */
            ;
        }
        
        // try to resolve first identifier as class.
        try {
            JavaTypeModel tm = JavaResolver.resolveTypeModelByName(firstName,ctx);
            // then this can be subclasses or fields.
            return resolveSubclassesAndStaticFieldsInName(tm,list.getSubtermAt(1));
        } catch (EntityNotFoundException ex) {
            ;
        }
        
        // try to resolve first few identifiers as names of classes with full package.
        Term curr=list.getSubtermAt(1);
        StringBuilder packageName=new StringBuilder();
        packageName.append(firstName);
        String currentClassName=curr.getSubtermAt(0).getSubtermAt(0).getString();
        
        while(!curr.isNil()) {
            try {
                JavaTypeModel tm=JavaResolver.resolveTypeModelFromPackage(currentClassName,packageName.toString());
                return resolveSubclassesAndStaticFieldsInName(tm,curr.getSubtermAt(1));
            }catch(EntityNotFoundException ex){
                ;
            }
            packageName.append(".");
            packageName.append(currentClassName);
            curr=curr.getSubtermAt(1);
            currentClassName=curr.getSubtermAt(0).getSubtermAt(0).getString();
        }
        
        //nothing found. die.
        
        throw new EntityNotFoundException(" name ", TermHelper.termToString(t), "expression");
    }
    
    public static JavaTypeModel  resolveFieldInName(JavaVariableModel v, Term l) throws EntityNotFoundException, TermWareException {
        if (l.isNil()) {
            return v.getType();
        }else{
            JavaTypeModel vt=v.getType();
            String name = l.getSubtermAt(0).getSubtermAt(0).getString();
            JavaMemberVariableModel mv=JavaResolver.resolveMemberVariableByName(name,vt);
            return resolveFieldInName(mv,l.getSubtermAt(1));
        }
    }
    
    public static JavaTypeModel resolveSubclassesAndStaticFieldsInName(JavaTypeModel tm,Term l) throws EntityNotFoundException, TermWareException {
        if (l.isNil()) {
            return tm;
        }else{
            String name=l.getSubtermAt(0).getSubtermAt(0).getString();
            try {
                JavaMemberVariableModel mv=JavaResolver.resolveMemberVariableByName(name,tm);
                // TODO: check that one is static.
                return resolveSubclassesAndStaticFieldsInName(mv.getType(),l.getSubtermAt(1));
            }catch(EntityNotFoundException ex){
                ;
            }
            JavaTypeModel tmn = JavaResolver.resolveTypeModelByName(name,tm,null,null);
            return resolveSubclassesAndStaticFieldsInName(tmn,l.getSubtermAt(1));
        }
    }
    
    
    
    public static List<JavaTypeModel>  resolveArgumentTypes(Term arguments,JavaPlaceContext context) throws EntityNotFoundException, TermWareException {
        List<JavaTypeModel> retval;
        // System.err.println("111:resolveArgumentTypes:"+TermHelper.termToString(arguments));
        if (arguments.getArity()==0)  {
            retval = Collections.emptyList();
        }else{
            Term argsList = arguments.getSubtermAt(0);
            if (argsList.isNil()) {
                retval=Collections.<JavaTypeModel>emptyList();
            }else{
                retval=new LinkedList<JavaTypeModel>();
                while(!argsList.isNil()) {
                    Term t = argsList.getSubtermAt(0);
                    argsList = argsList.getSubtermAt(1);
                    JavaTypeModel tm = resolveExpressionType(t,context);
                    retval.add(tm);
                }
            }
        }
        return retval;
    }
    
    static Term getTypeTermFromAllocation(Term t) throws TermWareException {
        return getTypeTermFromAllocationExpression(t.getSubtermAt(1));
    }
    
    static Term getTypeTermFromAllocationExpression(Term t) throws TermWareException {
        Term typeTerm = t.getSubtermAt(ALLOCATION_EXPRESSION_TYPE_INDEX);
        Term arrayDimsAndInit = t.getSubtermAt(ALLOCATION_EXPRESSION_ARRAY_DIMS_AND_INITS_INDEX);
        int nReferences=0;
        if (arrayDimsAndInit.getName().equals("ArrayDimsAndInits")) {
            Term arrayDims=arrayDimsAndInit.getSubtermAt(0);
            Term inside = arrayDims.getSubtermAt(0);
            if (inside.getName().equals("ArrayDim")) {
                // it's not list, i. e. we have not list rule in AST Processing for arrayDims yet
                nReferences=inside.getArity();
            }else if (inside.getName().equals("cons")) {
                while(!inside.isNil())  {
                    ++nReferences;
                    inside=inside.getSubtermAt(1);
                }
            }else if (inside.isNil()) {
                // All ok, do nothing.
            }else{
                throw new InvalidJavaTermException("Invalid ArrayDimsAndInit:",arrayDimsAndInit);
            }
        }
        if (nReferences==0) {
            return typeTerm;
        }else{
            Term nReferencesTerm = TermUtils.createInt(nReferences);
            return TermWare.getInstance().getTermFactory().createTerm("ReferenceType",nReferencesTerm,typeTerm);
        }
    }
    
    public static final int ALLOCATION_EXPRESSION_TYPE_INDEX = 0;
    public static final int ALLOCATION_EXPRESSION_ARRAY_DIMS_AND_INITS_INDEX=2;
}
