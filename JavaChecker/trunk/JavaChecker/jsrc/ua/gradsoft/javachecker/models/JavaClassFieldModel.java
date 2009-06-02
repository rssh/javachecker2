/*
 * JavaClassFieldModel.java
 *
 * Copyright (c) 2004-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.FunctionMap;
import ua.gradsoft.javachecker.util.ImmutableMappedCollection;
import ua.gradsoft.javachecker.util.ImmutableMappedList;
import ua.gradsoft.javachecker.util.IntegerOrderList;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for field of class
 * @author Ruslan Shevchenko
 */
public class JavaClassFieldModel extends JavaMemberVariableModel {
    
    /** Creates a new instance of JavaClassFieldModel */
    public JavaClassFieldModel(Field field,JavaClassTypeModel classTypeModel) {
        field_=field;
        classTypeModel_=classTypeModel;
    }
    
    
    public String getName() {
        return field_.getName();
    }
    
    public JavaModifiersModel getModifiers() {
        int jmodifiers=field_.getModifiers();
        return new JavaClassModifiersModel(getAnnotationsList(),JavaClassTypeModel.translateModifiers(jmodifiers));
    }
    
    public JavaTypeModel getOwnerType() {
        return classTypeModel_;
    }
    
    public JavaTypeModel getType() throws TermWareException {
        Type fieldType = field_.getGenericType();
        return JavaClassTypeModel.createTypeModel(fieldType);
    }
    
    public  Map<String,JavaAnnotationInstanceModel> getAnnotationsMap() {
        return new FunctionMap<String,JavaAnnotationInstanceModel>(
                new ImmutableMappedCollection<Integer,String>(
                new IntegerOrderList(field_.getDeclaredAnnotations().length),
                new Function<Integer,String>(){
            public String function(Integer x) {
                return field_.getDeclaredAnnotations()[x].annotationType().getName();
            }
        }
        ),
                new Function<String,JavaAnnotationInstanceModel>(){
            public JavaAnnotationInstanceModel function(String value) throws TermWareException
            {
                Annotation annotation = field_.getAnnotation(JavaClassTypeModel.forName(value));
                if (annotation==null) {
                    return null;
                }
                return new JavaClassAnnotationInstanceModel(ElementType.FIELD, annotation ,this);
            }
            
        }
        );
    }
    
    public List<JavaAnnotationInstanceModel> getAnnotationsList()
    {
        return new ImmutableMappedList<Integer,JavaAnnotationInstanceModel>(
                new IntegerOrderList(field_.getDeclaredAnnotations().length),
                new Function<Integer,JavaAnnotationInstanceModel>(){
            public JavaAnnotationInstanceModel function(Integer x) {
                return new JavaClassAnnotationInstanceModel(ElementType.FIELD, field_.getDeclaredAnnotations()[x],this);
            }
        }
                );
    }
    
    public boolean isSupportInitializerExpression()
    { return false; }
    
    public JavaExpressionModel  getInitializerExpression()
    { return null; }
    
    public boolean isConstant()
    {
        return this.getModifiers().isFinal() && this.getModifiers().isStatic();
    }

    /**
     * ClassField(this)
     */
    public Term getModelTerm() throws TermWareException {
        Term tctx = TermUtils.createJTerm(this);
        return TermUtils.createTerm("ClassField",tctx);
    }
    
    
    private JavaClassTypeModel classTypeModel_;
    private Field field_;
}
