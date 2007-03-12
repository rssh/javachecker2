/*
 * AbstractChecker.java
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.javachecker.util.Holder;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Abstract interface for checkers.
 * @author Ruslan Shevchenko
 */
public abstract class AbstractChecker {

    public AbstractChecker(String name, 
                    String category,
                    String description,                                    
                    boolean  enabled)
    {
        name_=name;
        category_=category;
        description_=description;           
        enabled_=enabled;
    }
    
    public String getName()
    { return name_; }
    
    public String getCategory()
    { return category_; }
    
    public String getDescription()
    { return description_; }
    
    public boolean isEnabled()
    { return enabled_; }
   
    
    public abstract CheckerType  getCheckerType();  
    
    public abstract void configure(JavaFacts facts) throws ConfigException;
    
    public abstract void run(JavaTermTypeAbstractModel tm, Holder<Term> astTerm, Holder<Term> modelTerm) throws TermWareException;
    
    private String name_;
    private String category_;
    private String description_;
    private boolean      enabled_;
    
}
