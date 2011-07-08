package x;

import java.io.FileOutputStream;
import java.io.PrintWriter;

public class P2
{

  public static void wr(String s) throws Exception
  {
    PrintWriter wr = new PrintWriter(new FileOutputStream(s));
    wr.println("qqq");
    // nust report about unclosed here.
    //wr.close();
  }

}
