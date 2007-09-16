/*
 * JavaArrayTypeModel.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Type model for Java array
 * @author Ruslan Shevchenko
 */
public class JavaArrayTypeModel extends JavaTypeModel {
    
    /** Creates a new instance of JavaArrayTypeModel */
    public JavaArrayTypeModel(JavaTypeModel referencedType, JavaExpressionModel lengthInitializer) {
        super(referencedType.getPackageModel());
        referencedType_=referencedType;
        lengthModel_=new LengthMemberVariableModel();
        lengthInitializer_=lengthInitializer;
    }
    
    public String getName() {
        return referencedType_.getName()+"[]"; }
    
    public String getErasedName() {
        return referencedType_.getErasedName()+"[]";
    }
    
    
    public Term getShortNameAsTerm() throws TermWareException {
        return TermUtils.createTerm("ReferenceType",TermUtils.createInt(1), referencedType_.getShortNameAsTerm());
    }
    
    public Term getFullNameAsTerm() throws TermWareException
    {
        return TermUtils.createTerm("ReferenceType",TermUtils.createInt(1), referencedType_.getFullNameAsTerm());
    }
    
    /*
    public Term getFullNameAsTerm() throws TermWareException
    {
       return TermUtils.createTerm("ReferenceType",referencedType_.getFullNameAsTerm(),1);
    }
     */
    
    public JavaTermModifiersModel getModifiersModel() {
        return JavaModelConstants.PUBLIC_MODIFIERS; }
    
    public boolean isClass() {
        return false; }
    
    public boolean isInterface() {
        return false; }
    
    public boolean isEnum() {
        return false; }
    
    public boolean isAnnotationType() {
        return false; }
    
    public boolean isPrimitiveType() {
        return false; }
    
    public  boolean isArray() {
        return true; }
    
    public boolean isWildcardBounds() {
        return false; }
    
    public boolean isUnknown() {
        return false; }
    
    public boolean isNull() {
        return false; }
    
    
    public boolean isTypeVariable() {
        return false; }
    
    public  JavaTypeModel  getEnclosedType() throws NotSupportedException {
        throw new NotSupportedException(); }
    
    /**
     *@return referenced type.
     */
    public JavaTypeModel  getReferencedType() throws NotSupportedException {
        return referencedType_;
    }
    
    public boolean hasTypeParameters() throws TermWareException {
        return referencedType_.hasTypeParameters(); }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException {
        return referencedType_.getTypeParameters(); }
    
    public boolean hasMethodModels() {
        return false; }
    
    /**
     *@return empty Map
     */
    public Map<String, List<JavaMethodModel>>   getMethodModels() throws NotSupportedException {
        return Collections.emptyMap(); }
    
    public boolean hasMemberVariableModels() {
        return true; }
    
    public Map<String, JavaMemberVariableModel> getMemberVariableModels() {
        return Collections.<String,JavaMemberVariableModel>singletonMap("length",lengthModel_);
    }
    
    public List<JavaConstructorModel>  getConstructorModels() {
        return Collections.emptyList(); }
    
    public List<JavaInitializerModel>  getInitializerModels() {
        return Collections.emptyList(); }
    
    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
        throw new NotSupportedException();
    }
    
    /**
     *throw NotSupportedException
     */
    public JavaAnnotationInstanceModel getDefaultAnnotationInstanceModel() throws NotSupportedException {
        throw new NotSupportedException();
    }
    
    
    
    public boolean isNested() {
        return false; }
    
    public boolean hasNestedTypeModels() {
        return false; }
    
    public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException {
        throw new NotSupportedException(); }
    
    public JavaTypeModel  getSuperClass() throws TermWareException {
        return JavaResolver.resolveJavaLangObject();
    }
    
    public List<JavaTypeModel> getSuperInterfaces() {
        return Collections.emptyList();
    }
    
    public boolean isLocal() {
        return false; }
    
    public boolean isAnonimous() {
        return false; }
    
    public JavaStatementModel getEnclosedStatement() {
        return null; }
    
    
    public boolean hasASTTerm() {
        return referencedType_.hasASTTerm();
    }
    
    public Term getASTTerm() throws TermWareException, EntityNotFoundException {
        return TermUtils.createTerm("ReferencedType",referencedType_.getASTTerm());
    }
    
    public Term getModelTerm() throws TermWareException, EntityNotFoundException {
        return TermUtils.createTerm("ReferencedType",referencedType_.getModelTerm());
    }
    
    /**
     *arrays types are not annotated.
     */
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap() {
        return Collections.emptyMap(); }
    
    
    class LengthMemberVariableModel extends JavaMemberVariableModel {
        public JavaTermModifiersModel getModifiers() {
            return JavaModelConstants.PUBLIC_MODIFIERS;
        }
        
        /**
         *@return name of member variable.
         */
        public String getName() {
            return "length";
        }
        
        public JavaTypeModel getType() {
            return JavaPrimitiveTypeModel.INT;
        }
        
        public  JavaTypeModel getOwnerType() {
            return JavaArrayTypeModel.this;
        }
        
        public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap() {
            return Collections.emptyMap(); }
        
        /**
         * ArrayLength
         */
        public Term getModelTerm() throws TermWareException {
            return TermUtils.createAtom("ArrayLength");
        }
        
        public boolean isSupportInitializerExpression() {
            return lengthInitializer_!=null; }
        
        public  JavaExpressionModel  getInitializerExpression() {
            return lengthInitializer_;
        }
        
    }
    
    private JavaTypeModel referencedType_;
    private LengthMemberVariableModel lengthModel_;
    private JavaExpressionModel lengthInitializer_;
    
    
}
