/*
 * ExpressionPriorities.java
 * 
 *
 * Copyright (c) 2006, 2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.printers.java5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Helper class, which provide expression priorities.
 *(less means less, i. e. ConditionalExpression - 1, PrimaryExpression - bigger.
 * @author Ruslan Shevchenko
 */
public class ExpressionPriorities {
    
    public static final int EXPRESSION_PRIORITY = 0;
    public static final int CONDITIONAL_EXPRESSION_PRIORITY = 1;
    public static final int CONDITIONAL_OR_EXPRESSION_PRIORITY = 2;
    public static final int CONDITIONAL_AND_EXPRESSION_PRIORITY = 3;
    public static final int INCLUSIVE_OR_EXPRESSION_PRIORITY = 4;
    public static final int EXCLUSIVE_OR_EXPRESSION_PRIORITY = 5;
    public static final int AND_EXPRESSION_PRIORITY = 6;
    public static final int EQUALITY_EXPRESSION_PRIORITY = 7;
    public static final int INSTANCE_OF_EXPRESSION_PRIORITY = 8;
    public static final int RELATIONAL_EXPRESSION_PRIORITY = 9;
    public static final int SHIFT_EXPRESSION_PRIORITY = 10;
    public static final int ADDITIVE_EXPRESSION_PRIORITY = 11;
    public static final int MULTIPLICATIVE_EXPRESSION_PRIORITY = 12;
    public static final int UNARY_EXPRESSION_PRIORITY = 13;
    public static final int CAST_EXPRESSION_PRIORITY = 14;
    public static final int POSTFIX_EXPRESSION_PRIORITY = 15;
    public static final int PRIMARY_EXPRESSION_PRIORITY = 16;

    
    
}
