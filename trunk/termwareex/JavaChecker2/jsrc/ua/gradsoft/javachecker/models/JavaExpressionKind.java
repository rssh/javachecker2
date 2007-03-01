/*
 * JavaExpressionKind.java
 *
 * Created on понеділок, 5, лютого 2007, 6:07
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

/**
 *Kind of Java Expression
 * @author Ruslan Shevchenko
 */
public enum JavaExpressionKind 
{
    
    /**
     * expression with subexpression inside.
     */
    EXPRESSION,     

    /**
     * AssigmentExpression
     */        
    ASSIGMENT,        
    
    /**
     * ConditionalExpression
     */
    CONDITIONAL,
    
    /**    
     * ConditionalOrExpression
     */
    CONDITIONAL_OR,
            
    /**
     * ConditionalAndExpression
     */           
    CONDITIONAL_AND,
    
    /**
     *InclusiveOrExpression
     */
    INCLUSIVE_OR,
         
    /**
     *ExclusiveOrExpression
     */
    EXCLUSIVE_OR,
    
    /**
     *AndExpression
     */
    AND,
    
    /**
     *EqualityExpression
     */
    EQUALITY,
    
    /**
     *InstanceOfExpression
     */
    INSTANCEOF,
    
    /**
     *RelationalExpression
     */
    RELATIONAL,
    
    /**
     *ShiftExpression
     */
    SHIFT,
              
    /**
     *AdditiveExpression
     */
    ADDITIVE,
    
    /**
     *MultiplicativeExpression
     */
    MULTIPLICATIVE,
    
    /**
     *UnaryExpression, UnaryNotPlusMinusExpression
     */    
    UNARY,
    
    /**
     *PreIncrementExression
     */
    PREINCREMENT,
    
    /**
     *PreIncrementExression
     */
    PREDECREMENT,
    
    /**
     * CastExpression
     */
    CAST,
    
    /**
     * Postfix
     */
    POSTFIX,
    
    /**
     * IntegerLiteral
     */
    INTEGER_LITERAL,
    
    /**
     * FloatingPointLiteral
     */
    FLOATING_POINT_LITERAL,
    
    /**
     * CharacterLiteral
     */
    CHARACTER_LITERAL,
    
    /**
     * StringLiteral
     */
    STRING_LITERAL,
    
    /**
     * BooleanLiteral
     */
    BOOLEAN_LITERAL,

    /**
     * NullLiteral
     */
    NULL_LITERAL,
    
    /**
     * This
     */
    THIS,

    /**
     * This as primary prefix
     */
    THIS_PREFIX,
    
    
    /**
     * Super
     */
    SUPER,

    /**
     * Super as primary prefix
     */
    SUPER_PREFIX,
    
    
    /**
     *ClassLiteral
     */
    CLASS_LITERAL,
    
    /**
     *AllocationExpression
     */
    ALLOCATION_EXPRESSION,
    
    
    /**
     *Inner allocation
     */
    INNER_ALLOCATION,
    
    /**
     * IDENTIFIER
     */
    IDENTIFIER,
    
    /**
     * FunctionCall
     */
    FUNCTION_CALL,
    
    
    /**
     * MethodCall
     */
    METHOD_CALL,
            

    /**
     * SpecializedMethodCall
     */
    SPECIALIZED_METHOD_CALL,            
    
    /**
     * Field
     */
    FIELD,
    
    /**
     * ArrayIndex
     */
    ARRAY_INDEX,
            
    /**
     * NAME
     */         
    NAME,
    
    /**
     * Name of type
     */
    TYPE_NAME,
    
    /**
     * TYPE_FIELD
     */
    TYPE_FIELD,
    
    /**
     * StaticField
     */ 
    STATIC_FIELD,
            
    /**
     *ArrayInitializer
     */
    ARRAY_INITIALIZER,
    
    /**
     * SwitchConstant
     */
    SWITCH_CONSTANT
            
    
            
}
