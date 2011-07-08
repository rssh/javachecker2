package x;

import java.util.List;

public class X
{

 public static void fun1(List<String> l)
 {
   for(int i=0; i<l.size(); ++i) {
      System.out.println(l.get(i));
   }
 }

 public static void fun2(String[] a)
 {
   for(int i=0; i<a.length; ++i) {
      System.out.println(a[i]);
   }
 }

}
