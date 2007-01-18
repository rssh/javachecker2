/*
 * JPEStartTransformer.java
 *
 * Created on ������, 3, ���� 2007, 23:58
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.jpe;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;

/**
 *Start transformer, which insert JPE in each term inside compilation unit.
 * @author Ruslan Shevchenko
 */
public class JPEStartTransformer 
{
    
    public JPEStartTransformer(String transformationName)
    {
        transformationName_=transformationName;
    }
    
    public Term transform(Term t) throws TermWareException
    {
        //System.err.println("get term for transformation:"+TermHelper.termToString(t));
        Term[] retBody=new Term[t.getArity()];
        for(int i=0; i<t.getArity(); ++i) {
            Term ct = t.getSubtermAt(i);
            Term[] ctb = new Term[1];
            ctb[0]=ct;
            retBody[i]=TermWare.getInstance().getTermFactory().createTerm(transformationName_,ctb);
        }
        Term retval= TermWare.getInstance().getTermFactory().createTerm(t.getName(),retBody);
        //System.err.println("retval:"+TermHelper.termToString(retval));
        return retval;
    }
    
    private String transformationName_;
}
