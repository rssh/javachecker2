/*
 * ReportFormat.java
 *
 */

package ua.gradsoft.javachecker;

/**
 *Set of possible formats for report
 * @author RSSH
 */
public enum ReportFormat {
    

    TEXT("text"),
    HTML("html"),
    XML("xml")
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
