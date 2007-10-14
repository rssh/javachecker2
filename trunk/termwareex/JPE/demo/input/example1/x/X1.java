package x;

public class X
{
  public static void main(String[] args)
  {
   if (CompileTimeConstants.DEBUG) {
      debug("begin");
   }
   System.out.println("inside");
   if (CompileTimeConstants.DEBUG) {
      debug("end");
   }
  }

  private static void debug(String message)
  {
   System.out.println(message);
  }

}
