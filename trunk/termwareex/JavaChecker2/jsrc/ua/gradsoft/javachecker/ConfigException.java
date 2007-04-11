/*
 * ConfigParseException.java
 *
 */

package ua.gradsoft.javachecker;

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

    /**
     * Constructs an instance of <code>ConfigParseException</code> with the specified message
     * and underlaying exception.
     * @param msg the detail message.
     * @parma ex the caused-by exception.
     */
    public ConfigException(String msg,Exception ex) {
        super(msg,ex);
    }
    
    
}
