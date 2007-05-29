/*
 * JavaClassTypeModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *TypeModel, based on java class
 * @author Ruslan Shevchenko
 */
public class JavaClassTypeModel extends JavaTypeModel
{
    
    /** Creates a new instance of JavaClassTypeModel */
    public JavaClassTypeModel(Class theClass) {
        super(Main.getFacts().getPackagesStore().findOrAddPackage(theClass.getPackage().getName()));
        theClass_=theClass;       
    }
    
    public String getName()
    {       
       return theClass_.getSimpleName(); 
    }
  
    public Term getShortNameAsTerm() throws TermWareException
    {
        Term identifier=TermWare.getInstance().getTermFactory().createTerm("Identifier",
                          TermWare.getInstance().getTermFactory().createString(theClass_.getName())
                        );  
        return identifier;
    }
  
  
   
  /**
   * get canonical name, siutable for use in JVM
   */
  public String getCanonicalName()
  {
      return theClass_.getCanonicalName();
  }
  
  public boolean isClass()
  {
      return !theClass_.isInterface() && !theClass_.isAnnotation() && !theClass_.isArray() && !theClass_.isPrimitive();
  }
  
  public boolean isInterface()
  {
      return theClass_.isInterface();
  }
  
  public boolean isEnum()
  {
      return theClass_.isEnum();
  }
  
  public boolean isAnnotationType()
  {
      return theClass_.isAnnotation();
  }
    
  public boolean isPrimitiveType()
  {
      return theClass_.isPrimitive();
  }
  
  public boolean isArray()
  {
      return theClass_.isArray();
  }
  
  public boolean isTypeVariable()
  {
      return false;
  }
  
  public boolean isWildcardBounds()
  {
      return false;
  }

  public boolean isNull()
  {
    return false;  
  }

  
  public boolean isUnknown()
  {
    return false;  
  }
  
  public JavaTermModifiersModel getModifiersModel()
  {
      return new JavaTermModifiersModel(translateModifiers(theClass_.getModifiers()));
  }
  
  public JavaTypeModel  getSuperClass() throws TermWareException
  {
    if (theClass_.getGenericSuperclass()!=null)  {
        Type tp = theClass_.getGenericSuperclass();
        return JavaClassTypeModel.createTypeModel(tp);      
    }else{
        if (theClass_.isInterface()) {    
          try{  
            return JavaResolver.resolveJavaLangObject();
          }catch(TermWareException ex){
              throw new TermWareRuntimeException(ex);
          }
        }
        return JavaNullTypeModel.INSTANCE;
    }
  }
  
  public List<JavaTypeModel>  getSuperInterfaces() throws TermWareException
  {
      Type[] rfinterfaces =theClass_.getGenericInterfaces();
      if (rfinterfaces.length==0) {
          return Collections.emptyList();
      }else{
          List retval=new LinkedList();
          for(int i=0; i<rfinterfaces.length;++i){
              retval.add(createTypeModel(rfinterfaces[i]));
          }
          return retval;
      }
  }
  
  
  /**
   *return enclosed class
   */
  public JavaTypeModel  getEnclosedType() throws NotSupportedException, TermWareException
  {
      Class<?> enclosingClass = theClass_.getEnclosingClass();
      if (enclosingClass==null) {
          return null;
      }else{
          return new JavaClassTypeModel(enclosingClass);
      }
  }
          
    
  
  /**
   *@return referenced type. Works only if isArray()==true, otherwise
   *throws NotSupportedException
   */
  public JavaTypeModel  getReferencedType() throws NotSupportedException, TermWareException
  {
   if (theClass_.isArray()) {      
       return new JavaClassTypeModel(theClass_.getComponentType());
   }else{
       throw new NotSupportedException();
   }
  }
  

  public boolean hasMethodModels()
  {
      if (! (theClass_.isArray() || theClass_.isEnum()) ) {
          return true;
      }else{
          return false;
      }
  }
    
  /**
   * key of return values is name of methods.   
   */
  public Map<String, List<JavaMethodModel>>   getMethodModels() throws NotSupportedException
  {
     Map<String, List<JavaMethodModel> > retval = new TreeMap<String, List<JavaMethodModel> >();
     Method[] methods = theClass_.getDeclaredMethods();
     for(int i=0; i<methods.length; ++i) {
         Method m=methods[i];
         String name=m.getName();         
         List<JavaMethodModel> methodModels=retval.get(name);
         if (methodModels==null) {
             methodModels = new LinkedList<JavaMethodModel>();
             retval.put(name,methodModels);
         }
         methodModels.add(new JavaClassMethodModel(m,this));             
     }
     
     return  retval;
  }
    
  
  public List<JavaMethodModel>  findMethodModels(String name) throws EntityNotFoundException, NotSupportedException
  {
    boolean debug=false;  
  
   // if (name.equals("createTerm"))  {
   //     debug=true;
   // }
    
    List<JavaMethodModel> methodModels = new LinkedList<JavaMethodModel>();  
    Method[] methods = theClass_.getDeclaredMethods();
    boolean found=false;
    for(int i=0; i<methods.length; ++i) {
        if (debug) {
           LOG.log(Level.INFO,"compare with "+methods[i].getName()); 
        }
        if (methods[i].getName().equals(name)) {
            methodModels.add(new JavaClassMethodModel(methods[i],this));
            found=true;
        }
    }
    if (!found) {
      throw new EntityNotFoundException("method",name," in class "+this.getName());      
    }
    return methodModels;
  }

  public boolean hasMemberVariableModels()
  {
      if (theClass_.isArray() || theClass_.isEnum()||theClass_.isAnnotation()) {
          return false;
      }else{
          return true;
      }
  }
  
  public Map<String, JavaMemberVariableModel> getMemberVariableModels() throws NotSupportedException
  {
    Field[] fields=theClass_.getDeclaredFields();
    Map<String,JavaMemberVariableModel> retval = new TreeMap<String,JavaMemberVariableModel>();
    for(int i=0; i<fields.length; ++i) {
        retval.put(fields[i].getName(),new JavaClassFieldModel(fields[i],this));
    }
    return retval;
  }
  
  public JavaMemberVariableModel findMemberVariableModel(String name) throws EntityNotFoundException, NotSupportedException
  {
    try {  
     return new JavaClassFieldModel(theClass_.getDeclaredField(name),this);
    }catch(NoSuchFieldException ex){
      throw new EntityNotFoundException("Field ",name,"in "+this.getFullName());  
    }
  }
  
  public Map<String,JavaEnumConstantModel>  getEnumConstantModels() throws NotSupportedException
  {
     if (!theClass_.isEnum()) {
         throw new NotSupportedException();
     }     
     Object[] enumConstants = theClass_.getEnumConstants();
     Map<String,JavaEnumConstantModel> retval = new TreeMap<String,JavaEnumConstantModel>();
     for(int i=0; i<enumConstants.length; ++i) {
         JavaClassEnumConstantModel ce = new JavaClassEnumConstantModel(enumConstants[i],this);
         retval.put(ce.getName(),ce);
     }
     return retval;
  }
  
  
  public List<JavaConstructorModel>  getConstructorModels()
  {
      Constructor[] constructors = theClass_.getConstructors();
      List<JavaConstructorModel> retval = new ArrayList<JavaConstructorModel>(constructors.length);
      for(int i=0; i<constructors.length; ++i) {
          JavaClassConstructorModel cm = new JavaClassConstructorModel(constructors[i],this);
          retval.add(cm);
      }
      return retval;
  }
  
  public List<JavaInitializerModel> getInitializerModels()
  {
     return Collections.emptyList(); 
  }
  
  
  public boolean isNested()
  {
     return theClass_.getEnclosingClass()!=null;
  }
  
  public boolean hasNestedTypeModels()
  {
      return true;
  }
  
  public Map<String,JavaTypeModel> getNestedTypeModels() throws NotSupportedException, TermWareException
  {
      Class[] classes=theClass_.getDeclaredClasses();
      Map<String,JavaTypeModel> retval = new TreeMap<String,JavaTypeModel>();
      for(int i=0; i<classes.length; ++i) {
          retval.put(classes[i].getSimpleName(),new JavaClassTypeModel(classes[i]));
      }
      return retval;
  }
 
  public boolean hasTypeParameters()
  { return theClass_.getTypeParameters().length!=0; }
  
   
  public boolean isLocal()
  { return theClass_.isLocalClass(); }
  
  public boolean isAnonimous()
  { return theClass_.isAnonymousClass(); }
  
  public JavaStatementModel  getEnclosedStatement()
  { return null; }
  
  public Class<?>  getJavaClass()
  { return theClass_; }
  
  /**
   * return type parameters. (i. e.  for class<U,B> { ... }  definitions are <U,B> )
   */
  public List<JavaTypeVariableAbstractModel>  getTypeParameters()
  {
      TypeVariable<?> tv[]=theClass_.getTypeParameters();
      List<JavaTypeVariableAbstractModel> retval= new LinkedList<JavaTypeVariableAbstractModel>();
      for(int i=0; i<tv.length; ++i) {
          retval.add(new JavaClassTypeVariableModel(tv[i]));
      }
      return retval;
  }
  
  
  public boolean hasAnnotation(String annotationName)
  {
      Annotation[] annotations_ = theClass_.getAnnotations();
      for(int i=0; i<annotations_.length; ++i) {
          String shortName = annotations_[i].annotationType().getSimpleName();
          if (shortName.equals(annotationName)) {
              return true;
          }
      }
      return false;
  }
  
  public JavaAnnotationInstanceModel getAnnotation(String annotationName) throws NotSupportedException
  {
      Annotation[] annotations_ = theClass_.getAnnotations();
      for(int i=0; i<annotations_.length; ++i) {
          String shortName = annotations_[i].annotationType().getSimpleName();
          if (shortName.equals(annotationName)) {
              ElementType et =  ElementType.TYPE;         
              return new JavaClassAnnotationInstanceModel(et,annotations_[i],this);
          }
      }
      throw new NotSupportedException();      
  }
  
  public Map<String,JavaAnnotationInstanceModel> getAnnotationsMap() throws TermWareException
  {
    TreeMap<String,JavaAnnotationInstanceModel> retval = new TreeMap<String,JavaAnnotationInstanceModel>();
    Annotation[] annotations_ = theClass_.getAnnotations();
    for(int i=0; i<annotations_.length; ++i) {
        String shortName=annotations_[i].annotationType().getSimpleName();
        retval.put(shortName,new JavaClassAnnotationInstanceModel(ElementType.TYPE,annotations_[i],this));
    }
    return retval;
  }
  
  public boolean hasASTTerm()
  { return false; }
  
  public Term  getASTTerm()
  { return null; }
  
  /**
   * return "ClassTypeModel(context)"
   */
  public Term getModelTerm() throws TermWareException
  {
      return TermUtils.createTerm("ClassTypeModel",TermUtils.createJTerm(JavaPlaceContextFactory.createNewTypeContext(this)));
  }
  
  static public JavaTypeModel createTypeModel(Type type) throws TermWareException
  {
    if (type instanceof GenericArrayType) {
        GenericArrayType gtype = (GenericArrayType)type;
        JavaTypeModel componentType=createTypeModel(gtype.getGenericComponentType());
        return new JavaArrayTypeModel(componentType,null);
    }else if (type instanceof ParameterizedType)  {
        ParameterizedType ptype=(ParameterizedType)type;
        List<JavaTypeModel> typeArguments = new LinkedList<JavaTypeModel>();
        Type ta[] =ptype.getActualTypeArguments();        
        Type rt=ptype.getRawType();
        JavaTypeModel otm = createTypeModel(rt);
        for(int i=0; i<ta.length; ++i) {
            JavaTypeModel cm=createTypeModel(ta[i]);
            typeArguments.add(cm);
        }
        return new JavaTypeArgumentBoundTypeModel(otm,typeArguments,otm);
    }else if (type instanceof TypeVariable<?>) {
        return new JavaClassTypeVariableModel((TypeVariable)type);
    }else if (type instanceof WildcardType) {
        WildcardType wtype=(WildcardType)type;
        Type[] lb=wtype.getLowerBounds();
        Type[] ub=wtype.getUpperBounds();
        Type[] b=null;
        JavaWildcardBoundsKind wtmt=JavaWildcardBoundsKind.OBJECT;
        if (lb.length==0 && ub.length==0) {
            // nothing, all as normal.
        }else if(lb.length==0 && ub.length!=0) {
            // only upper bounds, extends.
            wtmt=JavaWildcardBoundsKind.EXTENDS;
            b=ub;
        }else if(lb.length!=0 && ub.length==0) {
            wtmt=JavaWildcardBoundsKind.SUPER;
            b=lb;
        }else{
            JavaTypeModel lbm = createTypeModel(lb[0]);
            JavaTypeModel ubm = createTypeModel(ub[0]);  
            if (ubm.getName().equals("Object") && ubm.getPackageModel().getName().equals("java.lang")) {
                wtmt=JavaWildcardBoundsKind.SUPER;
                b=lb;
            }else{
                // lower and upper type both exists. impossible.
                throw new AssertException("extends and uper in one type bound are impossible, lbm="+lbm.getFullName()+",ubm="+ubm.getFullName());
            }
        }
        JavaTypeModel retval;
        if (b!=null && b.length > 1) {
            retval=new JavaWildcardBoundsTypeModel(wtmt,createTypeModel(b[0]));
        }else{
            retval=new JavaWildcardBoundsTypeModel();
        }
        return retval;   
    }else if (type instanceof Class<?>) {
        Class<?> ctype = (Class<?>)type;
        if (ctype.isArray()) {
            JavaTypeModel componentType = JavaClassTypeModel.createTypeModel(ctype.getComponentType());
            return new JavaArrayTypeModel(componentType,null);
        }else if (ctype.isPrimitive()) {
            if (ctype.equals(Boolean.TYPE)) {
                return JavaPrimitiveTypeModel.BOOLEAN;
            }else if (ctype.equals(Character.TYPE)) {
                return JavaPrimitiveTypeModel.CHAR;                
            }else if (ctype.equals(Byte.TYPE)) {
                return JavaPrimitiveTypeModel.BYTE;
            }else if (ctype.equals(Short.TYPE)) {
                return JavaPrimitiveTypeModel.SHORT;
            }else if (ctype.equals(Integer.TYPE)) {
                return JavaPrimitiveTypeModel.INT;
            }else if (ctype.equals(Long.TYPE)) {
                return JavaPrimitiveTypeModel.LONG;
            }else if (ctype.equals(Float.TYPE)) {
                return JavaPrimitiveTypeModel.FLOAT;
            }else if (ctype.equals(Double.TYPE)) {
                return JavaPrimitiveTypeModel.DOUBLE;
            }else if (ctype.equals(Void.TYPE)) {
                return JavaPrimitiveTypeModel.VOID;
            }else{
                throw new AssertException("Invalid primitive type:"+ctype.getName());
            }
        }
        return new JavaClassTypeModel(ctype);
    }else{
        throw new AssertException("Impossible Java type");
    }
  }
  
  static public Class forName(String className) throws TermWareException
  {
    try {  
      return Main.getFacts().getPackagesStore().getJarsClassLoader().loadClass(className);
    }catch(ClassNotFoundException ex){
      try {  
        return Class.forName(className);
      }catch(ClassNotFoundException ex1){
          throw new AssertException("Can't load class "+className,ex1);
      }
    }
  }
 
    /**
     * translate modifiers from java.lang.reflect modifiers to our ModifiersModel
     */
    static public int translateModifiers(int jm)
    {
      int retval=0;
      if (Modifier.isAbstract(jm)) {
          retval |= JavaTermModifiersModel.ABSTRACT;
      }
      if (Modifier.isFinal(jm)){
          retval |= JavaTermModifiersModel.FINAL;
      }
      if (Modifier.isNative(jm)){
          retval |= JavaTermModifiersModel.NATIVE;
      }
      if (Modifier.isPrivate(jm)) {
          retval |= JavaTermModifiersModel.PRIVATE;
      }else if(Modifier.isProtected(jm)){
          retval |= JavaTermModifiersModel.PROTECTED;
      }else if(Modifier.isPublic(jm)){
          retval |= JavaTermModifiersModel.PUBLIC;
      }
      if (Modifier.isStatic(jm)) {
          retval |= JavaTermModifiersModel.STATIC;
      }
      if (Modifier.isStrict(jm)) {
          retval |= JavaTermModifiersModel.STRICTFP;
      }
      if (Modifier.isSynchronized(jm)) {
          retval |= JavaTermModifiersModel.SYNCHRONIZED;
      }
      if (Modifier.isTransient(jm)) {
          retval |= JavaTermModifiersModel.TRANSIENT;
      }
      if (Modifier.isVolatile(jm)) {
          retval |= JavaTermModifiersModel.VOLATILE;
      }
      return retval;        
    }
    
    private Class<?> theClass_;
    private static final Logger LOG = Logger.getLogger(JavaClassTypeModel.class.getName());
    
}
