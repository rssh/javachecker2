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
        checkEmptyCatchClauses_=prefs.getBoolean("CheckEmptyCatchClauses", true);
        checkGenericExceptionSpecifications_=prefs.getBoolean("CheckGenericExceptionSpecifications",true);
        checkGenericExceptionCatchClauses_=prefs.getBoolean("CheckGenericExceptionCathClauses", true);
        checkOverloadedEquals_=prefs.getBoolean("CheckOverloadedEquals",  true);
        checkVariablePatterns_=prefs.getBoolean("CheckVariablePatterns", true);
        checkNonFinalPublicFields_=prefs.getBoolean("CheckNonFinalPublicFields", true);
        nonFinalFieldNamePattern_=prefs.get("NonFinalFieldNamePattern", getNonFinalFieldNamePattern());
        finalFieldNamePattern_=prefs.get("FinalFieldNamePattern", getFinalFieldNamePattern());
        checkClassNamePatterns_=prefs.getBoolean("CheckClassNamePatterns", true);
        classNamePattern_=prefs.get("ClassNamePattern", getClassNamePattern());
        checkMethodNamePatterns_=prefs.getBoolean("CheckMethodNamePatterns", true);
        methodNamePattern_=prefs.get("MethodNamePattern", getMethodNamePattern());
        checkPackageDeclarations_=prefs.getBoolean("CheckPackageDeclarations", true);
        
        checkStyleViolations_=checkVariablePatterns_ && checkClassNamePatterns_ 
                              && checkNonFinalPublicFields_ && checkPackageDeclarations_
                              && checkMethodNamePatterns_
                              ;
        checkHiding_ = prefs.getBoolean("CheckHiding", true);
        checkSynchronizeViolations_=prefs.getBoolean("CheckSynchronizeViolations", true);
        
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
       }
     }
     if (doAdd) {
       compilationUnits_.add(t);
       addCompilationUnitToPackage(JUtils.getCompilationUnitPackageName(t), t);
     }
    }
    
    
    
       

    void addCompilationUnitToPackage(String packageName,ITerm compilationUnit) throws TermWareException
    {
     if (checkPackageDeclarations_) {
         if (packageName.equals("default")) {
             emptyPackageDeclarationDiscovered(compilationUnit);
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
    
    
    // called from systems:
    
    public boolean isCheckEmptyCatchClauses()
    { return checkEmptyCatchClauses_; }
    
    public boolean emptyCatchClauseDiscovered(ITerm partOfCode) throws TermWareException
    {
        DefectReportItem item=new DefectReportItem("exception", "empty catch clause",JUtils.getFileAndLine(partOfCode));
        addDefectReportItem(item);
        nEmptyCatchClauses_++;
        return true;
    }
    
    public boolean isCheckGenericExceptionSpecifications()
    { return checkGenericExceptionSpecifications_; }
    
    
    public boolean genericExceptionSpecificationDiscovered(ITerm name) throws TermWareException
    {
        DefectReportItem item=new DefectReportItem("exceptions", "generic exception specification", JUtils.getFileAndLine(name));
        addDefectReportItem(item);
        nGenericExceptionSpecifications_++;
        return true;        
    }
    
    public boolean isCheckGenericExceptionCatchClauses()
    {  return checkGenericExceptionCatchClauses_; }
    
    public boolean genericExceptionCatchClauseDiscovered(ITerm name) throws TermWareException
    {
        DefectReportItem item=new DefectReportItem("exceptions", "generic exception catch clause", JUtils.getFileAndLine(name));
        addDefectReportItem(item);
        nGenericExceptionCatchClauses_++;
        return true;
    }
    
    // overloaded equals.
    
    public boolean isCheckOverloadedEquals()
    { return checkOverloadedEquals_; }
    
    public boolean equalsWithoutHashcodeDiscovered(ITerm classnameTerm) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("equals", "overloaded equaks without hashcode discovered in class "+JUtils.getJavaIdentifierAsString(classnameTerm), JUtils.getFileAndLine(classnameTerm));
     addDefectReportItem(item);
     ++nOverloadedEquals_;
     return true;
    }
    
    public boolean hashcodeWithoudEqualsDiscovered(ITerm classnameTerm) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("equals", "overloaded hashcode without equals discovered in class "+JUtils.getJavaIdentifierAsString(classnameTerm), JUtils.getFileAndLine(classnameTerm));
     addDefectReportItem(item);
     ++nOverloadedEquals_;
     return true;
    }

    public boolean isCheckVariablePatterns()
    { return checkVariablePatterns_; }

    String getFinalFieldNamePattern()
    { return finalFieldNamePattern_; }
    
    String getNonFinalFieldNamePattern()
    { return nonFinalFieldNamePattern_; }
    
    void finalFieldNamePatternViolationDiscovered(ITerm variableDeclarator) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("style","bad final fieldname discovered",JUtils.getFileAndLine(variableDeclarator));
     addDefectReportItem(item);
     ++nStyleViolations_;
    }
    
    void nonFinalFieldNamePatternViolationDiscovered(ITerm variableDeclarator) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("style","bad non-final fieldname discovered",JUtils.getFileAndLine(variableDeclarator));
     addDefectReportItem(item);
     ++nStyleViolations_;
    }
    
    
    public boolean isCheckNonFinalPublicFields()
    {
      return checkNonFinalPublicFields_; 
    }
    
    void nonFinalPublicFieldDiscovered(ITerm variableDeclarator) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("style","non-final public field discovered",JUtils.getFileAndLine(variableDeclarator));
     addDefectReportItem(item);
     ++nStyleViolations_;
    }
    
    public boolean isCheckClassNamePatterns()
    { return checkClassNamePatterns_; }
    
    public String getClassNamePattern()
    { return classNamePattern_; }
    
    void classNamePatternViolationDiscovered(ITerm t) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("style","bad classname pattern",JUtils.getFileAndLine(t));
     addDefectReportItem(item);
     ++nStyleViolations_;
    }

    public boolean isCheckMethodNamePatterns()
    { return checkMethodNamePatterns_; }
    
    public String getMethodNamePattern()
    { return methodNamePattern_; }
    
    public void methodNamePatternViolationDiscovered(ITerm t) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("style","bad method name pattern",JUtils.getFileAndLine(t));
     addDefectReportItem(item);
     ++nStyleViolations_;
    }
    
    
    void emptyPackageDeclarationDiscovered(ITerm compilationUnit) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("style","empty package",JUtils.getFileAndLine(compilationUnit));
     addDefectReportItem(item);
     ++nStyleViolations_;
    }

    public boolean isCheckSynchronizeViolations()
    { return checkSynchronizeViolations_; }
    
    public boolean synchronizeViolationDiscovered(ITerm varTerm,ITerm synchronizerTerm) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("synchronization","variable synchronized violation",JUtils.getFileAndLine(synchronizerTerm));
     addDefectReportItem(item);
     ++nSynchronizeViolations_;
     return true;
    }
    
    
    public boolean isCheckHiding()
    {
     return checkHiding_;
    }
    
    public void hidingOfFormalParameterDiscovered(ITerm t) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("hiding","hiding of formal parameter",JUtils.getFileAndLine(t));
     addDefectReportItem(item);
     ++nHidings_;
    }
    
    public void hidingOfClassFieldDiscovered(ITerm t) throws TermWareException
    {
     DefectReportItem item=new DefectReportItem("hiding","hiding of class field",JUtils.getFileAndLine(t));
     addDefectReportItem(item);
     ++nHidings_;
    }
        
    
    public void invalidCheckerCommentDiscovered(ITerm t,String message) throws TermWareException
    {
      DefectReportItem item=new DefectReportItem("checker","invalid checker comment:"+message,JUtils.getFileAndLine(t));
      addDefectReportItem(item);
      ++nInvalidCheckerComments_;
    }
    
    //
    
    void report(PrintStream out)
    {
      Iterator it=defectReportItems_.iterator();
      while(it.hasNext()) {
          DefectReportItem item=(DefectReportItem)it.next();
          item.println(out);
      }
      if (checkEmptyCatchClauses_) {
          out.println("empty catch clauses:"+nEmptyCatchClauses_);
      }
      if (checkGenericExceptionSpecifications_) {
          out.println("generic exception specifications:"+nGenericExceptionSpecifications_);
      }
      if (checkGenericExceptionCatchClauses_) {
          out.println("generic exception catch clauses:"+nGenericExceptionCatchClauses_);
      }
      if (checkOverloadedEquals_) {
          out.println("incorrectly overloaded equals or hashCode:"+nOverloadedEquals_);
      }
      if (checkHiding_) {
          out.println("hiding defects:"+nHidings_);
      }
      if (checkSynchronizeViolations_) {
          out.println("synchronize violations:"+nSynchronizeViolations_);
      }
      if (checkStyleViolations_){
          out.println("style violations:"+nStyleViolations_);
      }
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
    
    private boolean checkEmptyCatchClauses_=true;
    private int nEmptyCatchClauses_ = 0;
    private boolean checkGenericExceptionSpecifications_=true;
    private int nGenericExceptionSpecifications_ = 0;
    private boolean checkGenericExceptionCatchClauses_=true;
    private int nGenericExceptionCatchClauses_ = 0;
    private boolean checkOverloadedEquals_=true;
    private int nOverloadedEquals_ = 0;
    private boolean  checkHiding_ = true;
    private int nHidings_ = 0;
    
    private boolean checkStyleViolations_=true;
    private int nStyleViolations_=0;
    
    private boolean checkVariablePatterns_=true;
    private String  nonFinalFieldNamePattern_="[a-z]+.*";
    private String  finalFieldNamePattern_="[A-Z]+(_|[A-Z]|[0-9])*";
    private boolean checkClassNamePatterns_=true;
    private String  classNamePattern_="[A-Z_]+.*";
    private boolean  checkMethodNamePatterns_=true;
    private String  methodNamePattern_="[a-z]+([A-Z]|[0-9]|[a-z]|_)*";
    
    
    
    private boolean checkNonFinalPublicFields_=true;
    
    private boolean checkPackageDeclarations_=true;
    
    private boolean checkSynchronizeViolations_=true;
    private int     nSynchronizeViolations_=0;
    
    private int     nInvalidCheckerComments_=0;
    
    private HashSet compilationUnits_=new HashSet();
    
    private TreeMap packageModels_=new TreeMap(
       new Comparator(){
           public int compare(Object x, Object y)
           { return ((String)x).compareTo((String)y); }
       });
}
