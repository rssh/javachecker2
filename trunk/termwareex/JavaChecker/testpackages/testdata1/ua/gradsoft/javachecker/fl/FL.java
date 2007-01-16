/*
 * FL.java
 *
 * Created on неділя, 29, лютого 2004, 15:02
 */

package ua.gradsoft.javachecker.fl;

/**
 *Holder for file and line markers.
 * @author  Ruslan Shevchenko
 */
public class FL {
    
    /**
     * Creates a new instance of FL
     */
    public FL(String fname, int line) {
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
