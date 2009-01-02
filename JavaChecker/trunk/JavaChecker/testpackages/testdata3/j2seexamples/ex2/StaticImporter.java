package j2seexamples.ex2;

/*
License for Java 1.5 'Tiger': A Developer's Notebook
     (O'Reilly) example package

Java 1.5 'Tiger': A Developer's Notebook (O'Reilly) 
by Brett McLaughlin and David Flanagan.
ISBN: 0-596-00738-8

You can use the examples and the source code any way you want, but
please include a reference to where it comes from if you use it in
your own products or services. Also note that this software is
provided by the author "as is", with no expressed or implied warranties. 
In no event shall the author be liable for any direct or indirect
damages arising in any way out of the use of this software.
*/
import static java.lang.System.err;
import static java.lang.System.out;

import java.io.IOException;
import java.io.PrintStream;

public class StaticImporter {

  public static void writeError(PrintStream err, String msg) 
    throws IOException {
   
    // Note that err in the parameter list overshadows the imported err
    err.println(msg); 
  }

  public static void main(String[] args) {
    out.println("Good morning, " + "java2s");
    out.println("Have a day!");

    try {
      writeError(System.out, "Error occurred.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

