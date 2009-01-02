package j2seexamples.ex1;

/*
Java 2, v5.0 (Tiger) New Features
by Herbert Schildt
ISBN: 0072258543
Publisher: McGraw-Hill/Osborne, 2004
*/
 
import static java.lang.Math.sqrt; 
import static java.lang.Math.pow; 
 
// Compute the hypotenuse of a right triangle. 
public class Hypot { 
  public static void main(String args[]) { 
    double side1, side2; 
    double hypot; 
 
    side1 = 3.0; 
    side2 = 4.0; 

    //!!!
    int x = pow(side1,2);
 
    // Here, sqrt() and pow() can be called by themselves, 
    // without their class name. 
    hypot = sqrt(pow(side1, 2) + pow(side2, 2)); 
 
    System.out.println("Given sides of lengths " + 
                       side1 + " and " + side2 + 
                       " the hypotenuse is " + 
                       hypot); 
  } 
}
