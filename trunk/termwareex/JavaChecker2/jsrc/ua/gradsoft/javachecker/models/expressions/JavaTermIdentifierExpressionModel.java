/*
 * JavaTermIdentifierExpressionModel.java
 *
 * Created on середа, 7, лютого 2007, 18:37
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models.expressions;

import java.util.Collections;
import java.util.List;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.models.JavaExpressionKind;
import ua.gradsoft.javachecker.models.JavaExpressionModel;
import ua.gradsoft.javachecker.models.JavaPlaceContext;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTermExpressionModel;
import ua.gradsoft.javachecker.models.JavaTermStatementModel;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaVariableModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Expression emodel for identifier.
 *(Variable
 * @author Ruslan Shevchenko
 */
public class JavaTermIdentifierExpressionModel extends JavaTermExpressionModel
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
    
    
    
    private void lazyInitJavaVariableModel() throws TermWareException, EntityNotFoundException
    {
        if (variable_==null && type_==null) {
          JavaPlaceContext ctx = createPlaceContext();
          try {
            variable_=JavaResolver.resolveVariableByName(getIdentifier(),ctx);            
            isType_=false;
          }catch(EntityNotFoundException ex){
            type_=JavaResolver.resolveTypeTerm(t_,ctx);
            isType_=true;
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
