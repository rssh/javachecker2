package x;


public class Eb<K extends Enum<K> >
{


 public void printDeclaringClass(K x)
 {
 
   System.out.println(x.getDeclaringClass().getName());
   
 }


}