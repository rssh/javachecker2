package testpackages.sv;


public class Sv1
{

  public synchronized void setMyVariable(int x)
  {
   myVariable_=x;
  }

  public void incrementMyVariable()
  {
   ++myVariable_;
  }

  private int myVariable_=0;
}