/*
 * JavaAdditiveOperatorKind.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Kind of AdditiveOperator
 * @author Ruslan Shevchenko
 */
public enum JavaAdditiveOperatorKind {
    
    PLUS {
        public String getString()
        { return "+"; }
    },
    MINUS {
        public String getString()
        { return "-"; }
    }
    ;
    
    public abstract String getString();
    
    public boolean checkString(String s)
    { return getString().equals(s); }
    
    public static JavaAdditiveOperatorKind create(String s) throws AssertException
    {
        for(JavaAdditiveOperatorKind v: values()) {
            if (v.getString().equals(s)) {
                return v;
            }
        }
        throw new AssertException("Invalid additive operator:"+s);
    }
    
}
