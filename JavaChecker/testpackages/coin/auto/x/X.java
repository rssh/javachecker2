package x;

public class X
{

  public static void test1()
  {
   int x=0;
   int y;
  }

  public static void test2()
  {
    List<String> x = test3();
  }

  public List<String> test3()
  {
    List<String> retval = new ArrayList<String>();
    retval.add("1");
    retval.add("2");
    retval.add("3");
    return retval;
  }

  public VeryLongTypeName qqq = new VeryLongTypeName(10);

}
