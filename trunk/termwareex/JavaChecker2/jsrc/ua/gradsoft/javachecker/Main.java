package ua.gradsoft.javachecker;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;
import ua.gradsoft.javachecker.checkers.Checkers;
import ua.gradsoft.javachecker.models.AnalyzedUnitRef;
import ua.gradsoft.javachecker.models.AnalyzedUnitType;
import ua.gradsoft.javachecker.models.JavaCompilationUnitModel;
import ua.gradsoft.javachecker.models.JavaPackageModel;
import ua.gradsoft.termware.IEnv;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.envs.SystemEnv;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.printers.java5.JavaPrinterFactory;
import ua.gradsoft.termware.exceptions.ExternalException;




public class Main
{

 public static void main(String[] args)
 {   
   Main app = new Main();
   try {
     app.init(args);
   }catch(ConfigException ex){
       System.err.println("Error during parsing options:"+ex.getMessage());
       System.err.println("try JavaChecker --help  for help");
       return;
   }
   if (helpOnly_) {
       help();
       return;
   }
   try {
     app.process();
   }catch(ProcessingException ex){
      System.err.println(ex.getMessage());
      if (debug_) {
          ex.printStackTrace();
      }
   }
 }
 
 
 
 public void process() throws ProcessingException
 {      
   
   try {
     readAndCheckSources();
   }catch(TermWareException ex){
       if (debug_) {
         ex.printStackTrace();
       }
       throw new ProcessingException("Error during reading sources:"+ex.getMessage(),ex);
   }catch(ConfigException ex){
       if (debug_) {
           ex.printStackTrace();
       }
       throw new ProcessingException("Error during reading sources:"+ex.getMessage(),ex);
   }

   try {
      report();
   }catch(AssertException ex){
       throw new ProcessingException(ex.getMessage(),ex);
   }
 }


 /**
  * initialize. This must be called before process.
  */
 public void init(String[] args) throws ConfigException
 {
  env_ = new SystemEnv();
  try {
    TermWare.getInstance().init(args);  
  }catch(TermWareException ex){
     throw new ConfigException("Error during TermWare initialization",ex);
  }   

   TermWare.getInstance().addParserFactory("Java",new JavaParserFactory()); 
   TermWare.getInstance().addPrinterFactory("Java",new JavaPrinterFactory());
   getPreferences();
 
   if (home_==null) {
     home_=System.getProperty("javachecker.home");
     if (home_==null) {
       home_=System.getenv("JAVACHECKER_HOME");
       if (home_==null) {           
           throw new ConfigException("JAVACHECKER_HOME  is not set");
       }
     }
   }
   try {
     TermWare.getInstance().getTermLoader().addSearchPath(home_+File.separator+"systems");
   }catch(ExternalException ex){
       throw new ConfigException("error during adding search path",ex.getException());
   }
       
  try {
    facts_ = new JavaFacts(env_,prefs_);
  }catch(TermWareException ex){
    throw new ConfigException("Error during facts initialization",ex);  
  }
  parseArgs(args);    

  initTmpDirBase();
  
   try {
     loadCheckSystems(args);
   }catch(TermWareException ex){
       if (debug_) {
         ex.printStackTrace();
       }
       throw new ConfigException("Can't init check systems:"+ex.getMessage(),ex);
   }
  
  nProcessedFiles_=0;
  nLoadedFiles_=0;
 }
 
 // TODO
 private void parseArgs(String[] args) throws ConfigException
 {
   ArrayList<String> jars = new ArrayList<String>();
   for(int i=0; i<args.length; ++i) {
      if (args[i].equals("--prefs")) {
           if (args.length==i+1) {
               throw new ConfigException("--prefs option require argument");
           }
           prefsFname_= args[i+1];
           ++i;
      }else if(args[i].equals("--debug")) {
           debug_=true;
      }else if(args[i].equals("--q")) {
           qOption_=true;
      }else if(args[i].equals("--dump")) {
          dump_=true;
      }else if(args[i].equals("--output")) {
          if (args.length==i+1) {
              throw new ConfigException("--output option require argument");
          }
          outputFname_=args[i+1];
          ++i;
      }else if(args[i].equals("--help")) {
          helpOnly_=true;
      }else if(args[i].equals("--showFiles")) {
          showFiles_=true;
      }else if(args[i].equals("-I")) {
          // skip this option and next - they will be passed to TermWareSingleton.init
          if (args.length==i+1) {
              throw new ConfigException("-I option require argument");
          }
          ++i;
      }else if(args[i].equals("--include")){
          if (args.length==i+1) {
              throw new ConfigException("--include option require argument");
          }
          getFacts().getPackagesStore().getSourceDirs().add(args[i+1]);    
          ++i;
      }else if(args[i].equals("--includejar")) {
          if (args.length==i+1) {
              throw new ConfigException("--includejar option require argument");
          }
          String jarName=args[i+1];        
          jars.add(jarName);
          ++i;         
      }else if(args[i].equals("--enable")){
          if (args.length==i+1) {
              throw new ConfigException("--enable option require argument");
          }
          String checkName = args[i+1];
          ++i;
          getFacts().setCheckEnabled(checkName,true);
      }else if (args[i].equals("--disable")){
          if (args.length==i+1) {
              throw new ConfigException("--disable option require argument");
          }
          String checkName = args[i+1];
          ++i;
          getFacts().setCheckEnabled(checkName,false);          
      }else if (args[i].equals("--config")){
          if (args.length==i+1 || args.length==i+2) {
              throw new ConfigException("--config option require two arguments");              
          }
          String configName=args[i+1];
          String configValue=args[i+2];
          i+=2;
          getFacts().setConfigValue(configName,configValue);
      }else if (args[i].equals("--output-format")) {
          if (args.length==i+1) {
              throw new ConfigException("--disable option require argument");
          }
          String formatName = args[i+1];
          ++i;
          boolean found=false;
          for(ReportFormat rf: ReportFormat.values()) {
              if (rf.getName().equals(formatName)) {
                  reportFormat_=rf;
                  found=true;
                  break;
              }
          }          
          if (!found) {
              throw new ConfigException("Unknown output format:"+formatName);
          }
      }else{
          getFacts().getPackagesStore().getSourceDirs().add(args[i]);    
          getFacts().getPackagesStore().getSourceDirsToProcess().add(args[i]);
      }
   }
   getFacts().getPackagesStore().setJars(jars);
 }
 
 private void getPreferences() throws ConfigException
 {
  if (prefsFname_!=null) {
    FileInputStream prefsStream;
    try {
        prefsStream=new FileInputStream(prefsFname_);
    }catch(IOException ex){
        throw new ConfigException("Can't open file '"+prefsFname_+"' for reading:"+ex.getMessage());
    }
    try {
        Preferences.importPreferences(prefsStream);
    }catch(IOException ex){
        throw new ConfigException("Error during reading '"+prefsFname_+"'"+ex.getMessage());
    }catch(InvalidPreferencesFormatException ex){
        throw new ConfigException("Can't parse +'"+prefsFname_+"':"+ex.getMessage());
    }
  }
  prefs_=Preferences.userNodeForPackage(this.getClass());
  String[] keys;
  try {
    keys=prefs_.keys();
  }catch(BackingStoreException ex){
      throw new ConfigException("BackingStoreException during parsing prefernces:"+ex.getMessage());
  }
  //if (keys.length==0) {
  //    System.err.println("Warning: user preferences is not set. To set preferences, use -prefs option");
  //}
 }
 
 private void loadCheckSystems(String[] args) throws TermWareException, ConfigException
 {  
   checkers_ = new Checkers(this);
   checkers_.configure();
 }
 
 
 private static void help() 
 {
   System.err.println("JavaChecker: TermWare java static analysis tool.");
   System.err.println("(C) Ruslan Shevchenko, Grad-Soft Ltd, 2004 - 2006");
   System.err.println("http://www.gradsoft.ua");
   System.err.println();
   System.err.println("Usage: JavaChecker [options] directory");  
   System.err.println("where options must be one from:");
   System.err.println("  --prefs fname                  read configuration from preferences file fname.");   
   System.err.println("  --showFiles                    during check, print names of analyzed files.");   
   System.err.println("  --help                         output this help message.");     
   System.err.println("  --output fname                 write report to file fname");
   System.err.println("  --output-format (text|html)    format report in text or html");
   System.err.println("  --enable check-name            enable checking for check-name");
   System.err.println("  --disable check-name           disable checking for check-name");
   System.err.println("  --config  name value           set configuration item of name to value");
   System.err.println("  --include dir                  set directory, where situated source files, from which processed sources are depend");
   System.err.println("  --includejar fname             set jar, where situated classes, from which processed sources are depend  ");
   System.err.println("  --debug                        put to stderr a lot of debug output");
   System.err.println("  --dump                         dump to stdout AST of parsed files");
   System.err.println("  --q                            minimize output to stdout");
   System.err.println();
   System.err.println("note, that JavaChecker rules are configured throught preferences.");
   System.err.println("example of preferences file can be found in 'etc' subdirectory of distribution.");
 }
 
 private void readAndCheckSources() throws ConfigException, TermWareException
 {
     if (getFacts().getPackagesStore().getSourceDirs().isEmpty()) {
         throw new ConfigException("no directories to read");
     }
     for(String dirName: getFacts().getPackagesStore().getSourceDirsToProcess()){        
         File f = new File(dirName);         
         if (!f.exists()) {
             throw new ConfigException(dirName + " does not exists");
         }
         if (!f.isDirectory()) {
             throw new ConfigException(dirName + " is not a directory");
         }
         readAndCheckSources(dirName,"",f);
     }
 }
 
 /**
  *read and check sources.
  *If <code> d </code> is directory, than recursive read directory, else -- read and check java file.
  *@param sourceDir  -- one from source dirs, which we read now.
  *@param relativeDir -- package structure under sourcedir. entries are separated by '.'
  *@param File d -- current file or directory. 
  */
 private void readAndCheckSources(String sourceDir, String packageDir, File d) throws TermWareException
 {
     File[] files=d.listFiles();
     for(int i=0; i<files.length; ++i) {   
         if (files[i].isDirectory()) {
             String nextPackageDir = (packageDir.length()==0 ? files[i].getName() : packageDir+"."+files[i].getName());           
             readAndCheckSources(sourceDir, nextPackageDir, files[i]);
         }else if(files[i].getName().endsWith(".java")) {
             readAndCheckSourceFile(sourceDir, packageDir, files[i]);
         }
     }
 }
 
 private void readAndCheckSourceFile(String sourceDir, String packageDirName, File f) throws TermWareException
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
       facts_.violationDiscovered("EmptyFile","Empty java file:"+f.getAbsolutePath(),source);  
     }else{
       String packageSrcName = JUtils.getCompilationUnitPackageName(source);
     
          
       if (!packageSrcName.equals(packageDirName)) {
         facts_.violationDiscovered("package","package name does not math directory structure",source);
       }
     
       JavaPackageModel pm=facts_.getPackagesStore().findOrAddPackage(packageSrcName);
       JavaCompilationUnitModel cu = new JavaCompilationUnitModel(f.getAbsolutePath());
       cu.setPackageModel(pm);
       AnalyzedUnitRef ref = new AnalyzedUnitRef(AnalyzedUnitType.SOURCE,sourceDir+"/"+packageDirName.replace('.','/'),f.getName(),cu);
       pm.addCompilationUnit(source,cu,ref);   
       
       try {
          checkSource(source,cu);              
       }catch(TermWareException ex){
           System.err.println("error during checking "+f.getAbsolutePath()+":"+ex.getMessage());
           if (ex instanceof SourceCodeLocation) {
               FileAndLine fileAndLine=((SourceCodeLocation)ex).getFileAndLine();
               System.err.println("at "+fileAndLine.getFname()+","+fileAndLine.getLine());
           }
           System.err.println("skipping");
           if (true/*isVerbose()*/) {
               ex.printStackTrace();
           }
       }
     }
          
     ++nLoadedFiles_;          
 }
 
 private void checkSource(Term source, JavaCompilationUnitModel cu) throws TermWareException
 {   
   checkers_.checkCompilationUnitAST(source);  
   checkers_.checkTypes(cu);  
   ++nProcessedFiles_;
 }
 
 
 
 private void report() throws AssertException
 {   
   if (outputFname_!=null) {
       File f=new File(outputFname_);
       PrintStream fout;
       try {
         fout=new PrintStream(new BufferedOutputStream(new FileOutputStream(f)));
       }catch(FileNotFoundException ex){
          throw new AssertException("Can't open file "+outputFname_+" for writing:"+ex.getMessage());
       }
       try {           
         facts_.report(fout, reportFormat_);
       }finally{
           fout.close();
       }
   }else{
      facts_.report(System.out, reportFormat_);  
   }
 }

 public static boolean isQOption()
 {
   return qOption_;
 }
 
 public static void  setQOption(boolean v)
 { qOption_=v; }
 
 
 public static boolean isShowFiles()
 {
   return showFiles_;   
 }
 
 public static void  setShowFiles(boolean showFiles)
 { 
   showFiles_=showFiles;
 }
 
 public static boolean  isDump()
 {
     return dump_;
 }
 
 public static void     setDump(boolean dump)
 {
     dump_=dump;
 }
 
 
 public static ReportFormat  getReportFormat()
 {  return reportFormat_; }
 
 public static void  setReportFormat(ReportFormat reportFormat)
 { reportFormat_=reportFormat; }
 
 
 public static boolean isNoClean()
 {
   return noClean_;  
 }
 
 public static void setNoClean(boolean noClean)
 {
   noClean_=noClean;  
 }
 
 /**
  *add directory.
  *@param directory -- directory to add.
  *@param process -- if true, files in this directory will be processed by checkers, otherwise only loaded on demand.
  */
 public static void  addInputDirectory(String directory, boolean process)
 {
  JavaFacts facts=getFacts();
  PackagesStore packagesStore=facts.getPackagesStore();
  List<String> sourceDirs=packagesStore.getSourceDirs();
  sourceDirs.add(directory);
  if (process) {
      List<String> sourceDirsToProcess = packagesStore.getSourceDirsToProcess();
      sourceDirsToProcess.add(directory);
  }
 }
 
 
 
 public static String  getHome()
 { return home_; }
 
 public static void  setHome(String home)
 { home_=home; }
 
 public static int  getNProcessedFiles()
 { 
   return nProcessedFiles_;
 }
 
 public static JavaFacts getFacts()
 { return facts_; }
 
 public static String getOutputFname()
 { return outputFname_; }
 
 public static void setOutputFname(String outputFname)
 {
   outputFname_=outputFname;
 }
 
 public static String getPrefsFname()
 {
   return prefsFname_;
 }
 
 public static void setPrefsFname(String prefsFname)
 {
   prefsFname_=prefsFname;
 }  
 
 public  static String getTmpDir()
 {
     if (tmpDirName_==null) {         
         tmpDirName_=tmpDirBase_+File.separator+"jc"+System.currentTimeMillis();
     }
     return tmpDirName_;
 }
 
 public  static boolean isInShutdown()
 { 
     return inShutdown_;
 }
 
 private  static  void initTmpDirBase() throws ConfigException
 {
   if (tmpDirBase_==null) {  
     String candidate = null;
     if (candidate==null) {
       candidate = System.getenv("TEMP");
       if (candidate!=null) {
         File f = new File(candidate);
         if (!(f.exists() && f.isDirectory() && f.canWrite())) {
             candidate=null;
         }       
       }
     }
     if (candidate==null) {
       candidate = System.getenv("TMP");
       if (candidate!=null) {
         File f = new File(candidate);
         if (!(f.exists() && f.isDirectory() && f.canWrite())) {
             candidate=null;
         }
       }
     }
     if (candidate==null) {
         candidate = File.separator+"tmp";
         File f = new File(candidate);
         if (!(f.exists() && f.isDirectory() && f.canWrite())) {
             candidate=null;
         }
     }
     if (candidate==null) {
         throw new ConfigException("can't find temp directory");
     }
     tmpDirBase_=candidate;
   }
 }
 
 
 
 private static IEnv         env_          = null;

 private static HashSet      sourcesSet_    = null;


 private static String       prefsFname_  = null;
 private static Preferences  prefs_       = null;
 
 private static String       tmpDirName_  = null;
 private static String       tmpDirBase_  = null;
 
 private static boolean      debug_ = false;
 private static boolean      showFiles_ = false;
 private static boolean      helpOnly_ = false;
 private static boolean      qOption_ = false;
 private static boolean      dump_=false;
 private static boolean      noClean_=false;
 private static ReportFormat reportFormat_=ReportFormat.TEXT;
 
 private static int           nLoadedFiles_ = 0;
 private static int           nProcessedFiles_ = 0;
 
 private static String              outputFname_=null;
 private static String              home_=null;
 
 private static JavaFacts    facts_ = null;
 //private static TermSystem   mainSystem_ = null;
 private static Checkers      checkers_ = null;
 
 private static boolean      inShutdown_ = false;
 
}