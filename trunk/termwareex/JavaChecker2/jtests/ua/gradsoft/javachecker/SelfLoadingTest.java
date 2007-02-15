/*
 * SelfLoadingTest.java
 *
 * Created on четвер, 25, січня 2007, 3:53
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeModel;

/**
 *Some tests accross JavaChecker2 sources.
 * @author Ruslan Shevchenko
 */
public class SelfLoadingTest extends TestCase
{
    
    
    protected void setUp() throws Exception
    {
        JavaCheckerFacade.init();
        JavaCheckerFacade.addInputDirectory("jsrc");
    }

    public void  testLoadJavaResolver() throws Exception
    {
        JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("ua.gradsoft.javachecker.models.JavaResolver");
        assertTrue("ua.gradsoft.javachecker.models.JavaResolver exists",true);
        tm = JavaResolver.resolveTypeModelByFullClassName("ua.gradsoft.javachecker.models.JavaTypeModel");
        assertTrue("ua.gradsoft.javachecker.models.JavaTypeModel exists",true);
    }
    
    
}
