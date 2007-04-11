/*
 * ViolationSummary.java
 *
 *
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
    
    public void report(PrintStream out, ReportFormat format)
    {
      if (enabled_) {
       switch(format)   {
           case TEXT:   
               out.print(description_);
               out.println("\t:\t"+counter_);
               break;
           case HTML:
               out.print("<tr><td>");
               out.print(description_);
               out.print("</td><td>");
               out.print(counter_);
               out.print("</td></tr>");
               break;
       }
      }
    }
    
    private String name_;
    private String description_;
    private String category_;
    private int    counter_;
    private boolean enabled_;
    private boolean enabledByDefault_;
    
}
