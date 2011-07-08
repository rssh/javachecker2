/*
 * JavaEnumConstantModel.java
 *
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

    public JavaTermModifiersModel getModifiers()
    {
      return MODIFIERS_MODEL;
    }
    
    
   final public static JavaTermModifiersModel MODIFIERS_MODEL = new JavaTermModifiersModel(JavaTermModifiersModel.FINAL & JavaTermModifiersModel.PUBLIC & JavaTermModifiersModel.STATIC );
    
}
