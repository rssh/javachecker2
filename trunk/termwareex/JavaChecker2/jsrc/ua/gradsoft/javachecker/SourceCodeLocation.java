/*
 * SourceCodeLocation.java
 *
 * Created on 24 ������� 2007 �., 15:09
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
