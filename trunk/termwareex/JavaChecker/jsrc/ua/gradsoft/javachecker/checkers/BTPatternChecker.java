/*
 * EmptyCatchClauseChecker.java
 *
 * Created on середа, 18, лютого 2004, 13:25
 */

package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.*;
import ua.gradsoft.termware.ITermRewritingStrategy;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

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
        ITermRewritingStrategy strategy=TermWare.getInstance().createStrategyByName("BottomTop");
        sys_=new TermSystem(strategy,facts,TermWare.getInstance()); 
        //sys_.setDebugEntity("All");
        //sys_.setDebugMode(true);
        enabled_=false;
        
        
        if (facts.isCheckEnabled("EmptyCatchClauses")) {
           sys_.addRule("java_catch($formal_parameter, java_empty_block ) -> PROBLEM [ violationDiscovered(EmptyCatchClauses,\" empty catch clause \",$formal_parameter) ] ");
           enabled_=true;
        }
        if (facts.isCheckEnabled("GenericExceptionSpecifications")) {
           sys_.addRule("MethodDeclaration($resultType,$declarator,NameList(Name([Identifier(Exception)])),$body) -> PROBLEM [ violationDiscovered(GenericExceptionSpecifications,\" generic exception specifications\",$declarator) ] ");
           enabled_=true;
        }
        if (facts.isCheckEnabled("GenericExceptionCatchClauses")) {
           sys_.addRule("java_catch(java_formal_parameter($x,java_name([java_identifier(\"Exception\")]),$final),$block) -> PROBLEM [ violationDiscovered(GenericExceptionCatchClauses,\"generic exception catch clause\",$x) ] ");           
           enabled_=true;
        }
        
        
        if (facts.isDebugMode()) {
            sys_.setDebugMode(true);
            sys_.setDebugEntity("All");
        }
    }
    
    
    
    public void check(Term t) throws TermWareException
    {
      if (enabled_) {
        Term r=sys_.reduce(t);
      }
    }
    
    public boolean isEnabled()
    { return enabled_; }

       
    
    private TermSystem sys_;
    private JavaFacts   facts_;
    private boolean     enabled_;
  
}
