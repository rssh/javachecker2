/*
 * JavaRelationOperatorKind.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Kind of relation operator
 * @author Ruslan Shevchenko
 */
public enum JavaRelationOperatorKind {
    
    LESS 
    { 
        public String  getString()
        { return "<"; }
    },
    GREATER
    {
        public String getString()
        { return ">"; }
    },
    LESS_EQ
    {
        public String getString()
        { return "<="; }
    },
    GREATER_EQ
    {
        public String getString()
        { return ">="; }
    }                
         ;
    
    public boolean checkString(String s)
    { return getString().equals(s); }
    
    public abstract String  getString();
    
    public static JavaRelationOperatorKind  create(String s) throws AssertException
    {
       for(JavaRelationOperatorKind v: values()) {
           if (v.checkString(s)) {
               return v;
           }
       }
       throw new AssertException("Invalid relational operator:"+s);
    }
    
}
