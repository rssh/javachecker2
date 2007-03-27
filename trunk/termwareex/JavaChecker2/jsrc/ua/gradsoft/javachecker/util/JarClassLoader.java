/*
 * JarClassLoader.java
 *
  */

package ua.gradsoft.javachecker.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *Simple JarClassLoader, for accessing classes from supplied jars.
 * @author RSSH
 */
public class JarClassLoader extends URLClassLoader
{
    
    /** Creates a new instance of JarClassLoader */
    public JarClassLoader(String[] jarNames) throws MalformedURLException
    {
        super(jarURLs(jarNames));
    }
    
    
    
    private static URL[] jarURLs(String[] jarNames) throws MalformedURLException
    {        
        URL[] retval = new URL[jarNames.length];
        for(int i=0; i<jarNames.length; ++i) {
          retval[i] = new URL("file","",jarNames[i]);
        }             
        return retval;
    }
    
    
}
