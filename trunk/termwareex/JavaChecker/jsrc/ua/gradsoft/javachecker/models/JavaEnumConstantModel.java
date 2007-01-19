/*
 * JavaEnumConstantModel.java
 *
 * Created on п'€тниц€, 19, с≥чн€ 2007, 13:34
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

/**
 *Abstract class for enum constants. (which are represents as final public static fields)
 * @author Ruslan Shevchenko
 */
public abstract class JavaEnumConstantModel extends JavaMemberVariableModel
{

    public JavaModifiersModel getModifiersModel()
    {
      return MODIFIERS_MODEL;
    }
    
    
   final public static JavaModifiersModel MODIFIERS_MODEL = new JavaModifiersModel(JavaModifiersModel.FINAL & JavaModifiersModel.PUBLIC & JavaModifiersModel.STATIC );
    
}
