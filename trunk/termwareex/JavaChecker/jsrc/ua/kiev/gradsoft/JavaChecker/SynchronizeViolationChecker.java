/*
 * SynchronizeViolationChecker.java
 *
 * Created on понеділок, 23, лютого 2004, 16:54
 */

package ua.kiev.gradsoft.JavaChecker;

import java.util.*;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Check for violation of synchronize rules.
 * @author  Ruslan Shevchenko
 */
public class SynchronizeViolationChecker {
    
    /** Creates a new instance of SynchronizeViolationChecker */
    public SynchronizeViolationChecker(JavaFacts facts) throws TermWareException
    {
        sys_=TermWareSingleton.getRoot().resolveSystem("SynchronizeViolation"); 
        if (facts.isDebugMode()) {
          sys_.setDebugMode(true);
          sys_.setDebugEntity("All");
        }
    }
    
    public boolean check(JavaTypeModel typeModel, String variableName) throws TermWareException
    {
      boolean retval=true;
      ITerm synchronizedObject=ITermFactory.createAtom("java_this");
      if (!typeModel.hasMethodModels()) {
          return true;
      }
      Map modelVectors;
      try {
         modelVectors=typeModel.getMethodModels();
      }catch(NotSupportedException ex){
         throw new AssertException("Call of SynchronizeViolationChecker for non-term Java Model");
      }
      Iterator it=modelVectors.entrySet().iterator();
      while(it.hasNext()) {
          Map.Entry me=(Map.Entry)it.next();
          Collection cm=(Collection)me.getValue();
          Iterator it1=cm.iterator();
          while(it1.hasNext()) {
              JavaMethodModel methodModel=(JavaMethodModel)it1.next();
              if (!(methodModel instanceof JavaTermMethodModel)) {
                  throw new AssertException("Call of SynchronizeViolationChecker for non-term Java Model");
              }
              JavaTermMethodModel termMethodModel=(JavaTermMethodModel)methodModel;
              if (!termMethodModel.isSynchronized()) {
                retval &= check(termMethodModel,variableName, synchronizedObject);
              }
          }
      }
      return retval;
    }
    
    public boolean check(JavaTermMethodModel method, String variableName, ITerm synchronizedObject) throws TermWareException
    {
        ITerm t=ITermFactory.createComplexTerm1("java_identifier", 
                                               ITermFactory.createString(variableName));
        t=ITermFactory.createComplexTerm3("C", method.getMethodBody(),t,synchronizedObject);
        ITerm r=sys_.reduce(t);
        if (r.isBoolean()) {
            return r.getBoolean();
        }else{
            System.out.println("reduced term:"+TermHelper.termToString(r));
            return false;
        }
    }
    

    private ITermSystem sys_;
    
}
