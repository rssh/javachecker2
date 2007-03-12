/*
 * JavaUnknownTypeModel.java
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *Really 'Unknown' type model. Resoved, when we need model, but one is inaccessible
 *during some reasons. (Or it is impossible to build one).
 * @author Ruslan Shevchenko
 */
public class JavaUnknownTypeModel extends JavaTypeModel {
    
    /** Creates a new instance of JavaUnknownTypeModel */
    private JavaUnknownTypeModel() {
        super(Main.getFacts().getPackagesStore().findOrAddPackage("UNKNOWN"));
    }
    
    
    public String getName() {
        return "UNKNOWN"; }
    
    public Term getShortNameAsTerm() throws TermWareException {
        return TermWare.getInstance().getTermFactory().createAtom("UNKNOWN"); }
    
    public String getFullName() {
        return "UNKNOWN";
    }
    
    public Term getFullNameAsTerm() throws TermWareException {
        return getShortNameAsTerm();
    }
    
    public JavaModifiersModel  getModifiersModel()
    { return JavaModelConstants.PUBLIC_MODIFIERS; }
    
    /**
     * get canonical name, siutable for use in JVM
     */
    public String getCanonicalName() 
    {
      return getName();
    }
    
    public boolean isClass()
    {
      return false;  
    }
    
    public  boolean isInterface()
    {
      return false;  
    }
    
    public  boolean isEnum()
    {
      return false;  
    }
    
    public boolean isAnnotationType()
    {
      return false;  
    }
    
    public boolean isPrimitiveType()
    { return false; }
    
    public boolean isArray()
    { return false; }
    
    public boolean isTypeArgument()
    { return false; }
    
    public boolean isWildcardBounds()
    { return false; }
    
    public boolean isNull()
    { return false; }
    
    public boolean isUnknown()
    { return true; }
    
    public boolean isLocal()
    { return false; }
    
    public boolean isAnonimous()
    { return false; }

    public JavaStatementModel getEnclosedStatement()
    { return null; }
    
    public JavaTypeModel  getEnclosedType() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    /**
     *throws NotSupportedException
     */
    public JavaTypeModel  getReferencedType() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    
    public JavaTypeModel getSuperClass() throws TermWareException
    { return JavaResolver.resolveJavaLangObject(); }
    
    public List<JavaTypeModel> getSuperInterfaces()
    { return Collections.emptyList(); }
    
    public boolean canCheck()
    { return false; }
    
    public boolean check() 
    { return true; }
    
    /**
     *@return false
     */
    public boolean hasMethodModels()
    { return false; }
    
    /**
     * throws NotSupportedException
     */
    public Map<String, List<JavaMethodModel>>   getMethodModels() throws NotSupportedException
    {
        throw new NotSupportedException();
    }
    
    
    
    public boolean hasMemberVariableModels()
    {
      return false;  
    }
    
    public Map<String, JavaMemberVariableModel> getMemberVariableModels() throws NotSupportedException
    {
        throw new NotSupportedException();
    }
    
    
    public boolean isNested()
    { return false; }
    
    public boolean hasNestedTypeModels()
    { return false; }
    
    public boolean hasASTTerm()
    { return false; }
    
    public Term getASTTerm()
    { return TermUtils.createNil(); }
    
    /**
     * UnknownTypeModel(context)
     */
    public Term getModelTerm() throws TermWareException
    {
      JavaPlaceContext ctx = JavaPlaceContextFactory.createNewTypeContext(this);
      Term tctx = TermUtils.createJTerm(ctx);
      return TermUtils.createTerm("UnknownTypeModel",tctx);
    }

    /**
     * throw NotSupportedException
     */ 
    public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException
    {
      throw new NotSupportedException();  
    }

    /**
     * throw NotSupportedException
     */ 
    public Map<String, JavaEnumConstantModel> getEnumConstantModels() throws NotSupportedException {
        throw new NotSupportedException();  
    }
    
    
    public boolean hasTypeParameters()
    { return false; }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters()
    {
      return Collections.emptyList();
    }        
    
    static final public JavaUnknownTypeModel INSTANCE = new JavaUnknownTypeModel();
    
    

    
}
