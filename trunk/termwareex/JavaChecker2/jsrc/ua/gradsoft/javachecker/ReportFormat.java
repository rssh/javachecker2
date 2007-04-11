/*
 * ReportFormat.java
 *
 * Created on 24 Март 2007 г., 8:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker;

/**
 *
 * @author RSSH
 */
public enum ReportFormat {
    

    TEXT("text"),
    HTML("html")
    ;
    
    ReportFormat(String name)
    {
      name_=name;  
    }
    
    public static ReportFormat getByName(String name) throws ConfigException
    {       
        for(ReportFormat rf: values()) {
            if (rf.getName().equals(name)) {        
                return rf;
            }
        }
        throw new ConfigException("name of output format ("+name+") is invalid");
    }
    
    public String getName()
    { return name_; }       
    
    private String name_;
}
