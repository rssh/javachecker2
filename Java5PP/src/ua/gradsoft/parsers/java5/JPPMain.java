package ua.gradsoft.parsers.java5;


import java.io.StringReader;
import ua.gradsoft.parsers.java5.jjt.JJTJavaParser;
import ua.gradsoft.parsers.java5.jjt.ParseException;
import ua.gradsoft.termware.Term;




public class JPPMain
{

  public static void main(String[] args)
  {
      JJTJavaParser parser=null;
      try {
        parser = new JJTJavaParser(new java.io.FileInputStream(args[0]));
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
      Term t = (Term)parser.getRootNode();
      
      t.print(System.out);
      //System.out.println("qqq");
  }
  
  public static Term  parseString(String text) throws ParseException
  {
    JJTJavaParser parser=null;
    
    parser = new JJTJavaParser(new StringReader(text));
    parser.CompilationUnit();

    Term t = parser.getRootNode();
    return t;

    
  }

}