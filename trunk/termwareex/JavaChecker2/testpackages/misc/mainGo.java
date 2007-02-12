import ua.gradsoft.CNNGenerator.sequence.*; 

import java.util.*;
import java.io.*;
import org.exolab.castor.xml.*; 
import org.exolab.castor.mapping.*;



public class mainGo{

  public static void main(String args[]){
    SequencesList seq;
    seq = SequencesList.readFromFile("test.xml");
    if(seq!=null){
      System.out.println(seq.toString());
    }
  }
}



