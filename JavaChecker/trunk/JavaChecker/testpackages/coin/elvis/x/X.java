package x;

public class X
{

  int main(String[] args) 
  {
    int al;
    if (args!=null) {
      al=args.length();
    }else{
      al=0;
    }

    int al2 = (args!=null ? args.length() : 1 );

    int al3 = ((args==null) ? 1 : args.length() );

    String nArgs = (args[0]==null ?  null : args[0]);

  }

  String  fun1(String s)
  {
    String retval=null;
    if (s!=null) {
        retval=s.replace("*","A");
    }
  }

  
  String  fun2(String s)
  {
    if (s!=null) {
        s.replace("*","A");
    }
  }

  String safeIndex(String[] ss, int i)
  {
    return (ss!=null) ? ss[i] : null;
  }


}
