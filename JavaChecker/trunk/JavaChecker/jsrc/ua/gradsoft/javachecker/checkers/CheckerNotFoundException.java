package ua.gradsoft.javachecker.checkers;

/**
 *Throwed when we can't find checker with given name,
 * @author rssh
 */
public class CheckerNotFoundException extends Exception
{
   public CheckerNotFoundException(String name)
   { super("Checker with string '"+name+"' is not found"); }

}
