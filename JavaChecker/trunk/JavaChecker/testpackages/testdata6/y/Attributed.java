package y;

import java.util.Map;

public interface Attributed<T>
{

 public T  getAttribute(String name);

 public void  setAttribute(String name, T value);

 public Map<String,T>  getAttributes();

}