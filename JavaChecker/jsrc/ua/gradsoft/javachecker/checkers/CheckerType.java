/*
 * CheckerType.java
 *
 * Copyright (c) 2004-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.checkers;

/**
 *Type of checker
 * @author Ruslan Shevchenko
 */
public enum CheckerType {
    
    /**
     * ruleset over AST, which applied to type with BT strategy
     */
    BT_TYPE_RULESET,

    /**
     * ruleset over AST, which applied to type with FT strategy
     */
    FT_TYPE_RULESET,

    
    /**
     * ruleset over AST, which applied to compilation unit.
     */
    BT_COMPILATION_UNIT_RULESET,
    
    /**
     * java class, which called for type.
     */
    JAVA_CLASS,
    
    /**
     * ruleset over model term, which called for type.
     */
    MODEL_RULESET,
    
    /**
     * java class, which called after processing and use facts API. 
     */
    POST_PROCESS_JAVA_CLASS,

    /**
     * dummy type, whcih do nothing. 
     */
     DUMMY
        
}
