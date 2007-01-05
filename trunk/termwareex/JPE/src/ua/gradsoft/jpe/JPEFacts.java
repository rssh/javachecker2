/*
 * JPEFacts.java
 *
 * Created on вівторок, 2, січня 2007, 2:20
 *
 * Copyright (c) 2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.jpe;

import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.IEnv;
import ua.gradsoft.termware.TermWareException;

/**
 *Facts for JPE
 * @author Ruslan Shevchenko
 */
public class JPEFacts extends DefaultFacts
{
    
    public JPEFacts() throws TermWareException
    { super(); }
    
    public void setConfiguration(Configuration configuration)
    {
      configuration_=configuration;  
    }
    
    
    private Configuration configuration_;
}
