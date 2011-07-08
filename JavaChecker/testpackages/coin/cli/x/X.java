package x;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;


public class X
{

  public List<String> l = Arrays.asList("a","b","c","d");

  public static Map<String,String> a = new HashMap<String,String>();
  static {
     a.put("x1","x2");
     a.put("x2","x3");
     a.put("x3","x4");
  };

}
