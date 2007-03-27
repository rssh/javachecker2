/*
 * JavaCheckerTask.java
 *
 * Created 07/04/2004, 4:11
 * $Id: JavaCheckerTask.java,v 1.4 2007-03-27 09:33:26 rssh Exp $
 */

package ua.gradsoft.javachecker.ant;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.DirSet;
import org.apache.tools.ant.types.Path;
import ua.gradsoft.javachecker.ConfigException;

import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.ProcessingException;
import ua.gradsoft.javachecker.ReportFormat;

/**
 *Ant task for JavaChecker
 * @author  Ruslan Shevchenko
 */
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
      System.err.println("addConfiguredConfig:"+nvPair.toString());  
      configNVPairs_.add(nvPair);  
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
    
    
    
    public void execute() throws BuildException
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
      
      
      try {
        main.init(new String[0]);
      }catch(ConfigException ex){
          throw new BuildException(ex.getMessage(),ex);
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

     
      for(CheckName cname: enabled_) {
          main.getFacts().setCheckEnabled(cname.getCheck(),true);
      }
      for(CheckName cname: disabled_) {
           main.getFacts().setCheckEnabled(cname.getCheck(),false);
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
      
      try {
         main.getFacts().getPackagesStore().setJars(includeJars_);
      }catch(ConfigException ex){
          throw new BuildException(ex.getMessage(),ex);
      }
      
      try {
          main.process();
      }catch(ProcessingException ex){
          throw new BuildException(ex.getMessage(),ex);
      }
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
    private List<ConfigNVPair> configNVPairs_=new LinkedList<ConfigNVPair>();
    
    private List<String> inputDirectories_=new LinkedList<String>();
    private List<String> includeDirectories_=new LinkedList<String>();
    private List<String> includeJars_=new LinkedList<String>();
    
}
