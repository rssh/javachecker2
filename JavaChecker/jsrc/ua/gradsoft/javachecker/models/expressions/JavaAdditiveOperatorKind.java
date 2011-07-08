/*
 * JavaAdditiveOperatorKind.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Kind of AdditiveOperator
 * @author Ruslan Shevchenko
 */
public enum JavaAdditiveOperatorKind {
    
    PLUS {
        public String getString() {
            return "+"; }
        
        public Object doOperation(Object x, Object y) throws TermWareException {
            if (x instanceof String) {
                if (y instanceof String) {
                    return ((String)x)+((String)y);
                }else{
                    return ((String)x)+y.toString();
                }
            }else if (x instanceof Number) {
                Number nx = (Number)x;
                if (y instanceof Number) {
                    Number ny = (Number)y;
                    if (nx instanceof Double || ny instanceof Double) {
                        return nx.doubleValue()+ny.doubleValue();
                    }else if (nx instanceof Float || ny instanceof Float) {
                        return nx.floatValue()+ny.floatValue();
                    }else if (nx instanceof Long || ny instanceof Long){
                        return nx.longValue()+ny.longValue();
                    }else if (nx instanceof Integer || ny instanceof Integer){
                        return nx.intValue()+ny.intValue();
                    }else if (nx instanceof Short || ny instanceof Short){
                        return nx.shortValue()+ny.shortValue();
                    }else if (nx instanceof Byte || ny instanceof Byte){
                        return nx.byteValue()+ny.byteValue();
                    }else{
                        throw new AssertException("Bad types for '+' operator");
                    }
                }else{
                    throw new AssertException("Bad types for '+' ("+x.toString()+","+y.toString());
                }
            }else{
                throw new AssertException("Bad types for '+' operator");
            }
        }
        
        
    },
    MINUS {
        public String getString() {
            return "-"; }
        
        public Object doOperation(Object x, Object y) throws TermWareException
        {
            if ((x instanceof Number)&& (y instanceof Number)) {
                Number nx = (Number)x;
                Number ny = (Number)y;
                if (nx instanceof Double || ny instanceof Double) {
                    return nx.doubleValue() - ny.doubleValue();
                }else if((nx instanceof Float)||(ny instanceof Float)) {
                    return nx.floatValue() - ny.floatValue();
                }else if ((nx instanceof Long)||(ny instanceof Long)){
                    return nx.longValue() - ny.longValue();
                }else if ((nx instanceof Integer)||(ny instanceof Integer)){
                    return nx.intValue() - ny.intValue();
                }else if ((nx instanceof Short)||(ny instanceof Short)){
                    return nx.shortValue() - ny.shortValue();
                }else if ((nx instanceof Byte)||(ny instanceof Byte)){
                    return nx.byteValue() - ny.byteValue();
                }else{
                    throw new AssertException("Bad Number types for '-' operator");
                }
            }else{
                throw new AssertException("Bad types for '-' operator");
            }
        }
        
    }
    ;
    
    public abstract String getString();
    
    public abstract Object doOperation(Object x,Object y) throws TermWareException;
    
    public boolean checkString(String s) {
        return getString().equals(s); }
    
    public static JavaAdditiveOperatorKind create(String s) throws AssertException {
        for(JavaAdditiveOperatorKind v: values()) {
            if (v.getString().equals(s)) {
                return v;
            }
        }
        throw new AssertException("Invalid additive operator:"+s);
    }
    
}
