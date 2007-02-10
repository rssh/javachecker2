/*
 * InvalidJavaTermException.java
 *
 * Created on субота, 20, січня 2007, 2:39
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Throws when we get somehwere invalid or non-compiling term
 * @author Ruslan Shevchenko
 */
public class InvalidJavaTermException extends AssertException
{
    
    public InvalidJavaTermException(String message, Term t)
    {
     super(message+":"+TermHelper.termToString(t));   
    }
    
}
