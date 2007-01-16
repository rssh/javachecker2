/*
 * JavaTypeVariableAbstractModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Abstract model for Java Type Variable
 * @author Ruslan Shevchenko
 */
public abstract class JavaTypeVariableAbstractModel extends JavaTypeModel {
    
    public JavaTypeVariableAbstractModel() {
        super(Main.getFacts().getPackagesStore().findOrAddPackage("java.lang"));
    }
    
    public abstract String getName();
    
    public abstract List<JavaTypeModel> getBounds() throws TermWareException;
    
    public  Term  getShortNameAsTerm() throws TermWareException
    { return TermUtils.createIdentifier(getName()); }
    
    /**
     * return false;
     **/
    public boolean isClass() {
        return false; }

    /**
     * return false;
     **/    
    public boolean isInterface() {
        return false; }
    
    /**
     * return false;
     **/    
    public boolean isEnum()
    { return false; }
    
    /**
     * return false;
     **/    
    public boolean isAnnotationType()
    { return false; }
    
    public boolean isPrimitiveType()
    { return false; }
        
    public boolean isArray()
    { return false; }
        
    public boolean isTypeArgument()
    { return true; }
    
    public boolean isWildcardBounds()
    { return false; }
    
    public boolean isNull()
    { return false; }
    
    public boolean isUnknown()
    { return true; }
    
    /**
     * throws NotSupportedException
     */
    public JavaTypeModel  getEnclosedType() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    /**
     *@return referenced type. Works only if isArray()==true, otherwise
     *throws NotSupportedException
     */
    public JavaTypeModel  getReferencedType() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    
    public JavaTypeModel getSuperClass()
    { return JavaNullTypeModel.INSTANCE; }
    
    public List<JavaTypeModel> getSuperInterfaces()
    { return JavaModelConstants.TYPEMODEL_EMPTY_LIST; }

    
    public boolean canCheck() {
        return true; }
    
    
    public boolean hasMethodModels() {
        return false; }
    
    /**
     * throws NotSupportedException
     */
    public Map<String,List<JavaMethodAbstractModel> >   getMethodModels() throws NotSupportedException {
        throw new NotSupportedException();
    }
    
    
    public boolean hasMemberVariableModels() {
        return false; }
    
    public Map<String,JavaMemberVariableAbstractModel> getMemberVariableModels() throws NotSupportedException {
        throw new NotSupportedException(); }
    
    
    public boolean isNested() {
        return false; }
    
    public boolean hasNestedTypeModels() {
        return false; }
    
    public boolean isLocal()
    { return false; }
    
    public JavaStatementModel getEnclosedStatement()
    { return null; }
    
    public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException, TermWareException {
        throw new NotSupportedException();
    }
    
    
    public boolean hasTypeParameters()
    { return false; }
    
    /**
     *@return empty list
     */
    public List<JavaTypeVariableAbstractModel>  getTypeParameters() {
        return new LinkedList<JavaTypeVariableAbstractModel>();
    }
}
