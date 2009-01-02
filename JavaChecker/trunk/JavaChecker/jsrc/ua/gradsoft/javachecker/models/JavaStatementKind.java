/*
 * JavaStatementKind.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

/**
 *Kind of Java Statements
 * @author Ruslan Shevchenko
 */
public enum JavaStatementKind {
    
    LABELED_STATEMENT (true),
    ASSERT_STATEMENT  (true),
    BLOCK (true),
    EMPTY_STATEMENT (true),
    STATEMENT_EXPRESSION_STATEMENT (false),
    SWITCH_STATEMENT (true),
    SWITCH_LABEL_BLOCK(true),
    IF_STATEMENT (true),
    WHILE_STATEMENT (true),
    DO_STATEMENT (true),
    FOR_STATEMENT (true),
    BREAK_STATEMENT (false),
    CONTINUE_STATEMENT (false),
    RETURN_STATEMENT (false),
    THROW_STATEMENT (false),
    SYNCHRONIZED_STATEMENT (true),
    TRY_STATEMENT (true),
    CATCH_SEQUENCE(true),
    CATCH(true),
    LOCAL_VARIABLE_DECLARATION(false),
    CLASS_OR_INTERFACE_DECLARATION(false),
    EXPLICIT_SUPER_CONSTRUCTOR_INVOCATION(false),
    EXPLICIT_THIS_CONSTRUCTOR_INVOCATION(false)
    ;            
    
    JavaStatementKind(boolean haveChilds)
    {
      haveChilds_=haveChilds;  
    }
        
    private boolean haveChilds_;
}
