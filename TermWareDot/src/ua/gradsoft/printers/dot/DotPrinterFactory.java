/*
 * Part of TermWareDot
 * (C) Grad-Soft ltd.
 * http://www.gradsoft.ua
 */

package ua.gradsoft.printers.dot;

import java.io.PrintWriter;
import ua.gradsoft.termware.IPrinter;
import ua.gradsoft.termware.IPrinterFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;

/**
 *Factoru for dot graphwiz language.
 * @author rssh
 */
public class DotPrinterFactory implements IPrinterFactory
{

    /**
     * create printer.
     */
    public IPrinter createPrinter(PrintWriter out, String outTag, TermSystem sys, Term arg) throws TermWareException {
        return new DotPrinter(out, outTag);
    }

    
}
