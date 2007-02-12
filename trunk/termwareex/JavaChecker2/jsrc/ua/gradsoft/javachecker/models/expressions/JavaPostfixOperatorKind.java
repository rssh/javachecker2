/*
 * JavaPostfixOperatorKind.java
 *
 * Created on вівторок, 6, лютого 2007, 3:40
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Postfix operator: INCREMENT or DECREMENT
 * @author Ruslan Shevchenko
 */
public enum JavaPostfixOperatorKind {
    
    INCREMENT {
        public String getString()
        { return "++"; }
    },
    DECREMENT {
        public String getString()
        { return "--"; }
    };
    
    public abstract String getString();
    
    public static JavaPostfixOperatorKind create(String s) throws AssertException
    {
        for(JavaPostfixOperatorKind v: values()) {
            if (s.equals(v.getString())) {
                return v;
            }
        }
        throw new AssertException("Invalid postfix operator:"+s);
    }
    
}
