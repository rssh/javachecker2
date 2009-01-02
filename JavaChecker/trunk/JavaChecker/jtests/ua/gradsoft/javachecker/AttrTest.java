/*
 * AttrTest.java
 *
 * Created on May 16, 2007, 1:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker;

import java.io.PrintWriter;
import java.util.List;
import junit.framework.TestCase;
import ua.gradsoft.javachecker.models.JavaConstructorModel;
import ua.gradsoft.javachecker.models.JavaMemberVariableModel;
import ua.gradsoft.javachecker.models.JavaMethodModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.termware.Term;

/**
 *
 * @author rssh
 */
public class AttrTest extends TestCase
{
    
    public void testNonCloseableStringWriter() throws Exception
    {
      JavaCheckerFacade.init();
      JavaCheckerFacade.addInputDirectory("testpackages/testdata10/src",true);
      JavaCheckerFacade.addPropertiesDirectory("testpackages/testdata10/attrs");
      JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("java.io.StringWriter");
      
      Term t = tm.getAttribute("NonCloseable");
      
      //PrintWriter wr = new PrintWriter(System.out);
      //tm.getAttributes().print(wr);
      //wr.flush();
      
      assertTrue("NonCloseable attribute must be defined",t!=null);
   
      assertTrue("NonCloseable must be true",t.getBoolean()==true);
      
      Term t1 = tm.getAttribute("Unexistent");
      
      assertTrue("Unexistend attribute must be nil",t1.isNil());
      
    }
            
          
    /**
     * test loading of attributes from external file
     */
    public void testX1Attributes() throws Exception
    {
      JavaCheckerFacade.init();
      JavaCheckerFacade.addInputDirectory("testpackages/testdata10/src",true);
      JavaCheckerFacade.addPropertiesDirectory("testpackages/testdata10/attrs");
      JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("x.X");
      
      Term t = tm.getAttribute("p1");
      
      assertTrue("attribute p1 of x.X must be defined",!t.isNil());
      
      assertTrue("attribute p1 of x.X must be atom",t.isAtom());
      
      assertTrue("name(x.X.getAttribute(p1)) must be v1",t.getName().equals("v1"));
      
      t = tm.getAttribute("p2");
      
      assertTrue("attribute p2 of x.X must be defined",!t.isNil());
      
      assertTrue("attribute p2 of x.X must be boolean",t.isBoolean());
      
      List<JavaConstructorModel> constructors = tm.getConstructorModels();
      
      JavaConstructorModel cn1 = constructors.get(0);
      
      t=cn1.getAttribute("c1");
      
      assertTrue("attribute c1 of x.X constructor must be defined",!t.isNil());
      
      assertTrue("attribute c1 of x.X constructor must be integer",t.isInt());
      
      assertTrue("attribute c1 of x.X constructor must be 1",t.getInt()==1);
      
      List<JavaMethodModel> methods = tm.getMethodModels().get("throwRuntimeException");
      
      JavaMethodModel mm = methods.get(0);
      
      t=mm.getAttribute("BreakFlow");
      
      assertTrue("attribute BreakFlow of x.X.throwRuntimeException must be defined",!t.isNil());
      
      assertTrue("attribute BreakFlow of x.X.throwRuntimeException must be boolean",t.isBoolean());
      
      JavaTypeModel tm1 = tm.getNestedTypeModels().get("XX");
      
      t=tm1.getAttribute("isXX");
      
      assertTrue("attribute isXX x.X.XX must be defined",!t.isNil());
      
      assertTrue("attribute isXX x.X.XX must be boolean",t.isBoolean());
      
      assertTrue("attribute isXX x.X.XX must be true",t.getBoolean()==true);
      
      t=tm1.getAttribute("aaa");
      
      assertTrue("attribute aaa of x.X.XX must be defined",!t.isNil());
      
      assertTrue("attribute aaa of x.X.XX must be boolean",t.isBoolean());
      
      assertTrue("attribute aaa x.X.XX must be false",t.getBoolean()==false);
      
      JavaMemberVariableModel vm = tm.getMemberVariableModels().get("qqq");
      
      t=vm.getAttribute("xx");
      
      assertTrue("attribute xx in x.X.qqq must exists",!t.isNil());
      
      t=vm.getAttribute("unexistend");
      
      assertTrue("attribute unexistend in x.X.qqq must not exists",t.isNil());
      
    }
    
    /**
     * test loading of attributes from own sources.
     */
      public void testX1SAttributes() throws Exception
      {
       
       JavaCheckerFacade.init();       
       JavaCheckerFacade.addInputDirectory("testpackages/testdata10/src",true);
       JavaCheckerFacade.addPropertiesDirectory("testpackages/testdata10/attrs");             
       JavaTypeModel tm = JavaResolver.resolveTypeModelByFullClassName("x.X");

       Term t = tm.getAttribute("AAA");
 
       PrintWriter pw = new PrintWriter(System.out);
       tm.getAttributes().print(pw);
       pw.flush();
       
       assertTrue("attribute AAA of x.X must be defined",!t.isNil());
      
       assertTrue("attribute AAA of x.X must be boolean",t.isBoolean());
      
        assertTrue("attribute AAA of x.X must be true",t.getBoolean());
       
       
      }
    
}
