/*
 * FileAndLine.java
 *
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
    
    public boolean equals(Object o)
    {
        if (!(o instanceof FileAndLine)) {
            return false;
        }else{
            FileAndLine flo = (FileAndLine)o;
            return flo.getFname().equals(fname_)&&flo.getLine()==line_;
        }
    }
    
    public int hashCode()
    { return fname_.hashCode()+line_; }
    
    public String toString()
    {
       return fname_+":"+line_; 
    }
    
    private String fname_;
    private int    line_;

}
