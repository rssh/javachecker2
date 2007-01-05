/*
 * HidingCheckFacts.java
 *
 * Created on четвер, 26, лютого 2004, 1:51
 */

package ua.gradsoft.javachecker.checkers;

import java.util.Set;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.IEnv;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;



/**
 *
 * @author  Ruslan Shevchenko
 */
public class HidingCheckFacts extends DefaultFacts {
    
    /** Creates a new instance of HidingCheckFacts */
    public HidingCheckFacts(IEnv env,int hideType)  throws TermWareException
    {
        setEnv(env);
        hideType_=hideType;
    }
    
   
    
    public boolean isTargetName(Term name) throws TermWareException
    {
     if (name.getName().equals("Identifier")) {
        return targetNames_.contains(name.getSubtermAt(0).getString());
     }
     return false;
    }
    
    public  void setTargetNames(Set<String> setOfNames)
    {
      targetNames_=setOfNames;
      nHides_=0;
    }
    
    public  void setHideType(int hideType)
    {
      hideType_=hideType;  
    }
    
    
    public boolean hidingDiscovered(Term t) throws TermWareException
    {
      ++nHides_;
      switch(hideType_) {
          case HidingChecker.HIDING_OF_FORMAL_PARAMETER:
              getJavaFacts().violationDiscovered("Hiding", "hiding of formal parameter by local variable discovered", t);
              break;
          case HidingChecker.HIDING_OF_CLASS_FIELD:
              getJavaFacts().violationDiscovered("Hiding", "hiding of class field by local variable discovered", t);
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
    
    private Set<String> targetNames_=null;
    private int     hideType_;
    private int     nHides_=0;
    
}
