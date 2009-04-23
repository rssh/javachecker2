package x;

public class X
{

  public int v1(byte x)
  {
    return x & 0xff ;
  }

  public int v2(byte x)
  {
    return x & 255 ;
  }

  public int v3(int x)
  {
    return (0xff & x);
  }

}
