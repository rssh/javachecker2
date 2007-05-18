/*
 * Facade.java
 *
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

/**
 *Facade java class, to invoke JavaChecker from other programs.
 * @author Ruslan Shevchenko
 */
public class JavaCheckerFacade {
    
    /**
     *Mark class as singleton.
     */
    private JavaCheckerFacade()
    {}
    
    /**
     * initialize JavaCheker.
     *Must be called before any operation on it.
     */
    public static void init() throws ConfigException
    {
      main_=new Main();  
      main_.init(new String[0]);
    }
    
    /**
     *add input directory
     */
    public static void addInputDirectory(String inputDirectory, boolean process)
    {
      main_.addInputDirectory(inputDirectory, process);   
    }
    
    /**
     *add properties directory
     */
    public static void addPropertiesDirectory(String inputDirectory)
    {
      main_.addPropertiesDirectory(inputDirectory);  
    }
    
    /**
     *get packages store
     */
    public static PackagesStore getPackagesStore()
    {
      return main_.getFacts().getPackagesStore();  
    }
    
    
    private static Main main_;
    
}
