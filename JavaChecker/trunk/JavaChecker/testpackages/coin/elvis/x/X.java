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
  }

}
