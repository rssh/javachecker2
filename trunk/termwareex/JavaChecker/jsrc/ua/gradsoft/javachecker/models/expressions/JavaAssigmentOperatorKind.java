/*
 * JavaAssigmentOperatorKind.java
 *
 * Created on понеділок, 5, лютого 2007, 14:29
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Kind of Java assigment operator
 * @author Ruslan Shevchenko
 */
public enum JavaAssigmentOperatorKind {
    
    ASSIGN {
      public boolean checkName(String name)
      {
        return name.equals("op_assign") || name.equals("=");  
      }
    }, 
    MULTIPLY_ASSIGN {
      public boolean checkName(String name)
      {
        return name.equals("op_multiply_assign") || name.equals("*=");  
      }            
    } ,    
    DIVIDE_ASSIGN {
      public boolean checkName(String name)
      {
        return name.equals("op_divide_assign") || name.equals("/=");  
      }                                
    } ,
    MODULE_ASSIGN {
      public boolean checkName(String name)
      {
        return name.equals("op_module_assign") || name.equals("%=");  
      }                                                            
    }  ,    
    PLUS_ASSIGN {
      public boolean checkName(String name)
      {
        return name.equals("op_plus_assign") || name.equals("+=");  
      }                                                                                         
    } ,
    MINUS_ASSIGN {
      public boolean checkName(String name)
      {
        return name.equals("op_minus_assign") || name.equals("-=");  
      }                                                                                                                      
    }  ,
    LEFT_SHIFT_ASSIGN {
      public boolean checkName(String name)
      {
        return name.equals("op_left_shift_assign") || name.equals("<<=");  
      }                                                                                                                                                           
    } ,
    RIGHT_SHIFT_ASSIGN {
      public boolean checkName(String name)
      {
        return name.equals("op_left_shift_assign") || name.equals("<<=");  
      }                                                                                                                                                           
    } ,
    LEFT_SSHIFT_ASSIGN {
      public boolean checkName(String name)
      {
        return name.equals("op_left_sshift_assign") || name.equals(">>>=");  
      }                                                                                                                                                                   
    },
    AND_ASSIGN {
      public boolean checkName(String name)
      {
        return name.equals("op_and_assign") || name.equals("&=");  
      }                                                                                                                                                                           
    },
    XOR_ASSIGN{
      public boolean checkName(String name)
      {
        return name.equals("op_xor_assign") || name.equals("^=");  
      }                                                                                                                                                                                   
    },
    OR_ASSIGN{
      public boolean checkName(String name)
      {
        return name.equals("op_or_assign") || name.equals("|=");  
      }                                                                                                                                                                                           
    };

    public abstract boolean checkName(String name);

    public static JavaAssigmentOperatorKind create(Term t) throws TermWareException
    {
       String s=null; 
       if (t.isAtom()) {
           s=t.getName();
       }else if (t.isString()) {
           s=t.getString();
       }else{
           throw new AssertException("argument of JavaAssigmentOperatorKind must be atom or string:"+TermHelper.termToString(t));
       }
       for(JavaAssigmentOperatorKind v:values()) {
           if (v.checkName(s)) {
               return v;
           }
       }       
       throw new AssertException("invalid assigment operator:"+s);
    }
    
}
