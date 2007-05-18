package x;

import ua.gradsoft.javachecker.annotations.*;

@TypeCheckerProperties(value={"AAA","true"})
public class X
{

  X() { qqq=1; }

  void throwRuntimeException(String message)
  { throw new RuntimeException("message"); }

  public class XX
  {}

  private int qqq;
}
