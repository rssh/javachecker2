/*
 * JavaClassFieldModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for field of class
 * @author Ruslan Shevchenko
 */
public class JavaClassFieldModel extends JavaMemberVariableAbstractModel
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
        return new JavaModifiersModel(jmodifiers);
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
    
    public boolean canCheck()
    { return false; }
    
    public boolean check()
    { return true; }
    
       
    private JavaClassTypeModel classTypeModel_;
    private Field field_;
}
