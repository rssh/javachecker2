/*
 * Facade.java
 *
 * Created on понеділок, 15, січня 2007, 1:11
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

/**
 *Facade java class, to invoke JavaChecker from other programs.
 * @author Ruslan Shevchenko
 */
public class JavaCheckerFacade {
    
    /**
     * create new instance of JavaChecker
     */
    public JavaCheckerFacade() throws ConfigException
    {
      main_=new Main();  
      main_.init(new String[0]);
    }
    
    /**
     *add input directory
     */
    public void addInputDirectory(String inputDirectory)
    {
      main_.addInputDirectory(inputDirectory);   
    }
    
    /**
     *get packages store
     */
    public PackagesStore getPackagesStore()
    {
      return main_.getFacts().getPackagesStore();  
    }
    
    
    private Main main_;
    
}
