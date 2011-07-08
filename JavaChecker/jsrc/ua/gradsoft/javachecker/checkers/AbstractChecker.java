/*
 * AbstractChecker.java
 * Copyright (c) 2004-2009 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.checkers;

import java.util.Iterator;
import ua.gradsoft.javachecker.*;
import ua.gradsoft.termware.ITermTransformer;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Abstract class for checkers.
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
    
    protected static void addRuleset(TermSystem system, Term rules) throws TermWareException, ConfigException
    {
        if (rules.getName().equals("ruleset")) {
            for(int i=0; i<rules.getArity(); ++i) {
                Term current=rules.getSubtermAt(i);
                if (current.getName().equals("import")) {
                    if(current.getArity()==2) {
                        Term termPatternName=current.getSubtermAt(1);
                        if (!termPatternName.isString()&&!termPatternName.isAtom()) {
                            throw new AssertException("secong argument of import must be atom or string");
                        }
                        String patternName=termPatternName.getName();
                        TermSystem fromSys = TermWare.getInstance().getRoot().resolveSystem(current.getSubtermAt(0));
                        Iterator<ITermTransformer> it=fromSys.getStrategy().getStar().iterator(patternName);
                        while(it.hasNext()) {
                            ITermTransformer tr=it.next();
                            //System.out.println("adding normalizer"+tr.toString());
                            system.addNormalizer(patternName, tr);
                        }
                    }else{
                        throw new AssertException("import in checkers.def must have arity 2");
                    }
                }else{
                   system.addRule(current);
                }
            }
        }else{
            throw new ConfigException("ruleset term required");
        }        
    }
    
    private String name_;
    private String category_;
    private String description_;
    private boolean      enabled_;
    
}
