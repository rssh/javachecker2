package x.y;


import java.util.List;
import java.util.LinkedList;


public class Pair<T,U extends T>
{

  public Pair()
  { t=null; u=null; }

  public Pair(T tt, U uu)
  { t=tt; u=uu; }

  public List<T> getAsList()
   {
     LinkedList<T> retval = new LinkedList<T>();
     retval.add(t);
     retval.add(u);
     return retval;
   }

  public T  getT()
   { return t; }

  public U  getU()
   { return u; }


  private T t;
  private U u;
}