/*
 * x1test.java
 *
 * Owner: Ruslan Shevchenko
 *
 * Copyright (c) 2006-2009 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5.test;

import junit.framework.TestCase;
import ua.gradsoft.parsers.java5.JPPMain;
import ua.gradsoft.parsers.java5.JavaParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;


/**
 *
 * @author Ruslan Shevchenko
 */
public class X1test extends TestCase
{
    
    /** Creates a new instance of x1test */
    public X1test() {
        
    }
    
    protected void setUp()
    {                                                  
        TermWare.getInstance().addParserFactory("Java",new JavaParserFactory());
    }
    
    public void testX1() throws Exception
    {
        Term t = JPPMain.parseString(xClass);
        t.println(System.out);
        assert(t.getName().equals("CompilationUnit"));
        Term dcl=t.getSubtermAt(0).getSubtermAt(1);
        assert(dcl.getName().equals("ClassOrInterfaceDeclaration"));
        assert(dcl.getSubtermAt(0).getName().equals("class"));
        Term idt=dcl.getSubtermAt(1);
        assert(idt.getName().equals("Identifier"));
        assert(idt.getSubtermAt(0).getName().equals("X"));        
     }
  
    
    /*
    
    public void testFX1() throws Exception
    {
      Term source=null;
      String fname = "test/data/ua/gradsoft/test/x1/X1.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      source.println(System.out);   
    }
    
    public void testFX2() throws Exception
    {
      Term source=null;
      String fname = "test/data/ua/gradsoft/test/x1/X1.java";
      Reader reader = new FileReader(fname);
      Term option=TermWare.getInstance().getTermFactory().createAtom("simplify");
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,option,TermWare.getInstance()).readTerm();
      source.println(System.out);   
    }
    
     public void testFX2_2() throws Exception
    {
      Term source=null;
      String fname = "test/data/ua/gradsoft/test/x1/X2.java";
      Reader reader = new FileReader(fname);
      Term option=TermWare.getInstance().getTermFactory().createAtom("simplify");
      //Term option=TermWare.getInstance().getTermFactory().createNil();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,option,TermWare.getInstance()).readTerm();
      String comment=TermHelper.getAttribute(source,"comment").getAsString(TermWare.getInstance());      
      System.out.println(comment);
      source.println(System.out);   
      assertTrue("comment must contain words 'special comments'",comment.indexOf("special comment")!=-1);
      Term typeDeclarationTerm=source.getSubtermAt(1);
      assertTrue(" subterm must be TypeDeclaration",typeDeclarationTerm.getName().equals("TypeDeclaration"));      
      comment=TermHelper.getAttribute(typeDeclarationTerm.getSubtermAt(0),"comment").getString();      
      assertTrue("class comment must contain words 'Comment for class'",comment.indexOf("Comment for class")!=-1);      
      comment=TermHelper.getAttribute(typeDeclarationTerm,"comment").getString();      
      
      assertTrue("class comment must contain words 'Comment for class'",comment.indexOf("Comment for class")!=-1);      
    }
    
     
     public void testFX2_3() throws Exception
    {
      Term source=null;
      String fname = "test/data/ua/gradsoft/test/x1/X4.java";
      Reader reader = new FileReader(fname);
      Term option=TermWare.getInstance().getTermFactory().createAtom("simplify");
      //Term option=TermWare.getInstance().getTermFactory().createNil();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,option,TermWare.getInstance()).readTerm();      
      Term typeDeclarationTerm=source.getSubtermAt(2);
      Term mt=this.findClassOrInterfaceBodyDeclarationForMethod(typeDeclarationTerm);
      Term ct=TermHelper.getAttribute(mt,"comment");

      System.out.println();
      typeDeclarationTerm.println(System.out);
      
      System.out.println();
      mt.println(System.out);

      assertFalse("ClassOrInterfaceBodyDeclaration with method must have comment",ct.isNil());
      
     }

     public void testT2() throws Exception
    {
      Term source=null;
      String fname = "test/data/ua/gradsoft/test/x1/T2.java";
      Reader reader = new FileReader(fname);
      Term option=TermWare.getInstance().getTermFactory().createAtom("simplify");
      //Term option=TermWare.getInstance().getTermFactory().createNil();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,option,TermWare.getInstance()).readTerm();      
      Term typeDeclarationTerm=source.getSubtermAt(1);

      assertTrue("T2 type declaration not found",typeDeclarationTerm!=null);
      //typeDeclarationTerm.println(System.out);
                  
     }

    public void testFPt() throws Exception
    {
      Term source=null;
      String fname = "test/data/ua/gradsoft/test/x1/FPt.java";
      Reader reader = new FileReader(fname);
      Term option=TermWare.getInstance().getTermFactory().createAtom("simplify");
      //Term option=TermWare.getInstance().getTermFactory().createNil();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,option,TermWare.getInstance()).readTerm();      
      Term typeDeclarationTerm=source.getSubtermAt(1);

    //  typeDeclarationTerm.println(System.out);
      assertTrue("FPt type declaration not found",typeDeclarationTerm!=null);
                  
     }

    public void testJCCT() throws Exception
    {
      Term source=null;
      String fname = "testdata/jc/CheckerType.java";
      Reader reader = new FileReader(fname);
      Term nil=TermWare.getInstance().getTermFactory().createNIL();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,nil,TermWare.getInstance()).readTerm();
      source.println(System.out);   
    }
    
    
     
     public void testCT() throws Exception
     {
       Term source=null;
       String fname = "test/data/ua/gradsoft/test/x1/CT.java";

      Reader reader = new FileReader(fname);
      Term option=TermWare.getInstance().getTermFactory().createAtom("simplify");
      //Term option=TermWare.getInstance().getTermFactory().createNil();
      source=TermWare.getInstance().getParserFactory("Java").createParser(reader,fname,option,TermWare.getInstance()).readTerm();      
      Term typeDeclarationTerm=source.getSubtermAt(1);

      
      Term classOrInterfaceDeclaration=typeDeclarationTerm.getSubtermAt(1);
      
      //System.out.print("classOrInterfaceDeclaration:");
      //classOrInterfaceDeclaration.println(System.out);
      
      Term clt=classOrInterfaceDeclaration.getSubtermAt(0);
      
      Term cln=classOrInterfaceDeclaration.getSubtermAt(1);
      
      assertTrue("class name must be identifier",cln.getName().equals("Identifier"));
      
      Term a1 = TermHelper.getAttribute(cln,"file");
      assertTrue("file attribute of identifier must not be nil",!a1.isNil());
       
     }
     */
     
     protected Term findClassOrInterfaceBodyDeclarationForMethod(Term t)
     {
         //System.out.print("findClassOrInterfaceBodyDeclaration:");
         //t.println(System.out);
         if (t.getName().equals("ClassOrInterfaceBodyDeclaration")) {
             if (t.getSubtermAt(1).getName().equals("MethodDeclaration")) {
                 return t;
             }
         }
         
         for(int i=0; i<t.getArity();++i) {
            Term rt=findClassOrInterfaceBodyDeclarationForMethod(t.getSubtermAt(i));
            if (!rt.isNil()) {
               return rt;
            }
         }
                      
         return TermWare.getInstance().getTermFactory().createNil();
     }
     
     
    
    private static String xClass="class X {}";
    
}
