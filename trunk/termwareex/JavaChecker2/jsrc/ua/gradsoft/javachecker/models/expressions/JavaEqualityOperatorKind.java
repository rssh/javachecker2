/*
 * JavaEqualityOperatorKind.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

/**
 *Enumeration of all equality operators.
 * @author Ruslan Shevchenko
 */
public enum JavaEqualityOperatorKind {
    
    EQUALS
    {
       public String getString() { return "=="; }
    },
    NOT_EQUALS
    {
            public String getString() { return "!="; }
    };
            
    public abstract String getString();            
  
}
