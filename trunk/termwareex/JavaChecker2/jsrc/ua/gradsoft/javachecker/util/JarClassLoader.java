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
    public JarClassLoader(String jarName) throws MalformedURLException
    {
        super(oneJarURL(jarName));
    }
    
    
    private static URL[] oneJarURL(String jarName) throws MalformedURLException
    {        
        URL url = new URL("file","",jarName);
        URL[] retval = new URL[1];
        retval[0]=url;
        return retval;
    }
    
}
