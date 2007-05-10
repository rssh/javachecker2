package x;

import ann.*;

@A2C("sss")
public class XI1
{

 @A1C
 public class I1
 {
   public int x;
   public int y;
 }

 @A3C(x=1,y=2)
 @A2C("asdfghjkl")
 static public interface I2
 {
   String x();
 }

 @A1C
 enum E
 {
   E1,
   E2,
   E3
 }


}
