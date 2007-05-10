/*
 * JavaIntializerModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.io.PrintWriter;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for Java initializer
 * @author Ruslan Shevchenko
 */
public abstract class JavaInitializerModel implements JavaTopLevelBlockOwnerModel
{
    
    /**
     * get initializer modifiers.
     */
    public abstract JavaTermModifiersModel getModifiers();
    
    
    /**
     *get model term.
     */
    public abstract Term getModelTerm() throws TermWareException, EntityNotFoundException;
    
    
    public void printSignature(PrintWriter out)
    {
      out.print("{initializer}");  
    }
    
}
