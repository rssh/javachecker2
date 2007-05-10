package x;


import ann.*;

@A3C(x=8,y=9)
@A4C(
  xy=@A3A(x=99,y=22), z=9, xys={@A3C(x=11,y=11),@A3C(x=12,y=12)}
)
public class XM {

  @A1M(q="aaa")
  public static void main(String[] args)
  {
    System.out.print("AAA:");
  }


  @A1M(q="bbb")
  public static void main1(String[] args)
  {
    System.out.print("BBB:");
  }
  

}

