/*
 * JPEFacts.java
 *
 *
 * Copyright (c) 2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.jpe;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.javachecker.models.AnalyzedUnitRef;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.IParserFactory;
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
      unitsToProcess_ = new LinkedList<AnalyzedUnitRef>();
    }    
    
    //
    // Facts methods
    //
    
    /**
     *resolve name, if it is one which needed 
     */
    public boolean isJPEField(TransformationContext ctx,JavaVariableModel v,Term to) throws TermWareException
    {
      System.out.println("!!!!");  
      System.out.println("resolveName called");
       if (!to.isX()) {
          throw new AssertException("second term argument of resolveName must be a propositional variable");
      }
      
      boolean retval=false;
      
      System.out.println("from is:"+v.getName());
      System.out.println("to is:"+TermHelper.termToString(to));
      switch(v.getKind()){
          case FORMAL_PARAMETER:                                 
          case LOCAL_VARIABLE:
              break;
          case MEMBER_VARIABLE:
          {
              JavaMemberVariableModel mv = (JavaMemberVariableModel)v;
              JavaTypeModel tm = mv.getOwnerType();
              String tmName = tm.getFullName();
              if (tmName.endsWith("CompileTimeConstants")) {                
                  String value = configuration_.getCompileTimeProperties().get(mv.getName());
                  if (value!=null) {      
                     StringReader reader = new StringReader(value);
                     Term optionTerm = TermWare.getInstance().getTermFactory().createAtom("Expression");
                     IParserFactory parserFactory = TermWare.getInstance().getParserFactory("Java");
                     IParser parser = parserFactory.createParser(reader,"inline",optionTerm,TermWare.getInstance());
                     Term t = parser.readTerm();
                     ctx.getCurrentSubstitution().put(to,t);
                     System.out.println("put:");
                     t.println(System.out);
                     retval=true;
                  }                                        
              }
          }
          break;
          default:
              throw new AssertException("Invalid variable kind:"+v.getKind());
              
      }
      
      return retval;
    }
    
    public List<AnalyzedUnitRef> getUnitsToProcess()
    { return unitsToProcess_; }
    
    private Configuration configuration_;
    
    private List<AnalyzedUnitRef>  unitsToProcess_;
    
}
