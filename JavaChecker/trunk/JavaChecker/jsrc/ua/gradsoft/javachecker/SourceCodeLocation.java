/*
 * SourceCodeLocation.java
 *
 */

package ua.gradsoft.javachecker;

/**
 *
 * @author RSSH
 */
public interface SourceCodeLocation {
    
    public  FileAndLine  getFileAndLine();
    
    public  void  setFileAndLine(FileAndLine fileAndLine);
    
}
