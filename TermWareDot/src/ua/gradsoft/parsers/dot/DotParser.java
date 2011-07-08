/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.gradsoft.parsers.dot;

import ua.gradsoft.parsers.dot.jjt.JJTDotParser;
import ua.gradsoft.parsers.dot.jjt.ParseException;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.TermParseException;

/**
 *
 * @author rssh
 */
public class DotParser implements IParser
{

    
    DotParser(JJTDotParser jjtDotParser, Term args, DotParserFactory owner) throws TermWareException
    {
      jjtDotParser_=jjtDotParser;
      owner_=owner;
      eof_=false; 
    }
    

    public Term  readTerm()  throws TermWareException
    {
      try {
          jjtDotParser_.dotGraph();     
      }catch(ParseException ex){
          throw new TermParseException(ex.getMessage());
      }
      Term t = jjtDotParser_.getRootNode();
          //System.err.println("simplify is set");
      t=owner_.getASTTransformers().simplifyBefore(t);
      t=owner_.getASTTransformers().eraseJjtParents(t);
      t=owner_.getASTTransformers().transformSeqToList(t);
      t=owner_.getASTTransformers().simplifyAfter(t);
      eof_=true;
      return t;
    }
    
    
    public boolean eof()
    {
        return eof_;
    }
    
   
    private JJTDotParser jjtDotParser_;
    private DotParserFactory owner_;
    private boolean           eof_;
    
}
