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
     *Must be called before any operation on it or on resolver.
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
     *add include jar
     */
    public static void addIncludeJar(String includeJar)
    {
      main_.getFacts().getPackagesStore().addJar(includeJar);  
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
    
    /**
     * set home
     */
    public static void setHome(String home)
    {
      main_.setHome(home);  
    }
    
    /**
     * set home
     */
    public static String getHome()
    {
      return main_.getHome();
    }    

    public static void process() throws ProcessingException
    {
        main_.process();
    }
    
    public static void processOneFile(String fullClassName) throws ProcessingException 
    {
        main_.processOneClass(fullClassName);
    }
   
    
    private static Main main_;
    
}
