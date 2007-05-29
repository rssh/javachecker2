package x;

import java.io.*;


public class OOStream
{

  public void writeSerializedObject(File f, Object o)
  {
          ObjectOutputStream oo=null;
            try {
                oo=new ObjectOutputStream(new FileOutputStream(f));
                oo.writeObject(o);
            }catch(FileNotFoundException ex){
                throw new RuntimeException("Can't open file "+f.getAbsolutePath()+" for writing",ex);
            }catch(IOException ex){
                throw new RuntimeException("Can't output object to file "+f.getAbsolutePath(),ex);
            }finally{
                if (oo!=null) {
                    try {
                        oo.close();
                    }catch(IOException ex){
                        System.err.println("exception diring closing just-writed swp file:"+ex.getMessage());
                    }
                }
            }
   }

}
