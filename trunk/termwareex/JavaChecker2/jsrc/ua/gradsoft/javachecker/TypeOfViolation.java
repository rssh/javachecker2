/*
 * ViolationSummary.java
 *
 * Created on понеділок, 17, травня 2004, 4:46
 */

package ua.gradsoft.javachecker;

import java.io.*;
import java.util.prefs.*;

/**
 * Type of violation
 */
public class TypeOfViolation {
    
    /** Creates a new instance of ViolationSummary 
     *@param name   - name of such statistics
     *@param category - category of checks.
     */
    public TypeOfViolation(String name, String category, String description, boolean enabledByDefault) {
        name_=name;
        category_=category;
        description_=description;
        enabledByDefault_=enabledByDefault;
        counter_=0;
        enabled_=enabledByDefault;
    }
    
    public  void readPreferences(JavaFacts facts)
    {
      enabled_=facts.getBooleanConfigValue("Check"+name_, enabledByDefault_);  
    }
    
    public  String getName()
    { return name_; }
    
    public  String getCategory()
    { return category_; }
    
    public  boolean isEnabled()
    { return enabled_; }
    
    public  void  setEnabled(boolean enabled)
    { enabled_=enabled; }
    
    public  int getCounter()
    { return counter_; }
    
    public  void  increment()
    { ++counter_; }
    
    public void report(PrintStream out)
    {
      if (enabled_) {
        out.print(description_);
        out.println("\t:\t"+counter_);
      }
    }
    
    private String name_;
    private String description_;
    private String category_;
    private int    counter_;
    private boolean enabled_;
    private boolean enabledByDefault_;
    
}
