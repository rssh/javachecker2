/*
 * JavaClassFieldModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for field of class
 * @author Ruslan Shevchenko
 */
public class JavaClassFieldModel extends JavaMemberVariableModel
{
    
    /** Creates a new instance of JavaClassFieldModel */
    public JavaClassFieldModel(Field field,JavaClassTypeModel classTypeModel) {
        field_=field;
        classTypeModel_=classTypeModel;
    }

    
    public String getName()
    {
      return field_.getName();  
    }
    
    public JavaModifiersModel getModifiersModel()
    {
        int jmodifiers=field_.getModifiers();
        return new JavaModifiersModel(JavaClassTypeModel.translateModifiers(jmodifiers));
    }
    
    public JavaTypeModel getOwner()
    {
      return classTypeModel_;  
    }
    
    public JavaTypeModel getTypeModel() throws TermWareException
    {
        Type fieldType = field_.getGenericType();
        return JavaClassTypeModel.createTypeModel(fieldType);
    }
    
    /**
     * ClassField(this)
     */
    public Term getModelTerm() throws TermWareException
    {       
       Term tctx = TermUtils.createJTerm(this);
       return TermUtils.createTerm("ClassField",tctx);
    }
    
    
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
    
       
    private JavaClassTypeModel classTypeModel_;
    private Field field_;
}
