/*
 * FileAndLine.java
 *
 * Created on неділя, 29, лютого 2004, 15:02
 */

package ua.gradsoft.javachecker;

/**
 *Holder for file and line markers.
 * @author  Ruslan Shevchenko
 */
public class FileAndLine {
    
    /** Creates a new instance of FileAndLine */
    public FileAndLine(String fname, int line) {
        fname_=fname;
        line_=line;
    }
    
    public String getFname()
    { return fname_; }
    
    public int getLine()
    { return line_; }
    
    public static final FileAndLine UNKNOWN = new FileAndLine("unknown",-1);
    
    
    
    private String fname_;
    private int    line_;

}
