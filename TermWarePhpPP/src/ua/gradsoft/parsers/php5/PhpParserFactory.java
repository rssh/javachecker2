
package ua.gradsoft.parsers.php5;

import java.io.Reader;
import ua.gradsoft.parsers.php5.jjt.PHP;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.IParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareInstance;

/**
 *Parser factory
 * @author rssh
 */
public class PhpParserFactory implements IParserFactory
{


    public IParser createParser(Reader in, String inFname, Term arg, TermWareInstance instance)
            throws TermWareException
    {
        PHP phpParser = new PHP(in);
        phpParser.setInFname(inFname);
        phpParser.setTermWareInstance(instance);
        if (astTransformer_==null) {
            astTransformer_=new ASTTransformer();
        }
        return new PhpParser(phpParser,arg,this);
    }

    ASTTransformer getASTTransformer()
    { return astTransformer_; }

    private ASTTransformer astTransformer_=null;



}
