/*
 * JavaTermMemberVariableModel.java
 *
 */

package ua.gradsoft.javachecker.models;


import java.lang.annotation.ElementType;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
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
    public JavaTermMemberVariableModel(Term modifiers, Term type,Term variableDeclarator, JavaTypeModel owner)  throws TermWareException
    {
        if (!variableDeclarator.getName().equals("VariableDeclarator")) {
            throw new AssertException("argument of JavaMemberVariableModel constructor must be VariableDeclarator");
        }
        type_=type;
        owner_=owner;      
        modifiersModel_=new JavaTermModifiersModel(modifiers, ElementType.FIELD, this);        
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
    
    public JavaTypeModel  getTypeModel() throws TermWareException, EntityNotFoundException
    { 
      try {  
        return JavaResolver.resolveTypeToModel(type_,owner_); 
      }catch(EntityNotFoundException ex){
          //TODO: log
          ex.setFileAndLine(JUtils.getFileAndLine(variableDeclarator_));
          throw ex;
      }
    }
        
    public JavaTermModifiersModel  getModifiersModel()
    { return modifiersModel_; }
    
   
    public Term getVariableDeclaratorTerm()
    { return variableDeclarator_; }
    
    public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap()
    { return modifiersModel_.getAnnotationsMap(); }
       
    /**
     * MemberVariableModel(modifiers, TypeRef, name, initializer,this)
     */
    public Term getModelTerm() throws TermWareException, EntityNotFoundException
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
    private JavaTermModifiersModel  modifiersModel_;
    
}
