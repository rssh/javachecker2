/*
 * JavaTypeModelHelper.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Iterator;
import java.util.List;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Helper for operations under types.
 * @author Ruslan Shevchenko
 */
public class JavaTypeModelHelper {
    
    
    
    /**
     * define subtyping.
     *See section 4.10  of  Java Language Specification
     *@return  true if s is supertype of t  ( t < s)
     */
    public static boolean subtypeOrSame(JavaTypeModel t, JavaTypeModel s) throws TermWareException
    {
      if (s.isTypeArgument()) {
         JavaTypeVariableAbstractModel sv = (JavaTypeVariableAbstractModel)s;
         for(JavaTypeModel bound : sv.getBounds()) {
             if (!subtypeOrSame(t,bound)) {
                 return false;
             }
         }
         return true;         
      } else if (s.isWildcardBounds()) {
         JavaWildcardBoundsTypeModel sw = (JavaWildcardBoundsTypeModel)s;
         switch(sw.getKind()) {
             case OBJECT: return true;
             case EXTENDS:  return subtypeOrSame(t,sw.getBoundTypeModel());
             case SUPER: return subtypeOrSame(sw.getBoundTypeModel(),t);
         }
      }else if (t.isPrimitiveType()) {
          if (s.isPrimitiveType()) {
              if (t==s) {
                  return true;
              }else{
                 return subtypeOrSamePrimitive(t.getName(),s.getName());
              }
          }else{
              return false;
          }
      }else if (t.isClass()) {
          if (s.isClass()) {
              if (s instanceof JavaClassTypeModel) {
                  // in runtime we have no type arguments, i. e. rowtype.
                  JavaClassTypeModel cs = (JavaClassTypeModel)s;
                  if (t instanceof JavaClassTypeModel) {
                      JavaClassTypeModel ct = (JavaClassTypeModel)t;
                      return cs.getJavaClass().isAssignableFrom(ct.getJavaClass());
                  }else{                                            
                      if (!same(t,cs)) {
                          if (t.getName().equals("Object")) {
                              if (t.getPackageModel().getName().equals("java.lang")) {
                                  return cs.getJavaClass().equals(java.lang.Object.class);
                              }
                          }
                          try {
                            return subtypeOrSame(t.getSuperClass(),cs);
                          }catch(NotSupportedException ex){
                              return false;
                          }
                      }else{
                          return true;
                      }                      
                  }
              }else{
                  // two class models.                                  
                  if (samePrimaryName(t,s)) {   
                     return subtypeOrSameWithSameName(t,s); 
                  }else{
                    try {  
                     JavaTypeModel td=t.getSuperClass();
                     if (td!=null && !td.isNull()) {
                         return subtypeOrSame(td,s);
                     }else{
                         return same(s,JavaResolver.resolveJavaLangObject());
                     }
                    }catch(NotSupportedException ex){
                        return false;
                    }
                  }                          
              }
          }else if (s.isInterface()) {
              try {
                // s is interface.
                List<JavaTypeModel> tds=t.getSuperInterfaces();              
                for(JavaTypeModel td: tds) {
                  if (subtypeOrSame(td,s)) {
                      return true;
                  }
                }
              }catch(NotSupportedException ex){
                  return false;
              }
              return false;
          }else{
             // t - class
             // s - ! class & ! interface
             return false; 
          }
      }else if (t.isInterface()){
          if (s.isClass()) {
              return same(s,JavaResolver.resolveJavaLangObject());
          }else if (s.isInterface()) {
              if (samePrimaryName(t,s)) {
                  return subtypeOrSameWithSameName(t,s); 
              }else{
                try {  
                  for(JavaTypeModel td : t.getSuperInterfaces()) {
                      if (subtypeOrSame(td,s)) {
                          return true;
                      }
                  }
                  return false;
                }catch(NotSupportedException ex){
                    return false;
                }
              }
          }else{
              return false;
          }
      }else if(t.isArray()){
          if (s.isArray()) {
            try {  
              return subtypeOrSame(t.getReferencedType(),s.getReferencedType());
            }catch(NotSupportedException ex){
                // impossible.
                return false;
            }
          }else if (s.isClass()) {
              return same(s,JavaResolver.resolveJavaLangObject());
          }else if (s.isInterface()){
              return same(s,JavaResolver.resolveJavaLangCloneable())||same(s,JavaResolver.resolveJavaIoSerializable());
          }else{
              return false;
          }
      }else if(t.isEnum()) {
          if (s.isEnum()) {
              return same(t,s);
          }else if(s.isClass()) {
              return same(s,JavaResolver.resolveJavaLangObject());
          }else if(s.isInterface()){
            try {  
              for(JavaTypeModel td: t.getSuperInterfaces()) {
                  if (subtypeOrSame(td,s)) {
                      return true;
                  }
              }
              return false;
            }catch(NotSupportedException ex){
                return false;
            }
          }else{
              return false;
          }
      }else if(t.isAnnotationType()) {
          if (s.isAnnotationType()) {
              return same(t,s);
          }else if(s.isClass()) {
              return same(s,JavaResolver.resolveJavaLangAnnotation());
          }else{
              return false;
          }
      }else if(t.isTypeArgument()){
          JavaTypeVariableAbstractModel tv = (JavaTypeVariableAbstractModel)t;
          for(JavaTypeModel b: tv.getBounds()) {
              if (!subtypeOrSame(b,tv)) {
                  return false;
              }
          }
          return true;          
      }else if(t.isWildcardBounds()) {
          JavaWildcardBoundsTypeModel tw = (JavaWildcardBoundsTypeModel)t;
          switch(tw.getKind()) {
              case OBJECT: return same(s,JavaResolver.resolveJavaLangObject());
              case EXTENDS:
                  return subtypeOrSame(tw.getBoundTypeModel(),s);
              case SUPER:
                  return subtypeOrSame(JavaResolver.resolveJavaLangObject(),s);
              default:
                  throw new AssertException("unknown wildcardbounds kind");
          }
      }else if(t.isUnknown()){
          return false;
      }
      return false; 
    }
    
    public static boolean same(JavaTypeModel x, JavaTypeModel y) throws TermWareException
    {
      if (x.isClass()) {
          return y.isClass() && sameNames(x,y);                 
      }else if (x.isInterface()){
          return y.isInterface() && sameNames(x,y);
      }else if (x.isArray()) {
        try {  
          return y.isArray() && same(x.getReferencedType(),y.getReferencedType());
        }catch(NotSupportedException ex){
            return false;
        }
      }else if (x.isAnnotationType()) {
          return y.isAnnotationType() && sameNames(x,y);
      }else if (x.isEnum()) {
          return y.isEnum() && sameNames(x,y);
      }else if (x.isPrimitiveType()) {
          return y.isPrimitiveType() && x.getName().equals(y.getName());
      }else if (x.isTypeArgument()) {
          if (y.isTypeArgument()) {
              JavaTypeVariableAbstractModel xv = (JavaTypeVariableAbstractModel)x;
              JavaTypeVariableAbstractModel yv = (JavaTypeVariableAbstractModel)y;
              List<JavaTypeModel> xb = xv.getBounds();
              List<JavaTypeModel> yb = yv.getBounds();
              if (xb.size()!=yb.size()) return false;
              Iterator<JavaTypeModel> itx = xb.iterator();
              Iterator<JavaTypeModel> ity = yb.iterator();
              while(itx.hasNext()) {
                  JavaTypeModel xbe = itx.next();
                  JavaTypeModel ybe = ity.next();
                  if (!same(xbe,ybe)) return false;
              }
              return true;
          }else{
              return false;
          }
      }else if (x.isWildcardBounds()) {
          if (y.isWildcardBounds()) {
              JavaWildcardBoundsTypeModel xw = (JavaWildcardBoundsTypeModel)x;
              JavaWildcardBoundsTypeModel yw = (JavaWildcardBoundsTypeModel)x;
              if (xw.getKind()==JavaWildcardBoundsKind.OBJECT) {
                  return yw.getKind()==JavaWildcardBoundsKind.OBJECT;
              }else{
                  return xw.getKind()==yw.getKind() && same(xw.getBoundTypeModel(),yw.getBoundTypeModel());
              }
          }else{
              return false;
          }
      }else{
          return false;
      }
    }

    
    public static boolean subtypeOrSamePrimitive(String tn,String sn)
    {
        if (sn==tn) return true;
        if (sn.equals("double")) {
            return tn.equals("double") || tn.equals("float") || tn.equals("long") || tn.equals("int")
                 || tn.equals("char") || tn.equals("short") || tn.equals("byte");
        }else if(sn.equals("float")){
            return tn.equals("float") || tn.equals("long") || tn.equals("int")
                 || tn.equals("char") || tn.equals("short") || tn.equals("byte");            
        }else if(sn.equals("long")) {
            return tn.equals("long") || tn.equals("int")
                 || tn.equals("char") || tn.equals("short") || tn.equals("byte");            
        }else if(sn.equals("int")) {
            return tn.equals("int")
                 || tn.equals("char") || tn.equals("short") || tn.equals("byte");            
        }else if(sn.equals("char")) {
            return tn.equals("char");
        }else if(sn.equals("short")) {
            return tn.equals("byte");
        }            
        return false;
    }

    
    public static boolean isBoolean(JavaTypeModel x)
    {
        return x.equals(JavaPrimitiveTypeModel.BOOLEAN) || x.getFullName().equals("java.lang.Boolean");
    }

    public static boolean isByte(JavaTypeModel x)
    {
       return x.equals(JavaPrimitiveTypeModel.BYTE) || x.getFullName().equals("java.lang.Byte");
    }
    
    
    public static boolean isShort(JavaTypeModel x)
    {
       return x.equals(JavaPrimitiveTypeModel.SHORT) || x.getFullName().equals("java.lang.Short");
    }

    
    public static  boolean isInt(JavaTypeModel x)
    {
        return x.equals(JavaPrimitiveTypeModel.INT) || x.getFullName().equals("java.lang.Integer");
    }
    
    public static  boolean isLong(JavaTypeModel x)
    {
        return x.equals(JavaPrimitiveTypeModel.INT) || x.getFullName().equals("java.lang.Long");        
    }

    public static boolean isFloat(JavaTypeModel x)
    {
        return x.equals(JavaPrimitiveTypeModel.FLOAT) || x.getFullName().equals("java.lang.Float");
    }

    public static boolean isDouble(JavaTypeModel x)
    {
        return x.equals(JavaPrimitiveTypeModel.DOUBLE) || x.getFullName().equals("java.lang.Double");
    }
    
    public static boolean isChar(JavaTypeModel x)
    {
        return x.equals(JavaPrimitiveTypeModel.CHAR) || x.getFullName().equals("java.lang.Char");
    }

    public static JavaTypeModel unboxingConversion(JavaTypeModel x)
    {
            if (isBoolean(x)) {
                return JavaPrimitiveTypeModel.BOOLEAN;
            }else if (isByte(x)) {
                return JavaPrimitiveTypeModel.BYTE;
            }else if (isChar(x)){
                return JavaPrimitiveTypeModel.CHAR;
            }else if (isShort(x)){
                return JavaPrimitiveTypeModel.SHORT;
            }else if (isInt(x)) {
                return JavaPrimitiveTypeModel.INT;
            }else if (isLong(x)) {
                return JavaPrimitiveTypeModel.LONG;
            }else if (isFloat(x)){
                return JavaPrimitiveTypeModel.FLOAT;
            }else if (isDouble(x)){
                return JavaPrimitiveTypeModel.DOUBLE;
            }else{
                return x;
            }
    }
    
    private static boolean subtypeOrSameWithSameName(JavaTypeModel x, JavaTypeModel y) throws TermWareException
    {
      if (x instanceof JavaArgumentBoundTypeModel) {
          JavaArgumentBoundTypeModel xa=(JavaArgumentBoundTypeModel)x;
          if (y instanceof JavaArgumentBoundTypeModel) {
              JavaArgumentBoundTypeModel ya=(JavaArgumentBoundTypeModel)y;
              if (xa.getResolvedTypeArguments().size()!=ya.getResolvedTypeArguments().size()) {
                  return false;
              }
              List<JavaTypeModel> xal=xa.getResolvedTypeArguments();
              List<JavaTypeModel> yal=ya.getResolvedTypeArguments();
              Iterator<JavaTypeModel> xit=xal.iterator();
              Iterator<JavaTypeModel> yit=xal.iterator();
              while(xit.hasNext()) {
                  JavaTypeModel t=xit.next();
                  JavaTypeModel s=yit.next();
                  if (!subtypeOrSame(t,s)) {
                      return false;
                  }
              }
              return true;
          }else if(y.hasTypeParameters()) {
              List<JavaTypeVariableAbstractModel> yl = y.getTypeParameters();
              List<JavaTypeModel> xl=xa.getResolvedTypeArguments();
              if (xl.size() != yl.size()) {
                  return false;
              }
              Iterator<JavaTypeVariableAbstractModel> yit=yl.iterator();
              Iterator<JavaTypeModel> xit=xl.iterator();
              while(xit.hasNext()) {
                  JavaTypeModel t=xit.next();
                  JavaTypeModel s=yit.next();
                  if (!subtypeOrSame(t,s)) {
                      return false;
                  }                  
              }
              return true;
          }else{
              // y is row type
              return true;
          }                                  
      }else if (x.hasTypeParameters()) {
          if (y instanceof JavaArgumentBoundTypeModel) {
              JavaArgumentBoundTypeModel ya=(JavaArgumentBoundTypeModel)y;              
              List<JavaTypeModel> yl=ya.getResolvedTypeArguments();
              List<JavaTypeVariableAbstractModel> xl=x.getTypeParameters();
              if (xl.size()!=yl.size()) return false;
              Iterator<JavaTypeVariableAbstractModel> xit=xl.iterator();
              Iterator<JavaTypeModel> yit=yl.iterator();
              while(xit.hasNext()) {
                  JavaTypeModel t = xit.next();
                  JavaTypeModel s = yit.next();
                  if (!subtypeOrSame(t,s)) {
                      return false;
                  }
              }
              return true;                      
          }else if(y.hasTypeParameters()){
              List<JavaTypeVariableAbstractModel> xl=x.getTypeParameters();
              List<JavaTypeVariableAbstractModel> yl=x.getTypeParameters();
              if (xl.size()!=yl.size()) return false;
              Iterator<JavaTypeVariableAbstractModel> xit=xl.iterator();
              Iterator<JavaTypeVariableAbstractModel> yit=yl.iterator();
              while(xit.hasNext()) {
                  JavaTypeModel t = xit.next();
                  JavaTypeModel s = yit.next();
                  if (!subtypeOrSame(t,s)) {
                      return false;
                  }
              }
              return true;                                    
          }else{
              // y is row type.
              return true;
          }            
      }else{
          // x is row type, unsafe
          return true;
      }
    }
    
    public static boolean sameNames(JavaTypeModel x,JavaTypeModel y)
    {
      return x.getName().equals(y.getName()) && 
              x.getPackageModel().getName().equals(y.getPackageModel().getName());  
    }

    
    public static Term createModelTermList(List<JavaTypeModel> l) throws TermWareException
    {
      Term retval=TermUtils.createNil();
      int size=l.size();
      for(int i=0; i<size;++i){
          JavaTypeModel tm = l.get(size-i-1);
          retval=TermUtils.createTerm("cons",tm.getModelTerm(),retval);
      }
      return retval;
    }
    
    private static boolean samePrimaryName(JavaTypeModel x,JavaTypeModel y)
    {
        while (x instanceof JavaArgumentBoundTypeModel) {
            JavaArgumentBoundTypeModel xa=(JavaArgumentBoundTypeModel)x;              
            x=xa.getOrigin();
        }
        while (y instanceof JavaArgumentBoundTypeModel) {
            JavaArgumentBoundTypeModel ya=(JavaArgumentBoundTypeModel)y;              
            y=ya.getOrigin();
        }
        return sameNames(x,y);
    }
    
    
}
