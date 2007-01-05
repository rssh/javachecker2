/*
 * JavaClassTypeModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.Main;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
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
       int lastDotIndex = theClass_.getName().lastIndexOf('.');
       if (lastDotIndex==-1) {
           return theClass_.getName();
       }else{
           return theClass_.getName().substring(lastDotIndex+1);
       }
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
  
  public boolean isTypeArgument()
  {
      return false;
  }
  
  public boolean isWildcardBounds()
  {
      return false;
  }
  
  public boolean isUnknown()
  {
    return false;  
  }
  
  /**
   *return enclosed class
   */
  public JavaTypeModel  getEnclosedType() throws NotSupportedException, TermWareException
  {
      Class enclosingClass = theClass_.getEnclosingClass();
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
  
  public boolean canCheck()
  { return false; }
  
  public boolean check() throws TermWareException
  { return true; }

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
  public Map<String,List<JavaMethodAbstractModel> >   getMethodModels() throws NotSupportedException
  {
     Map<String, List<JavaMethodAbstractModel> > retval = new TreeMap<String, List<JavaMethodAbstractModel> >();
     Method[] methods = theClass_.getMethods();
     for(int i=0; i<methods.length; ++i) {
         Method m=methods[i];
         String name=m.getName();         
         List<JavaMethodAbstractModel> methodModels=retval.get(name);
         if (methodModels==null) {
             methodModels = new LinkedList<JavaMethodAbstractModel>();
             retval.put(name,methodModels);
         }
         methodModels.add(new JavaClassMethodModel(m,this));             
     }
     
     return  retval;
  }
    
  
  public List<JavaMethodAbstractModel>  findMethodModels(String name) throws EntityNotFoundException, NotSupportedException
  {
    List<JavaMethodAbstractModel> methodModels = new LinkedList<JavaMethodAbstractModel>();  
    Method[] methods = theClass_.getMethods();  
    boolean found=false;
    for(int i=0; i<methods.length; ++i) {
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
      if (theClass_.isArray() || theClass_.isEnum()) {
          return false;
      }else{
          return true;
      }
  }
  
  public Map<String,JavaMemberVariableAbstractModel> getMemberVariableModels() throws NotSupportedException
  {
    Field[] fields=theClass_.getFields();
    Map<String,JavaMemberVariableAbstractModel> retval = new TreeMap<String,JavaMemberVariableAbstractModel>();
    for(int i=0; i<fields.length; ++i) {
        retval.put(fields[i].getName(),new JavaClassFieldModel(fields[i],this));
    }
    return retval;
  }
  
  public JavaMemberVariableAbstractModel findMemberVariableModel(String name) throws EntityNotFoundException, NotSupportedException
  {
    try {  
     return new JavaClassFieldModel(theClass_.getField(name),null);
    }catch(NoSuchFieldException ex){
      throw new EntityNotFoundException("Field ",name,"in "+this.getFullName());  
    }
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
      Class[] classes=theClass_.getClasses();
      Map<String,JavaTypeModel> retval = new TreeMap<String,JavaTypeModel>();
      for(int i=0; i<classes.length; ++i) {
          retval.put(classes[i].getSimpleName(),new JavaClassTypeModel(classes[i]));
      }
      return retval;
  }
 
  public boolean hasTypeParameters()
  { return theClass_.getTypeParameters().length!=0; }
  
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
  
  
  static public JavaTypeModel createTypeModel(Type type) throws TermWareException
  {
    if (type instanceof GenericArrayType) {
        GenericArrayType gtype = (GenericArrayType)type;
        JavaTypeModel componentType=createTypeModel(gtype.getGenericComponentType());
        return new JavaArrayTypeModel(componentType);
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
        return new JavaArgumentBoundTypeModel(otm,typeArguments,otm);
    }else if (type instanceof TypeVariable<?>) {
        return new JavaClassTypeVariableModel((TypeVariable)type);
    }else if (type instanceof WildcardType) {
        WildcardType wtype=(WildcardType)type;
        Type[] lb=wtype.getLowerBounds();
        Type[] ub=wtype.getUpperBounds();
        Type[] b=null;
        JavaWildcardBoundsTypeModel.Type wtmt=JavaWildcardBoundsTypeModel.Type.OBJECT;
        if (lb.length==0 && ub.length==0) {
            // nothing, all as normal.
        }else if(lb.length==0 && ub.length!=0) {
            // only upper bounds, extends.
            wtmt=JavaWildcardBoundsTypeModel.Type.EXTENDS;
            b=ub;
        }else if(lb.length!=0 && ub.length==0) {
            wtmt=JavaWildcardBoundsTypeModel.Type.SUPER;
            b=lb;
        }else{
            // lower and upper type both exists. impossible.
            throw new AssertException("extends and uper in one type bound are impossible");
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
        return new JavaClassTypeModel(ctype);
    }else{
        throw new AssertException("Impossible Java type");
    }
  }
 
  
    
    private Class<?> theClass_;
    
}
