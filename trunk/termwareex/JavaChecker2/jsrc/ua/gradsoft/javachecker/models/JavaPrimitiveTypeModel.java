/*
 * JavaPrimitiveTypeModel.java
 *
 * Copyright (c) 2004-2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaPrimitiveTypeModel extends JavaTypeModel {
    
    public final static JavaPrimitiveTypeModel BOOLEAN = new JavaPrimitiveTypeModel("boolean");
    public final static JavaPrimitiveTypeModel CHAR = new JavaPrimitiveTypeModel("char");
    public final static JavaPrimitiveTypeModel BYTE = new JavaPrimitiveTypeModel("byte");
    public final static JavaPrimitiveTypeModel SHORT = new JavaPrimitiveTypeModel("short");
    public final static JavaPrimitiveTypeModel INT = new JavaPrimitiveTypeModel("int");
    public final static JavaPrimitiveTypeModel LONG = new JavaPrimitiveTypeModel("long");
    public final static JavaPrimitiveTypeModel FLOAT = new JavaPrimitiveTypeModel("float");
    public final static JavaPrimitiveTypeModel DOUBLE = new JavaPrimitiveTypeModel("double");
    public final static JavaPrimitiveTypeModel VOID = new JavaPrimitiveTypeModel("void");
    
    /** Creates a new instance of JavaPrimitiveTypeModel */
    private JavaPrimitiveTypeModel(String name) {
        super(Main.getFacts().getPackagesStore().findOrAddPackage("java.lang"));
        name_=name;
    }
    
    public String getName() {
        return name_; }
    
    
    public Term getShortNameAsTerm() {
        return TermUtils.getTermFactory().createAtom(name_); }
    
    public JavaTermModifiersModel getModifiersModel() {
        return JavaModelConstants.PUBLIC_MODIFIERS;
    }
    
    public boolean isClass() {
        return false; }
    
    public boolean isInterface() {
        return false; }
    
    public boolean isEnum() {
        return false; }
    
    public boolean isAnnotationType() {
        return false; }
    
    
    public boolean isPrimitiveType() {
        return true; }
    
    public boolean isTypeArgument() {
        return false; }
    
    public boolean isUnknown() {
        return false; }
    
    public boolean isNull() {
        return false; }        
    
    public boolean isWildcardBounds() {
        return false; }
    
    public JavaTypeModel  getEnclosedType() throws NotSupportedException {
        return null; }
    
    public boolean isArray() {
        return false; }
    
    public boolean isLocal()
    { return false; }

    public boolean isAnonimous()
    { return false; }
    
    
    public JavaStatementModel  getEnclosedStatement()
    { return null; }
    
    public JavaTypeModel  getReferencedType() throws NotSupportedException {
        throw new NotSupportedException(); }
    
    
    public boolean hasMethodModels() {
        return false; }
    
    /**
     * key of return values is name of methods.
     */
    public Map<String, List<JavaMethodModel>>   getMethodModels() throws NotSupportedException {
        throw new NotSupportedException();
    }
    
    
    public boolean hasMemberVariableModels() {
        return false;
    }
    
    public Map<String, JavaMemberVariableModel> getMemberVariableModels() throws NotSupportedException {
        throw new NotSupportedException();
    }

    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
        throw new NotSupportedException();
    }
    
    public List<JavaConstructorModel>  getConstructorModels()
    {  return Collections.emptyList(); }
    
    public boolean isNested() {
        return false; }
    
    public boolean hasNestedTypeModels() {
        return false; }
    
    public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException {
        throw new NotSupportedException(); }
    
    public boolean hasTypeParameters() {
        return false; }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() {
        return Collections.emptyList(); }

    public JavaTypeModel getSuperClass()
    { return JavaNullTypeModel.INSTANCE; }

    public List<JavaTypeModel> getSuperInterfaces()
    { return Collections.emptyList(); }
    
    
    /**
     *@return empty map
     */
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    { return Collections.emptyMap(); }
    
    
    public boolean hasASTTerm()
    { return true; }
    
    public Term getASTTerm()
    { return TermUtils.createAtom(name_); }
      
    
    public Term getModelTerm()
    {
        return TermUtils.createAtom(name_);
    }
    
    private String name_;
        

}
