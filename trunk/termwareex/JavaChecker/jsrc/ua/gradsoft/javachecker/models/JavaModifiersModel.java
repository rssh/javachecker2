/*
 * JavaModifiersModel.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

/**
 *Model for Java Modifiers
 * @author Ruslan Shevchenko
 */
public class JavaModifiersModel {
    
    public JavaModifiersModel(int modifiers)
    {
        modifiers_=modifiers;
    }
    
    public JavaModifiersModel()
    {
        modifiers_=0;
    }

     public static final int PUBLIC = 0x0001;
     public static final int PROTECTED = 0x0002;
     public static final int PRIVATE = 0x0004;
     public static final int ABSTRACT = 0x0008;
     public static final int STATIC = 0x0010;
     public static final int FINAL = 0x0020;
     public static final int SYNCHRONIZED = 0x0040;
     public static final int NATIVE = 0x0080;
     public static final int TRANSIENT = 0x0100;
     public static final int VOLATILE = 0x0200;
     public static final int STRICTFP = 0x1000;

     /** A set of accessors that indicate whether the specified modifier
         is in the set. */

     public boolean isPublic()
     {
       return (modifiers_ & PUBLIC) != 0;
     }

     public boolean isProtected()
     {
       return (modifiers_ & PROTECTED) != 0;
     }

     public boolean isPrivate()
     {
       return (modifiers_ & PRIVATE) != 0;
     }

     public boolean isStatic()
     {
       return (modifiers_ & STATIC) != 0;
     }

     public boolean isAbstract()
     {
       return (modifiers_ & ABSTRACT) != 0;
     }

     public boolean isFinal()
     {
       return (modifiers_ & FINAL) != 0;
     }

     public boolean isNative()
     {
       return (modifiers_ & NATIVE) != 0;
     }

     public boolean isStrictfp()
     {
       return (modifiers_ & STRICTFP) != 0;
     }

     public boolean isSynchronized()
     {
       return (modifiers_ & SYNCHRONIZED) != 0;
     }

     public boolean isTransient()
      {
       return (modifiers_ & TRANSIENT) != 0;
     }

     public boolean isVolatile()
     {
       return (modifiers_ & VOLATILE) != 0;
     }

     /**
      * Removes the given modifier.
      */
      void removeModifier(int mod)
     {
        modifiers_ = ( modifiers_ & ~mod );
     }
   

      /**
       *@return internal int value
       */
     public int getIntValue()
     { return modifiers_; }
    
    private int modifiers_;
    
}
