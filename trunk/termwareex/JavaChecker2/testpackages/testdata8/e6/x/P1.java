package x;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class P1
{

  public static void wr(String s) throws Exception
  {
    PrintWriter wr = new PrintWriter(new FileOutputStream(s));
    wr.println("qqq");
    wr.close();
  }

}
