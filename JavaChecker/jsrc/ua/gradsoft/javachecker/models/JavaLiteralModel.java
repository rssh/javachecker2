/*
 * JavaLiteralModel.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.TermWareException;

/**
 *Literal
 * @author Ruslan Shevchenko
 */
public interface JavaLiteralModel extends JavaExpressionModel
{
            
    public String getString() throws TermWareException;
    
}
