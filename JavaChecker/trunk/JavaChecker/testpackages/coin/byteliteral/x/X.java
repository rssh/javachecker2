package x;

public class X
{
  int x()
  {
    byte b1 = (byte)122;
    byte b2 = (byte)'\013';
    return b1+b2;
  }

  int x2()
  {
    byte b2 = (byte)'\010';
    return (int)b2;
  }
}
