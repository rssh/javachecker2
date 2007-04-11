/*
 * EntittyNotFoundException.java
 *
 */

package ua.gradsoft.javachecker;

/**
 *Throwed when we can't find some entity in model-s findXXX method.
 *  (for example - method with some name)
 * @author  Ruslan Shevchenko
 */
public class EntityNotFoundException extends Exception implements SourceCodeLocation
{

    
    
    public EntityNotFoundException(String entityType, String entityName, String msg) {
        super("entity "+entityName +" ("+entityType+") is not found "+msg);
    }
 
    public EntityNotFoundException(String entityType, String entityName, String msg, FileAndLine fileAndLine) {
        super("entity "+entityName +" ("+entityType+") is not found "+msg);
        fileAndLine_=fileAndLine;
    }

    public EntityNotFoundException(String entityType, String entityName, String msg, FileAndLine fileAndLine, Exception ex) {
        super("entity "+entityName +" ("+entityType+") is not found "+msg,ex);
        fileAndLine_=fileAndLine;
    }
    
    
    public FileAndLine getFileAndLine()
    {
        return fileAndLine_;
    }
    
    public void setFileAndLine(FileAndLine fileAndLine)
    {
        fileAndLine_=fileAndLine;
    }
    
    private FileAndLine fileAndLine_=FileAndLine.UNKNOWN;
}
