package ua.gradsoft.parsers.java5;


import java.io.StringReader;
import ua.gradsoft.termware.Term;



public class JPPMain
{

  public static void main(String[] args)
  {
      JavaParser parser=null;
      try {
        parser = new JavaParser(new java.io.FileInputStream(args[0]));
      } catch (java.io.FileNotFoundException e) {
        System.err.println("file not found:"+args[0]);
        return;
      }
      try {
        parser.CompilationUnit();
      }catch(ParseException ex){
          System.err.println(ex.getMessage());
          ex.printStackTrace();
      }
      Term t = (Term)parser.jjtree.rootNode();
      t.print(System.out);
  }
  
  public static Term  parseString(String text) throws ParseException
  {
    JavaParser parser=null;
    
    parser = new JavaParser(new StringReader(text));
    parser.CompilationUnit();

    Term t = (Term)parser.jjtree.rootNode();
    return t;

    
  }

}