/*
 * FileAndLine.java
 *
 * Created on �����, 29, ������ 2004, 15:02
 */

package ua.kiev.gradsoft.JavaChecker;

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
    
    private String fname_;
    private int    line_;

}
