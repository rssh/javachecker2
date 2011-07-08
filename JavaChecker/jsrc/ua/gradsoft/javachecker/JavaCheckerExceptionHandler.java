/*
 * JavaCheckerExceptionHandler.java
 *
 *Part of JavaChecker
 */

package ua.gradsoft.javachecker;

import ua.gradsoft.javachecker.annotations.Nullable;

/**
 *Exception handlers for exceptions, which are thrown from checkers.
 * @author rssh
 */
public interface JavaCheckerExceptionHandler {

    public void handle(String checkName, String filename, Throwable ex, @Nullable SourceCodeLocation scl) throws ProcessingException;
    
    
}
