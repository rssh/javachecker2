package x;

import java.util.List;

public class X
{

 public int sum(List<Integer> l)
 {
   int sum=0;
   for(Integer x:l) {
     sum+=x;
   }
   return sum;
 }

 public int sum1(int[] arr)
 {
   int sum=0;
   for(int x:arr) {
     sum+=x;
   }
   return sum;
 }


}
