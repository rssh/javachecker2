/*
 * ExprTest.java
 *
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5.test;

import java.io.StringReader;
import junit.framework.TestCase;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.IParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;

/**
 *Test for parsing Java Expression
 * @author Ruslan Shevchenko
 */
public class ExprTest extends TestCase
{
    
    protected void setUp()
    {                                                  
        TermWare.getInstance().addParserFactory("Java",new JavaParserFactory());
    }

    
    
    public void testExpr1() throws Exception
    {
      String expr="x+y";
      StringReader reader = new StringReader(expr);
      Term optionTerm = TermWare.getInstance().getTermFactory().createAtom("Expression");
      IParserFactory parserFactory = TermWare.getInstance().getParserFactory("Java");
      IParser parser = parserFactory.createParser(reader,"inline",optionTerm,TermWare.getInstance());
      Term t = parser.readTerm();
      assertTrue("t must be complex term",t.isComplexTerm());
    }
    
    
    
}
