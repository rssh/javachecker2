/*
 * ITermVisitor.java
 *
 * Created on вівторок, 12, вересня 2006, 1:21
 *
 * UBS application
 *
 * Class: __MAME__
 *
 * Created on: вівторок, 12, вересня 2006, 1:21
 *
 * Owner: Ruslan Shevchenko
 *
 * Description:
 *
 * History:
 *
 * Copyright (c) 2004-2005 Infopulse Ukraine
 * Copyright (c) 2004-2005 GradSoft  Ukraine
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


