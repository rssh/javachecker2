/*
 * DefectReport.java
 *
 * Created on неділя, 29, лютого 2004, 14:40
 */

package ua.gradsoft.javachecker;

import java.io.PrintStream;



/**
 *Defect report item.
 * @author  Ruslan Shevchenko
 */
public class DefectReportItem {
    
    /** Creates a new instance of DefectReport */
    public DefectReportItem(String category,String description, FileAndLine marker) 
    {
     category_=category;
     description_=description;
     marker_=marker;
    }
    
    public void println(PrintStream out, ReportFormat format)
    {
     switch(format) {
         case TEXT:
          out.print(category_);
          out.print(":");
          out.print(description_);
          out.print("(file:");
          out.print(marker_.getFname());
          out.print(",line:"+marker_.getLine());
          out.print(")");
          out.println();
          break;
         case HTML:
             out.print("<tr>");
             out.print("<td>");
             out.print(category_);
             out.print(":");
             out.print("</td><td>");
             out.print(description_);
             out.print("</td><td>");
             out.print("(file:");
             out.print(marker_.getFname());
             out.print(",line:"+marker_.getLine());
             out.print(")");
             out.println("</td>");
             out.println("</tr>");
           break;
     }  
    }
    
    public String getCategory()
    { return category_; }
    
    public String getDescription()
    { return description_; }
    
    
    private String category_;
    private String description_;
    private FileAndLine marker_;
}
