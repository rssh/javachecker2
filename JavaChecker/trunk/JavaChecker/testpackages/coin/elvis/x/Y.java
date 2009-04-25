package x;

public class Y
{

 public static int checkA(Y y)
 {
  return y==null ? null :  y.a;
 }

 public static int checkAA()
 {
  return getA()==null ? null :  getA().a;
 }

 public static Y getA()
 {
  return a_;
 }

 public Integer a;
 
 private static Y a_ = new Y();

}
