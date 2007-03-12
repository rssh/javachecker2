/*
 * JavaTermMemberVariableModel.java
 *
 * Created on понеділок, 23, лютого 2004, 10:59
 */

package ua.gradsoft.javachecker.models;


import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;



/**
 *Model for java Member variable.
 * @author  Ruslan Shevchenko
 */
public class JavaTermMemberVariableModel extends JavaMemberVariableModel
{
    
    /**
     * Creates a new instance of JavaTermMemberVariableModel
     */
    public JavaTermMemberVariableModel(int modifiers, Term type,Term variableDeclarator, JavaTypeModel owner)  throws TermWareException
    {
        if (!variableDeclarator.getName().equals("VariableDeclarator")) {
            throw new AssertException("argument of JavaMemberVariableModel constructor must be VariableDeclarator");
        }
        type_=type;
        owner_=owner;      
        modifiersModel_=new JavaModifiersModel(modifiers);        
        variableDeclarator_=variableDeclarator;
        Term variableDeclaratorId=variableDeclarator.getSubtermAt(0);
        Term identifierTerm = variableDeclaratorId.getSubtermAt(0);
        if (variableDeclaratorId.getArity() > 1) {
            int nReferences = variableDeclaratorId.getSubtermAt(1).getInt();
            if (nReferences > 0) {
                Term nReferencesTerm = TermUtils.createInt(nReferences);
                type_=TermUtils.createTerm("ReferenceType",nReferencesTerm,type_);                
            }
        }        
        name_=identifierTerm.getSubtermAt(0).getName();
    }
    
    public String getName()
    { return name_; }
    
    JavaFacts getJavaFacts()
    { return owner_.getJavaFacts(); }
    
    public JavaTypeModel  getOwner()
    { return owner_; }
    
    public JavaTypeModel  getTypeModel() throws TermWareException
    { 
      try {  
        return JavaResolver.resolveTypeToModel(type_,owner_); 
      }catch(EntityNotFoundException ex){
          //TODO: log
          System.err.println(ex.getMessage());
          return JavaUnknownTypeModel.INSTANCE;
      }
    }
        
    public JavaModifiersModel  getModifiersModel()
    { return modifiersModel_; }
    
   
    public Term getVariableDeclaratorTerm()
    { return variableDeclarator_; }
    
       
    /**
     * MemberVariableModel(modifiers, TypeRef, name, initializer,this)
     */
    public Term getModelTerm() throws TermWareException
    {
        Term modifiersModelTerm = modifiersModel_.getModelTerm();
        JavaTypeModel tm = getTypeModel();
        Term ttm= TermUtils.createJTerm(tm);
        Term typeRef = TermUtils.createTerm("TypeRef",type_,ttm);
        Term identifierTerm = variableDeclarator_.getSubtermAt(0).getSubtermAt(0);
        Term initializer = TermUtils.createNil();
        if (variableDeclarator_.getArity()>1) {
            initializer = variableDeclarator_.getSubtermAt(1);
        }               
        Term tthis = TermUtils.createJTerm(this);
        return TermUtils.createTerm("MemberVariableModel",modifiersModelTerm,typeRef,identifierTerm,initializer,tthis);
    }
    
    private String  name_;
    private Term    type_=null;
    private Term    variableDeclarator_=null;
    private JavaTypeModel owner_=null;
    private JavaModifiersModel  modifiersModel_;
    
}
