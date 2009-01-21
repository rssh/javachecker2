
package ua.gradsoft.printers.php5;

import java.io.PrintWriter;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.printers.AbstractPrinter;

/**
 *Printer for PHP
 * @author rssh
 */
public class PhpPrinter extends AbstractPrinter
{

    public PhpPrinter(PrintWriter out, String outTag) {
        super(out, outTag);
    }

    public void writeTerm(Term t) throws TermWareException {
        writeTerm(t,0);
    }

    public void writeTerm(Term t, int level) {
        t.print(out_);
    }

    public void flush()
    {
       out_.flush();
    }

    private void writeIdent(int level)
    {
        for(int i=0; i<level; ++i) {
            out_.print(' ');
        }
    }
    
    private void writeNextLine(int level)
    {
        out_.println();
        writeIdent(level);
    }

}
