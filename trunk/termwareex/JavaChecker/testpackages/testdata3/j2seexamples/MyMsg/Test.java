// Static import user-defined static fields. 
import j2seexamples.MyMsg.*; 
 
import static MyMsg.Msg.*;  
 
class Test {  
  public static void main(String args[]) {  
    Msg m = new Msg("Testing static import."); 
 
    m.showMsg(MIXED); 
    m.showMsg(LOWER); 
    m.showMsg(UPPER); 
  }  
}
