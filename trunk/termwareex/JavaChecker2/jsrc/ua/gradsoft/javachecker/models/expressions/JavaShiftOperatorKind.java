/*
 * JavaShiftOperatorKind.java
 *
 *
 * Copyright (c) 2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.termware.exceptions.AssertException;

/**
 *ShiftExpressionKind
 * @author Ruslan Shevchenko
 */
public enum JavaShiftOperatorKind {
    
    LEFT_SHIFT {
        public String getString()
        { return "<<"; }
    },
    RIGHT_SIGNED_SHIFT {
        public String getString()
        { return ">>"; }
    },
    RIGHT_UNSIGNED_SHIFT {
        public String getString() 
        { return ">>>"; }
    }
    ;
    
    public abstract String getString();
    
    public boolean checkString(String s)
    {
      return s.equals(getString());  
    }
    
    public static JavaShiftOperatorKind create(String s) throws AssertException
    {
        for(JavaShiftOperatorKind v: values()) {
            if (v.getString().equals(s)) {
                return v;
            }
        }
        throw new AssertException("Invalid shift operator:"+s);
    }
    
    
}
