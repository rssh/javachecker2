/*
 * Checkers.java
 *
 */

package ua.gradsoft.javachecker.checkers;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import ua.gradsoft.javachecker.CheckerComment;
import ua.gradsoft.javachecker.CheckerType;
import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.ProcessingException;
import ua.gradsoft.javachecker.SourceCodeLocation;
import ua.gradsoft.javachecker.Violations;
import ua.gradsoft.javachecker.models.JavaCompilationUnitModel;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.javachecker.util.Holder;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *This class incapsulate set of Checkers.
 * @author RSSH
 */
public class Checkers {
    
    /** Creates a new instance of Checkers */
    public Checkers(Main main) {
        facts_=main.getFacts();
        checkers_=new TreeMap<String,AbstractChecker>();
    }
    
    public void configure() throws ConfigException {               
        try {
             loadBuildinCheckers();
            
            String checkersFname=getEtcDirectory()+File.separator+"checkers.def";            
            Term checkers=TermWare.getInstance().load(checkersFname);
            if (!checkers.getName().equals("Checkers")) {
                throw new ConfigException("context of file "+checkersFname+" must be Checkers term");
            }
            Term l=checkers.getSubtermAt(0);
            while(!l.isNil()) {
                Term ct = l.getSubtermAt(0);
                l=l.getSubtermAt(1);
                if (ct.getName().equals("define")) {
                    Term tname = ct.getSubtermAt(0);
                    String name = null;
                    if (tname.isAtom()) {
                        name=tname.getName();
                    }else if (tname.isString()) {
                        name=tname.getString();
                    }else{
                        throw new ConfigException("name must be atom or string, here is: "+TermHelper.termToString(tname));
                    }
                    Term tcategory = ct.getSubtermAt(1);
                    String category = null;
                    if (tcategory.isAtom()) {
                        category=tcategory.getName();
                    }else if (tcategory.isString()) {
                        category=tcategory.getString();
                    }else{
                        throw new ConfigException("category must be atom or string, here is: "+TermHelper.termToString(tname));
                    }
                    Term tdescription = ct.getSubtermAt(2);
                    String description = null;
                    if (tdescription.isString()) {
                        description=tdescription.getString();
                    }else{
                        throw new ConfigException("description must be string, here is:"+TermHelper.termToString(tdescription));
                    }
                    Term ttype = ct.getSubtermAt(3);
                    CheckerType type = getCheckerType(ttype);
                    Term rules = ct.getSubtermAt(4);
                    Term tenabledByDefault = ct.getSubtermAt(5);
                    boolean enabledByDefault = false;
                    if (tenabledByDefault.isBoolean()) {
                        enabledByDefault = tenabledByDefault.getBoolean();
                    }
                    AbstractChecker checker=null;
                    switch(type) {
                        case BT_TYPE_RULESET:
                            checker=new BTTypeChecker(name,category,description,rules,enabledByDefault);
                            break;
                        case BT_COMPILATION_UNIT_RULESET:
                            checker=new BTCompilationUnitChecker(name,category,description,rules,enabledByDefault);
                            break;
                        case JAVA_CLASS:
                            checker=new ClassChecker(name,category,description,rules,enabledByDefault);
                            break;
                        case MODEL_RULESET:
                            checker=new ModelChecker(name,category,description,rules,enabledByDefault);
                            break;
                        default:
                            throw new ConfigException("Unknown checker type:"+type);
                    }
                    checkers_.put(name,checker);                    
                    boolean enabled = facts_.getBooleanConfigValue("Check"+name,enabledByDefault);
                    Violations violations=Main.getFacts().getViolations();
                    violations.addType(name,category,description,enabled);                    
                    checker.configure(Main.getFacts());                    
                }else{
                    throw new ConfigException("define term required instead "+TermHelper.termToPrettyString(ct));
                }
            }
            
            // now read local enabled or disabled stuff.
            // (in future, now leave as is)
        }catch(TermWareException ex){
            throw new ConfigException("Can't configure checkers:"+ex.getMessage(),ex);
        }
    }
    
    public void checkCompilationUnitAST(String fname, Term compilationUnitAST) throws ProcessingException
    {
       for(Map.Entry<String,AbstractChecker> e: checkers_.entrySet()) {
           AbstractChecker checker0 = e.getValue();
           if (checker0 instanceof AbstractCompilationUnitChecker) {
               AbstractCompilationUnitChecker checker = (AbstractCompilationUnitChecker)checker0;
               if (facts_.isCheckEnabled(e.getKey())) {
                 try {  
                   checker.run(compilationUnitAST);
                 }catch(Throwable ex){
                     Main.getExceptionHandler().handle(checker.getName(),fname,ex,JUtils.getSourceCodeLocation(ex));
                 }
               }               
           }else{
               continue;
           }
       } 
    }
    
    public void checkTypes(String fname, JavaCompilationUnitModel cu) throws ProcessingException
    {
      checkTypes(fname,cu,1);  
    }

    public void checkTypes2(String fname, JavaCompilationUnitModel cu) throws ProcessingException
    {
      checkTypes(fname,cu,2);  
    }
    
    
    public void checkTypes(String fname, JavaCompilationUnitModel cu, int pass) throws ProcessingException
    {
                
        // for all types all type checkers.
        for(JavaTypeModel tm: cu.getTypeModels()) {
            JavaTermTypeAbstractModel ttm=null;
            if (tm instanceof JavaTermTypeAbstractModel) {
                ttm=(JavaTermTypeAbstractModel)tm;
            }
            Holder<Term> astTermHolder = new Holder<Term>();
            Holder<Term> modelTermHolder = new Holder<Term>();
            for (Map.Entry<String,AbstractChecker> e:  checkers_.entrySet()) {
                AbstractChecker checker0 = e.getValue();                
                if (! (checker0 instanceof AbstractTypeChecker)) {
                    continue;
                }
                AbstractTypeChecker checker = (AbstractTypeChecker)checker0;               
                //boolean enabled=checker.isEnabled();       
                if (pass==2 && !checker.hasSecondPass()) {
                    continue;
                }
                boolean enabled=facts_.isCheckEnabled(e.getKey());
                if (ttm!=null) {
                    CheckerComment checkerComment = ttm.getCheckerComment();
                    if (checkerComment!=null) {
                        if (checkerComment.isDisable(e.getKey())||checkerComment.isDisable("All")) {
                            enabled=false;
                        }
                    }
                    try {
                      Set<String> disabledAttributes = ttm.getDisabledChecks();
                      if (disabledAttributes.contains(e.getKey())||disabledAttributes.contains("All")) {
                        enabled=false;
                      }
                    }catch(TermWareException ex){
                        //TODO: log to logger.
                        System.out.println("exception during getting disabled checks");
                        if (ex instanceof SourceCodeLocation) {
                          SourceCodeLocation scl = (SourceCodeLocation)ex;
                          System.out.println(scl.getFileAndLine().getFname()+":"+scl.getFileAndLine().getLine());                            
                        }
                    }catch(EntityNotFoundException ex){
                        //TODO: log to logger.
                        System.out.println("exception during getting disabled checks");
                        if (ex instanceof SourceCodeLocation) {
                          SourceCodeLocation scl = (SourceCodeLocation)ex;
                          System.out.println(scl.getFileAndLine().getFname()+":"+scl.getFileAndLine().getLine());                            
                        }                        
                    }
                }
                if (enabled && ttm!=null) {  
                  try {  
                    switch(pass){
                        case 1:
                            checker.run(ttm,astTermHolder,modelTermHolder);
                            break;
                        case 2:
                            checker.runSecondPass(ttm,astTermHolder,modelTermHolder);
                            break;
                        default:
                            throw new AssertException("pass must be 1 or 2");
                            
                    }                                            
                  }catch(Throwable ex){                                            
                      Main.getExceptionHandler().handle(checker.getName(),fname,ex,JUtils.getSourceCodeLocation(ex));
                  }
                }
            }
        }
        
    }
    
    
    public void printDocumentation(PrintWriter writer) {
        TreeMap<String,TreeMap<String,String>> categories = new TreeMap<String,TreeMap<String,String>>();
        for(Map.Entry<String,AbstractChecker> e: checkers_.entrySet()) {
            String name=e.getKey();
            AbstractChecker checker=e.getValue();
            String description=checker.getDescription();
            String category= checker.getCategory();
            TreeMap<String,String> currentCategory = categories.get(category);
            if (currentCategory==null) {
                currentCategory = new TreeMap<String,String>();
                categories.put(category,currentCategory);
            }
            currentCategory.put(name,description);
        }
        for(Map.Entry<String,TreeMap<String,String>> e: categories.entrySet()) {
            writer.print("<h2>");
            writer.print(e.getKey());
            writer.println("</h2>");
            for(Map.Entry<String,String> e1: e.getValue().entrySet()) {
                writer.print("<p>");
                writer.print("<h3>");
                writer.print(e1.getKey());
                writer.print("</h3>");
                writer.print("</p><p>");
                writer.print(e1.getValue());
                writer.print("</p>");
                writer.println();
            }
        }
    }
    
    private CheckerType getCheckerType(Term ttype) throws ConfigException
    {
        String name;
        if (ttype.isString()) {
            name=ttype.getString();
        }else if (ttype.isAtom()) {
            name=ttype.getName();
        }else{
            throw new ConfigException("Atom or String expected, instead we have:"+TermHelper.termToString(ttype));
        }
        return CheckerType.valueOf(name);
    }
    
    private void loadBuildinCheckers() throws TermWareException, ConfigException
    {
        ClassChecker nameChecker=new ClassChecker("NamePatterns","style","check name patterns",TermUtils.createString("ua.gradsoft.javachecker.checkers.NamePatternsChecker"),true);
        checkers_.put(nameChecker.getName(),nameChecker);
        facts_.getViolations().addType(nameChecker.getName(),nameChecker.getCategory(),nameChecker.getDescription(),nameChecker.isEnabled());
        ClassChecker equalsHashCodeChecker=new ClassChecker("EqualsHashCode","basic","check that overloaded hashCode and equals are correspond",TermUtils.createString("ua.gradsoft.javachecker.checkers.EqualsHashCodeChecker"),true);
        checkers_.put(equalsHashCodeChecker.getName(),equalsHashCodeChecker);
        facts_.getViolations().addType(equalsHashCodeChecker.getName(),equalsHashCodeChecker.getCategory(),equalsHashCodeChecker.getDescription(),equalsHashCodeChecker.isEnabled());
    }
    
    private String getEtcDirectory() {
        return Main.getHome()+File.separator+"etc";
    }
    
    
    private JavaFacts facts_;
    
    private TreeMap<String,AbstractChecker> checkers_;
    
    
}
