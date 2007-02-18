package y;

import java.util.Map;
import java.util.HashMap;

public class AttributedIndexedEntity<T,E> extends AttributedEntity<T,E> implements AttributedIndexed<T>
{

 public AttributedIndexedEntity(E e)
 { super(e); }

 public T  getAt(int i)
 { return map_.get("_"+i); }

 public void  setAt(int i, T value)
 { map_.put("_"+i,value); }


}