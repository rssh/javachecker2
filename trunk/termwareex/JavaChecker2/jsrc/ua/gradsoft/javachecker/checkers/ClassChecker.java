/*
 * ClassChecker.java
 *
 */

package ua.gradsoft.javachecker.checkers;

import ua.gradsoft.javachecker.CheckerType;
import ua.gradsoft.javachecker.ConfigException;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.models.JavaTermTypeAbstractModel;
import ua.gradsoft.javachecker.util.Holder;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *
 * @author RSSH
 */
public class ClassChecker extends AbstractTypeChecker
{
    
    /** Creates a new instance of ClassChecker */
    public ClassChecker(String name, String category, String description, Term rules, boolean enabled) throws ConfigException
    {
        super(name,category,description,enabled);
        if (!rules.isString()) {
            throw new ConfigException("class checker must be identified by string - name of class, we have "+TermHelper.termToString(rules));
        }
        String className=rules.getString();
        Class theClass = null;
        try {
            theClass=Class.forName(className);
        }catch(ClassNotFoundException ex){
            throw new ConfigException("Class "+className+" is not found",ex);
        }
        Object o = null;
        try {
           o=theClass.newInstance();
        }catch(InstantiationException ex){
            throw new ConfigException("Can't instantiate "+className,ex);
        }catch(IllegalAccessException ex){
            throw new ConfigException("Illegal access during checker "+className+" instantiation",ex);
        }
        if (o instanceof JavaTypeModelProcessor) {
            instance_=(JavaTypeModelProcessor)o;
        }else{
            throw new ConfigException("Class "+className+" must be instance of JavaTypeModelProcessor");
        }        
    }
    
    public CheckerType getCheckerType()
    {
      return CheckerType.JAVA_CLASS;  
    }
    
    public void configure(JavaFacts facts) throws ConfigException
    {
      try {  
       instance_.configure(facts); 
      }catch(TermWareException ex){
          throw new ConfigException("Can't configure "+instance_.getClass().getName(),ex);
      }
    }
    
    
    
    public void run(JavaTermTypeAbstractModel tm, Holder<Term> astTermHolder, Holder<Term> modelTermHolder) throws TermWareException
    {
      try {  
        instance_.process(tm,Main.getFacts());
      }catch(EntityNotFoundException ex){
          throw new AssertException(ex.getMessage()+" at "+ex.getFileAndLine().getFname()+","+ex.getFileAndLine().getLine(),ex);
      }
    }
    
    public boolean hasSecondPass()
    { return instance_.hasSecondPass(); }
    
    public void runSecondPass(JavaTermTypeAbstractModel tm, Holder<Term> astTermHolder, Holder<Term> modelTermHolder) throws TermWareException
    {
      try {  
        instance_.processSecondPass(tm,Main.getFacts());
      }catch(EntityNotFoundException ex){
          throw new AssertException(ex.getMessage()+" at "+ex.getFileAndLine().getFname()+","+ex.getFileAndLine().getLine(),ex);
      }
    }
  
    
    
    private JavaTypeModelProcessor instance_;
}

    