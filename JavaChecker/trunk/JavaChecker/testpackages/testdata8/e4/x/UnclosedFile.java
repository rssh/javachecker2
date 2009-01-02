package x;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class UnclosedFile
{

 public static void main(String[] args)
 {
  FileReader reader = null;
  try {
   reader = new FileReader(args[1]);
  }catch(FileNotFoundException ex){
   ex.printStackTrace();
   return;
  }  
  int q=0;   
  for(int i=0; i<10; ++i) {
    int ch=0;
    try {
      ch=reader.read();
    }catch(IOException ex){
      ex.printStackTrace();
      return;
    }
    System.out.print(ch);
  }
  if (reader!=null) {
    try {
      reader.close();
    }catch(IOException ex){
    }
  }
 }

}