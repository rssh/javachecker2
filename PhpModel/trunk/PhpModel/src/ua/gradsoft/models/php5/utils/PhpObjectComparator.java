
package ua.gradsoft.models.php5.utils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import ua.gradsoft.models.php5.PhpEvalEnvironment;
import ua.gradsoft.models.php5.PhpValueModel;

/**
 *Comparator for php objects
 * @author rssh
 */
public class PhpObjectComparator implements Comparator<PhpValueModel> {

    public PhpObjectComparator(PhpEvalEnvironment pee)
    {
        this.pee=pee;
    }


    public int compare(PhpValueModel x, PhpValueModel y) {
       int c1 = x.getType().getOrder() - y.getType().getOrder();
       if (c1!=0) {
           return c1;
       }
       switch(x.getType()) {
           case BOOLEAN:
               if (x.getBoolean()) {
                   if (y.getBoolean()) {
                       return 0;
                   } else {
                       return 1;
                   }
               } else {
                   if (y.getBoolean()) {
                       return -1;
                   } else {
                       return 0;
                   }
               }
           case INTEGER:
               return x.getInt()-y.getInt();
           case FLOAT:
           {
               double fc = x.getFloat() - y.getFloat();
               if (fc < 0) {
                   return -1;
               }else if (fc > 0) {
                   return 1;
               }else{
                   return 0;
               }
           }
           case STRING:
               return x.getString().compareTo(y.getString());
           case ARRAY:
           {
               int c = x.getArray().size() - y.getArray().size();
               if (c!=0) return c;
               c = x.getArray().getMap().hashCode() - y.getArray().getMap().hashCode();
               if (c!=0) return c;
               Iterator<Map.Entry<PhpValueModel,PhpEntityModel>> itx = x.getArray().getMap().entrySet().iterator();
               Iterator<Map.Entry<PhpValueModel,PhpEntityModel>> ity = y.getArray().getMap().entrySet().iterator();
               while(itx.hasNext()) {
                   Map.Entry<PhpValueModel,PhpEntityModel> ex = itx.next();
                   Map.Entry<PhpValueModel,PhpEntityModel> ey = ity.next();
                   c=INSTANCE.compare(ex.getKey(), ey.getKey());
                   if (c!=0) return c;
                   c=INSTANCE.compare(ex.getValue().getValue(), ey.getValue().getValue());
                   if (c!=0) return c;
               }
               return c;
           }
           case OBJECT:
           {

           }


       }
    }


    private PhpEvalEnvironment pee;
}
