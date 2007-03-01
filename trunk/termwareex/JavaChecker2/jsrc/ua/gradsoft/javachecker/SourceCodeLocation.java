/*
 * SourceCodeLocation.java
 *
 * Created on 24 Февраль 2007 г., 15:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
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
