/*
 * AbstractChecker.java
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import ua.gradsoft.termware.Term;

/**
 *Abstract interface for checkers.
 * @author Ruslan Shevchenko
 */
public abstract class AbstractChecker {

    AbstractChecker(String name, String description,
                    CheckerScope checkerScope, 
                    CheckerType checkerType,
                    Term rules,
                    boolean  enabledByDefault)
    {
        name_=name;
        description_=description;
        checkerScope_=checkerScope;
        checkerType_=checkerType;
        enabledByDefault_=enabledByDefault;
    }
    
    public String getName()
    { return name_; }
    
    public CheckerScope  getCheckerScope()
    { return checkerScope_; }

    public CheckerType  getCheckerType()
    { return checkerType_; }

    
    public abstract void configure(Main main);
    
    public abstract void check(Term t, JavaFacts javaFacts);
    
    private String name_;
    private String description_;
    private CheckerScope checkerScope_;
    private CheckerType  checkerType_;
    private boolean      enabledByDefault_;
    
}
