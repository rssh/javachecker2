/*
 * Violations.java
 *
 */

package ua.gradsoft.javachecker;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Set of violations, found in source
 */
public class Violations {
    
    /** Creates a new instance of Violations */
    public Violations() {
        byTypes_=new TreeMap<String,TypeOfViolation>();
    }
    
    public void addType(String typeOfViolation, String category, String description, boolean enabledByDefault, boolean show)
    {
        TypeOfViolation o=byTypes_.get(typeOfViolation);
        if (o==null) {
            o=new TypeOfViolation(typeOfViolation,category,description,enabledByDefault,show, Main.getStatistics());
            byTypes_.put(typeOfViolation, o);
            Main.getStatistics().addItem(o);
        }
    }
    
    
    public  void readPreferences(JavaFacts facts)
    {        
        Iterator it=byTypes_.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry me=(Map.Entry)it.next();
            TypeOfViolation v = (TypeOfViolation)me.getValue();
            v.readPreferences(facts);
        }
        
                        // add special meta-property
        boolean checkNamePatterns = facts.getBooleanConfigValue("CheckNamePatterns",true);
        if (!checkNamePatterns) {
            String [] namePatterns =  {"VariablePatterns",                                      
                                        "ClassNamePatterns",
                                       "MethodNamePatterns",
                                       "EnumConstantNamePatterns",
                                       "TypeArgumentNamePatterns"};            
            for(String s : Arrays.asList(namePatterns)) {
                TypeOfViolation v = byTypes_.get(s);
                if (v!=null) {
                    v.setEnabled(false);
                }
            }
        }

        
    }
    
    public  String getCategory(String typeOfViolation) 
    {
        Object o=byTypes_.get(typeOfViolation);
        if (o==null) {
            return "uncategorized";
        }
        TypeOfViolation v=(TypeOfViolation)o;
        return v.getCategory();
    }
    
    public  void discovered(String typeOfViolation)
    {
        Object o=byTypes_.get(typeOfViolation);
        if (o==null) {
            o=byTypes_.get("*");
        }
        TypeOfViolation v=(TypeOfViolation)o;
        v.increment();
        return;
    }
    
    public  boolean enabled(String typeOfViolation)
    {
        TypeOfViolation tov=byTypes_.get(typeOfViolation);
        if (tov==null){
            // ? - may be throw
            return false;
        }
        return tov.isEnabled();
    }
    
    public void  setEnabled(String typeOfViolation, boolean value) {
        TypeOfViolation tov=byTypes_.get(typeOfViolation);
        if (tov==null) {
            // ? - may be throw or log
            return;
        }
        tov.setEnabled(value);
    }
    
    public int  getCounterByName(String name)
    {
        TypeOfViolation tov=byTypes_.get(name);
        if (tov==null) {
            return 0;
        }else{
            return tov.getCounter();
        }
    }
    
   
    public  void report(PrintStream out, ReportFormat format)
    {
        if (format==ReportFormat.HTML) {
            out.println("<table>");
        }
        for(Map.Entry<String,TypeOfViolation> me : byTypes_.entrySet()) {
            if (me.getValue().isEnabled()) {
               //System.err.println("enabled "+me.getValue().getName());
               try {
                me.getValue().report(out,format,StatisticScope.ALL);
               }catch(TermWareException ex){
                   throw new TermWareRuntimeException(ex);
               }
            }
        }
        if (format==ReportFormat.HTML) {
            out.print("</table>");
        }
        
    }
    
    
    private TreeMap<String,TypeOfViolation> byTypes_;
    
}
