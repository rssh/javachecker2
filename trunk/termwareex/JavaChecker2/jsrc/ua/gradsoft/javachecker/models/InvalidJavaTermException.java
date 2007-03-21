/*
 * InvalidJavaTermException.java
 *
 * Created on субота, 20, січня 2007, 2:39
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import ua.gradsoft.javachecker.FileAndLine;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.SourceCodeLocation;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Throws when we get somehwere invalid or non-compiling term
 * @author Ruslan Shevchenko
 */
public class InvalidJavaTermException extends AssertException implements SourceCodeLocation
{

    public InvalidJavaTermException(String message, FileAndLine where, Exception ex)
    {        
      super(message, ex);  
      fileAndLine_=where;
    }
    
    public InvalidJavaTermException(String message, Term t, Exception ex)
    {
     super(message+":"+TermHelper.termToString(t),ex);   
     try {
       fileAndLine_=JUtils.getFileAndLine(t);
     }catch(TermWareException ex1){
         throw new TermWareRuntimeException(ex1);
     }
    }

    
    public InvalidJavaTermException(String message, Term t)
    {
     super(message+":"+TermHelper.termToString(t));   
     try {
       fileAndLine_=JUtils.getFileAndLine(t);
     }catch(TermWareException ex){
         throw new TermWareRuntimeException(ex);
     }
    }
    
    public FileAndLine getFileAndLine()
    {
      return fileAndLine_;  
    }
    
    public void setFileAndLine(FileAndLine fileAndLine)
    {
       fileAndLine_=fileAndLine;
    }
    
    private FileAndLine fileAndLine_ = FileAndLine.UNKNOWN;
    
}
