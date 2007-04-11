/*
 * ITermVisitor.java
 *
 * Copyright (c) 2004-2007 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker;

import java.util.HashSet;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHolder;
import ua.gradsoft.termware.TermWareException;



public interface ITermVisitor
{

 public boolean  doFirst(Term t, TermHolder result, HashSet<Term> trace)
                                                   throws TermWareException;

 public boolean  doSecond(Term t, TermHolder result, HashSet<Term> trace)
                                                   throws TermWareException;
 

}


