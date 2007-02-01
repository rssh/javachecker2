/*
 * Java5SrcTest.java
 *
 * Created on четвер, 1, лютого 2007, 15:21
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.io.File;
import java.util.List;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.AnalyzedUnitRef;
import ua.gradsoft.javachecker.models.AnalyzedUnitType;
import ua.gradsoft.javachecker.models.JavaCompilationUnitModel;
import ua.gradsoft.javachecker.models.JavaPackageModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author Ruslan Shevchenko
 */
public class Java5SrcTest extends TestCase
{
    
    static {
        try {
        String javaHome=System.getenv("JAVA_HOME");
        if (javaHome!=null) {
            String javaSrcHome = javaHome+File.separator+"src";
            File f = new File(javaSrcHome);
            if (f.exists()) {
                javaSrcHome_=javaSrcHome;
                disabled_=false;
            }else{
                disabled_=true;
            }
        }
        }catch(Exception ex){
            ex.printStackTrace();
            disabled_=true;
        }        
    }
    
    public void testAllJava5Models() throws Exception
    {
      JavaCheckerFacade.init();      
      if (!disabled_) {
          JavaCheckerFacade.addInputDirectory(javaSrcHome_);
          readAndGetModelForSources();
          assertTrue(nLoadedFiles_>0);
      }  
    }
    
    private void readAndGetModelForSources() throws ConfigException, TermWareException
   {
         String dirName = javaSrcHome_;
         File f = new File(dirName);         
         if (!f.exists()) {
             throw new ConfigException(dirName + " does not exists");
         }
         if (!f.isDirectory()) {
             throw new ConfigException(dirName + " is not a directory");
         }
         readAndGetModelForSources(dirName,"",f);
 }
 
 /**
  *read and check sources.
  *If <code> d </code> is directory, than recursive read directory, else -- read and check java file.
  *@param sourceDir  -- one from source dirs, which we read now.
  *@param relativeDir -- package structure under sourcedir. entries are separated by '.'
  *@param File d -- current file or directory. 
  */
 private void readAndGetModelForSources(String sourceDir, String packageDir, File d) throws TermWareException
 {
     File[] files=d.listFiles();
     for(int i=0; i<files.length; ++i) {   
         if (files[i].isDirectory()) {
             String nextPackageDir = (packageDir.length()==0 ? files[i].getName() : packageDir+"."+files[i].getName());           
             readAndGetModelForSources(sourceDir, nextPackageDir, files[i]);
         }else if(files[i].getName().endsWith(".java")) {
             readAndGetModelSourceFile(sourceDir, packageDir, files[i]);
         }
     }
 }
 
 private void readAndGetModelSourceFile(String sourceDir, String packageDirName, File f) throws TermWareException
 {
     if (!qOption_) {
       System.out.println("reading file "+f.getAbsolutePath());
     }     
     Term source=null;
     try {       
       source = JUtils.readSourceFile(f);
     }catch(TermWareException ex){
       System.err.println(ex.getMessage());
       System.err.println("skipping");
       return;
     }
     if (dump_) {
         System.out.println();
         //source.print(System.out);
         System.out.println(TermHelper.termToPrettyString(source));
         System.out.println();
     }
     if (source.getArity()==0) {
      // empty file: possible 
      // skip
     }else{
       String packageSrcName = JUtils.getCompilationUnitPackageName(source);
     
          
       //if (!packageSrcName.equals(packageDirName)) {
       //  facts_.violationDiscovered("package","package name does not math directory structure",source);
       //}
     
       
       JavaPackageModel pm = JavaCheckerFacade.getPackagesStore().findOrAddPackage(packageSrcName);
       
       JavaCompilationUnitModel cu = new JavaCompilationUnitModel(f.getAbsolutePath());
       cu.setPackageModel(pm);
       AnalyzedUnitRef ref = new AnalyzedUnitRef(AnalyzedUnitType.SOURCE,sourceDir+"/"+packageDirName.replace('.','/'),f.getName(),cu);
       pm.addCompilationUnit(source,cu,ref);   
          
       List<JavaTypeModel> typeModels = cu.getTypeModels();
       for(JavaTypeModel tm:typeModels) {
              Term modelTerm = tm.getModelTerm();
       }
     }
          
     ++nLoadedFiles_;          
 }
 
    
    static boolean disabled_=false;
    static String  javaSrcHome_;
    
    static boolean qOption_=false;
    static boolean dump_=false;
    static int     nLoadedFiles_=0;
}
