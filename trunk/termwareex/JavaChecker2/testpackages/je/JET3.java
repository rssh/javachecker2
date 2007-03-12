package je;

public class JET3 
{



    public static void main(String[] args)
    {
      try {
       doSomething();
      }catch(Exception ex){
        ex.printStackTrace();
      }
    }

    public static void doSomething() throws Exception
    {
      throw new Exception("qqq");        
    }



}