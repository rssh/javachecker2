package x;

public class X
{

  int main(String[] args)
  {
    try {
      doSomething();
    } catch (Exception1 ex) {
      handleEx(ex); 
    } catch (Exception2 ex) {
      handleEx(ex);
    }
     

  }

}
