package j2seexamples.ex4;

/*
Java 2, v5.0 (Tiger) New Features
by Herbert Schildt
ISBN: 0072258543
Publisher: McGraw-Hill/Osborne, 2004
*/

public class VarArgs2 { 
 
  // Here, msg is a normal parameter and v is a varargs parameter. 
  static void vaTest(String msg, int ... v) { 
    System.out.print(msg + v.length + " Contents: "); 
 
    for(int x : v) 
      System.out.print(x + " "); 
 
    System.out.println(); 
  } 
 
  public static void main(String args[])  
  { 
    vaTest("One vararg: ", 10); 
    vaTest("Three varargs: ", 1, 2, 3);  
    vaTest("No varargs: ");  
  } 
}

