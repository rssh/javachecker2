package ua.kiev.gradsoft.JavaChecker;


import java.io.*;
import java.util.*;
import java.util.prefs.*;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.envs.SystemEnv;
import ua.kiev.gradsoft.TermWare.exceptions.*;
import ua.kiev.gradsoft.TermWare.set.*;


public class Main
{

 public static void main(String[] args)
 {   
   Main app = new Main();
   try {
     app.parseArgs(args);
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
     app.process(args);
   }catch(ProcessingException ex){
      System.err.println(ex.getMessage());
      if (debug_) {
          ex.printStackTrace();
      }
   }
 }
 
 public void process(String[] args) throws ProcessingException
 {
   try {
     getPreferences();
   }catch(ConfigException ex){
       throw new ProcessingException("Error during setting preferences:"+ex.getMessage());
   }
   try {
     initCheckSystems(args);
   }catch(TermWareException ex){
       if (debug_) {
         ex.printStackTrace();
       }
       throw new ProcessingException("Can't init check systems:"+ex.getMessage(),ex);
   }
   try {
     readSources();
   }catch(TermWareException ex){
       if (debug_) {
         ex.printStackTrace();
       }
       throw new ProcessingException("Error during reading sources:"+ex.getMessage(),ex);
   }catch(ConfigException ex){
       System.err.println("Error during reading sources:"+ex.getMessage());
       return;
   }
   try {
     checkSources(); 
   }catch(TermWareException ex){
       ex.printStackTrace();
       throw new ProcessingException("error during processing:"+ex.getMessage(),ex);
   }catch(Exception ex){
       ex.printStackTrace();
       throw new ProcessingException("error during processing:"+ex.getMessage(),ex);
   }
   try {
      report();
   }catch(AssertException ex){
       throw new ProcessingException(ex.getMessage());
   }
 }


 // TODO
 private void parseArgs(String[] args) throws ConfigException
 {
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
      }else if(args[i].equals("--maxFiles")) {
          if (args.length==i+1) {
              throw new ConfigException("--maxFiles option require argument");
          }
          maxFilesOption_=true;
          try {
            maxNLoadedFiles_=Integer.parseInt(args[i+1]);
          }catch(NumberFormatException ex){
              throw new ConfigException("--maxFiles argument must be number");
          }          
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
      }else{
          if (inputDirs_==null) inputDirs_=new HashSet();
          inputDirs_.add(args[i]);
      }
   }
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
  if (keys.length==0) {
      System.err.println("Warning: user preferences is not set. To set preferences, use -prefs option");
  }
  if (!maxFilesOption_) {
     maxNLoadedFiles_=prefs_.getInt("MaxLoadedFiles", 400);
  }
 }
 
 private void initCheckSystems(String[] args) throws TermWareException
 {
  env_ = new SystemEnv();
  TermWareSingleton.init(env_,args);
  facts_ = new JavaFacts(env_,prefs_);
  ITermRewritingStrategy strategy=TermWareSingleton.createJavaStrategy(
             "ua.kiev.gradsoft.TermWare.strategies.FirstTopStrategy"
                                                                  );
  mainSystem_ = new ITermSystem(strategy,facts_,env_);
  if (debug_) {
      mainSystem_.setDebugMode(true);
  }
  ITermSystem sys=TermWareSingleton.getRoot().resolveSystem("sys");
  
  
//  sys.setDebugMode(true);
//  sys.setDebugEntity("All");
  btPatternChecker_=new BTPatternChecker(facts_);
  if (facts_.isCheckEnabled("OverloadedEquals")) {
      overloadedEqualsChecker_=new OverloadedEqualsChecker(facts_);
  }
  if (facts_.isCheckEnabled("Hiding")) {
      hidingChecker_=new HidingChecker();
  }
  if (facts_.isCheckEnabled("SynchronizeViolations")) {
      synchronizeViolationChecker_=new SynchronizeViolationChecker(facts_);
  }
 }
 
 private static void help() 
 {
   System.err.println("JavaChecker: TermWare java static analysis tool.");
   System.err.println("(C) Ruslan Shevchenko, Grad-Soft Ltd, 2004");
   System.err.println("http://www.gradsoft.ua");
   System.err.println();
   System.err.println("Usage: JavaChecker [options] directory");  
   System.err.println("where options must be one from:");
   System.err.println("  --prefs fname              read configuration from preferences file fname.");
   System.err.println("  --showFiles                during check, print names of analyzed files.");
   System.err.println("  --maxFiles N               set maximum number of loaded files at one time to N");
   System.err.println("  --help                     output this help message.");
   System.err.println("  --q                        minimize output to stdout");
   System.err.println("  --output fname             write report to file fname");
   System.err.println("  --debug                    put to stderr a lot of debug output");
   System.err.println("  --dump                     dump to stdout AST of parsed files");
   System.err.println();
   System.err.println("note, that JavaChecker rules are configured throught preferences.");
   System.err.println("example of preferences file can be found in 'etc' subdirectory of distribution.");
 }
 
 private void readSources() throws ConfigException, TermWareException
 {
     if (inputDirs_==null) {
         throw new ConfigException("no directories to read");
     }
     if (!qOption_) {
       System.out.println("reading sources:");
     }
     Iterator it=inputDirs_.iterator();
     while(it.hasNext()) {
         String dirName=(String)it.next();
         File f = new File(dirName);
         if (!f.exists()) {
             throw new ConfigException(dirName + " does not exists");
         }
         if (!f.isDirectory()) {
             throw new ConfigException(dirName + " is not a directory");
         }
         readSources(f);
     }
 }
 
 private void readSources(File d) throws TermWareException
 {
     File[] files=d.listFiles();
     for(int i=0; i<files.length; ++i) {
         if (files[i].isDirectory()) {
             readSources(files[i]);
         }else if(files[i].getName().endsWith(".java")) {
             readSourceFile(files[i]);
         }
     }
 }
 
 private void readSourceFile(File f) throws TermWareException
 {
     if (!qOption_) {
       System.out.println("reading file "+f.getAbsolutePath());
     }
     ITerm source=null;
     try {
       source = TermWareSingleton.loadFile(mainSystem_,f.getAbsolutePath(),TermWareSingleton.getParserFactory("Java"),ITermFactory.createNil());
     }catch(TermWareException ex){
       System.err.println(ex.getMessage());
       System.err.println("skipping");
       return;
     }
     if (dump_) {
         System.out.println();
         source.print(System.out);
         System.out.println();
     }
     facts_.addCompilationUnit(source);
     ++nLoadedFiles_;
     if (nLoadedFiles_ >= maxNLoadedFiles_) {
         checkSources();
     }
 }
 
 private void checkSources() throws TermWareException
 {
     HashSet compilationUnits=facts_.getCompilationUnits();
     if (!qOption_) {
        System.out.println("check package models.");
     }
     Map packageModels=facts_.getPackageModels();
     Iterator it=packageModels.entrySet().iterator();
     while(it.hasNext()) {
         Map.Entry me = (Map.Entry)it.next();
         JavaPackageModel pm=(JavaPackageModel)me.getValue();
         pm.check();
     }

     if (overloadedEqualsChecker_!=null) {
         if (!qOption_) {
            System.out.println("check for correspondance of overloading equals and hashCode");
         }
         it=compilationUnits.iterator();
         while(it.hasNext()) {
             ITerm t=(ITerm)it.next();
             overloadedEqualsChecker_.checkCompilationUnit(t.term_clone());
         }
     }
     
     nProcessedFiles_+=nLoadedFiles_;
     facts_.unloadSources();   
     nLoadedFiles_=0;
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
         facts_.report(fout);
       }finally{
           fout.close();
       }
   }else{
      facts_.report(System.out);  
   }
 }

 public static boolean isQOption()
 {
   return qOption_;
 }
 
 public static void  setQOption(boolean v)
 { qOption_=v; }
 
 public static boolean showFiles()
 {
   return showFiles_;   
 }
 
 public static void  setShowFiles(boolean showFiles)
 { 
   showFiles_=showFiles;
 }
 
 public static void  addInputDirectory(String directory)
 {
  if (inputDirs_==null) inputDirs_=new HashSet();
  inputDirs_.add(directory);
 }
 
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
 
 static BTPatternChecker  getBTPatternChecker()
 { return btPatternChecker_; }
 
 static SynchronizeViolationChecker getSynchronizeViolationChecker()
 { return synchronizeViolationChecker_; }
 
 static HidingChecker  getHidingChecker()
 { return hidingChecker_; }
 
 
 private static IEnv         env_          = null;

 private static HashSet      sourcesSet_    = null;


 private static String       prefsFname_  = null;
 private static Preferences  prefs_       = null;
 
 private static BTPatternChecker         btPatternChecker_ = null;
 private static OverloadedEqualsChecker  overloadedEqualsChecker_ = null;
 private static SynchronizeViolationChecker synchronizeViolationChecker_=null;
 private static HidingChecker               hidingChecker_=null;
 
 /**
  * set of directories to check.
  */
 private static HashSet      inputDirs_  = null;

 private static boolean      debug_ = false;
 private static boolean      showFiles_ = false;
 private static boolean      helpOnly_ = false;
 private static boolean      qOption_ = false;
 private static boolean      dump_=false;
 
 private static boolean       maxFilesOption_=false;
 private static int           maxNLoadedFiles_ = 500;
 private static int           nLoadedFiles_ = 0;
 private static int           nProcessedFiles_ = 0;
 
 private static String              outputFname_=null;
 
 private static JavaFacts    facts_ = null;
 private static ITermSystem  mainSystem_ = null;
 
}