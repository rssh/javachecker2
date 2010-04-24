
package ua.gradsoft.models.php5;

import java.util.Map;

/**
 *Helper for static operations on PHP values.
 * @author rssh
 */
public class PhpValueHelper {

    public static int looseComparison(PhpValueModel x, PhpValueModel y, PhpEvalEnvironment pee)
    {
//    BOOLEAN(1),
//    INTEGER(2),
//    FLOAT(3),
//    STRING(4),
//    ARRAY(5),
//    OBJECT(6),
//    RESOURCE(7),
//    NULL(8);
        
      switch(x.getType()) {
          case BOOLEAN:
              return (x.getBoolean() ? 1 : 0) - (y.getBoolean() ? 1 : 0);
          case INTEGER:
              switch(y.getType()) {
                  case BOOLEAN:
                      return (x.getBoolean() ? 1 : 0) - (y.getBoolean() ? 1 : 0);
                  case INTEGER:    
                      return x.getInt() - y.getInt();
                  case FLOAT:
                      return (int)Math.signum(x.getFloat()-y.getFloat());
                  case STRING:
                  case RESOURCE:        
                      return x.getInt() - y.getInt();
                  case ARRAY:
                  case OBJECT:
                      return -1;
                  case NULL:
                      return (x.getBoolean() ? 1 : 0);
              } 
              break;
          case FLOAT:
              switch(y.getType()) {
                  case BOOLEAN:
                      return (x.getBoolean() ? 1 : 0) - (y.getBoolean() ? 1 : 0);
                  case INTEGER:
                  case FLOAT:
                  case STRING:
                      return (int)Math.signum(x.getFloat()-y.getFloat());
                  case ARRAY:
                  case OBJECT:
                      return -1;
                  case NULL:
                      return (x.getBoolean() ? 1 : 0);
              }
              break;
          case STRING:
          case RESOURCE:
              switch(y.getType()) {
                  case BOOLEAN:
                      return (x.getBoolean() ? 1 : 0) - (y.getBoolean() ? 1 : 0);
                  case INTEGER:
                  case FLOAT:
                  case RESOURCE:
                      return (int)Math.signum(x.getFloat()-y.getFloat());
                  case STRING:
                      return x.getString(pee).compareTo(y.getString(pee));
                  case ARRAY:
                  case OBJECT:
                      return -1;
                  case NULL:
                      return (x.getBoolean() ? 1 : 0);
              }
              break;
          case ARRAY:
              switch(y.getType()) {
                  case BOOLEAN:
                  case INTEGER:
                  case FLOAT:
                  case STRING:
                  case RESOURCE:
                      return 1;
                  case ARRAY:
                  {
                      
                      int c=x.getArray(pee).size() - y.getArray(pee).size();
                      if (c!=0) return c;
                      for(Map.Entry<PhpValueModel,PhpValueModel> e: x.getArray(pee).getMap().entrySet()) {
                          PhpValueModel key = e.getKey();
                          PhpValueModel yv = y.getArray(pee).getMap().get(key);
                          if (yv==null) {
                              return 1;
                              //throw new NotComparableException();
                          }
                          c = PhpValueHelper.looseComparison(e.getValue(), yv, pee);
                          if (c!=0) return c;
                      }
                      return 0;
                  }
                  case OBJECT:
                      return -1;
                  case NULL:
                      return (x.getBoolean() ? 1 : 0);
              }
              break;
          case OBJECT:
              if (y.getType()!=PhpType.OBJECT) {
                  return 1;
              }else{
                  PhpObjectModel xm = x.getObject(pee);
                  PhpObjectModel ym = y.getObject(pee);
                  int c = xm.getClassDeclaration().getName().compareTo(ym.getClassDeclaration().getName());
                  if (c!=0) return c;
                  for(Map.Entry<String,PhpValueModel> e: xm.getMemberVariables().entrySet()) {
                      PhpValueModel yValue = ym.getMemberVariables().get(e.getKey());
                      if (yValue==null) {
                          return 1;
                      }
                      c = PhpValueHelper.looseComparison(e.getValue(), yValue, pee);
                      if (c!=0) {
                          return c;
                      }
                  }
                  return c;
              }
          case NULL:
              return (y.getBoolean() ? -1 : 0);
      }
    }

}
