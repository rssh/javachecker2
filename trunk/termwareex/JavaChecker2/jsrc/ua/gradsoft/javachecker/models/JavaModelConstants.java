/*
 * JavaModelConstants.java
 *
 * Copyright (c) 2004-2005 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.LinkedList;
import java.util.List;

/**
 *Interface which define few usefull constants
 * @author Ruslan Shevchenko
 */
public class JavaModelConstants {

    /**
     * ClassOrInterfaceModel(modifiers,"class"|"interface",Identifier,typeParameters,extendsList,ImplementsList,ClassOrInterfaceBody(membersList),context)
     */
    public final static int CLASSORINTERFACE_MODEL_MODIFIERS_INDEX = 0;
    public final static int CLASSORINTERFACE_MODEL_KIND_INDEX = 1;
    public final static int CLASSORINTERFACE_MODEL_NAME_INDEX = 2;
    public final static int CLASSORINTERFACE_MODEL_TYPE_ARGUMENTS_INDEX = 3;
    public final static int CLASSORINTERFACE_MODEL_EXTENDS_INDEX = 4;
    public final static int CLASSORINTERFACE_MODEL_IMPLEMENTS_INDEX = 5;
    public final static int CLASSORINTERFACE_MODEL_BODY_INDEX = 6;
    
    /**
     *ConstructorModel(modifiers,TypeParameters,identifier,FormalParameters,trowsNameList,BlockModel,context)
     */
    public final static int CONSTRUCTOR_MODEL_MODIFIERS_INDEX = 0;
    public final static int CONSTRUCTOR_MODEL_TYPE_PARAMETERS_INDEX = 1;
    public final static int CONSTRUCTOR_MODEL_NAME_INDEX = 2;
    public final static int CONSTRUCTOR_MODEL_FORMAL_PARAMETERS_INDEX = 3;
    public final static int CONSTRUCTOR_MODEL_THROWS_INDEX = 4;
    public final static int CONSTRUCTOR_MODEL_BLOCK_INDEX = 5;
    public final static int CONSTRUCTOR_MODEL_CONTEXT_INDEX = 6;
    
    /**
     * MemberVariableModel(modifiers, TypeRef, name, initializer,this)
     */
    public final static int MEMBER_VARIABLE_MODEL_MODIFIERS_INDEX=0;
    public final static int MEMBER_VARIABLE_MODEL_TYPE_INDEX=1;
    public final static int MEMBER_VARIABLE_MODEL_NAME_INDEX=2;
    public final static int MEMBER_VARIABLE_MODEL_INITIALIZER_INDEX=3;
    public final static int MEMBER_VARIABLE_MODEL_MODEL_INDEX=4;
    
    /**
     * MethodModel(modifiers,typeParameters,ResultType,name,formalParameters,throws,block,context)
     */
    public final static int METHOD_MODEL_MODIFIERS_INDEX=0;
    public final static int METHOD_MODEL_TYPE_PARAMETERS_INDEX=1;
    public final static int METHOD_MODEL_RESULT_TYPE_INDEX=2;
    public final static int METHOD_MODEL_NAME_INDEX=3;
    public final static int METHOD_MODEL_FORMAL_PARAMETERS_INDEX=4;
    public final static int METHOD_MODEL_THROWS_INDEX=5;
    public final static int METHOD_MODEL_BLOCK_INDEX=6;
    public final static int METHOD_MODEL_CONTEXT_INDEX=6;

    /**
    * AnnotationTypeDeclarationModel(modifiers, name,[...],context)
    */
    public final static int ANNOTATION_TYPE_MODEL_MODIFIERS_INDEX = 0;
    public final static int ANNOTATION_TYPE_MODEL_NAME_INDEX = 2;
    public final static int ANNOTATION_TYPE_MODEL_MEMBERS_INDEX = 3;
    public final static int ANNOTATION_TYPE_MODEL_CONTEXT_INDEX = 4;
    
    
    
    
}
