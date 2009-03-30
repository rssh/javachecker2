/*
 * JavaTypeModelHelper.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
    
    
    public static boolean subtypeOrSame(JavaTypeModel t, JavaTypeModel s) throws TermWareException, EntityNotFoundException {
        return subtypeOrSame(t,s,new MethodMatchingConversions(),false,false);
    }
    
    
    /**
     * define subtyping.
     *See section 4.10  of  Java Language Specification
     *fill necessor matching conversion if needed.
     *@param t first typeModel to check
     *@param s second typemodel to check
     *@param conversions - matching conversions, necessory for matching
     *@param freeTypeArguments - when true, match type argument with any type and add matching to substitution
     *@param debug -- provide additional logging when true
     *@return  true if s is supertype of t  ( t < s)
     */
    public static boolean subtypeOrSame(JavaTypeModel t, JavaTypeModel s, MethodMatchingConversions conversions, boolean freeTypeArguments,boolean debug) throws TermWareException {
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
        if (s.isTypeVariable()) {
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
                if (!subtypeOrSame(t,bound,cn,freeTypeArguments,debug)) {
                    if (debug) {
                        LOG.info("subtimeOrSame failed [1]");
                    }
                    return false;
                }
            }
            cn.getSubstitution().put(sv,t);
            if (debug) {
                LOG.info("subtypeOrSame success [2]");
                LOG.info("substitution is:"+cn.getSubstitution().toString());
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
                    retval=subtypeOrSame(t,sw.getBoundTypeModel(),cn,freeTypeArguments,debug);
                    break;
                case SUPER:
                    retval=true;
                    break;
                default:
                    throw new AssertException("Invalid wildcard bound type:"+sw.getKind());
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
                        if (debug) {
                            LOG.info("class models:"+cs.getJavaClass().getName()+","+ct.getJavaClass().getName());
                            LOG.info("return assignable");
                            LOG.info("ClassLoaders:"+cs.getJavaClass().getClassLoader()+","+ct.getJavaClass().getClassLoader());
                        }
                        Class<?> sClass = cs.getJavaClass();
                        Class<?> tClass = ct.getJavaClass();
                        retval=cs.getJavaClass().isAssignableFrom(ct.getJavaClass());
                    }else{
                        if (!same(t,cs)) {
                            if (t.getName().equals("Object")) {
                                if (t.getPackageModel().getName().equals("java.lang")) {
                                    retval = cs.getJavaClass().equals(java.lang.Object.class);
                                    if (debug) {
                                        LOG.log(Level.INFO,"return "+retval);
                                    }
                                    if (retval) {
                                        conversions.assign(cn);
                                    }
                                    return retval;
                                }
                            }
                            if (t instanceof JavaTypeArgumentBoundTypeModel) {
                                JavaTypeArgumentBoundTypeModel ta = (JavaTypeArgumentBoundTypeModel)t;
                                retval=subtypeOrSame(ta.getOrigin(),s,cn,freeTypeArguments,debug);
                                if (retval) {
                                    conversions.assign(cn);
                                }
                                return retval;
                            }
                            try {
                                cn.incrementNNarrows();
                                retval=subtypeOrSame(t.getSuperClass(),s,cn,freeTypeArguments,debug);
                            }catch(EntityNotFoundException ex){
                                throw new InvalidJavaTermException(ex.getMessage(),ex.getFileAndLine(),ex);
                            }
                        }else{
                            retval=true;
                        }
                    }
                }else{
                    // two class models.
                    if (samePrimaryName(t,s)) {
                        retval= subtypeOrSameWithSameName(t,s,cn,freeTypeArguments,debug);
                        if (debug)  {
                            LOG.info("subtimeOrSameWithSameName("+t.getFullName()+","+s.getFullName()+") return "+retval);
                        }
                        if (retval) {
                            conversions.assign(cn);
                        }
                        return retval;
                    }else{
                        try {
                            cn.incrementNNarrows();
                            JavaTypeModel td=t.getSuperClass();
                            if (td!=null && !td.isNull()) {
                                retval=subtypeOrSame(td,s,cn,freeTypeArguments,debug);
                            }else{
                                retval=same(s,JavaResolver.resolveJavaLangObject());
                            }
                        }catch(EntityNotFoundException ex){
                            throw new InvalidJavaTermException(ex.getMessage(),ex.getFileAndLine(),ex);
                        }
                    }
                }
            }else if (s.isInterface()) {
                    // s is interface.
                    List<JavaTypeModel> tds=t.getSuperInterfaces();
                    for(JavaTypeModel td: tds) {
                        if (subtypeOrSame(td,s,cn,freeTypeArguments,debug)) {
                            if (debug) {
                                LOG.log(Level.INFO,"subtypeOrSame result "+true);
                            }
                            cn.incrementNNarrows();
                            conversions.assign(cn);
                            return true;
                        }
                    }
                try{
                    cn.incrementNNarrows();
                    JavaTypeModel td = t.getSuperClass();
                    if (td!=null && !td.isNull()) {
                        retval=subtypeOrSame(td,s,cn,freeTypeArguments,debug);
                        if (debug) {
                            LOG.log(Level.INFO,"subtypeOrSame result "+retval);
                        }
                        if (retval) {
                            conversions.assign(cn);
                        }
                        return retval;
                    }
                }catch(EntityNotFoundException ex){
                    throw new InvalidJavaTermException(ex.getMessage(),ex.getFileAndLine(),ex);
                }
            }else{
                // t - class
                // s - ! class & ! interface
                retval=false;
            }
        }else if (t.isInterface()){
            if (s.isClass()) {
                retval=same(s,JavaResolver.resolveJavaLangObject());
            }else if (s.isInterface()) {
                if (samePrimaryName(t,s)) {
                    retval=subtypeOrSameWithSameName(t,s,cn,freeTypeArguments,debug);
                }else{
                        for(JavaTypeModel td : t.getSuperInterfaces()) {
                            if (subtypeOrSame(td,s,cn,freeTypeArguments,debug)) {
                                if (debug) {
                                    LOG.log(Level.INFO,"subtypeOrSame("+t.getName()+","+s.getName()+") result "+retval);
                                }
                                cn.incrementNNarrows();
                                conversions.assign(cn);
                                return true;
                            }
                        }
                        retval=false;
                }
            }else{
                retval=false;
            }
        }else if(t.isArray()){
            if (s.isArray()) {
                retval=subtypeOrSame(t.getReferencedType(),s.getReferencedType(),cn,freeTypeArguments,debug);
            }else if (s.isClass()) {
                retval=same(s,JavaResolver.resolveJavaLangObject());
            }else if (s.isInterface()){
                retval=same(s,JavaResolver.resolveJavaLangCloneable())||same(s,JavaResolver.resolveJavaIoSerializable());
            }else{
                retval=false;
            }
        }else if(t.isEnum()) {
            if (debug) {
                LOG.info("t is enum");
            }
            if (s.isEnum()) {
                if (debug) {
                    LOG.info("s is enum");
                }
                boolean tIsEnumConstant=false;
                boolean sIsEnumConstant=false;
                JavaTypeModel tEnclosed=null;
                JavaTypeModel sEnclosed=null;
                if (t.isNested()) {
                    tEnclosed=t.getEnclosedType();
                    if (tEnclosed.isEnum()) {
                        tIsEnumConstant=true;
                    }
                }
                if (s.isNested()) {
                    sEnclosed=s.getEnclosedType();
                    if (sEnclosed.isEnum()) {
                        sIsEnumConstant=true;
                    }
                }
                if (tIsEnumConstant) {
                    if (sIsEnumConstant) {
                        retval=same(t,s,debug);
                    }else{
                        retval=same(tEnclosed,s,debug);
                    }
                }else{
                    if (sIsEnumConstant) {
                        return false;
                    }else{
                        retval=same(t,s,debug);
                    }
                }
            }else if(s.isClass()) {
                if (debug) {
                    LOG.info("s is class");
                }
                try {
                    retval=subtypeOrSame(JavaResolver.resolveTypeModelByFullClassName("java.lang.Enum"),s,cn,freeTypeArguments,debug);
                }catch(EntityNotFoundException ex){
                    throw new AssertException("Can't resolve java.lang.Enum",ex);
                }
            }else if(s.isInterface()){
                if (debug) {
                    LOG.info("s is interface");
                }
                    for(JavaTypeModel td: t.getSuperInterfaces()) {
                        if (subtypeOrSame(td,s,cn,freeTypeArguments,debug)) {
                            retval=true;
                            if (debug) {
                                LOG.log(Level.INFO,"subtypeOrSame("+t.getName()+","+s.getName()+") result "+retval);
                            }
                            cn.incrementNNarrows();
                            if (retval) {
                                conversions.assign(cn);
                            }
                            return retval;
                        }
                    }
                    retval=false;
            }else{
                if (debug) {
                    LOG.info("s is not Enum, Class or Interface");
                }
                retval=false;
            }
        }else if(t.isAnnotationType()) {
            if (debug) {
                LOG.log(Level.INFO,"t is annotation");
            }
            if (s.isAnnotationType()) {
                retval=same(t,s);
            }else if(s.isClass()||s.isInterface()) {
                retval=same(s,JavaResolver.resolveJavaLangAnnotationAnnotation());
            }else{
                retval=false;
            }
        }else if(t.isTypeVariable()){
            if (debug) {
                LOG.log(Level.INFO,"t is TypeArgument");
            }
            JavaTypeVariableAbstractModel tv = (JavaTypeVariableAbstractModel)t;
            retval=true;
            List<JavaTypeModel> tvb = tv.getBounds();
            if (tvb.isEmpty()) {
                if (!freeTypeArguments) {
                    tvb = Collections.singletonList(JavaResolver.resolveJavaLangObject());
                }
            }
            for(JavaTypeModel b: tvb) {
                if (subtypeOrSame(b,s,cn,freeTypeArguments,debug)) {
                    retval=true;
                    if (debug) {
                        LOG.log(Level.INFO,"subtypeOrSame("+t.getName()+","+s.getName()+") result "+retval);
                    }
                    break;
                }else{
                    retval=false;
                }
            }
            if (retval) {
                cn.getSubstitution().put(tv,s);
                if (debug) {
                    LOG.log(Level.INFO,"substitution now is:"+cn.getSubstitution().toString());
                }
            }
        }else if(t.isWildcardBounds()) {
            JavaWildcardBoundsTypeModel tw = (JavaWildcardBoundsTypeModel)t;
            if (debug) {
                LOG.log(Level.INFO,"subtypeOrSame, t is WildCarsBoubbds");
            }
            switch(tw.getKind()) {
                case OBJECT:
                    // subtypeOrSame(t,s) = subtymePoSame(<?>, s) = s==Object.
                    retval=same(s,JavaResolver.resolveJavaLangObject());
                    break;
                case EXTENDS:
                    // subtypeOrSame(t,s) = subtypeOrSame(<? extends A>,s) = subtypeOrSame(A,s)
                    retval=subtypeOrSame(tw.getBoundTypeModel(),s,cn,freeTypeArguments,debug);
                    break;
                case SUPER:
                    // subtypeOrSame(t,s) = subtypeOrSame(<? super A>,s) = same(A,s) subtypeOrSame(Object,s)
                    retval=same(tw.getBoundTypeModel(),s) || subtypeOrSame(JavaResolver.resolveJavaLangObject(),s,cn,freeTypeArguments,debug);
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
            LOG.info("subtimeOrSame("+t.getName()+","+s.getName()+") result is "+retval);
        }
        if (retval) {
            conversions.assign(cn);
        }
        return retval;
    }
    
    public static boolean same(JavaTypeModel x, JavaTypeModel y) throws TermWareException {
        return same(x,y,false); }
    
    public static boolean same(JavaTypeModel x, JavaTypeModel y, boolean debug) throws TermWareException {
        boolean retval=false;
        if (debug) {
            LOG.log(Level.INFO,"same("+x.getFullName()+","+y.getFullName()+")");
        }
        if (x.isClass()) {
            retval = y.isClass() && sameNames(x,y);
        }else if (x.isInterface()){
            retval = y.isInterface() && sameNames(x,y);
        }else if (x.isArray()) {
            retval = y.isArray() && same(x.getReferencedType(),y.getReferencedType());
        }else if (x.isAnnotationType()) {
            retval = y.isAnnotationType() && sameNames(x,y);
        }else if (x.isEnum()) {
            retval = y.isEnum() && sameNames(x,y);
        }else if (x.isPrimitiveType()) {
            return y.isPrimitiveType() && x.getName().equals(y.getName());
        }else if (x.isTypeVariable()) {
            if (y.isTypeVariable()) {
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
                retval = true;
            }else{
                retval = false;
            }
        }else if (x.isWildcardBounds()) {
            if (y.isWildcardBounds()) {
                JavaWildcardBoundsTypeModel xw = (JavaWildcardBoundsTypeModel)x;
                JavaWildcardBoundsTypeModel yw = (JavaWildcardBoundsTypeModel)x;
                if (xw.getKind()==JavaWildcardBoundsKind.OBJECT) {
                    retval = yw.getKind()==JavaWildcardBoundsKind.OBJECT;
                }else{
                    retval = xw.getKind()==yw.getKind() && same(xw.getBoundTypeModel(),yw.getBoundTypeModel());
                }
            }else{
                retval = false;
            }
        }else{
            retval = false;
        }
        if (debug) {
            LOG.log(Level.INFO,"retval is "+retval);
        }
        return retval;
    }
    
    
    public static boolean subtypeOrSamePrimitive(String tn,String sn) {
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
    
    /**
     * find minimal maximum from x and y
     */
    public static JavaTypeModel minmax(JavaTypeModel x,JavaTypeModel y, MethodMatchingConversions cn, boolean debug) throws TermWareException, EntityNotFoundException
    {
       JavaTypeModel retval=null; 
       if (x.isWildcardBounds()) {
           JavaWildcardBoundsTypeModel wx = (JavaWildcardBoundsTypeModel)x;
           if (y.isWildcardBounds()) {
               JavaWildcardBoundsTypeModel wy = (JavaWildcardBoundsTypeModel)y;
               switch(wx.getKind()) {
                   case EXTENDS:
                       switch(wy.getKind()) {
                           case EXTENDS:
                           {
                               JavaTypeModel bwx = wx.getBoundTypeModel();
                               JavaTypeModel bwy = wy.getBoundTypeModel();
                               JavaTypeModel nbw = minmax(bwx,bwy,cn,debug);
                               retval = new JavaWildcardBoundsTypeModel(JavaWildcardBoundsKind.EXTENDS,nbw);
                           } 
                           break;
                           case SUPER:
                           {
                               JavaTypeModel bwx = wx.getBoundTypeModel();
                               JavaTypeModel bwy = wy.getBoundTypeModel();
                               if (JavaTypeModelHelper.subtypeOrSame(bwx,bwy,cn,true,debug)) {
                                   retval = new JavaWildcardBoundsTypeModel(JavaWildcardBoundsKind.OBJECT,null);
                               }else if(JavaTypeModelHelper.subtypeOrSame(bwy,bwx,cn,true,debug)){
                                   // TODO:  create bounf with super and extends at the same type.
                                   retval = bwx;
                               }else{
                                   retval = bwy;
                               }
                           }
                           break;
                           case OBJECT:
                               retval = new JavaWildcardBoundsTypeModel(JavaWildcardBoundsKind.OBJECT,null);
                               break;
                           default:
                               throw new AssertException("Unknow bound type "+wy.getKind());
                       }
                       break;
                   case SUPER:
                   {
                       switch(wy.getKind()) {
                           case EXTENDS:
                           {
                               JavaTypeModel bwx = wx.getBoundTypeModel();
                               JavaTypeModel bwy = wy.getBoundTypeModel();
                               if (JavaTypeModelHelper.subtypeOrSame(bwx,bwy,cn,true,debug)) {
                                   retval=bwy;
                               }else if(JavaTypeModelHelper.subtypeOrSame(bwy,bwx,cn,true,debug)){
                                   retval=new JavaWildcardBoundsTypeModel(JavaWildcardBoundsKind.OBJECT,null);
                               }else{
                                   retval=bwx;
                               }
                           }
                           break;
                           case SUPER:
                           {
                               JavaTypeModel bwx = wx.getBoundTypeModel();
                               JavaTypeModel bwy = wy.getBoundTypeModel();
                               JavaTypeModel nw = minmax(bwx,bwy,cn,debug);
                               retval = new JavaWildcardBoundsTypeModel(JavaWildcardBoundsKind.SUPER,nw);
                           }
                           break;
                           case OBJECT:
                               retval=new JavaWildcardBoundsTypeModel(JavaWildcardBoundsKind.OBJECT,null);
                               break;
                           default:
                               throw new AssertException("Unknow bound type "+wy.getKind());
                       }                      
                   }
                   break;
                   case OBJECT:
                       retval=new JavaWildcardBoundsTypeModel(JavaWildcardBoundsKind.OBJECT,null);
                       break;               
                   default:
                       throw new AssertException("Unknow bound type "+wx.getKind());
               }                                                 
           }else{
             JavaTypeModel bwx = wx.getBoundTypeModel();
             switch(wx.getKind()) {
                 case EXTENDS:
                     if (subtypeOrSame(y,bwx,cn,true,debug)) {
                         retval=bwx;
                     }else{
                         retval=new JavaWildcardBoundsTypeModel(JavaWildcardBoundsKind.OBJECT,null);
                     }
                     break;
                 case SUPER:
                     if (subtypeOrSame(bwx,y,cn,true,debug)) {
                         retval=bwx;
                     }else{
                         retval=new JavaWildcardBoundsTypeModel(JavaWildcardBoundsKind.OBJECT,null);
                     }
                     break;
                 case OBJECT:
                     retval=bwx;
                     break;
                 default:
                     throw new AssertException("Invalid wildcard kind:"+wx.getKind());
             }
           }
       }else if (x instanceof JavaTypeArgumentBoundTypeModel) {
           if (y instanceof JavaTypeArgumentBoundTypeModel) {
               JavaTypeArgumentBoundTypeModel tx = (JavaTypeArgumentBoundTypeModel)x;
               JavaTypeArgumentBoundTypeModel ty = (JavaTypeArgumentBoundTypeModel)y;
               JavaTypeModel otx = tx.getOrigin();
               JavaTypeModel oty = ty.getOrigin();
               if (same(otx,oty)) {
                   List<JavaTypeModel> tvx=tx.getResolvedTypeArguments();
                   List<JavaTypeModel> tvy=ty.getResolvedTypeArguments();
                   if (tvx.size()!=tvy.size()) {
                       // impossible ?
                       retval=otx;
                   }else{
                       Iterator<JavaTypeModel> tvxi=tvx.iterator();
                       Iterator<JavaTypeModel> tvyi=tvy.iterator();
                       Iterator<JavaTypeVariableAbstractModel> ai = otx.getTypeParameters().iterator();
                       List<JavaTypeModel> nv = new ArrayList<JavaTypeModel>(tvx.size());
                       JavaTypeArgumentsSubstitution s = new JavaTypeArgumentsSubstitution();                                       
                       while(tvxi.hasNext()) {
                           JavaTypeModel cx = tvxi.next();
                           JavaTypeModel cy = tvyi.next();
                           JavaTypeModel ctn = minmax(cx,cy,cn,debug);
                           nv.add(cx);                                     
                           s.put(ai.next(),cx);
                       }
                       //Hack (may be pass content ('where' information) here ?)
                       retval = new JavaTypeArgumentBoundTypeModel(otx,nv,x);
                   }
               }else{
                  // genertal case, do generic evaluation later, as for all 
               }
           }else{
               // May be we compare with original class ?
               JavaTypeArgumentBoundTypeModel tx = (JavaTypeArgumentBoundTypeModel)x;
               JavaTypeModel otx = tx.getOrigin();
               if (JavaTypeModelHelper.same(otx,y)) {
                   retval=y;
               }
           }
       }else if( y instanceof JavaTypeArgumentBoundTypeModel) {
           JavaTypeArgumentBoundTypeModel ty = (JavaTypeArgumentBoundTypeModel)y;
           JavaTypeModel oty = ty.getOrigin();
           if (JavaTypeModelHelper.same(x,oty)) {
               retval=x;
           }
       }else if (x.isTypeVariable()) {
           JavaTypeVariableAbstractModel tx = (JavaTypeVariableAbstractModel)x;           
           if (y.isTypeVariable()) {
               JavaTypeVariableAbstractModel ty = (JavaTypeVariableAbstractModel)y;
               List<JavaTypeModel> xbounds = tx.getBounds();
               List<JavaTypeModel> ybounds = ty.getBounds();
               if (same(tx,ty)) {
                   retval=tx;
               }
               if (xbounds.isEmpty()||ybounds.isEmpty()) {
                  retval = JavaResolver.resolveJavaLangObject();
               }else{
                   // for now, does not resolve bounds minmax (not needed ?)
                   retval = JavaResolver.resolveJavaLangObject();
               }               
           }else{
               List<JavaTypeModel> xbounds = tx.getBounds();
               if (xbounds.size()==0) {
                   retval = JavaResolver.resolveJavaLangObject();
               }else{
                   retval = y; 
                   for(JavaTypeModel xb: xbounds) {
                     retval = JavaTypeModelHelper.minmax(retval,xb,cn,debug);
                   }                 
               }
           }
       }else if (y.isTypeVariable()) {
           JavaTypeVariableAbstractModel ty = (JavaTypeVariableAbstractModel)y;
           List<JavaTypeModel> ybounds = ty.getBounds();
           if (ybounds.size()==0) {
               retval = JavaResolver.resolveJavaLangObject();
           }else{
               retval = x;
               for(JavaTypeModel yb: ybounds) {
                   retval = JavaTypeModelHelper.minmax(retval,yb,cn,debug);
               }
           }           
       }else if (x.isPrimitiveType()) {
           return minmax(boxingConversion(x,cn),y,cn,debug);
       }else if (y.isPrimitiveType()) {
           return minmax(x,boxingConversion(y,cn),cn,debug);
       }
       
       // now all special cases are handled, generic algorithm follows:
       if (retval==null) {     
           if (subtypeOrSame(x,y,cn,true,debug)) {
               retval=y;
           }else if (subtypeOrSame(y,x,cn,true,debug)){
               retval=x;
           }else{           
               LinkedList<JavaTypeModel> toCheck = new LinkedList<JavaTypeModel>();
               toCheck.add(x);
               while(!toCheck.isEmpty()) {
                   JavaTypeModel current = toCheck.removeLast();
                   if (subtypeOrSame(y,current,cn,true,debug)) {
                       retval=current;
                       break;
                   }else{
                       List<JavaTypeModel> interfaces = current.getSuperInterfaces();
                       toCheck.addAll(interfaces);
                       if (!current.isEnum()) {
                           JavaTypeModel superclass = current.getSuperClass();
                           if (superclass!=null && !superclass.isNull()) {
                              toCheck.addLast(superclass);
                           }
                       }
                   }                   
               } 
           }           
       }
       if (retval==null) {
           LOG.warning("Possible incorrect minmax between "+x.getFullName()+" and "+y.getFullName());
           retval=JavaResolver.resolveJavaLangObject();
       }
       
       return retval;       
    }
    
    public static boolean isBoolean(JavaTypeModel x) {
        return x.equals(JavaPrimitiveTypeModel.BOOLEAN) || x.getFullName().equals("java.lang.Boolean");
    }
    
    public static boolean isByte(JavaTypeModel x) {
        return x.equals(JavaPrimitiveTypeModel.BYTE) || x.getFullName().equals("java.lang.Byte");
    }
    
    
    public static boolean isShort(JavaTypeModel x) {
        return x.equals(JavaPrimitiveTypeModel.SHORT) || x.getFullName().equals("java.lang.Short");
    }
    
    
    public static  boolean isInt(JavaTypeModel x) {
        return x.equals(JavaPrimitiveTypeModel.INT) || x.getFullName().equals("java.lang.Integer");
    }
    
    public static  boolean isLong(JavaTypeModel x) {
        return x.equals(JavaPrimitiveTypeModel.LONG) || x.getFullName().equals("java.lang.Long");
    }
    
    public static boolean isFloat(JavaTypeModel x) {
        return x.equals(JavaPrimitiveTypeModel.FLOAT) || x.getFullName().equals("java.lang.Float");
    }
    
    public static boolean isDouble(JavaTypeModel x) {
        return x.equals(JavaPrimitiveTypeModel.DOUBLE) || x.getFullName().equals("java.lang.Double");
    }
    
    public static boolean isChar(JavaTypeModel x) {
        return x.equals(JavaPrimitiveTypeModel.CHAR) || x.getFullName().equals("java.lang.Character");
    }
    
    /**
     * Object -> appropriate primitive
     */
    public static JavaTypeModel unboxingConversion(JavaTypeModel x,MethodMatchingConversions c) {
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
    public static JavaTypeModel  boxingConversion(JavaTypeModel x, MethodMatchingConversions c) {
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
    
    private static boolean subtypeOrSameWithSameName(JavaTypeModel x, JavaTypeModel y, MethodMatchingConversions conversions, boolean freeTypeArguments,boolean debug) throws TermWareException {
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
                    throw new InvalidJavaTermException(ex.getMessage(),xa.getTypeArguments(),ex);
                }
                try {
                    yal=ya.getResolvedTypeArguments();
                }catch(EntityNotFoundException ex){
                    throw new InvalidJavaTermException(ex.getMessage(),ya.getTypeArguments(),ex);
                }
                if (xal.size()!=yal.size()) {
                    return false;
                }
                Iterator<JavaTypeModel> xit=xal.iterator();
                Iterator<JavaTypeModel> yit=yal.iterator();
                while(xit.hasNext()) {
                    JavaTypeModel t=xit.next();
                    JavaTypeModel s=yit.next();
                    if (!subtypeOrSame(t,s,conversions,freeTypeArguments,debug)) {
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
                    if (!subtypeOrSame(t,s,conversions,freeTypeArguments,debug)) {
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
    
    public static boolean sameNames(JavaTypeModel x,JavaTypeModel y) throws TermWareException {
        if (x.isNested()) {
            if (y.isNested()) {
                 return sameNames(x.getEnclosedType(),y.getEnclosedType()) && x.getName().equals(y.getName());
            }else{
                return false;
            }
        }else{
            return x.getName().equals(y.getName()) &&
                    x.getPackageModel().getName().equals(y.getPackageModel().getName());
        }
    }
    
    
    public static Term createModelTermList(List<JavaTypeModel> l) throws TermWareException, EntityNotFoundException {
        Term retval=TermUtils.createNil();
        int size=l.size();
        for(int i=0; i<size;++i){
            JavaTypeModel tm = l.get(size-i-1);
            retval=TermUtils.createTerm("cons",tm.getModelTerm(),retval);
        }
        return retval;
    }
    
    private static boolean samePrimaryName(JavaTypeModel x,JavaTypeModel y) throws TermWareException {
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
