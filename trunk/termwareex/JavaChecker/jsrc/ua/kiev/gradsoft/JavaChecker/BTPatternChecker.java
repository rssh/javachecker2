/*
 * EmptyCatchClauseChecker.java
 *
 * Created on середа, 18, лютого 2004, 13:25
 */

package ua.kiev.gradsoft.JavaChecker;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;


/**
 *Checker for various patterns.
 *<ul>
 *    <li>  empty cath clauses. </li>
 *    <li>  generic exceptions specs. </li>
 *</ul>
 * @author  Ruslan Shevchenko
 */
public class BTPatternChecker {
    
    /** Creates a new instance of EmptyCatchClauseChecker */
    public BTPatternChecker(JavaFacts facts) throws TermWareException
    {
        facts_=facts;
        ITermRewritingStrategy strategy=TermWareSingleton.createStrategyByName("BottomUp");
        sys_=new ITermSystem(strategy,facts,facts.getEnv()); 
        enabled_=false;
        if (facts.isCheckEnabled("EmptyCatchClauses")) {
           sys_.addRule("java_catch($formal_parameter, java_empty_block ) -> PROBLEM // violationDiscovered(EmptyCatchClauses,\" empty catch clause \",$formal_parameter)");
           enabled_=true;
        }
        if (facts.isCheckEnabled("GenericExceptionSpecifications")) {
           sys_.addRule("java_method_declaration($name,$attrs,$result_type,$params,[java_name([java_identifier(\"Exception\")])],$body)-> PROBLEM // violationDiscovered(GenericExceptionSpecifications,\" generic exception specifications\",$name) ");
           enabled_=true;
        }
        if (facts.isCheckEnabled("GenericExceptionCatchClauses")) {
           sys_.addRule("java_catch(java_formal_parameter($x,java_name([java_identifier(\"Exception\")]),$final),$block) -> PROBLEM // violationDiscovered(GenericExceptionCatchClauses,\"generic exception catch clause\",$x) ");           
           enabled_=true;
        }
        if (facts.isDebugMode()) {
            sys_.setDebugMode(true);
            sys_.setDebugEntity("All");
        }
    }
    
    
    public void check(ITerm t) throws TermWareException
    {
      if (enabled_) {
        ITerm r=sys_.reduce(t);
      }
    }
    
    public boolean isEnabled()
    { return enabled_; }

   
    private ITermSystem sys_;
    private JavaFacts   facts_;
    private boolean     enabled_;
  
}
