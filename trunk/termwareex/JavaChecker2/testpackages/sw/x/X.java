package x;


public class X
{

 void incorrectSwitch(int x)
 {
    switch(x) {
       case 1:
       case 2:
              System.out.println("x<3");
              break;
       case 3:
              System.out.println("x<4");
              throw new Exception("qqq");
       case 4:
              System.out.println("x<5");
       case 5:
              System.out.println("x<6");
              return;
       default:
              System.out.println("x>=6");
    }
 }

}
