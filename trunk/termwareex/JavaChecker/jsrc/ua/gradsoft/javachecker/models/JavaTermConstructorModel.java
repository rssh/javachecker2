/*
 * JavaTermConstructorModel.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.Term;

/**
 *
 * @author Ruslan Shevchenko
 */
public class JavaTermConstructorModel {
    
    /** Creates a new instance of JavaTermConstructorModel */
    public JavaTermConstructorModel(int modifiers,Term constructor,JavaTypeModel owner) {
        modifiers_=new JavaModifiersModel(modifiers);
        t_=constructor;
        owner_=owner;
    }
    
    
    private JavaModifiersModel modifiers_;
    private Term               t_;
    private JavaTypeModel      owner_;
    
}
