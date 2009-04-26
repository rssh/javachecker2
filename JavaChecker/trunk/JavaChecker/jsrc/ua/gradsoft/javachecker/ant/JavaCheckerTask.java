/*
 * JavaCheckerTask.java
 *
 * Created 07/04/2004, 4:11
 */

package ua.gradsoft.javachecker.ant;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.ExecuteWatchdog;
import org.apache.tools.ant.taskdefs.LogStreamHandler;
import org.apache.tools.ant.types.Commandline;
import org.apache.tools.ant.types.CommandlineJava;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.Path;
import ua.gradsoft.javachecker.ConfigException;

import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.ProcessingException;
import ua.gradsoft.javachecker.ReportFormat;
import ua.gradsoft.javachecker.annotations.CheckerDisable;
import ua.gradsoft.javachecker.models.AnalyzedUnitRef;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Ant task for JavaChecker
 * @author  Ruslan Shevchenko
 *
 *(all checks is disabled becouse ANT is not in common classpath)
 */
@CheckerDisable({"All"})
public class JavaCheckerTask extends Task {
    
    /** Creates a new instance of JavaCheckerTask */
    public JavaCheckerTask() {
    }
    
    public void  setInput(String inputDirectory)
    {
     inputDirectory_=inputDirectory;   
    }
        
    
    public void  setOutput(String output)
    {
     outputFname_=output;
    }
    
    public void  setOutputFormat(String name)
    {
        reportFormatName_ = name;
    }
    
    public void  setPrefs(String prefs)
    {
     prefsFname_=prefs;
    }
    
    public void setQ(boolean q)
    { q_=q; }
    
    public void setDebug(boolean debug)
    {
        debug_=debug;
    }
    
    public void setShowfiles(boolean showfiles)
    {
        showFiles_=showfiles;
    }
    
    public void setJchhome(String jchhome)
    {
        jchhome_=jchhome;
    }
    
    public static class CheckName
    {
        private String check_=null;
        
        public String getCheck() { return check_; }
        public void setCheck(String check) { check_=check; }
    }
    
    
    public void addConfiguredEnable(CheckName enabled)
    {
        enabled_.add(enabled);
    }
      
    
    public void addConfiguredDisable(CheckName disabled)
    {
        disabled_.add(disabled);
    }
    
    public static class ConfigNVPair
    {
        private String name_;
        private String value_;
        
        public String getName()            { return name_; }
        public void setName(String name)   { name_=name; }
        
        public String getValue()           { return value_; }
        public void   setValue(String value) { value_=value; }       
        
        public String toString()
        { return "("+name_+","+value_+")"; }
    }
    
    
    public void addConfiguredConfig(ConfigNVPair nvPair)
    {
      configNVPairs_.add(nvPair);  
    }

    public void setExplicitEnabledOnly(boolean value)
    {
        explicitEnabledOnly_ = value;
    }

    
    public void addConfiguredInclude(DirSet dirSet)
    {                       
      dirSet.setProject(this.getProject());  
      includeDirectories_.add(dirSet.getDir(getProject()).getAbsolutePath());
    }
    
    
    public void addConfiguredInput(DirSet dirSet) 
    {        
        dirSet.setProject(this.getProject());       
        inputDirectories_.add(dirSet.getDir(getProject()).getAbsolutePath());
    }

    
    public void addConfiguredClasspath(Path path)
    {
        includeJars_.addAll(Arrays.asList(path.list()));
    }
    
    public void setFork(boolean fork)
    {
        fork_=fork;
    }
    
    public Commandline.Argument createJvmarg()
    {
        return cmd.createVmArgument();
    }
    
    public void  setFailOnError(boolean value)
    {
        failOnError_=value;
    }
        
    public void execute() throws BuildException
    {
        if (failOnError_) {
            executeWithError();
        }else{
            try {
                executeWithError();
            }catch(BuildException ex){
                log("Caught exception :"+ex.getMessage(),Project.MSG_WARN);
                Throwable ex1 = ex;
                while(ex1!=null) {
                    StackTraceElement[] stack = ex1.getStackTrace();
                    for(int i=0; i<stack.length; ++i) {
                        log(stack[i].getClassName()+"."+stack[i].getMethodName()+" at "+stack[i].getFileName()+","+stack[i].getLineNumber(),Project.MSG_VERBOSE);
                    }
                    ex1 = ex1.getCause();
                    if (ex1!=null) {
                        log("Caused by "+ex1.getMessage(),Project.MSG_VERBOSE);
                    }
                }
            }
        }
    }
    
    private void executeWithError() throws BuildException
    {
        if (fork_) {
            executeFork();
        }else{
            executeNoFork();
        }
    }
    
    private void executeNoFork() throws BuildException
    {
      Main main=new Main();
      main.setQOption(q_);
      main.setShowFiles(showFiles_);
      main.setDump(debug_);

      if (jchhome_==null) {
          throw new BuildException("jchhome parameter is required");
      }else{
          main.setHome(jchhome_);
      }

      if (explicitEnabledOnly_) {
          main.setExplicitEnabledOnly(explicitEnabledOnly_);
      }
      
      
      try {
        main.init(new String[0]);
      }catch(ConfigException ex){
          throw new BuildException(ex.getMessage(),ex);
      }   

            for(CheckName cname: enabled_) {
          main.getFacts().setCheckEnabled(cname.getCheck(),true);
      }
      for(CheckName cname: disabled_) {
           main.getFacts().setCheckEnabled(cname.getCheck(),false);
      }

      
      if (inputDirectory_!=null) {
         main.addInputDirectory(inputDirectory_,true);  
      }
            
      if (outputFname_!=null) {
          main.setOutputFname(outputFname_);
      }
      if (prefsFname_!=null) {
          main.setPrefsFname(prefsFname_);
      }

      
      if (reportFormatName_!=null) {
          try {
            main.setReportFormat(ReportFormat.getByName(reportFormatName_));
          }catch(ConfigException ex){
              throw new BuildException(ex.getMessage(),ex);
          }
      }
    
      
      for(ConfigNVPair nvPair: configNVPairs_) {
          main.getFacts().setConfigValue(nvPair.getName(),nvPair.getValue());
      }

      main.getFacts().getPackagesStore().getSourceDirs().addAll(includeDirectories_);
      
      main.getFacts().getPackagesStore().getSourceDirs().addAll(inputDirectories_);
      main.getFacts().getPackagesStore().getSourceDirsToProcess().addAll(inputDirectories_);      
      if (main.getFacts().getPackagesStore().getSourceDirsToProcess().isEmpty()) {
          throw new BuildException("inputDirectory tag or input nested tag must be present in task");
      }
      
   
      main.getFacts().getPackagesStore().setJars(includeJars_);
      
      try {
          List<AnalyzedUnitRef> unitRefs = new LinkedList<AnalyzedUnitRef>();
          main.process(unitRefs);
      }catch(ProcessingException ex){
          ex.printStackTrace();
          throw new BuildException(ex.getMessage(),ex);
      }catch(TermWareRuntimeException ex){
          ex.printStackTrace();
          throw new BuildException(ex.getMessage(),ex);
      }
    }
    
    private void executeFork() throws BuildException
    {
      addJavaCheckerTasksToClassPath();     
      if (q_) {
          cmd.createArgument().setValue("--q");
      }
      if (showFiles_) {
          cmd.createArgument().setValue("--showFiles");
      }
      if (debug_) {
          cmd.createArgument().setValue("--debug");
      } 
      
      for(String include: includeDirectories_) {
          cmd.createArgument().setValue("--include");
          cmd.createArgument().setValue(include);
      }

      for(String include: includeJars_) {
          cmd.createArgument().setValue("--includejar");
          cmd.createArgument().setValue(include);
      }
      
      if (outputFname_!=null) {
          cmd.createArgument().setValue("--output");
          cmd.createArgument().setValue(outputFname_);
      }
      if (prefsFname_!=null) {
          cmd.createArgument().setValue("--prefs");
          cmd.createArgument().setValue(prefsFname_);
      }
      if (jchhome_==null) {
          throw new BuildException("jchhome parameter is required");
      }else{
          cmd.createVmArgument().setValue("-Djavachecker.home="+jchhome_);
      }

      for(CheckName cname: enabled_) {
          cmd.createArgument().setValue("--enable");
          cmd.createArgument().setValue(cname.getCheck());
      }
      for(CheckName cname: disabled_) {
          cmd.createArgument().setValue("--disable");
          cmd.createArgument().setValue(cname.getCheck());          
      }
      for(ConfigNVPair nvPair: configNVPairs_) {
          cmd.createArgument().setValue("--config");
          cmd.createArgument().setValue(nvPair.getName());          
          cmd.createArgument().setValue(nvPair.getValue());          
      }

      if (explicitEnabledOnly_) {
          cmd.createArgument().setValue("--explicit-enabled-only");
      }
      
      
      Path classpath=cmd.createClasspath(getProject());
      classpath.createPathElement().setPath(jchhome_+File.separator+"lib"+File.separator+"TermWare-2.3.2.jar");
      classpath.createPathElement().setPath(jchhome_+File.separator+"lib"+File.separator+"JavaChecker-2.5.0p3.jar");
      classpath.createPathElement().setPath(jchhome_+File.separator+"lib"+File.separator+"JavaChecker2Annotations-2.5.0p3.jar");
      classpath.createPathElement().setPath(jchhome_+File.separator+"lib"+File.separator+"TermWareJPP-1.1.1.jar");
      
      cmd.setClassname("ua.gradsoft.javachecker.Main");
      
      if (inputDirectory_!=null) {
          cmd.createArgument().setValue(inputDirectory_);
      }else if (inputDirectories_.size()!=0) {
          for(String s: inputDirectories_) {
              cmd.createArgument().setValue(s);
          }
      }else{
          throw new BuildException("inputdirectory must be set");
      }
      
      ExecuteWatchdog watchdog = null; // TODO: add timeout and rral watchdog.
      Execute execute = new Execute(new LogStreamHandler(this,Project.MSG_INFO,Project.MSG_WARN),watchdog);
      execute.setCommandline(cmd.getCommandline());
      int retcode=0;
      try {
          retcode = execute.execute();
      }catch(IOException ex){
          throw new BuildException("fork failed",ex,getLocation());
      }
      if (failOnError_ && Execute.isFailure(retcode)) {
          throw new BuildException("fork process return with error",null,getLocation());
      }
      
    }

    private void addJavaCheckerTasksToClassPath()
    {
      //   File f = LoaderUtils.getResourceSource();
    }
   
    
    
    private boolean q_=false;
    private boolean showFiles_=false;
    private boolean debug_=false;
    private String inputDirectory_=null;
    private String outputFname_=null;
    private String prefsFname_=null;
    private String jchhome_=null;
    private String reportFormatName_=null;
    private List<CheckName>  enabled_=new LinkedList<CheckName>();
    private List<CheckName>  disabled_=new LinkedList<CheckName>();
    private boolean          explicitEnabledOnly_=false;
    private List<ConfigNVPair> configNVPairs_=new LinkedList<ConfigNVPair>();
    
    
    private List<String> inputDirectories_=new LinkedList<String>();
    private List<String> includeDirectories_=new LinkedList<String>();
    private List<String> includeJars_=new LinkedList<String>();
    
    private boolean fork_=false;
    private boolean failOnError_=true;
    private CommandlineJava cmd=new CommandlineJava();
}
