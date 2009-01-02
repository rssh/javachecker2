package x;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ClosedFile
{

 public static void main(String[] args)
 {
  FileReader reader = null;
  try {
   reader = new FileReader(args[1]);
   int ch=reader.read();
   int q=0;   
  }catch(FileNotFoundException ex){
   ex.printStackTrace();
  }catch(IOException ex){
   ex.printStackTrace();
  }finally{
    if (reader!=null) {
      reader.close();
    }
  }
 }

}