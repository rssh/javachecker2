/*
 * PackagesStore.java
 *
 * Copyright (c) 2006-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.models.JavaPackageModel;
import ua.gradsoft.javachecker.util.JarClassLoader;

/**
 *Store for packages 
 * @author Ruslan Shevchenko
 */
public class PackagesStore 
{
    
    PackagesStore(JavaFacts owner)
    {
        owner_=owner;
        packageModels_=new HashMap<String,JavaPackageModel>();    
        sourceDirsToProcess_=new ArrayList<String>();
        allSourceDirs_=new ArrayList<String>();     
        jars_=new ArrayList<String>();
    }
    
    public JavaPackageModel  findOrAddPackage(String packageName)
    {
      // 1. check - are one already loaded ?  
      JavaPackageModel retval = packageModels_.get(packageName);
      if (retval!=null) {
          return retval;
      }
      //  create empty JavaPackageModel and add one
      retval=new JavaPackageModel(packageName,owner_);
      packageModels_.put(packageName,retval);
      return retval;
    }
    
    /**
     * remove package from set of loaded packages.
     */
    public  void  removePackage(String packageName)
    {
      packageModels_.remove(packageName);  
    }
    
    
    public  Map<String,JavaPackageModel>  getLoadedPackageModels()
    { return packageModels_; }
    
    
    /**
     * get source dirs, which we check or use for loading of depended sources;
     */    
    public  List<String>  getSourceDirs()
    { return allSourceDirs_; }

    /**
     * get dirs for sources, which we check.
     */    
    public  List<String>  getSourceDirsToProcess()
    { 
      return sourceDirsToProcess_;  
    }
    
    public  void  setJars(List<String> jars) throws ConfigException
    {
      jars_=jars;  
      jars_.add(Main.getHome()+File.separator+"JavaChecker2Annotations.jar");
      try {
          classLoader_=new JarClassLoader(jars.toArray(new String[0]));
      }catch(MalformedURLException ex){
          throw new ConfigException(ex.getMessage(),ex);
      }
    }
    
    public  ClassLoader getJarsClassLoader()
    {
        return classLoader_;
    }
    
    private JavaFacts   owner_;
    private HashMap<String, JavaPackageModel> packageModels_;    
    
    /**
     * dirs for sources, which we check.
     */
    private ArrayList<String>  sourceDirsToProcess_;
    
    /**
     * source dirs, which we check or use for loading of depended sources;
     */
    private ArrayList<String>  allSourceDirs_;
    
    
    
    /**
     * jar files, from which we want to load file.
     */
    private List<String>  jars_;
    private JarClassLoader     classLoader_;
    
}
