/*
 * Checkers.java
 *
 */

package ua.gradsoft.javachecker.checkers;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import ua.gradsoft.javachecker.CheckerComment;
import ua.gradsoft.javachecker.checkers.CheckerNotFoundException;
import ua.gradsoft.javachecker.checkers.CheckerType;
import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.ProcessingException;
import ua.gradsoft.javachecker.SourceCodeLocation;
import ua.gradsoft.javachecker.StatisticItem;
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
        } catch (TermWareException ex){
            throw new ConfigException("Can't load buildin checkers",ex);
        }

        loadCheckersAndStatisticsFromFiles();
        //}
    }

    public void loadCheckersAndStatisticsFromFiles() throws ConfigException
    {
        String dirfname = getEtcDirectory();
        if (!dirfname.endsWith(File.separator)) {
            dirfname = dirfname + File.separator;
        }
        File dir = new File(dirfname);
        FilenameFilter nameFilter = new FilenameFilter(){
            public boolean accept(File dir, String name) {
                return name.startsWith("checkers") && name.endsWith(".def");
            }
        };
        String[] fnames = dir.list(nameFilter);
        if (fnames==null) {
            throw new ConfigException("checkers are not found in dir "+dir.getName());
        }
        for(String fname: fnames) {
            loadCheckersAndStatisticsFromFile(dirfname+fname);
        }
    }

    public void loadCheckersAndStatisticsFromFile(String checkersFname) throws ConfigException
    {
            if (!Main.isMandatoryCheckersLoading()) {
                File checkersFile = new File(checkersFname);
                if (!checkersFile.exists()) {
                    return;
                }                
            }
            Term checkers=null;
            try {
             checkers = TermWare.getInstance().load(checkersFname);
            } catch (TermWareException ex){
                throw new ConfigException("Can't load checkers from file "+checkersFname,ex);
            }
            if (!checkers.getName().equals("Checkers")) {
                throw new ConfigException("context of file "+checkersFname+" must be Checkers term");
            }
            Term l=checkers.getSubtermAt(0);
            while(!l.isNil()) {
                Term ct = l.getSubtermAt(0);
                l=l.getSubtermAt(1);
                if (ct.getName().equals("define")||ct.getName().equals("checker")) {
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
                    try {
                      switch(type) {
                        case BT_TYPE_RULESET:
                            checker=new BTTypeChecker(name,category,description,rules,enabledByDefault);
                            break;
                        case FT_TYPE_RULESET:
                            checker=new FTTypeChecker(name,category,description,rules,enabledByDefault);
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
                    }catch(TermWareException ex){
                        throw new ConfigException("Can't create checker with name "+name,ex);
                    }
                    checkers_.put(name,checker);
                    boolean enabled = facts_.getBooleanConfigValue("Check"+name,enabledByDefault);
                    if (Main.isExplicitEnabledOnly()) {
                        enabled = Main.getExplicitEnabled().contains(name);
                    }
                    boolean show=true;
                    Term showAttr = TermHelper.getAttribute(ct,"Show");
                    if (showAttr.isBoolean()) {
                        show=showAttr.getBoolean();
                    }
                    Violations violations=Main.getFacts().getViolations();
                    violations.addType(name,category,description,enabled,show);
                    checker.configure(Main.getFacts());
                }else if (ct.getName().equals("statistics")||ct.getName().equals("calculate")){
                    try {
                      StatisticItem si = Main.getStatistics().parseStatisticDefinition(ct);
                      boolean enabled = facts_.getBooleanConfigValue("Check"+si.getName(),si.isEnabled());
                      if (Main.isExplicitEnabledOnly()) {
                        enabled = Main.getExplicitEnabled().contains(si.getName());
                        si.setShow(enabled);
                      }
                      si.setEnabled(enabled);
                      Main.getStatistics().addItem(si);
                    }catch(TermWareException ex){
                      throw new ConfigException("Can't read statistic item: "+ex.getMessage(),ex);
                    }
                }else{
                  try {
                    throw new ConfigException("define or calculate term required instead "+TermHelper.termToPrettyString(ct));
                  }catch(TermWareException ex){
                      throw new ConfigException("define term required instead "+ct.getName());
                  }
                }
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

    public AbstractChecker getChecker(String name)  throws CheckerNotFoundException
    {
       AbstractChecker retval = checkers_.get(name);
       if (retval==null) {
           throw new CheckerNotFoundException(name);
       }
       return retval;
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
        boolean checkerEnabled = (Main.isExplicitEnabledOnly() ? Main.getExplicitEnabled().contains("NamePattern") : true);
        ClassChecker nameChecker=new ClassChecker("NamePatterns","style","check name patterns",TermUtils.createString("ua.gradsoft.javachecker.checkers.NamePatternsChecker"),checkerEnabled);
        checkers_.put(nameChecker.getName(),nameChecker);
        facts_.getViolations().addType(nameChecker.getName(),nameChecker.getCategory(),nameChecker.getDescription(),nameChecker.isEnabled(),true);
        checkerEnabled = (Main.isExplicitEnabledOnly() ? Main.getExplicitEnabled().contains("EqualsHashCode") : true);
        ClassChecker equalsHashCodeChecker=new ClassChecker("EqualsHashCode","basic","check that overloaded hashCode and equals are correspond",TermUtils.createString("ua.gradsoft.javachecker.checkers.EqualsHashCodeChecker"),checkerEnabled);
        checkers_.put(equalsHashCodeChecker.getName(),equalsHashCodeChecker);
        facts_.getViolations().addType(equalsHashCodeChecker.getName(),equalsHashCodeChecker.getCategory(),equalsHashCodeChecker.getDescription(),equalsHashCodeChecker.isEnabled(),true);
        InvalidCheckerCommentChecker invalidCheckerCommentChecker = new InvalidCheckerCommentChecker();
        checkers_.put(invalidCheckerCommentChecker.getName(), invalidCheckerCommentChecker);
    }
    
    private String getEtcDirectory() {
        String home=Main.getHome();
        return home!=null ? home+File.separator+"etc" : "./etc" ;
    }
    
    
    private JavaFacts facts_;
    
    private TreeMap<String,AbstractChecker> checkers_;
    
    
}
