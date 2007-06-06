package y;


import java.util.TimerTask;

public class Y
{

 private void fun()
 {  System.out.println("fun"); }

 private void x()
 {  System.out.println("x"); }
 
 public int write()
 { 
   x();
 }

 private class YY extends TimerTask
 {
   public void run()
   {
    try {
     Y.this.fun();
     Y.this.x();
    }catch(Exception ex){
      ex.printStackTrace();
    }
   }
 }

/*
 public void runYY(String[] args)
 {
   YY y = new YY();
   y.run();
 }
*/

}
