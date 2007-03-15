/*
 * JavaTypeModelHelper.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ua.gradsoft.javachecker.EntityNotFoundException;
import ua.gradsoft.javachecker.NotSupportedException;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Helper for operations under types.
 * @author Ruslan Shevchenko
 */
public class JavaTypeModelHelper {
    
    
    public static boolean subtypeOrSame(JavaTypeModel t, JavaTypeModel s) throws TermWareException
    {
      return subtypeOrSame(t,s,new MethodMatchingConversions(),false);
    }
    
    
    /**
     * define subtyping.
     *See section 4.10  of  Java Language Specification
     *@return  true if s is supertype of t  ( t < s)
     */
    public static boolean subtypeOrSame(JavaTypeModel t, JavaTypeModel s, MethodMatchingConversions conversions, boolean debug) throws TermWareException
    {   
      boolean subtypeOrSameDebugEnabled=true;  
      //debug=true;
      if (debug)  {
          LOG.info("subtimeOrSame("+t.getFullName()+","+s.getFullName()+")");
      }
      boolean retval=false;
      if (s==t) {
          if (debug) {
              LOG.info("subtimeOrSame success");
          }
          return true;
      }
      MethodMatchingConversions cn=new MethodMatchingConversions(conversions);
      if (s.isTypeArgument()) {
         JavaTypeVariableAbstractModel sv = (JavaTypeVariableAbstractModel)s;          
             List<JavaTypeModel> svBounds = sv.getBounds();         
             for(JavaTypeModel bound : svBounds) {
               if (bound instanceof JavaTypeArgumentBoundTypeModel) {
                   boolean fixedPoint=false;
                   JavaTypeArgumentBoundTypeModel abound = (JavaTypeArgumentBoundTypeModel)bound;
                   try {
                     List<JavaTypeModel> l = abound.getResolvedTypeArguments();
                     for(JavaTypeModel il: l) {
                       if (il==s) {
                           fixedPoint=true;
                           break;
                       }
                     }
                   }catch(EntityNotFoundException ex){
                       throw new InvalidJavaTermException(ex.getMessage(),abound.getTypeArguments());
                   }
                   if (fixedPoint) {                   
                      continue;
                   }
               }  
               if (!subtypeOrSame(t,bound,cn,debug)) {
                 if (debug) {
                   LOG.info("subtimeOrSame failed [1]");
                 }
                 return false;
               }
             }         
            if (debug) {
               LOG.info("subtumeOrSame success [2]");
            }
            conversions.assign(cn); 
            return true;         
         
      } else if (s.isWildcardBounds()) {
         JavaWildcardBoundsTypeModel sw = (JavaWildcardBoundsTypeModel)s;        
         switch(sw.getKind()) {
             case OBJECT: 
                 retval=true;
                 break;
             case EXTENDS:  
                 retval=subtypeOrSame(t,sw.getBoundTypeModel(),cn,debug);
                 break;
             case SUPER:             
                 retval=true;
                 break;
         }
         if (retval) {
             conversions.assign(cn);
         }
         return retval;
      }else if (t.isPrimitiveType()) {        
          if (s.isPrimitiveType()) {
              if (t==s) {
                  retval=true;
              }else{
                   retval=subtypeOrSamePrimitive(t.getName(),s.getName());
              }
          }
      }else if (t.isClass()) {
          if (s.isClass()) {           
              if (s instanceof JavaClassTypeModel) {
                  // in runtime we have no type arguments, i. e. rowtype.
                  cn.incrementNRows();
                  JavaClassTypeModel cs = (JavaClassTypeModel)s;
                  if (t instanceof JavaClassTypeModel) {
                      JavaClassTypeModel ct = (JavaClassTypeModel)t;
                      retval=cs.getJavaClass().isAssignableFrom(ct.getJavaClass());
                  }else{                                            
                      if (!same(t,cs)) {
                          if (t.getName().equals("Object")) {
                              if (t.getPackageModel().getName().equals("java.lang")) {                                  
                                  retval = cs.getJavaClass().equals(java.lang.Object.class);
                                  if (debug) {
                                      LOG.log(Level.INFO,"return "+retval);
                                  }
                                  return retval;
                              }
                          }
                          if (t instanceof JavaTypeArgumentBoundTypeModel) {
                              JavaTypeArgumentBoundTypeModel ta = (JavaTypeArgumentBoundTypeModel)t;
                              return subtypeOrSame(ta.getOrigin(),s,cn,debug);
                          }
                          try {
                            cn.incrementNNarrows();  
                            retval=subtypeOrSame(t.getSuperClass(),s,cn,debug);
                          }catch(NotSupportedException ex){
                              retval=false;
                          }
                      }else{
                          retval=true;
                      }                      
                  }
              }else{
                  // two class models.                                  
                  if (samePrimaryName(t,s)) {   
                      retval= subtypeOrSameWithSameName(t,s,cn,debug); 
                      if (debug)  {
                         LOG.info("subtimeOrSameWithSameName("+t.getFullName()+","+s.getFullName()+") return "+retval);
                      }                     
                      return retval;
                  }else{
                    try {                          
                     cn.incrementNNarrows();   
                     JavaTypeModel td=t.getSuperClass();                   
                     if (td!=null && !td.isNull()) {
                         return subtypeOrSame(td,s,cn,debug);
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
                  if (subtypeOrSame(td,s,cn,debug)) {
                      if (debug) {
                          LOG.log(Level.INFO,"subtypeOrSame result "+true);
                      }
                      cn.incrementNNarrows();
                      conversions.assign(cn);
                      return true;
                  }
                }
              }catch(NotSupportedException ex){
                  if (debug) {
                      LOG.log(Level.INFO,"subtypeOrSame result "+false);
                  }                  
                  return false;
              }
              try {
                  cn.incrementNNarrows();
                  JavaTypeModel td = t.getSuperClass();
                  if (td!=null && !td.isNull()) {
                      retval=subtypeOrSame(td,s,cn,debug);
                      if (debug) {
                          LOG.log(Level.INFO,"subtypeOrSame result "+retval);
                      }
                      return retval;
                  }
              }catch(NotSupportedException ex){
                  retval=false;
              }            
          }else{
             // t - class
             // s - ! class & ! interface
             retval=false; 
          }
      }else if (t.isInterface()){
          if (s.isClass()) {
              return same(s,JavaResolver.resolveJavaLangObject());
          }else if (s.isInterface()) {
              if (samePrimaryName(t,s)) {
                  return subtypeOrSameWithSameName(t,s,cn,debug); 
              }else{
                try {  
                  for(JavaTypeModel td : t.getSuperInterfaces()) {
                      if (subtypeOrSame(td,s,cn,debug)) {
                          if (debug) {
                             LOG.log(Level.INFO,"subtypeOrSame("+t.getName()+","+s.getName()+") result "+retval);
                          }                          
                          cn.incrementNNarrows();
                          conversions.assign(cn);
                          return true;
                      }
                  }
                  retval=false;
                }catch(NotSupportedException ex){
                    retval=false;
                }
              }
          }else{
              retval=false;
          }
      }else if(t.isArray()){
          if (s.isArray()) {
            try {  
              retval=subtypeOrSame(t.getReferencedType(),s.getReferencedType(),cn,debug);
            }catch(NotSupportedException ex){
                // impossible.
                retval=false;
            }
          }else if (s.isClass()) {
              retval=same(s,JavaResolver.resolveJavaLangObject());
          }else if (s.isInterface()){
              retval=same(s,JavaResolver.resolveJavaLangCloneable())||same(s,JavaResolver.resolveJavaIoSerializable());
          }else{
              retval=false;
          }
      }else if(t.isEnum()) {
          if (s.isEnum()) {
              retval=same(t,s);
          }else if(s.isClass()) {
              retval=same(s,JavaResolver.resolveJavaLangObject());
          }else if(s.isInterface()){
            try {  
              for(JavaTypeModel td: t.getSuperInterfaces()) {
                  if (subtypeOrSame(td,s,cn,debug)) {
                      retval=true;
                      if (debug) {
                          LOG.log(Level.INFO,"subtypeOrSame("+t.getName()+","+s.getName()+") result "+retval);
                      }                     
                      cn.incrementNNarrows();
                      
                      return retval;
                  }
              }
              retval=false;
            }catch(NotSupportedException ex){
                retval=false;
            }
          }else{
              retval=false;
          }
      }else if(t.isAnnotationType()) {
          if (s.isAnnotationType()) {
              retval=same(t,s);
          }else if(s.isClass()) {
              retval=same(s,JavaResolver.resolveJavaLangAnnotation());
          }else{
              retval=false;
          }
      }else if(t.isTypeArgument()){      
          JavaTypeVariableAbstractModel tv = (JavaTypeVariableAbstractModel)t;
          retval=true;
          for(JavaTypeModel b: tv.getBounds()) {
              if (subtypeOrSame(b,s,cn,debug)) {
                  retval=true;
                  if (debug) {
                      LOG.log(Level.INFO,"subtypeOrSame("+t.getName()+","+s.getName()+") result "+retval);
                  }                          
                  break;
              }else{                  
                  retval=false;       
              }
          }                   
      }else if(t.isWildcardBounds()) {                   
          JavaWildcardBoundsTypeModel tw = (JavaWildcardBoundsTypeModel)t;
          LOG.log(Level.INFO,"subtypeOrSame, t is WildCarsBoubbds");
          switch(tw.getKind()) {
              case OBJECT: 
                  // subtypeOrSame(t,s) = subtymePoSame(<?>, s) = s==Object.
                  retval=same(s,JavaResolver.resolveJavaLangObject());
                  break;
              case EXTENDS:
                  // subtypeOrSame(t,s) = subtypeOrSame(<? extends A>,s) = subtypeOrSame(A,s)
                  retval=subtypeOrSame(tw.getBoundTypeModel(),s,cn,debug);
                  break;
              case SUPER:
                  // subtypeOrSame(t,s) = subtypeOrSame(<? super A>,s) = same(A,s) subtypeOrSame(Object,s)
                  retval=same(tw.getBoundTypeModel(),s) || subtypeOrSame(JavaResolver.resolveJavaLangObject(),s,cn,debug);
                  break;
              default:
                  throw new AssertException("unknown wildcardbounds kind");
          }
      }else if(t.isNull()) {
          // null is subtype of any type.
          retval=true;
      }else if(t.isUnknown()){
          retval=false;
      }else{
          //!! what is t ?
          LOG.warning("unknown kind of t, class="+t.getClass().getName());
      }
      if (debug) {
             LOG.info("subtumeOrSame("+t.getName()+","+s.getName()+") result is "+retval);
      }
      return retval;         
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
        return x.equals(JavaPrimitiveTypeModel.LONG) || x.getFullName().equals("java.lang.Long");        
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

    /**
     * Object -> appropriate primitive
     */
    public static JavaTypeModel unboxingConversion(JavaTypeModel x,MethodMatchingConversions c)
    {
            if (isBoolean(x) && x!=JavaPrimitiveTypeModel.BOOLEAN) {
                c.incrementNUnboxing();
                return JavaPrimitiveTypeModel.BOOLEAN;
            }else if (isByte(x)) {
                c.incrementNUnboxing();
                return JavaPrimitiveTypeModel.BYTE;
            }else if (isChar(x)){
                c.incrementNUnboxing();
                return JavaPrimitiveTypeModel.CHAR;
            }else if (isShort(x)){
                c.incrementNUnboxing();
                return JavaPrimitiveTypeModel.SHORT;
            }else if (isInt(x)) {
                c.incrementNUnboxing();
                return JavaPrimitiveTypeModel.INT;
            }else if (isLong(x)) {
                c.incrementNUnboxing();
                return JavaPrimitiveTypeModel.LONG;
            }else if (isFloat(x)){
                c.incrementNUnboxing();
                return JavaPrimitiveTypeModel.FLOAT;
            }else if (isDouble(x)){
                c.incrementNUnboxing();
                return JavaPrimitiveTypeModel.DOUBLE;
            }else{
                return x;
            }
    }
    
    /**
     * primitive type -> appropriative Object
     */
    public static JavaTypeModel  boxingConversion(JavaTypeModel x, MethodMatchingConversions c)
    {
      try {  
        if (isBoolean(x)) {
            c.incrementNBoxing();
            return JavaResolver.resolveTypeModelByFullClassName("java.lang.Boolean");            
        }else if (isByte(x)) {
            c.incrementNUnboxing();
            return JavaResolver.resolveTypeModelByFullClassName("java.lang.Byte");            
        }else if (isChar(x)) {
            c.incrementNUnboxing();
            return JavaResolver.resolveTypeModelByFullClassName("java.lang.Character");            
        }else if (isShort(x)) {
            c.incrementNUnboxing();
            return JavaResolver.resolveTypeModelByFullClassName("java.lang.Short");
        }else if (isInt(x)) {
            c.incrementNUnboxing();
            return JavaResolver.resolveTypeModelByFullClassName("java.lang.Integer");
        }else if (isLong(x)) {
            c.incrementNUnboxing();
            return JavaResolver.resolveTypeModelByFullClassName("java.lang.Long");
        }else if (isFloat(x)) {
            c.incrementNUnboxing();
            return JavaResolver.resolveTypeModelByFullClassName("java.lang.Float");
        }else if (isDouble(x)) {
            c.incrementNUnboxing();
            return JavaResolver.resolveTypeModelByFullClassName("java.lang.Double");
        }else{
            return x;
        }  
      }catch(EntityNotFoundException ex){
          //impossible, this is standard java
          throw new TermWareRuntimeException(ex);
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
    
    private static boolean subtypeOrSameWithSameName(JavaTypeModel x, JavaTypeModel y, MethodMatchingConversions conversions, boolean debug) throws TermWareException
    {
      if (debug){
          LOG.log(Level.INFO,"subtypeOrSameWithSameName("+x.getFullName()+","+y.getFullName()+","+debug+")");
      }  
      if (x==y) {    
           return true;
      }    
      if (x instanceof JavaTypeArgumentBoundTypeModel) {
          JavaTypeArgumentBoundTypeModel xa=(JavaTypeArgumentBoundTypeModel)x;
          String originName = xa.getOrigin().getName();
          if (originName.equals("Enum")) {
              return true;
          }
          if (y instanceof JavaTypeArgumentBoundTypeModel) {                            
              JavaTypeArgumentBoundTypeModel ya=(JavaTypeArgumentBoundTypeModel)y;              
              List<JavaTypeModel> xal;
              List<JavaTypeModel> yal;
              try {
                 xal=xa.getResolvedTypeArguments();                
              }catch(EntityNotFoundException ex){
                  throw new InvalidJavaTermException(ex.getMessage(),xa.getTypeArguments());
              }              
              try {
                yal=ya.getResolvedTypeArguments();
              }catch(EntityNotFoundException ex){
                  throw new InvalidJavaTermException(ex.getMessage(),ya.getTypeArguments());
              }              
              if (xal.size()!=yal.size()) {
                    return false;
              }                            
              Iterator<JavaTypeModel> xit=xal.iterator();
              Iterator<JavaTypeModel> yit=yal.iterator();
              while(xit.hasNext()) {
                  JavaTypeModel t=xit.next();
                  JavaTypeModel s=yit.next();
                  if (!subtypeOrSame(t,s,conversions,debug)) {
                      if (debug) {
                         LOG.log(Level.INFO,"subtypeOrSameWithSameName("+x.getFullName()+","+y.getFullName()+","+debug+") FAILED [1]");
                      }
                      return false;
                  }
              }
              return true;             
          }else if(y.hasTypeParameters()) {  
              return true;             
              /*
              List<JavaTypeVariableAbstractModel> yl = y.getTypeParameters();
              List<JavaTypeModel> xl;
              try {
                 xl=xa.getResolvedTypeArguments();
              }catch(EntityNotFoundException ex){
                  throw new InvalidJavaTermException(ex.getMessage(),xa.getTypeArguments());
              }                               
              if (xl.size() != yl.size()) {
                  return false;
              }
              Iterator<JavaTypeVariableAbstractModel> yit=yl.iterator();
              Iterator<JavaTypeModel> xit=xl.iterator();
              while(xit.hasNext()) {
                  JavaTypeModel t=xit.next();
                  JavaTypeModel s=yit.next();
                  if (!subtypeOrSame(t,s,debug)) {
                      return false;
                  }                  
              }
              return true;
               */
          }else{
              // y is row type
              conversions.incrementNRows();
              return true;
          }                                  
      }else if (x.hasTypeParameters()) {
          if (y instanceof JavaTypeArgumentBoundTypeModel) {
              /*
              JavaTypeArgumentBoundTypeModel ya=(JavaTypeArgumentBoundTypeModel)y;                            
              List<JavaTypeModel> yl;
              try {
                yl=ya.getResolvedTypeArguments();
              }catch(EntityNotFoundException ex){
                  throw new InvalidJavaTermException(ex.getMessage(),ya.getTypeArguments());
              }              
              List<JavaTypeVariableAbstractModel> xl=x.getTypeParameters();
              if (xl.size()!=yl.size()) return false;
              Iterator<JavaTypeVariableAbstractModel> xit=xl.iterator();
              Iterator<JavaTypeModel> yit=yl.iterator();
              while(xit.hasNext()) {
                  JavaTypeModel t = xit.next();
                  JavaTypeModel s = yit.next();
                  if (!subtypeOrSame(t,s,debug)) {
                      return false;
                    }
              }*/
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
                  if (!subtypeOrSame(t,s,conversions,debug)) {
                      return false;
                  }
              }
              return true;                                    
          }else{
              // y is row type.
              conversions.incrementNRows();
              return true;
          }            
      }else{
          // x is row type, unsafe
          conversions.incrementNRows();
          return true;
      }
    }
    
    public static boolean sameNames(JavaTypeModel x,JavaTypeModel y)
    {
      return x.getName().equals(y.getName()) && 
              x.getPackageModel().getName().equals(y.getPackageModel().getName());  
    }

    
    public static Term createModelTermList(List<JavaTypeModel> l) throws TermWareException, EntityNotFoundException
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
        while (x instanceof JavaTypeArgumentBoundTypeModel) {
            JavaTypeArgumentBoundTypeModel xa=(JavaTypeArgumentBoundTypeModel)x;              
            x=xa.getOrigin();
        }
        while (y instanceof JavaTypeArgumentBoundTypeModel) {
            JavaTypeArgumentBoundTypeModel ya=(JavaTypeArgumentBoundTypeModel)y;              
            y=ya.getOrigin();
        }
        return sameNames(x,y);
    }
    
    private static final Logger LOG = Logger.getLogger(JavaTypeModelHelper.class.getName());

}
