package x;

import java.util.Collection;

public class Util
{

  public static String printListOfPhoneNumbers(Collection<String> numbers)
    {
       StringBuilder retval=new StringBuilder();
       boolean frs=true;
       for(String pn: numbers) {
           if (frs) {
               frs=false;
           }else{
               retval.append(", ");
           }
           retval.append(pn);
       }
       return retval.toString();
    }

}
