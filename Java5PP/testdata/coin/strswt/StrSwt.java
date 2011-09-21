

public class StrSwt
{

  void main(String[] argv)
  {
    // block of code from coin proposal.
    switch(argv[1]) {
      case "quux":
         processQuux(s);
         // fall-through
      case "foo":
      case "bar":
          processFooOrBar(s);
          break;
      case "baz":
          processBaz(s);
          // fall-through
      default:
          processDefault(s);
          break;
    }
  }


}
