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
import ua.gradsoft.javachecker.models.expressions.JavaTermBooleanLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermCharacterLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermFloatingPointLiteralExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaTermIntegerLiteralExpressionModel;
import ua.gradsoft.termware.Term;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaPrimitiveTypeModel extends JavaTypeModel {


    public final static JavaPrimitiveTypeModel BOOLEAN = new JavaPrimitiveTypeModel("boolean",
                                                               JavaTermBooleanLiteralExpressionModel.getFalseExpression()
                                                                                   );
    public final static JavaPrimitiveTypeModel CHAR = new JavaPrimitiveTypeModel("char",
                                                               JavaTermCharacterLiteralExpressionModel.getZero()
                                                                                   );
    public final static JavaPrimitiveTypeModel BYTE = new JavaPrimitiveTypeModel("byte",
                                                               JavaTermIntegerLiteralExpressionModel.getZero()
                                                                                   );
    public final static JavaPrimitiveTypeModel SHORT = new JavaPrimitiveTypeModel("short",
                                                               JavaTermIntegerLiteralExpressionModel.getZero()
                                                                                   );

    public final static JavaPrimitiveTypeModel INT = new JavaPrimitiveTypeModel("int",
                                                               JavaTermIntegerLiteralExpressionModel.getZero()
                                                                                   );

    public final static JavaPrimitiveTypeModel LONG = new JavaPrimitiveTypeModel("long",
                                                JavaTermIntegerLiteralExpressionModel.getZeroL()
                                                                                   );

    public final static JavaPrimitiveTypeModel FLOAT = new JavaPrimitiveTypeModel("float",
                                                       JavaTermFloatingPointLiteralExpressionModel.getZeroFloat()
                                                                                   );

    public final static JavaPrimitiveTypeModel DOUBLE = new JavaPrimitiveTypeModel("double",
                                                       JavaTermFloatingPointLiteralExpressionModel.getZeroDouble()
                                                                                   );

    public final static JavaPrimitiveTypeModel VOID = new JavaPrimitiveTypeModel("void",null);

    
    /** Creates a new instance of JavaPrimitiveTypeModel */
    private JavaPrimitiveTypeModel(String name,JavaExpressionModel defaultInit) {
        super(Main.getFacts().getPackagesStore().findOrAddPackage("java.lang"));
        name_=name;
        defaultInitExpression_=defaultInit;
    }
    
    public String getName() {
        return name_; }
    
    public String getErasedName()
    { return name_; }
    
    public Term getShortNameAsTerm() {
        return TermUtils.getTermFactory().createAtom(name_); }
    
    public Term getFullNameAsTerm() {
        return getShortNameAsTerm(); 
    }
    
    
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
    
    public boolean isTypeVariable() {
        return false; }
    
    public boolean isUnknown() {
        return false; }
    
    public boolean isNull() {
        return false; }        
    
    public boolean isWildcardBounds() {
        return false; }
    
    public JavaTypeModel  getEnclosedType() {
        return null; }
    
    public boolean isArray() {
        return false; }
    
    public boolean isLocal()
    { return false; }

    public boolean isAnonimous()
    { return false; }
    
    
    public JavaStatementModel  getEnclosedStatement()
    { return null; }
    
    public JavaTypeModel  getReferencedType() {
        return null;
    }
    
    
    public boolean hasMethodModels() {
        return false; }
    
    /**
     *@return empty map.
     */
    public Map<String, List<JavaMethodModel>>   getMethodModels() {
        return Collections.emptyMap();
    }
    
    
    public boolean hasMemberVariableModels() {
        return false;
    }
    
    public Map<String, JavaMemberVariableModel> getMemberVariableModels() {
        return Collections.emptyMap();
    }

    public Map<String, JavaEnumConstantModel> getEnumConstantModels() {
       return Collections.emptyMap();
    }
    
    /**
     *@return null
     */    
    public JavaAnnotationInstanceModel getDefaultAnnotationInstanceModel()  
    {
        return null;
    }
        
    
    public List<JavaConstructorModel>  getConstructorModels()
    {  return Collections.emptyList(); }

    public List<JavaInitializerModel>  getInitializerModels()
    {  return Collections.emptyList(); }
    
    
    public boolean isNested() {
        return false; }
    
    public boolean hasNestedTypeModels() {
        return false; }
    
    public Map<String,JavaTypeModel> getNestedTypeModels() {
        return Collections.emptyMap();
    }
    
    public boolean hasTypeParameters() {
        return false; }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() {
        return Collections.emptyList();
    }

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

    public JavaExpressionModel getDefaultInitializerExpression()
    { return defaultInitExpression_; }


    private String name_;
    private JavaExpressionModel defaultInitExpression_;

}
