package x;

public class X
{

 private int unused_=0;
 private int used_=1;

 public void printUsed()
 {
   System.out.println("used_="+used_);
 }

 public static void main(String[] args)
 {
   (new X()).printUsed();
 }
 

}
