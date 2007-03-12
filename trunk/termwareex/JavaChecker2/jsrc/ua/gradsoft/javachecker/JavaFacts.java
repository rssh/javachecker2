/*
 * JavaFacts.java
 *
 * Created 18, 02, 2004, 8:14
 */

package ua.gradsoft.javachecker;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.prefs.Preferences;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.IEnv;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;




/**
 *Facts for java sources analysis.
 * @author  Ruslan Shevchenko
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
        //violations_.addType("EmptyFile","style","nothing in file",true);
        //violations_.addType("Hiding","style","hiding defects", true);
        //violations_.addType("SynchronizeViolations","threading","synchronize violations", true);
        violations_.addType("InvalidCheckerComments", "style","invalid checker comments",true);
        violations_.addType("*","uncategorized","*",true);
        
        
        nonFinalFieldNamePattern_=prefs.get("NonFinalFieldNamePattern", getNonFinalFieldNamePattern());
        finalFieldNamePattern_=prefs.get("FinalFieldNamePattern", getFinalFieldNamePattern());
        classNamePattern_=prefs.get("ClassNamePattern", getClassNamePattern());
        methodNamePattern_=prefs.get("MethodNamePattern", getMethodNamePattern());
        localVariableNamePattern_=prefs.get("LocalVariableNamePattern", getLocalVariableNamePattern());
        enumConstantNamePattern_=prefs.get("EnumConstantNamePattern",getEnumConstantNamePattern());
        typeArgumentNamePattern_=prefs.get("TypeArgumentNamePattern",getTypeArgumentNamePattern());
        
        violations_.readPreferences(prefs);

        
    }
    
    
    public PackagesStore getPackagesStore()
    {
      return packagesStore_;  
    }
    
    public Violations getViolations()
    {
      return violations_;  
    }
    
    // called from systems
    public boolean violationDiscovered(String name,String message,Term partOfCode) throws TermWareException
    {        
        violations_.discovered(name);
        DefectReportItem item=new DefectReportItem(violations_.getCategory(name),message,JUtils.getFileAndLine(partOfCode));
        addDefectReportItem(item);
        return true;
    }

    
    public boolean isCheckEnabled(String name)
    {
        return violations_.enabled(name);
    }
    
    public String  getStringConfigValue(String name,String sdefault)
    {        
        return preferences_.get(name,sdefault);
    }
    
    public int  getIntConfigValue(String name, int idefault)
    {
        return preferences_.getInt(name,idefault);
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
      DefectReportItem item=new DefectReportItem("checker","invalid checker comment:"+message,JUtils.getFileAndLine(t));
      addDefectReportItem(item);
    }
    
    //
    
    void report(PrintStream out)
    {
      Iterator it=defectReportItems_.iterator();
      while(it.hasNext()) {
          DefectReportItem item=(DefectReportItem)it.next();
          item.println(out);
      }
      
      violations_.report(out);
      
      out.println("Files:"+Main.getNProcessedFiles());
    }
    
    // utils      

    private void   addDefectReportItem(DefectReportItem item)
    {
        defectReportItems_.add(item);
        if (!Main.isQOption()&&Main.getOutputFname()!=null) {
            item.println(System.out);
        }
    }
    
    /**
     *@return list of collected report items.
     */
    public List<DefectReportItem> getDefectReportItems()
    { return defectReportItems_; }
    
    /**
     * clear list of defect report items.
     */
    public void clearDefectReportItems()
    {
      defectReportItems_.clear();  
    }
    
    private ArrayList<DefectReportItem> defectReportItems_=new ArrayList<DefectReportItem>();
    private Violations violations_ = new Violations();

    
    private Preferences  preferences_=null;        
    private HashMap<String,String>  localPreferences_=null;
    
    private String  nonFinalFieldNamePattern_="[a-z]+.*";
    private String  finalFieldNamePattern_="([A-Z]+(_|[A-Z]|[0-9])*)|serialVersionUID";
    private String  localVariableNamePattern_="[a-z]+([A-Z]|[a-z]|_[0-9])*";
    private boolean checkClassNamePatterns_=true;
    private String  classNamePattern_="[A-Z_]+.*";
    private boolean  checkMethodNamePatterns_=true;
    private String  methodNamePattern_="[a-z]+([A-Z]|[0-9]|[a-z]|_)*";
    private String  enumConstantNamePattern_="[A-Z](_|[A-Z]|[0-9])*";
    private String  typeArgumentNamePattern_="[A-Z]+([A-Z]|[0-9])*";
    private PackagesStore packagesStore_=new PackagesStore(this);
    
    
}
