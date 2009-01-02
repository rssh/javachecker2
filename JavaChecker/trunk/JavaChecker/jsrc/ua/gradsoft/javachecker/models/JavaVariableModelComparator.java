/*
 * JavaVariableModelComparator.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.Comparator;

/**
 *Comparator for JavaVariableModel
 * @author rssh
 */
public class JavaVariableModelComparator implements Comparator<JavaVariableModel>
{
    

    public int compare(JavaVariableModel x, JavaVariableModel y)
    {
      int cmp = x.getKind().compareTo(y.getKind());  
      if (cmp!=0) {
          return cmp;
      }
      cmp = x.getName().compareTo(y.getName());
      if (cmp!=0) {
          return cmp;
      }
      if (x.equals(y)) {
          return 0;
      }
      JavaTypeModel xowner = x.getOwnerType();
      JavaTypeModel yowner = y.getOwnerType();
      cmp = xowner.getErasedFullName().compareTo(yowner.getErasedFullName());
      if (cmp!=0) {
          return cmp;
      }
      JavaTopLevelBlockOwnerModel xbl = x.getTopLevelBlockOwner();
      JavaTopLevelBlockOwnerModel ybl = y.getTopLevelBlockOwner();
      if (xbl!=null && ybl!=null) {
          String sx = JavaTopLevelBlockOwnerModelHelper.getStringSignature(xbl);
          String sy = JavaTopLevelBlockOwnerModelHelper.getStringSignature(ybl);
          return sx.compareTo(sy);
      }
      return 0;
    }
    
    public boolean equals(JavaVariableModel x, JavaVariableModel y)
    {
      if (x==y) {
          return true;
      }  
      if (x.getKind()==y.getKind()) {
          if (x.getName().equals(y.getName())) {      
              JavaTypeModel xOwner = x.getOwnerType();
              JavaTypeModel yOwner = y.getOwnerType();
              if (xOwner.getErasedFullName().equals(yOwner.getErasedFullName())) {
                  JavaTopLevelBlockOwnerModel tbx = x.getTopLevelBlockOwner();
                  if (tbx==null) {
                      return true;
                  }
                  JavaTopLevelBlockOwnerModel tby = y.getTopLevelBlockOwner();
                  String tbxs = JavaTopLevelBlockOwnerModelHelper.getStringSignature(tbx);
                  String tbys = JavaTopLevelBlockOwnerModelHelper.getStringSignature(tby);
                  return tbxs.equals(tbys);
              }
          }
      }  
      return false;
    }
    
    public static final JavaVariableModelComparator INSTANCE = new JavaVariableModelComparator();
    
}
