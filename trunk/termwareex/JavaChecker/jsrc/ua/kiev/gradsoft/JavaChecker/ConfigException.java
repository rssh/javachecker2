/*
 * ConfigParseException.java
 *
 * Created on середа, 18, лютого 2004, 3:02
 */

package ua.kiev.gradsoft.JavaChecker;

/**
 *This exception is thrown, when we can't parse config file.
 * @author  Ruslan Shevchenko
 */
public class ConfigException extends Exception {
    
    
    /**
     * Constructs an instance of <code>ConfigParseException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ConfigException(String msg) {
        super(msg);
    }
    
}
