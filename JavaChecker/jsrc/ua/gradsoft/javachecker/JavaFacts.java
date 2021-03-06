/*
 * JavaFacts.java
 *
 * Created 18, 02, 2004, 8:14
 */

package ua.gradsoft.javachecker;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import ua.gradsoft.javachecker.attributes.ConfigurationAttributesStorage;
import ua.gradsoft.javachecker.checkers.CheckerNotFoundException;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.IEnv;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Facts for java sources analysis.
 */
public class JavaFacts extends DefaultFacts {
   
    
    /** Creates a new instance of JavaFacts */
    public JavaFacts(IEnv env,Preferences prefs) throws TermWareException
    {
        super();
        setEnv(env);
        preferences_=prefs;
        violations_=new Violations();
        //violations_.addType("Beans", "beans", "violations of beans contract", true);
        //violations_.addType("EmptyCatchClauses", "exceptions", "empty catch clauses", true);
        //violations_.addType("GenericExceptionSpecifications","exceptions","generic exception specifications",true);
        //violations_.addType("GenericExceptionCatchClauses","exceptions","generic exception catch clauses",true);
        //violations_.addType("OverloadedEquals","equals","equal without hashcode or vice-versa",true);
        //violations_.addType("VariablePatterns","style","violation of variable pattern",true);
        //violations_.addType("NonFinalPublicFields", "style", "non final public fields", true);
        //violations_.addType("ClassNamePatterns","style","violation of class name conventions", true);
        //violations_.addType("MethodNamePatterns","style","violation of method name conventions", true);
        //violations_.addType("EnumConstantNamePatterns","style","violation of enumeration constant name pattern",true);
        //violations_.addType("TypeArgumentNamePatterns","style","violation of type argument name pattern",true);
        //violations_.addType("EmptyPackageDeclarations","style","empty package declarations", true);
        violations_.addType("EmptyFile","style","nothing in file",
                Main.isExplicitEnabledOnly() ? Main.getExplicitEnabled().contains("EmptyFile"):true,true);
        //violations_.addType("Hiding","style","hiding defects", true);
        //violations_.addType("SynchronizeViolations","threading","synchronize violations", true);

        violations_.addType("InvalidCheckerComments", "style","invalid checker comments",
                Main.isExplicitEnabledOnly() ? Main.getExplicitEnabled().contains("InvalidCheckerComments"):true,true);

        violations_.addType("WrongPackage", "basic","package name does not match directory structure",
                Main.isExplicitEnabledOnly() ? Main.getExplicitEnabled().contains("WrangPackage"):true,true);


        violations_.addType("*","uncategorized","*",true,true);
        
        nonFinalFieldNamePattern_=getStringConfigValue("NonFinalNamePattern",nonFinalFieldNamePattern_);
        finalFieldNamePattern_=getStringConfigValue("FinalFieldNamePattern", getFinalFieldNamePattern());
        classNamePattern_=getStringConfigValue("ClassNamePattern", getClassNamePattern());
        methodNamePattern_=getStringConfigValue("MethodNamePattern", getMethodNamePattern());
        localVariableNamePattern_=getStringConfigValue("LocalVariableNamePattern", getLocalVariableNamePattern());
        enumConstantNamePattern_=getStringConfigValue("EnumConstantNamePattern",getEnumConstantNamePattern());
        typeArgumentNamePattern_=getStringConfigValue("TypeArgumentNamePattern",getTypeArgumentNamePattern());
        
        violations_.readPreferences(this);
        
    }
    
    
    public PackagesStore getPackagesStore()
    {
      return packagesStore_;  
    }
    
    public ConfigurationAttributesStorage getAttributesStorage()
    { return attributesStorage_; }
    
    public Violations getViolations()
    {
      return violations_;  
    }
    
    
    // called from systems
    public boolean violationDiscovered(String name,String message,Term partOfCode) throws TermWareException
    {
      try {
        DefectReportItem item=new DefectReportItem(violations_.getCategory(name),
                                        message,
                                        JUtils.getFileAndLine(partOfCode),
                                        Main.getCheckers().getChecker(name)
                              );
        if (addDefectReportItem(item)) {
           violations_.discovered(name);    
        }
        return true;
      }catch(CheckerNotFoundException ex){
          throw new AssertException(ex.getMessage());
      }
    }

    // called from systems
    public boolean notComplex(Term t)
    {
        return !t.isComplexTerm();
    }
    
    public void    setConfigValue(String key, String value)
    {
        localPreferences_.put(key,value);
    }
    
    
    public boolean isCheckEnabled(String name)
    {
        return violations_.enabled(name);
    }
    
    public void setCheckEnabled(String name, boolean value)
    {                   
        setConfigValue("Check"+name,value ? "true" : "false");
        violations_.setEnabled(name,value);
        if (value) {
            Main.getExplicitEnabled().add(name);
        } else {
            Main.getExplicitDisabled().add(name);
        }
    }
        
    public String  getStringConfigValue(String name,String sdefault)
    {        
        String retval = localPreferences_.get(name);
        if (retval==null) {
           retval=preferences_.get(name,sdefault);
        }
        return retval;
    }
    
    public int  getIntConfigValue(String name, int idefault)
    {
        String sretval = localPreferences_.get(name);
        int retval=idefault;
        if (sretval==null) {
          retval=preferences_.getInt(name,idefault);
        }else{
           try { 
             retval=Integer.parseInt(sretval);
           }catch(NumberFormatException ex){
               LOG.warning("invalid int config value "+name+", use default "+idefault);
           }
        }
        return retval;
    }
  
    public boolean getBooleanConfigValue(String name, boolean bdefault)
    {
        String sretval = localPreferences_.get(name);
        boolean retval = bdefault;
        if (sretval==null) {
          retval=preferences_.getBoolean(name,bdefault);
        }else{
            if (sretval.equals("true")||sretval.equals("yes")||sretval.equals("1")||sretval.equals("on")) {
                retval=true;
            }else if(sretval.equals("false")||sretval.equals("no")||sretval.equals("0")||sretval.equals("off")){
                retval=false;
            }else{
                LOG.warning("invalid boolean config value "+name+", use default "+bdefault);
            }
        }
        return retval;
    }
    
    public String getFinalFieldNamePattern()
    { return finalFieldNamePattern_; }
    
    public String getNonFinalFieldNamePattern()
    { return nonFinalFieldNamePattern_; }
    

    public String getClassNamePattern()
    { return classNamePattern_; }
    
    public String getLocalVariableNamePattern()
    { return localVariableNamePattern_; }
    
    public String getMethodNamePattern()
      { return methodNamePattern_; }
    
    public String getEnumConstantNamePattern()
      { return enumConstantNamePattern_; }
    
    public String getTypeArgumentNamePattern()
    { return typeArgumentNamePattern_; }
       
    
    public void invalidCheckerCommentDiscovered(Term t,String message) throws TermWareException
    {
      try {
        DefectReportItem item=new DefectReportItem("checker","invalid checker comment:"+message,JUtils.getFileAndLine(t),
                                                 Main.getCheckers().getChecker("InvalidCheckerComment"));
        addDefectReportItem(item);
      }catch(CheckerNotFoundException ex){
          // impossible.
          throw new AssertException(ex.getMessage());
      }
    }
    
    //
    
    void report(PrintStream out, ReportFormat format)
    {
      switch(format)  {
          case TEXT:
              break;
          case HTML:
              out.println("<html><head> JavaChecker report </head><body>");
              out.println("<p align=\"left\">Violations found:</p>");            
              out.println("<table>");            
              break;
          case XML:
              out.println("<JavaCheckerReport>");
          default:
              // internal error
              throw new AssertionError("internal error: unknown report format "+format);
      }

      for(ArrayList<DefectReportItem> items: defectReportItems_.values()) {
          for(DefectReportItem item: items) {
              StatisticItem si = Main.getStatistics().getItem(item.getChecker().getName());
              if (si.isShow()) {
                item.println(out,format);
              }
          }
      }
            
      switch(format)  {
          case TEXT:
              break;
          case HTML:           
              out.println("</table>");
              out.println("");
              out.println("<p>Summary:</p>");
              break;
          case XML:
              out.println("</JavaCheckerReport>");
          default:
              // internal error
              throw new AssertionError("internal error: unknown report format "+format);
      }
      

      Main.getStatistics().report(out, format);
      //violations_.report(out,format);
     
      switch(format)  {
          case TEXT:
              out.println("Files:"+Main.getNProcessedFiles());
              break;
          case HTML:
              out.println("Files:"+Main.getNProcessedFiles());
              break;
          default:
              // internal error
              throw new AssertionError("internal error: unknown report format "+format);
      }
      
      
    }
    
    // utils      

    private boolean   addDefectReportItem(DefectReportItem item)
    {      
        boolean retval=false;
        FileAndLine marker = item.getFileAndLine();
        ArrayList<DefectReportItem> items = defectReportItems_.get(marker);
        if (items==null) {
            items = new ArrayList<DefectReportItem>();
            items.add(item);
            defectReportItems_.put(marker,items);
            retval=true;
        }else{
            for(DefectReportItem ei: items) {
                if (ei.getDescription().equals(item.getDescription())) {

                    // duplicate
                    return retval;
                }
            }
            items.add(item);
            retval=true;
        }      
        if (!Main.isQOption()&&Main.getOutputFname()!=null
             && Main.getStatistics().getItem(item.getChecker().getName()).isShow()) {
            item.println(System.out,ReportFormat.TEXT);
        }
        return retval;
    }
    
    /**
     *@return unmodifiable list of collected report items.
     */
    public List<DefectReportItem> getDefectReportItems()
    {  
        ArrayList<DefectReportItem> retval = new ArrayList<DefectReportItem>();
        for(Map.Entry<FileAndLine,ArrayList<DefectReportItem>> e: defectReportItems_.entrySet()) {
            retval.addAll(e.getValue());
        }
        return Collections.unmodifiableList(retval);
    }
    
    /**
     * clear list of defect report items.
     */
    public void clearDefectReportItems()
    {
      defectReportItems_.clear();  
    }
    
    //private ArrayList<DefectReportItem> defectReportItems_=new ArrayList<DefectReportItem>();
    private HashMap<FileAndLine,ArrayList<DefectReportItem>> defectReportItems_=new HashMap<FileAndLine,ArrayList<DefectReportItem>>();
    private Violations violations_ = new Violations();

    
    private Preferences  preferences_=null;        
    private HashMap<String,String>  localPreferences_=new HashMap<String,String>();
    
    private String  nonFinalFieldNamePattern_="[a-z]+.*";
    private String  finalFieldNamePattern_="([A-Z]+(_|[A-Z]|[0-9])*)|serialVersionUID";
    private String  localVariableNamePattern_="[a-z]+([A-Z]|[a-z]|_|[0-9])*";
    private String  classNamePattern_="[A-Z_]+.*";  
    private String  methodNamePattern_="[a-z]+([A-Z]|[0-9]|[a-z]|_)*";
    private String  enumConstantNamePattern_="[A-Z](_|[A-Z]|[0-9])*";
    private String  typeArgumentNamePattern_="[A-Z]+([A-Z]|[0-9])*";
    
    private PackagesStore packagesStore_=new PackagesStore(this);
    private ConfigurationAttributesStorage attributesStorage_ = new ConfigurationAttributesStorage(this);
    
    private static final Logger LOG = Logger.getLogger(JavaFacts.class.getName());
    
    
}
