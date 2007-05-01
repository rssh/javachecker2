/*
 * JavaAnnotationInstanceModel.java
 *
 * (C) GradSoft Ltd, Kiev, Ukraine.
 *http://www.gradsoft.ua
 */

package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.javachecker.models.expressions.JavaIdentifierExpressionModel;
import ua.gradsoft.javachecker.models.expressions.JavaObjectConstantExpressionModel;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Instance of annotation.
 * @author rssh
 */
public abstract class JavaAnnotationInstanceModel {
    
    
    protected JavaAnnotationInstanceModel(ElementType et,Object target)
    {
        elementType_=et;
        target_=target;
    }
    
    public abstract JavaTypeModel  getAnnotationModel() throws TermWareException, EntityNotFoundException;
    
    
    public RetentionPolicy  getRetentionPolicy() throws TermWareException
    {       
      try {  
        if (getAnnotationModel().hasAnnotation("RetentionPolicy")) {
            JavaAnnotationInstanceModel instance;
            try {
               instance=getAnnotationModel().getAnnotation("RetentionPolicy");
            }catch(NotSupportedException ex){
                throw new AssertException("Impossible",ex);
            }
            JavaExpressionModel expr;
            try {
              expr = instance.getElement("value");
            }catch(NotSupportedException ex){
                // impossible.
                throw new AssertException("value is not aviable in RetentionPolicy",ex);
            }
            switch(expr.getKind()) {
                case IDENTIFIER:
                {
                    JavaIdentifierExpressionModel iexpr = (JavaIdentifierExpressionModel)expr;
                    String identifier = iexpr.getIdentifier();
                    if (identifier.equals("CLASS")) {
                        return RetentionPolicy.CLASS;
                    }else if (identifier.equals("RUNTIME")) {
                        return RetentionPolicy.RUNTIME;
                    }else if (identifier.equals("SOURCE")) {
                        return RetentionPolicy.SOURCE;
                    }else{
                        throw new AssertException("Invalid RetentionPolicy:"+identifier);
                    }
                }          
                case OBJECT_CONSTANT:                    
                {
                    JavaObjectConstantExpressionModel cexpr = (JavaObjectConstantExpressionModel)expr;
                    Object o = cexpr.getConstant();
                    if (o instanceof RetentionPolicy) {
                        return (RetentionPolicy)o;
                    }else{
                        throw new AssertException("Invalid RetentionPolicy:"+o.toString());
                    }
                }               
                default:                              
                throw new AssertException("Invalid annotation expression:"+expr.getKind());
            }
        }else{
            throw new AssertException("Mandatory annotation RetaintionPolicy is missing");
        }  
      }catch(EntityNotFoundException ex){
          throw new AssertException(ex.getMessage(),ex);
      }
    }
  
    public ElementType  getTargetElementType()
    { return elementType_; }
    
    /**
     *@return typeModel, for which annotation is bound.
     *defined only if target of this annotation is type,
     *otherwise throw InvalidAnnotationTargetType
     *@exception InvalidAnnotationTargetElementTypeException
     */
    public JavaTypeModel  getTargetTypeModel() throws InvalidAnnotationTargetElementTypeException
    {
        if (elementType_==ElementType.TYPE) {
            return (JavaTypeModel)target_;
        }else{
            throw new InvalidAnnotationTargetElementTypeException();
        }
    }        
    
    public JavaMethodModel  getTargetMethodModel()throws InvalidAnnotationTargetElementTypeException
    {
        if (elementType_==ElementType.METHOD) {
            return (JavaMethodModel)target_;
        }else{
            throw new InvalidAnnotationTargetElementTypeException();
        }
    }
    
    public JavaMemberVariableModel  getTargetFieldModel()throws InvalidAnnotationTargetElementTypeException
    {
        if (elementType_==ElementType.FIELD) {
            return (JavaMemberVariableModel)target_;
        }else{
            throw new InvalidAnnotationTargetElementTypeException();
        }
    }
    
    public JavaConstructorModel  getTargetConstructorModel()throws InvalidAnnotationTargetElementTypeException
    {
        if (elementType_==ElementType.FIELD) {
            return (JavaConstructorModel)target_;
        }else{
            throw new InvalidAnnotationTargetElementTypeException();
        }        
    }
    
    public JavaTypeModel getTargetAnnotationTypeModel()throws InvalidAnnotationTargetElementTypeException
    {
        if (elementType_==ElementType.ANNOTATION_TYPE) {
            return (JavaTypeModel)target_;
        }else{
            throw new InvalidAnnotationTargetElementTypeException();
        }                
    }
    
    public JavaLocalVariableModel  getTargetLocalVariableModel()throws InvalidAnnotationTargetElementTypeException
    {
        if (elementType_==ElementType.LOCAL_VARIABLE) {
            return (JavaLocalVariableModel)target_;
        }else{
            throw new InvalidAnnotationTargetElementTypeException();
        }
    }
    
    public JavaFormalParameterModel  getTargetFormalParameterModel()throws InvalidAnnotationTargetElementTypeException
    {
        if (elementType_==ElementType.PARAMETER) {
            return (JavaFormalParameterModel)target_;
        }else{
            throw new InvalidAnnotationTargetElementTypeException();
        }
    }
    
    /**
     * true, if element whith name <code> elementName </code> is
     *provided in this annotation
     */
    public abstract boolean   hasElement(String elementName);
   
    
    /**
     * expression, which is defined in annotations.
     */
    public abstract JavaExpressionModel  getElement(String elementName) throws NotSupportedException, TermWareException;      
    
    /**
     * Map, which contains declared fields.
     */
    public abstract Map<String,JavaExpressionModel>  getElements() throws TermWareException;
    
    
    public abstract Term getModelTerm() throws TermWareException, EntityNotFoundException;
    
    
    protected  ElementType     elementType_;
    protected  Object          target_;
    
       
}
