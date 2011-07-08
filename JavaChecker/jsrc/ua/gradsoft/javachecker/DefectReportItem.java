/*
 * DefectReport.java
 *
 */

package ua.gradsoft.javachecker;

import java.io.PrintStream;
import ua.gradsoft.javachecker.checkers.AbstractChecker;



/**
 *Defect report item.
 * @author  Ruslan Shevchenko
 */
public class DefectReportItem {
    
    /** Creates a new instance of DefectReport */
    public DefectReportItem(String category,
                            String description,
                            FileAndLine marker,
                            AbstractChecker checker)
    {
     category_=category;
     description_=description;
     marker_=marker;
     checker_=checker;
    }
    
    public void println(PrintStream out, ReportFormat format)
    {
     switch(format) {
         case TEXT:
          out.print(marker_.getFname());   
          out.print(":");   
          out.print(marker_.getLine());   
          out.print(":(");   
          out.print(category_);
          out.print(") ");
          out.print(description_);
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
         default:
             // UNKNOWN
          out.println("Warning: unknown output format:"+format);
          // output as text.
          out.print(marker_.getFname());   
          out.print(":");   
          out.print(marker_.getLine());   
          out.print(":(");   
          out.print(category_);
          out.print(") ");
          out.print(description_);
          out.println();                     
     }  
    }
    
    public String getCategory()
    { return category_; }
    
    public String getDescription()
    { return description_; }
    
    public FileAndLine  getFileAndLine()
    { return marker_; }
    
    public AbstractChecker getChecker()
    { return checker_; }

    private String category_;
    private String description_;
    private FileAndLine marker_;
    private AbstractChecker checker_;
}
