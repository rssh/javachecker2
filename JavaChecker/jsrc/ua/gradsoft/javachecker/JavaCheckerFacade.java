/*
 * Facade.java
 *
 *
 * Copyright (c) 2006-2009 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.models.AnalyzedUnitRef;

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
      Main.setHome(home);     
    }
    
    /**
     * set home
     */
    public static String getHome()
    {
      return Main.getHome();
    }    

    public static boolean isHomeRequired()
    { return Main.isHomeRequired(); }
    
    public static void setHomeRequired(boolean value)
    {
        Main.setHomeRequired(value);
    }
    
    public static String  getTmpDir()
    {
        return main_.getTmpDir();
    }
    
    public static boolean isMandatoryCheckersLoading()
    { return Main.isMandatoryCheckersLoading(); }
    
    public static void setMandatoryCheckersLoading(boolean value)
    { Main.setMandatoryCheckersLoading(value); }

    /**
     * @see Main#isInEmbeddedMode()
     * @return embedded mode flag.
     */
    public static boolean isInEmbeddedMode()
    {
      return Main.isInEmbeddedMode();
    }


    /**
     * set flag for embedded mode.
     * @see Main#setInEmbeddedMode(boolean)
     */
    public static void setInEmbeddedMode(boolean flag)
    {
      Main.setInEmbeddedMode(flag);
    }



    public static void process() throws ProcessingException
    {
        List<AnalyzedUnitRef> unitRefs = new LinkedList<AnalyzedUnitRef>();
        main_.process(unitRefs);
    }

    public static void process(List<AnalyzedUnitRef> unitRefs) throws ProcessingException
    {
        main_.process(unitRefs);
    }
    
    
    public static void processOneFile(String fullClassName) throws ProcessingException 
    {
        main_.processOneClass(fullClassName);
    }
   
    
    private static Main main_;
    
}
