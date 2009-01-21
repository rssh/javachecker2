package ua.gradsoft.parsers.php5;

import ua.gradsoft.parsers.php5.jjt.PHP;
import ua.gradsoft.parsers.php5.jjt.PHPConstants;
import ua.gradsoft.parsers.php5.jjt.ParseException;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.InvocationException;
import ua.gradsoft.termware.exceptions.TermParseException;

/**
 *Jaba
 * @author rssh
 */
public class PhpParser implements IParser
{



    public Term readTerm() throws TermWareException {
        try {
          phpParser_.token_source.SwitchTo(PHPConstants.HTML_STATE);
          phpParser_.PhpPage();
        }catch(ParseException ex){
            Exception ex1 = new InvocationException(ex);
            throw new TermParseException("can't parse page",ex);
        }
        Term retval = phpParser_.getRootNode();
        if (simplicify_) {
           retval = owner_.getASTTransformer().transform(retval);
        }
        eof_=true;
        return retval;
    }

    public boolean eof() {
        return eof_;
    }

    PhpParser(PHP phpParser, Term args, PhpParserFactory owner)
    {
      phpParser_=phpParser;
      owner_=owner;
    }

    private PHP phpParser_;
    private PhpParserFactory owner_;

    private boolean simplicify_;
    private boolean eof_;

}
