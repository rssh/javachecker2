package testpackages.sv;


public class Sv2
{

  public synchronized void setMyVariable(int x)
  {
   myVariable_=x;
  }

  public void incrementMyVariable()
  {
   synchronized(this) {
     ++myVariable_;
   }
  }

  private int myVariable_=0;
}