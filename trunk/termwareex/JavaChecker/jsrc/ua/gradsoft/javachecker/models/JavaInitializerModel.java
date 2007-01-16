/*
 * JavaIntializerModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

/**
 *Model for Java initializer
 * @author Ruslan Shevchenko
 */
public interface JavaInitializerModel extends JavaTopLevelBlockOwnerModel
{
    
    /**
     * get initializer modifiers.
     */
    public JavaModifiersModel getModifiers();
    
    
}
