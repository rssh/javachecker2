package x;

public class X
{

 public static void main()
 {
   Y y = new Y("hello");
   y_=y;
   System.out.println(y.getMessage());
 }

 private Y y_;

}

class Y
{

 Y(String message)
 {
  message_=message;
 }

 public String getMessage()
 { return message_; }

 private String message_;

}

