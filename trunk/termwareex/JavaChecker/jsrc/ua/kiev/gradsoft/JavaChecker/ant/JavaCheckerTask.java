/*
 * JavaCheckerTask.java
 *
 * Created 07/04/2004, 4:11
 * $Id: JavaCheckerTask.java,v 1.2 2004-07-06 23:00:59 rssh Exp $
 */

package ua.kiev.gradsoft.JavaChecker.ant;


import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

import ua.kiev.gradsoft.JavaChecker.Main;
import ua.kiev.gradsoft.JavaChecker.ProcessingException;

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
    
    public void  setPrefs(String prefs)
    {
     prefsFname_=prefs;
    }
    
    public void setQ(boolean q)
    { q_=q; }
    
    public void setShowfiles(boolean showfiles)
    {
        showFiles_=showfiles;
    }
    
    public void setJchhome(String jchhome)
    {
        jchhome_=jchhome;
    }
    
    public void execute() throws BuildException
    {
      Main main=new Main();
      main.setQOption(q_);
      main.setShowFiles(showFiles_);
      if (inputDirectory_==null) {
        throw new BuildException("input parameter is required");
      }
      main.addInputDirectory(inputDirectory_);
      if (outputFname_!=null) {
          main.setOutputFname(outputFname_);
      }
      if (prefsFname_!=null) {
          main.setPrefsFname(prefsFname_);
      }
      if (jchhome_==null) {
          throw new BuildException("jchhome parameter is required");
      }
      String[] args=new String[2];
      args[0]="-I";
      args[1]=jchhome_+"/systems";
      try {
          main.process(args);
      }catch(ProcessingException ex){
          throw new BuildException(ex.getMessage());
      }
    }
    
    
    private boolean q_=false;
    private boolean showFiles_=false;
    private String inputDirectory_=null;
    private String outputFname_=null;
    private String prefsFname_=null;
    private String jchhome_=null;
    
}
