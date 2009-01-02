/*
 * JavaSrcTest.java
 *
 */

package ua.gradsoft.parsers.java5.test;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import junit.framework.TestCase;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *This test is enabled when we have sources in Java directory.
 *
 * @author RSSH
 */
public class JavaSrcTest extends TestCase
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
        //disabled_=true;
    }
   
    protected void setUp()
    {                                                  
        TermWare.getInstance().addParserFactory("Java",new JavaParserFactory());
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


    public void testComJava5Models() throws Exception
    {         
      int prevLoadedFiles = nLoadedFiles_;
      if (!disabled_) {        
          String dirName = javaSrcHome_ + File.separator +"com";
          File f = new File(dirName);         
          readAndGetModelForSources(javaSrcHome_,"com",f);
          assertTrue(nLoadedFiles_>prevLoadedFiles);
      }  
    }


    
    
    public void testJavaJava5Model() throws Exception
    {
        _testDirJava5Models("java");
    }
    
    public void _testDirJava5Models(String pkg) throws Exception
    {
      int prevLoadedFiles = nLoadedFiles_;
      if (!disabled_) {
          String dirName = javaSrcHome_ + File.separator +pkg;
          File f = new File(dirName);         
          readAndGetModelForSources(javaSrcHome_,pkg,f);
          assertTrue(nLoadedFiles_>prevLoadedFiles);
      }  
    }


    public void _testDirJava5Models(String pkg1, String pkg2) throws Exception
    {
      int prevLoadedFiles = nLoadedFiles_;
      if (!disabled_) {
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

    
    private void readAndGetModelForSources() throws Exception
   {
         String dirName = javaSrcHome_;
         File f = new File(dirName);         
         if (!f.exists()) {
             throw new AssertException(dirName + " does not exists");
         }
         if (!f.isDirectory()) {
             throw new AssertException(dirName + " is not a directory");
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
 private void readAndGetModelForSources(String sourceDir, String packageDir, File d) throws Exception
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
 
 private void readAndGetModelSourceFile(String sourceDir, String packageDirName, File f) throws Exception
 {
     String fname=f.getAbsolutePath();
     if (!qOption_) {
       System.out.println("reading file "+fname);
     }     
     Term source=null;
     try {      
      Reader reader = new FileReader(fname);   
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      reader.close();
      //source.println(System.out);           
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
          
     ++nLoadedFiles_;          
     System.err.print("."+nLoadedFiles_+":");
    // if ((nLoadedFiles_ % 10)==0) {
        System.err.println(fname);
     //}
 }
 
    
    static boolean disabled_;
    static String  javaSrcHome_;
    
    static boolean qOption_=false;
    static boolean dump_=false;
    static int     nLoadedFiles_=0;
    
}
