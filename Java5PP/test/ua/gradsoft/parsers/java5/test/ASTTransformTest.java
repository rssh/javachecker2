/*
 * ASTTransformTest.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5.test;

import junit.framework.TestCase;
import ua.gradsoft.parsers.java5.ASTTransformers;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;

/**
 *
 * @author Ruslan Shevchenko
 */
public class ASTTransformTest extends TestCase
{
   
    public void testJavaArgsAsList() throws Exception
    {
        Term t = TermWare.getInstance().getTermFactory().createParsedTerm("ExtendsList");
        t = ASTTransformers.javaTermArgsAsList(t, 0, true);
        assertTrue(t.getArity()==1);
    }
   
     public void testTransformSeqToList() throws Exception
    {
        Term id = TermWare.getInstance().getTermFactory().createTerm(
                "Identifier",
                  TermWare.getInstance().getTermFactory().createString("x")
                );
        Term t = TermWare.getInstance().getTermFactory().createTerm("Name",id);
        t = ASTTransformers.transformSeqToList(t);
        assertTrue(t.getSubtermAt(0).getName().equals("cons"));
    }
    
}
