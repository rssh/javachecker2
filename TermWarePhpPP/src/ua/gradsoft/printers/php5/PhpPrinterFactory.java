
package ua.gradsoft.printers.php5;

import java.io.PrintWriter;
import ua.gradsoft.termware.IPrinter;
import ua.gradsoft.termware.IPrinterFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWareException;

/**
 *Printer factory for PHP
 * @author rssh
 */
public class PhpPrinterFactory implements IPrinterFactory
{

    public IPrinter createPrinter(PrintWriter out, String outTag, TermSystem sys, Term arg) throws TermWareException {
        return new PhpPrinter(out, outTag);
    }



}
