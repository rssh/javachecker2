/*
 * JavaMethodModel.java
 *
 * Created  23, 02, 2004, 10:00
 */

package ua.kiev.gradsoft.JavaChecker;

import java.util.*;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Model for java method 
 * @author  Ruslan Shevchenko
 */
public class JavaTermMethodModel extends JavaMethodModel
{
    
    
    /** Creates a new instance of JavaMethodModel */
    public JavaTermMethodModel(ITerm t, JavaTypeModel owner) throws TermWareException
    {
        super(owner);
        t_=t;
        if (t.getName().equals("java_method_declaration")) {
            ITerm nameTerm=t.getSubtermAt(0);
            methodName_=nameTerm.getSubtermAt(0).getString();
        }else{
            throw new AssertException("term must be java_method_declaration:"+TermHelper.termToString(t));
        }
        isSynchronized_=JUtils.getIsSynchronized(getMethodAttributes());
        accessLevelModel_=new JavaAccessLevelModel(getMethodAttributes());
    }
    
    
    /**
     * check name patterns.
     */
    public boolean checkNamePatterns() throws TermWareException
    {
     boolean retval=true;
     if (getFacts().isCheckMethodNamePatterns()) {
        if (!methodName_.matches(getFacts().getMethodNamePattern())) {
            getFacts().methodNamePatternViolationDiscovered(t_);
            retval=false;
        }
     }
     
     ITermHolder termHolder=new ITermHolder(ITermFactory.createBoolean(retval));
     if (getFacts().isCheckVariablePatterns()) {
         visitFormalParameterIdentifiers(new ITermVisitor()
         {
             public boolean doFirst(ITerm t, ITermHolder result, HashSet trace) throws TermWareException
             {          
               if (!t.getName().equals("java_identifier")) {
                  throw new AssertException("first subterm of formal parameter must have name java_identifier");
               }
               String formalParameterName=t.getSubtermAt(0).getName();
               if (!formalParameterName.matches(getFacts().getMethodNamePattern())) {
                   getFacts().methodNamePatternViolationDiscovered(t);
                   result.setValue(ITermFactory.createBoolean(false));
               }
               return true;
             }
             public boolean doSecond(ITerm t, ITermHolder result, HashSet trace)
             {return true;}
         },termHolder,null);
     }
     
     return retval;
    }
    
    public  boolean check() throws TermWareException
    {
      return checkNamePatterns() && checkHidingOfFormalParameters();
    }
    
    private class HidingVisitor implements ITermVisitor
    {
        public HidingVisitor(HashSet names)
        {
          names_=names;
        }
        
        public boolean doFirst(ITerm t,ITermHolder result,HashSet trace) throws TermWareException
        {
          names_.add(t.getSubtermAt(0).getString());  
          return true;  
        }
        public boolean doSecond(ITerm t,ITermHolder result, HashSet trace)
        { return true; }
        
        private HashSet names_;
    }
    
    /**
     * check that local variables does not hide formal parameters
     */
    public boolean checkHidingOfFormalParameters() throws TermWareException
    {
      boolean retval=true;
      if (getFacts().isCheckHiding()) {
        HashSet names=new HashSet();
        ITermVisitor visitor=new HidingVisitor(names);
        visitFormalParameterIdentifiers(visitor, null, null);
        HidingChecker hidingChecker=Main.getHidingChecker();
        retval=hidingChecker.check(getMethodBody().term_clone(), names, HidingChecker.HIDING_OF_FORMAL_PARAMETER);
      }
      return retval;
    }
    
    public  String getMethodName()
    { return methodName_; }
    
    public  ITerm getMethodAttributes() throws TermWareException
    { return t_.getSubtermAt(1); }
    
    public  ITerm  getReturnType() throws TermWareException
    { return t_.getSubtermAt(2); }
    
    public  ITerm  getFormalParametersList() throws TermWareException
    { return t_.getSubtermAt(3); }
    
    
    public  ITerm  getMethodBody() throws TermWareException
    { return t_.getSubtermAt(5); }
    
    public  boolean isSynchronized() throws TermWareException
    { return JUtils.getIsSynchronized(getMethodAttributes()); }
    
   
    
    
    public void  visitFormalParameterIdentifiers(ITermVisitor visitor,ITermHolder result,HashSet hs) throws TermWareException
    {
     ITerm t=getFormalParametersList();
     if (t.getName().equals("empty_list")) {
         return;
     }
     while(!t.isNil()) {
        if (t.getName().equals("cons")) {
          ITerm formalParameter=t.getSubtermAt(0);
          ITerm identifierTerm=formalParameter.getSubtermAt(0);
          visitor.doFirst(identifierTerm, result, hs);
          t=t.getSubtermAt(1);
        }else{
          throw new AssertException("formal parameters must be a list");
        }
     }     
    }
    
   
    
    private String methodName_;
    private ITerm t_; 
    
    private JavaAccessLevelModel accessLevelModel_=null;
    private boolean              isSynchronized_ =false ;
    
}
