/*
 * JavaPackageModel.java
 *
 */

package ua.kiev.gradsoft.JavaChecker;


import java.util.*;
import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Model for Java Package.
 * @author  Ruslan Shevchenko
 */
public class JavaPackageModel {
    
    
    /** Creates a new instance of JavaPackageModel */
    public JavaPackageModel(String name,JavaFacts owner) {
        name_=name;
        owner_=owner;
        typeModels_=new HashMap();
    }
    
    /**
     *@return name of package.
     */
    public String getName()
    {
      return name_;
    }
    
    /**
     *@todo implement
     */
    public void addCompilationUnit(ITerm compilationUnit) throws TermWareException
    {
       if (!compilationUnit.getName().equals("java_compilation_unit")) {
           throw new AssertException("Invalid compilation unit:"+TermHelper.termToString(compilationUnit));
       }
       ITerm typesList=compilationUnit.getSubtermAt(2);
       while(!typesList.isNil()) {
           if (typesList.getArity()==2 && typesList.getName().equals("cons")) {
               ITerm current=typesList.getSubtermAt(0);
               if (!current.getName().equals("java_empty_type_declaration")) {
                  JavaTypeModel typeModel = new JavaTermTypeModel(current, this);
                  String typeName=typeModel.getTypeName();
                  typeModels_.put(typeName, typeModel);
               }
               typesList=typesList.getSubtermAt(1);
           }else{
               throw new AssertException("typesList is not list");
           }
       }
    }
    
    public void check() 
    {
     try {
      Iterator it=typeModels_.entrySet().iterator();
      while(it.hasNext()) {
          Map.Entry me=(Map.Entry)it.next();
          JavaTypeModel tm=(JavaTypeModel)me.getValue();
          tm.check();
      }
     }catch(TermWareException ex){
         System.err.println(ex.getMessage());
         ex.printStackTrace();
     }
    }
    
    
    JavaFacts getFacts()
    { return owner_; }
    
    private String name_;
    private HashMap typeModels_;
    
    private JavaFacts owner_;
}
