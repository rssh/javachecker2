/*
 * HidingCheckFacts.java
 *
 * Created on четвер, 26, лютого 2004, 1:51
 */

package ua.kiev.gradsoft.JavaChecker;

import java.util.*;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;
import ua.kiev.gradsoft.TermWare.facts.*;


/**
 *
 * @author  Ruslan Shevchenko
 */
public class HidingCheckFacts extends DefaultFacts {
    
    /** Creates a new instance of HidingCheckFacts */
    public HidingCheckFacts(IEnv env,int hideType)  throws TermWareException
    {
        super(env);
        hideType_=hideType;
    }
    
   
    
    public boolean isTargetName(ITerm name) throws TermWareException
    {
     if (name.getName().equals("java_identifier")) {
        return targetNames_.contains(name.getSubtermAt(0).getString());
     }
     return false;
    }
    
    public  void setTargetNames(Set setOfNames)
    {
      targetNames_=setOfNames;
      nHides_=0;
    }
    
    public  void setHideType(int hideType)
    {
      hideType_=hideType;  
    }
    
    public boolean hidingDiscovered(ITerm t) throws TermWareException
    {
      ++nHides_;
      switch(hideType_) {
          case HidingChecker.HIDING_OF_FORMAL_PARAMETER:
              getJavaFacts().hidingOfFormalParameterDiscovered(t);  
              break;
          case HidingChecker.HIDING_OF_CLASS_FIELD:
              getJavaFacts().hidingOfClassFieldDiscovered(t);
              break;
          default:
              throw new AssertException("HidingCheckFacts: bad hidingType");
      }
      return true;
    }
    
    public int getNHides()
    {
        return nHides_;
    }
    
    private JavaFacts getJavaFacts()
    {
     return Main.getFacts();
    }
    
    private Set targetNames_=null;
    private int     hideType_;
    private int     nHides_=0;
    
}
