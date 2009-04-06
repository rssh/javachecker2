package x;

class X {

  public static void handleObject(Object o)
  {
    if (o instanceof Number) {
      System.out.println("o is Number");
    } else if (o instanceof Class) {
      System.out.println("o is Class");
    } else if (o instanceof X) {
      System.out.println("o is X");
    } else {
      System.out.println("o is "+o.getClass());
    }
  }

}
