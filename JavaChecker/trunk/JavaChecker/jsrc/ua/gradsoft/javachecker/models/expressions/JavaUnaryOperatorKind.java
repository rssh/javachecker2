/*
 * JavaUnaryOperatorKind.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Kind of unary operator
 */
public enum JavaUnaryOperatorKind {
    
    PLUS {
        public String getString()
        { return "+"; }
    },
    MINUS {
        public String getString()
        { return "-"; }
    },
    INVERSE {
        public String getString()
        { return "~"; }
    },
    NOT {
        public String getString()
        { return "!"; }
    }
    
    ;
    
    public abstract String getString();
    
    public static JavaUnaryOperatorKind create(String s) throws AssertException
    {
      for(JavaUnaryOperatorKind v: values()) {
          if (v.getString().equals(s)) {
              return v;
          }
      }
      throw new AssertException("Invalid unary operator:"+s);
    }
    
}
