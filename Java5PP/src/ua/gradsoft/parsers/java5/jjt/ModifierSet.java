/*
 * ModifierSet.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.parsers.java5.jjt;

/**
 * Copied from jjt for better maintance
 */
public class ModifierSet {
    
     /* Definitions of the bits in the modifiers field.  */
     public static final int PUBLIC    = 0x0001;
     public static final int PROTECTED = 0x0002;
     public static final int PRIVATE   = 0x0004;
     public static final int ABSTRACT  = 0x0008;
     public static final int STATIC    = 0x0010;
     public static final int FINAL     = 0x0020;
     public static final int SYNCHRONIZED = 0x0040;
     public static final int NATIVE       = 0x0080;
     public static final int TRANSIENT    = 0x0100;
     public static final int VOLATILE     = 0x0200;
     public static final int STRICTFP     = 0x1000;

     public static final int VARARGS      = 0x2000;



     /** A set of accessors that indicate whether the specified modifier
         is in the set. */

     public boolean isPublic(int modifiers)
     {
       return (modifiers & PUBLIC) != 0;
     }

     public boolean isProtected(int modifiers)
     {
       return (modifiers & PROTECTED) != 0;
     }

     public boolean isPrivate(int modifiers)
     {
       return (modifiers & PRIVATE) != 0;
     }

     public boolean isStatic(int modifiers)
     {
       return (modifiers & STATIC) != 0;
     }

     public boolean isAbstract(int modifiers)
     {
       return (modifiers & ABSTRACT) != 0;
     }

     public boolean isFinal(int modifiers)
     {
       return (modifiers & FINAL) != 0;
     }

     public boolean isNative(int modifiers)
     {
       return (modifiers & NATIVE) != 0;
     }

     public boolean isStrictfp(int modifiers)
     {
       return (modifiers & STRICTFP) != 0;
     }

     public boolean isSynchronized(int modifiers)
     {
       return (modifiers & SYNCHRONIZED) != 0;
     }

     public boolean isTransient(int modifiers)
      {
       return (modifiers & TRANSIENT) != 0;
     }

     public boolean isVolatile(int modifiers)
     {
       return (modifiers & VOLATILE) != 0;
     }

     public boolean isVarArg(int modifiers)
     {
       return (modifiers & VARARGS) != 0;
     }

     /**
      * Removes the given modifier.
      */
     static int removeModifier(int modifiers, int mod)
     {
        return modifiers & ~mod;
     }
   

    
}
