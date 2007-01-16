/*
 * JavaUnknownTypeModel.java
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *Null type model.  (represents superclasses of primitive and object)
 * @author Ruslan Shevchenko
 */
public class JavaNullTypeModel extends JavaTypeModel {
    
    /** Creates a new instance of JavaUnknownTypeModel */
    private JavaNullTypeModel() {
        super(Main.getFacts().getPackagesStore().findOrAddPackage("java.lang"));
    }
    
    
    public String getName() {
        return "null"; }
    
    public Term getShortNameAsTerm() throws TermWareException {
        return TermWare.getInstance().getTermFactory().createAtom("null"); }
    
    public String getFullName() {
        return "null";
    }
    
    public Term getFullNameAsTerm() throws TermWareException {
        return getShortNameAsTerm();
    }
    
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
    { return true; }
    
    public boolean isUnknown()
    { return false; }
    
    public boolean isLocal()
    { return false; }
    
    public JavaStatementModel  getEnclosedStatement()
    { return null; }
    
    public JavaTypeModel  getEnclosedType() throws NotSupportedException
    { throw new NotSupportedException(); }
    
    /**
     *throws NotSupportedException
     */
    public JavaTypeModel  getReferencedType() throws NotSupportedException
    { throw new NotSupportedException(); }
    
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
    public Map<String,List<JavaMethodAbstractModel> >   getMethodModels() throws NotSupportedException
    {
        throw new NotSupportedException();
    }
    
    
    
    public boolean hasMemberVariableModels()
    {
      return false;  
    }
    
    public Map<String, JavaMemberVariableAbstractModel> getMemberVariableModels() throws NotSupportedException
    {
        throw new NotSupportedException();
    }
    
    
    public boolean isNested()
    { return false; }
    
    public boolean hasNestedTypeModels()
    { return false; }

    /**
     * throw NotSupportedException
     */ 
    public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException
    {
      throw new NotSupportedException();  
    }
    
    public boolean hasTypeParameters()
    { return false; }
    
    public List<JavaTypeVariableAbstractModel>  getTypeParameters()
    {
      return JavaModelConstants.TYPEVARIABLE_EMPTY_LIST;  
    }        
    
    public JavaTypeModel getSuperClass()
    { return INSTANCE; }
    
    public List<JavaTypeModel> getSuperInterfaces()
    { return JavaModelConstants.TYPEMODEL_EMPTY_LIST; }
    
    
    static final public JavaNullTypeModel INSTANCE = new JavaNullTypeModel();
    
}
