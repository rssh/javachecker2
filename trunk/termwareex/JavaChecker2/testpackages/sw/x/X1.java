package x;


public class X1
{

 void incorrectSwitch1(int x)
 {
    switch(x) {
       case 1:
       case 2:
              System.out.println("x<3");
              break;
/*
   without defalut
       default:
              System.out.println("x>=6");
*/
    }
 }

}
