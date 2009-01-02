/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ua.gradsoft.parsers.dot;

import java.io.Reader;
import ua.gradsoft.parsers.dot.jjt.JJTDotParser;
import ua.gradsoft.termware.IParser;
import ua.gradsoft.termware.IParserFactory;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareInstance;

/**
 *
 * @author rssh
 */
public class DotParserFactory implements IParserFactory
{

/**
  * create parser object.
  *Term arg can be nil or atom option or list of options.
  **/
 public IParser  createParser(Reader in, String inFname, Term arg, TermWareInstance instance)  throws TermWareException
 {
     JJTDotParser parser = new JJTDotParser(in);
     parser.setInFname(inFname);
     return new DotParser(parser,arg,this);
 }
    
 public ASTTransformers  getASTTransformers()
 {
    if (astTransformers_==null) {
        astTransformers_=new ASTTransformers();
    } 
    return astTransformers_;
 }
 
 private ASTTransformers astTransformers_=null;
 
}
