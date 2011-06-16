/*
 * JavaPrinterFactory.java
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.printers.java5;

import java.io.PrintWriter;
import ua.gradsoft.termware.IPrinter;
import ua.gradsoft.termware.IPrinterFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;

/**
 *Implementatation of IPrinterFactory interface for Java language
 * @author Ruslan Shevchenko
 */
public class JavaPrinterFactory implements IPrinterFactory
{

    /**
     * create printer.
     */
    public IPrinter createPrinter(PrintWriter out, String outTag, TermSystem sys, Term arg) throws TermWareException {
        return new JavaPrinter(out, outTag);
    }

    
}
