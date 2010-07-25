package x;

import java.util.Arrays;

public class X
{

 public static void main() {
    String[]  x = new String[2];
    x[0]=("1111");
    x[1]=("2222");
    String s = Util.printListOfPhoneNumbers(Arrays.<String>asList(x));
    System.err.println(s);
 }

}
