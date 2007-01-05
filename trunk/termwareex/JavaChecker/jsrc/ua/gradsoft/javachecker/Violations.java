/*
 * Violations.java
 *
 * Created on понеділок, 17, травня 2004, 4:54
 */

package ua.gradsoft.javachecker;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 *
 */
public class Violations {
    
    /** Creates a new instance of Violations */
    public Violations() {
        byTypes_=new HashMap<String,TypeOfViolation>();
    }
    
    public void addType(String typeOfViolation, String category, String description, boolean enabledByDefault)
    {
        TypeOfViolation o=byTypes_.get(typeOfViolation);
        if (o==null) {
            o=new TypeOfViolation(typeOfViolation,category,description,enabledByDefault);
            byTypes_.put(typeOfViolation, o);
        }
    }
    
    
    public  void readPreferences(Preferences prefs)
    {        
        Iterator it=byTypes_.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry me=(Map.Entry)it.next();
            TypeOfViolation v = (TypeOfViolation)me.getValue();
            v.readPreferences(prefs);
        }
        
                        // add special meta-property
        boolean checkNamePatterns = prefs.getBoolean("CheckNamePatterns",true);
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
    
    
    public int  getCounterByName(String name)
    {
        TypeOfViolation tov=byTypes_.get(name);
        if (tov==null) {
            return 0;
        }else{
            return tov.getCounter();
        }
    }
    
    
    public  void report(PrintStream out)
    {
        for(Map.Entry<String,TypeOfViolation> me : byTypes_.entrySet()) {
            me.getValue().report(out);
        }
    }
    
    
    private HashMap<String,TypeOfViolation> byTypes_;
    
}
