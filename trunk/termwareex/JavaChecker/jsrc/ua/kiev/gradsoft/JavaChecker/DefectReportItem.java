/*
 * DefectReport.java
 *
 * Created on �����, 29, ������ 2004, 14:40
 */

package ua.kiev.gradsoft.JavaChecker;

import java.io.*;

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
    
    public void println(PrintStream out)
    {
     out.print(category_);
     out.print(":");
     out.print(description_);
     out.print("(file:");
     out.print(marker_.getFname());
     out.print(",line:"+marker_.getLine());
     out.print(")");
     out.println();
    }
    
    private String category_;
    private String description_;
    private FileAndLine marker_;
}
