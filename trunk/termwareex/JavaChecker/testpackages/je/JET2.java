/*
 * JET1.java
 *
 */

package testpackages.je;

import java.io.*;

/**
 *This is demo class, which have one problem - method wich throw generic exception
 * TermWare will automatically find this problem for us.
 * @author  Ruslan Shevchenko
 */
public class JET2 {
    

    public static void doSomething() throws Exception
    {
      throw new Exception("qqq");        
    }
    
    
}
