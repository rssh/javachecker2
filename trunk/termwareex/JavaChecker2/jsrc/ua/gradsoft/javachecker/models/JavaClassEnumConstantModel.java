/*
 * JavaClassEnumConstantModel.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Enum constant from class.
 * @author Ruslan Shevchenko
 */
public class JavaClassEnumConstantModel extends JavaEnumConstantModel
{
    
    /**
     * constructor of enum constant.
     *@param o must be a instance of enum constant
     *@param JavaClassTypeModel
     */
    public JavaClassEnumConstantModel(Object instance, JavaClassTypeModel owner)
    {
       owner_=owner;      
       instance_=instance;
    }
    
    
    public String getName()
    { return instance_.toString(); }
    
    public JavaTypeModel  getTypeModel()
    { return owner_; }
    
    public JavaTypeModel  getOwner()
    { return owner_; }
    
    
    /**
     * ClassEnumConstantModel(this)
     */
    public Term getModelTerm() throws TermWareException
    {
        Term tthis=TermUtils.createJTerm(this);
        Term retval=TermUtils.createTerm("ClassEnumConstantModel",tthis);
        return retval;
    }
    
    private Object instance_;
    private JavaClassTypeModel owner_;
        
    
}
