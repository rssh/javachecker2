package x;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ForgottenFile
{

 public static void main(String[] args)
 {
  try {
   FileReader reader = new FileReader(args[1]);
   int ch=reader.read();
   int q=0;   
  }catch(FileNotFoundException ex){
   ex.printStackTrace();
  }catch(IOException ex){
   ex.printStackTrace();
  }
 }

}