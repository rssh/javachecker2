package x.y;


public class Z extends Y
{

  
  public class E {


    public String getX()
    {
      return x;
    }

    public String getZX()
    {
      return Z.this.x;
    }

    public Y createY()
    {
      return new Y();
    }

    private String x="e-qqq";


  }

  public static void main(String args[])
  {

    E e = new Z().new E();
    System.out.println(e.getX());
    System.out.println(e.getZX());
    System.out.println(e.createY().getX());
  }


  private  java.lang.String x = "z-qqq";

}