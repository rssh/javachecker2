package x;

public class StrSwt
{

  void main(String[] argv)
  {
    // block of code from string in switch coin proposal.
    switch(argv[1]) {
      case "quux":
         processQuux(argv[2]);
         // fall-through
      case "foo":
      case "bar":
          processFooOrBar(argv[2]);
          break;
      case "baz":
          processBaz(argv[2]);
          // fall-through
      default:
          processDefault(argv[2]);
          break;
    }
  }

  void processQuux(String s)
  { System.out.println("quux:"+s); }

  void processFooOrBar(String s)
  { System.out.println("fooOrBar:"+s); }

  void processBaz(String s)
  { System.out.println("baz:"+s); }

  void processDefault(String s)
  { System.out.println("default:"+s); }

}
