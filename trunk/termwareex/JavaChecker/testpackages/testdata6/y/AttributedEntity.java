package y;

import java.util.Map;
import java.util.HashMap;

public class AttributedEntity<T,E> implements Attributed<T>
{

 public AttributedEntity(E e)
 {
  map_=new HashMap<String, T>();
  entity_=e;
 }

 public T  getAttribute(String name)
 { return map_.get(name); }

 public void  setAttribute(String name, T value)
 { map_.put(name,value); }

 public Map<String,T>  getAttributes()
 { return map_; }

 protected HashMap<String,T> map_;
 protected E entity_;

}