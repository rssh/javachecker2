/*
 * DefaultExceptionHandler.java
 *
 */

package ua.gradsoft.javachecker;

import ua.gradsoft.javachecker.annotations.Nullable;

/**
 *Default exception handler.
 * @see JavaCheckerExceptionHandler
 */
public class DefaultExceptionHandler implements JavaCheckerExceptionHandler {

    public void handle(String checkName, String filename, Throwable ex, @Nullable SourceCodeLocation scl) throws ProcessingException
    {
      System.err.println("Exception "+ex.getMessage()+" during check "+checkName+" during processing "+filename);
      if (scl!=null) {
          System.err.println("in file "+scl.getFileAndLine().toString());
      }
      ex.printStackTrace();
      if (ex instanceof Error) {
          if (! (ex instanceof OutOfMemoryError) && ! (ex instanceof StackOverflowError)) {
              throw new ProcessingException("Exception during check",ex);
          }
      }
    }
    
}
