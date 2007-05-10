/*
 * JavaClassFieldModel.java
 *
 * Copyright (c) 2004-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import ua.gradsoft.javachecker.util.Function;
import ua.gradsoft.javachecker.util.FunctionMap;
import ua.gradsoft.javachecker.util.ImmutableMappedCollection;
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
    
    public JavaModifiersModel getModifiersModel() {
        int jmodifiers=field_.getModifiers();
        return new JavaClassModifiersModel(JavaClassTypeModel.translateModifiers(jmodifiers));
    }
    
    public JavaTypeModel getOwner() {
        return classTypeModel_;
    }
    
    public JavaTypeModel getTypeModel() throws TermWareException {
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
                return new JavaClassAnnotationInstanceModel(ElementType.FIELD, field_.getAnnotation(JavaClassTypeModel.forName(value)),this);
            }
            
        }
        );
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
