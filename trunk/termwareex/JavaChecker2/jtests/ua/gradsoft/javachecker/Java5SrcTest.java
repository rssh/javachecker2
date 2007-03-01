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
import ua.gradsoft.termware.exceptions.AssertException;

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
        disabled_=true;
    }
   
    /*
    public void testAllJava5Models() throws Exception
    {
      JavaCheckerFacade.init();      
      if (!disabled_ && false) {
          JavaCheckerFacade.addInputDirectory(javaSrcHome_);
          readAndGetModelForSources();
          assertTrue(nLoadedFiles_>0);
      }  
    }
     */

/*
    public void testComJava5Models() throws Exception
    {
      JavaCheckerFacade.init();      
      int prevLoadedFiles = nLoadedFiles_;
      if (!disabled_) {
          JavaCheckerFacade.addInputDirectory(javaSrcHome_);
          String dirName = javaSrcHome_ + File.separator +"com";
          File f = new File(dirName);         
          readAndGetModelForSources(javaSrcHome_,"com",f);
          assertTrue(nLoadedFiles_>prevLoadedFiles);
      }  
    }
*/

      
    public void testJavaJava5Model() throws Exception
    {
        _testDirJava5Models("java");
    }

    
    public void _testDirJava5Models(String pkg) throws Exception
    {
      JavaCheckerFacade.init();      
      int prevLoadedFiles = nLoadedFiles_;
      if (!disabled_) {
          JavaCheckerFacade.addInputDirectory(javaSrcHome_);
          String dirName = javaSrcHome_ + File.separator +pkg;
          File f = new File(dirName);         
          readAndGetModelForSources(javaSrcHome_,pkg,f);
          assertTrue(nLoadedFiles_>prevLoadedFiles);
      }  
    }


    public void _testDirJava5Models(String pkg1, String pkg2) throws Exception
    {
      JavaCheckerFacade.init();      
      int prevLoadedFiles = nLoadedFiles_;
      if (!disabled_) {
          JavaCheckerFacade.addInputDirectory(javaSrcHome_);
          String dirName = javaSrcHome_ + File.separator +pkg1 + File.separator + pkg2;
          File f = new File(dirName);         
          readAndGetModelForSources(javaSrcHome_,pkg1+"."+pkg2,f);          
          assertTrue(nLoadedFiles_>prevLoadedFiles);
      }  
    }
    
    
/*
    public void testJavaxJava5Models() throws Exception
    {
      JavaCheckerFacade.init();      
      int prevLoadedFiles = nLoadedFiles_;
      if (!disabled_) {
          JavaCheckerFacade.addInputDirectory(javaSrcHome_);
          String dirName = javaSrcHome_ + File.separator +"javax";
          File f = new File(dirName);         
          readAndGetModelForSources(javaSrcHome_,"javax",f);
          assertTrue(nLoadedFiles_>prevLoadedFiles);
      }  
    }
*/


/*
    public void testOrgJava5Models() throws Exception
    {
      JavaCheckerFacade.init();      
      int prevLoadedFiles = nLoadedFiles_;
      if (!disabled_) {
          JavaCheckerFacade.addInputDirectory(javaSrcHome_);
          String dirName = javaSrcHome_ + File.separator +"org";
          File f = new File(dirName);         
          readAndGetModelForSources(javaSrcHome_,"org",f);
          assertTrue(nLoadedFiles_>prevLoadedFiles);
      }  
    }


    public void testSunwJava5Models() throws Exception
    {
      JavaCheckerFacade.init();      
      int prevLoadedFiles = nLoadedFiles_;
      if (!disabled_) {
          JavaCheckerFacade.addInputDirectory(javaSrcHome_);
          String dirName = javaSrcHome_ + File.separator +"sunw";
          File f = new File(dirName);         
          readAndGetModelForSources(javaSrcHome_,"sunw",f);
          assertTrue(nLoadedFiles_>prevLoadedFiles);
      }  
    }
*/

    
    private void readAndGetModelForSources() throws ConfigException, TermWareException, EntityNotFoundException
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
 private void readAndGetModelForSources(String sourceDir, String packageDir, File d) throws TermWareException, EntityNotFoundException
 {     
     System.err.println("readAndGetModelForSources("+sourceDir+","+packageDir+")");
     File[] files=d.listFiles();
     for(int i=0; i<files.length; ++i) {   
         if (files[i].isDirectory()) {
             String nextPackageDir = (packageDir.length()==0 ? files[i].getName() : packageDir+"."+files[i].getName());           
             readAndGetModelForSources(sourceDir, nextPackageDir, files[i]);       
         }else if(files[i].getName().endsWith(".java")) {
             readAndGetModelSourceFile(sourceDir, packageDir, files[i]);
         }
     }
     System.err.println();
 }
 
 private void readAndGetModelSourceFile(String sourceDir, String packageDirName, File f) throws TermWareException, EntityNotFoundException
 {
     String fname=f.getAbsolutePath();
     if (!qOption_) {
       System.out.println("reading file "+fname);
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
       System.out.print("getting package name:");  
       String packageSrcName = JUtils.getCompilationUnitPackageName(source);
       System.out.println(packageSrcName);  
          
       //if (!packageSrcName.equals(packageDirName)) {
       //  facts_.violationDiscovered("package","package name does not math directory structure",source);
       //}
     
       
       JavaPackageModel pm = JavaCheckerFacade.getPackagesStore().findOrAddPackage(packageSrcName);
       
       JavaCompilationUnitModel cu = new JavaCompilationUnitModel(f.getAbsolutePath());
       cu.setPackageModel(pm);
       AnalyzedUnitRef ref = new AnalyzedUnitRef(AnalyzedUnitType.SOURCE,sourceDir+"/"+packageDirName.replace('.','/'),f.getName(),cu);
       pm.addCompilationUnit(source,cu,ref);   
       
       System.out.println("start check typeModels");
       
       List<JavaTypeModel> typeModels = cu.getTypeModels();
       for(JavaTypeModel tm:typeModels) {
           try {
              System.out.println("get model term for "+tm.getFullName()); 
              Term modelTerm = tm.getModelTerm();
           }catch(AssertException ex){
               if (ex.getMessage().contains("ApproveSelectionAction")) {
                   // Yess, SynchFileChooserUI is not added in JDK5 sources.
                   // i. e. this is real bug in Java5 source distribution.
                   continue;
               }else{
                   System.err.println("error during getting model in file:"+fname);
                   if (ex instanceof SourceCodeLocation) {
                       FileAndLine fl = ((SourceCodeLocation)ex).getFileAndLine();
                       System.err.println("file "+fl.getFname()+", line "+fl.getLine());
                   }
                   throw ex;
               }
           }catch(EntityNotFoundException ex){
               if (ex.getMessage().contains("com.sun.org.omg")) {
                   //Yes, this is bug in JDK5 sources,
                   // com.sun.org.omg subsystem are not included
                   continue;
               }else{
                   System.err.println("error during getting model in file:"+fname);
                   if (ex instanceof SourceCodeLocation) {
                       FileAndLine fl = ((SourceCodeLocation)ex).getFileAndLine();
                       System.err.println("file "+fl.getFname()+", line "+fl.getLine());
                   }                   
                   throw ex;                   
               }
           }
       }
     }
          
     ++nLoadedFiles_;          
     System.err.print("."+nLoadedFiles_);
     if ((nLoadedFiles_ % 10)==0) {
        System.err.println();
     }
 }
 
    
    static boolean disabled_;
    static String  javaSrcHome_;
    
    static boolean qOption_=true;
    static boolean dump_=false;
    static int     nLoadedFiles_=0;
}
