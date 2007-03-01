package hd;

public class Hd3
{

 public static void main(String[] args)
 {
  final String[] otherArgs={ "qqq", "qqq1" };
  Runnable myRunnable = new Runnable() {
    public void run() {
      for(String s: otherArgs) {
         System.out.println(s);
      }
    }
  };
 }

}