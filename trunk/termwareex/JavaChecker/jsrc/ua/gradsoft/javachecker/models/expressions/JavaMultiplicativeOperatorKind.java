/*
 * JavaMultiplicativeOperatorKind.java
 *
 * Created on вівторок, 6, лютого 2007, 2:11
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Kind of multiplicative operator
 * @author Ruslan Shevchenko
 */
public enum JavaMultiplicativeOperatorKind {
  
    MULTIPLY {
        public String getString()
        { return "*"; }
    },
    DIVIDE {
        public String getString()
        { return "/"; }
    },
    MODULE {
        public String getString()
        { return "%"; }
    };
    
    public abstract String getString();
    
    public static JavaMultiplicativeOperatorKind create(String s) throws AssertException
    {
      for(JavaMultiplicativeOperatorKind v:values()) {
          if (s.equals(v.getString())) {
              return v;
          }
      }  
      throw new AssertException("Invalid multiplicative operator:"+s);
    }
    
}
