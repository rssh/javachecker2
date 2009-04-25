package x;

public class Z
{
 public Y  y;

 public static Integer qqq(Z[] z, int i)
 {
   if ( z!=null && z[i]!=null && z[i].y!=null) {
     return z[i].y.a;
   } else {
     return null;
   }
 }

}
