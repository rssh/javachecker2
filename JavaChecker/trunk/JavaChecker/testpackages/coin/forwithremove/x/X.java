package x;

import java.util.Iterator;
import java.util.Collection;

public class X
{

  public static<T> void removeIf(Collection<T> x, Predicat<T> predicat)
  {
    T e=null;
    Iterator it = x.iterator();
    while(it.hasNext()) {
       e=it.next();
       if (predicat.condition(e)) {
         it.remove();
       }
    }
  }

}
