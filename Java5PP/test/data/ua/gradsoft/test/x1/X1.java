package data.ua.gradsoft.test.x1;


import ua.gradsoft.termware.*;


class X1 {


  public int getX()
    { return x; }

  public void setX(int x)
    { this.x=x; }

  public int getSX()
   { return SX; }

  public int[] idArr(int[][] x, int y)
  {
      return x[y];
  }
  
  public int[] idArr1(int x[][], int y)
  {
      return x[y];
  }
  
  public void funWithException() throws TermWareException
  {
    throw new TermWareException("aaa");
  }


  private int x;
  public final static int SX=10;

}