/*
 * JavaModifiersModel.java
 *
 * Created on May 3, 2007, 2:04 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for Java Modifiers
 * @author rssh
 */
public abstract class JavaModifiersModel 
{
    
    public static final int ABSTRACT = 8;

    public static final int FINAL = 32;

    public static final int NATIVE = 128;

    public static final int PRIVATE = 4;

    public static final int PROTECTED = 2;


    public static final int PUBLIC = 1;

    public static final int STATIC = 16;

    public static final int STRICTFP = 4096;

    public static final int SYNCHRONIZED = 64;

    public static final int TRANSIENT = 256;

    public static final int VARARGS = 8192;

    public static final int VOLATILE = 512;

    /**
     * 
     * 
     * @return internal int value
     */
    public abstract int getIntValue();

    /*
     * A set of accessors that indicate whether the specified modifier
     *         is in the set.
     */
    
    
    public boolean isAbstract()
    { return (getIntValue() & ABSTRACT)!=0; }

    public boolean isFinal()
    { return (getIntValue() & FINAL)!=0; }

    public boolean isNative()
    { return (getIntValue() & NATIVE)!=0; }

    public boolean isPrivate()
    { return (getIntValue() & PRIVATE)!=0; }

    public boolean isProtected()
    { return (getIntValue() & PROTECTED)!=0; }

    public boolean isPublic()
    { return (getIntValue() & PUBLIC)!=0; }
    
    public boolean isStatic()
    { return (getIntValue() & STATIC)!=0; }
    

    public boolean isStrictfp()
    { return (getIntValue() & STRICTFP)!=0; }
    
    public boolean isSynchronized()
    { return (getIntValue() & SYNCHRONIZED)!=0; }

    public boolean isTransient()
    { return (getIntValue() & TRANSIENT)!=0; }

    public boolean isVarArgs()
    { return (getIntValue() & VARARGS)!=0; }

    public boolean isVolatile()
    { return (getIntValue() & VOLATILE)!=0; }
    
     public  Term getModelTerm() throws TermWareException
     {
         return TermUtils.createTerm("Modifiers",TermUtils.createInt(getIntValue()));
     }
    
}
