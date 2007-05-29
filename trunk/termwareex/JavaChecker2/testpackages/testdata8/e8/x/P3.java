package x;

import java.io.PrintStream;
import java.io.PrintWriter;

public class P3
{

  public static void wr(PrintStream s) throws Exception
  {
    PrintWriter wr = new PrintWriter(s);
    wr.println("qqq");
    // not-closing PrintStream is *not* a resource leak
    //wr.close();
  }

}
