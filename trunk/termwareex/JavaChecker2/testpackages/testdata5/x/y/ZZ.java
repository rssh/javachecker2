package x.y;


import java.io.Serializable;
import java.util.List;
import java.util.LinkedList;


public class ZZ<T> implements Serializable
{


  public List<T> createList(T l1, T l2)
   {  List<T> retval = new LinkedList<T>(); 
      retval.add(l1);
      retval.add(l2);
      return retval;
   }   


  public int plus(int x,int y)
  { return x+y; }

  public static class ZZZ extends ZZ<Integer>
  {
    
    public static int sumList(List<Integer> l)
    {
      int sum=0;
      for(Integer x:l) {
         sum+=x;
      }
      return sum;
    } 

    public static<T> void printList(List<T> l)
    {
       System.out.print("(");
       boolean frs=true;
       for(T t:l) {
         if (!frs) {
           System.out.print(",");
         }else{
           frs=false;
         }
         System.out.print(t.toString());         
       }
       System.out.println(")");
    }
    
  }



  public static void main(String[] args)
  {
    ZZ<Integer> zz = new ZZ<Integer>();
    List<Integer> ab=zz.createList(1,2);
    ZZZ.printList(ab);
    int sum=ZZZ.sumList(ab);
    System.out.println(sum);
  }

  private int x=0;

}