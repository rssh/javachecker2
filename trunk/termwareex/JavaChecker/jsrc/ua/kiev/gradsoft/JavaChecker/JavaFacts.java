/*
 * JavaFacts.java
 *
 * Created 18, 02, 2004, 8:14
 */

package ua.kiev.gradsoft.JavaChecker;

import java.io.*;
import java.util.*;
import java.util.prefs.*;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.facts.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Facts for java sources analysis.
 * @author  Ruslan Shevchenko
 */
public class JavaFacts extends DefaultFacts {
   
    
    /** Creates a new instance of JavaFacts */
    public JavaFacts(IEnv env,Preferences prefs) throws TermWareException
    {
        super(env);
        violations_=new Violations();
        violations_.addType("Beans", "beans", "violations of beans contract", true);
        violations_.addType("EmptyCatchClauses", "exceptions", "empty catch clauses", true);
        violations_.addType("GenericExceptionSpecifications","exceptions","generic exception specifications",true);
        violations_.addType("GenericExceptionCatchClauses","exceptions","generic exception catch clauses",true);
        violations_.addType("OverloadedEquals","equals","equal without hashcode or vice-versa",true);
        violations_.addType("VariablePatterns","style","violation of variable pattern",true);
        violations_.addType("NonFinalPublicFields", "style", "non final public fields", true);
        violations_.addType("ClassNamePatterns","style","violation of class name conventions", true);
        violations_.addType("MethodNamePatterns","style","violation of method name conventions", true);
        violations_.addType("EmptyPackageDeclarations","style","empty package declarations", true);
        violations_.addType("Hiding","style","hiding defects", true);
        violations_.addType("SynchronizeViolations","threading","synchronize violations", true);
        violations_.addType("InvalidCheckerComments", "style","invalid checker comments",true);
        
        nonFinalFieldNamePattern_=prefs.get("NonFinalFieldNamePattern", getNonFinalFieldNamePattern());
        finalFieldNamePattern_=prefs.get("FinalFieldNamePattern", getFinalFieldNamePattern());
        classNamePattern_=prefs.get("ClassNamePattern", getClassNamePattern());
        methodNamePattern_=prefs.get("MethodNamePattern", getMethodNamePattern());
        localVariableNamePattern_=prefs.get("LocalVariableNamePattern", getLocalVariableNamePattern());
        
        violations_.readPreferences(prefs);
    }
    
    
    public void addCompilationUnit(ITerm t) throws TermWareException
    {
     ITerm commentTerm=TermHelper.getAttribute(t,"comment");
     boolean doAdd=true;
     if (!commentTerm.isNil()) {
       try {
         CheckerComment comment=CheckerComment.extract(commentTerm.getString());
         if (comment!=null && comment.isDisable("All")) {
             doAdd=false;
         }
       }catch(InvalidCheckerCommentException ex){
         System.err.println("warning - invalid checker comment:"+ex.getMessage());
         violationDiscovered("InvalidCheckerComments","invalid checker comment",commentTerm);
       }
     }
     if (doAdd) {
       compilationUnits_.add(t);
       addCompilationUnitToPackage(JUtils.getCompilationUnitPackageName(t), t);
     }
    }
    
    
   
    void addCompilationUnitToPackage(String packageName,ITerm compilationUnit) throws TermWareException
    {
     if (isCheckEnabled("EmptyPackageDeclarations")) {
         if (packageName.equals("default")) {
             violationDiscovered("EmptyPackageDeclarations","empty package declaration",compilationUnit);
         }
     }
     Object o=packageModels_.get(packageName);
     JavaPackageModel packageModel;
     if (o==null) {
         packageModel=new JavaPackageModel(packageName, this);
         packageModels_.put(packageName, packageModel);
     }else{
         packageModel=(JavaPackageModel)o;
     }
     packageModel.addCompilationUnit(compilationUnit);
    }
    
    
    
    // called from systems
    public boolean violationDiscovered(String name,String message,ITerm partOfCode) throws TermWareException
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
    
    
  
    String getFinalFieldNamePattern()
    { return finalFieldNamePattern_; }
    
    String getNonFinalFieldNamePattern()
    { return nonFinalFieldNamePattern_; }
    

    String getClassNamePattern()
    { return classNamePattern_; }
    
    String getLocalVariableNamePattern()
    { return localVariableNamePattern_; }
    
    String getMethodNamePattern()
      { return methodNamePattern_; }
    
       
    
   // public void invalidCheckerCommentDiscovered(ITerm t,String message) throws TermWareException
   // {
   //   DefectReportItem item=new DefectReportItem("checker","invalid checker comment:"+message,JUtils.getFileAndLine(t));
   //   addDefectReportItem(item);
   //   ++nInvalidCheckerComments_;
   // }
    
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
    
    HashSet getCompilationUnits()
    { return compilationUnits_; }
    
    Map     getPackageModels()
    { return packageModels_; }
    
    void unloadSources()
    {
     compilationUnits_=new HashSet();   
     packageModels_=new TreeMap(
       new Comparator(){
           public int compare(Object x, Object y)
           { return ((String)x).compareTo((String)y); }
       });
    }

    private void   addDefectReportItem(DefectReportItem item)
    {
        defectReportItems_.add(item);
        if (!Main.isQOption()&&Main.getOutputFname()!=null) {
            item.println(System.out);
        }
    }
    
    private Vector defectReportItems_=new Vector();
    private Violations violations_ = new Violations();

            
    private String  nonFinalFieldNamePattern_="[a-z]+.*";
    private String  finalFieldNamePattern_="[A-Z]+(_|[A-Z]|[0-9])*";
    private String  localVariableNamePattern_="[a-z]+([A-Z]|[a-z]|_[0-9])*";
    private boolean checkClassNamePatterns_=true;
    private String  classNamePattern_="[A-Z_]+.*";
    private boolean  checkMethodNamePatterns_=true;
    private String  methodNamePattern_="[a-z]+([A-Z]|[0-9]|[a-z]|_)*";
       
    private HashSet compilationUnits_=new HashSet();
    
    private TreeMap packageModels_=new TreeMap(
       new Comparator(){
           public int compare(Object x, Object y)
           { return ((String)x).compareTo((String)y); }
       });
}
