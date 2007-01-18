/*
 * JPEFacts.java
 *
 * Created on вівторок, 2, січня 2007, 2:20
 *
 * Copyright (c) 2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.jpe;

import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Facts for JPE
 * @author Ruslan Shevchenko
 */
public class JPEFacts extends DefaultFacts
{
    
    public JPEFacts() throws TermWareException
    { super(); }
    
    public void setConfiguration(Configuration configuration)
    {
      configuration_=configuration;  
    }    
    
    //
    // Facts methods
    //
    
    /**
     *resolve name, if it is one which needed 
     */
    public boolean resolveName(TransformationContext ctx,Term from,Term to) throws TermWareException
    {
      System.out.println("!!!!");  
      System.out.println("resolveName called");
       if (!to.isX()) {
          throw new AssertException("second term argument of resolveName must be a propositional variable");
      }
      
      
      System.out.println("from is:"+TermHelper.termToString(from));
      System.out.println("to is:"+TermHelper.termToString(to));
      Term res=TermWare.getInstance().getTermFactory().createTerm("Name",from);
      ctx.getCurrentSubstitution().put(to,res);   
      return true;
    }
    
    
    private Configuration configuration_;
    
    
    
}
