/*
 * JavaTermIdentifierExpressionModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.JUtils;
import ua.gradsoft.javachecker.models.InvalidJavaTermException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.javachecker.models.TermUtils;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Expression emodel for identifier.
 *(Variable or type)
 * @author Ruslan Shevchenko
 */
public class JavaTermIdentifierExpressionModel extends JavaTermExpressionModel implements JavaIdentifierExpressionModel
{
    
    public JavaTermIdentifierExpressionModel(Term t, JavaTermStatementModel st, JavaTypeModel enclosedType) throws TermWareException
    {
     super(t,st,enclosedType);   
    }
    
    public JavaExpressionKind  getKind()
    {
      return JavaExpressionKind.IDENTIFIER;  
    }
    
    public JavaTypeModel  getType() throws TermWareException, EntityNotFoundException
    {
      lazyInitJavaVariableModel();
      return type_;
    }
    
    public boolean isType() throws TermWareException, EntityNotFoundException
    {
      lazyInitJavaVariableModel();
      return isType_;
    }
    
    public List<JavaExpressionModel>  getSubExpressions()
    { return Collections.emptyList(); }
    
    /**
     * VariableModel(name,variableModel)
     *|
     * TypeRef(name,typeModel)
     */
    public Term getModelTerm()  throws TermWareException, EntityNotFoundException
    {
      lazyInitJavaVariableModel();
      Term identifier=t_;
      Term retval=null;
      if (isType_) {
          JavaTypeModel tm=type_;
          Term tmt = TermUtils.createJTerm(tm);
          retval = TermUtils.createTerm("TypeRef",identifier,tmt);
      }else{
          JavaVariableModel vm = variable_;
          Term vmt = TermUtils.createJTerm(vm);
          retval = TermUtils.createTerm("VariableModel",identifier,vmt);
      }
      return retval;
    }
    
    
    private void lazyInitJavaVariableModel() throws TermWareException, EntityNotFoundException
    {
       String name = getIdentifier();        
       if (variable_==null && type_==null) {
          JavaPlaceContext ctx = createPlaceContext();
          try {
            variable_=JavaResolver.resolveVariableByName(name,ctx);            
            isType_=false;
          }catch(EntityNotFoundException ex){
              if (!ex.getEntityName().equals(name)) {
                  throw new InvalidJavaTermException("error during resolving "+name,ctx.getFileAndLine(),ex);
              }
              try {
                type_=JavaResolver.resolveTypeTerm(t_,ctx);
                isType_=true;
              }catch(EntityNotFoundException ex1){
                  EntityNotFoundException newEx=new EntityNotFoundException("type or variable",getIdentifier(),"");
                  newEx.setFileAndLine(JUtils.getFileAndLine(t_));
                  throw newEx;
              }
          }
          if (variable_!=null && type_==null) {              
              type_ = variable_.getTypeModel();
          }
        }        
    }
    
    public String getIdentifier()
    { return t_.getSubtermAt(0).getString(); }
    
    private JavaVariableModel  variable_=null;
    private JavaTypeModel      type_=null;
    private boolean            isType_=false;
}
