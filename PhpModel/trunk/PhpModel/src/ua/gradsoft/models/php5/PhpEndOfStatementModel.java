
package ua.gradsoft.models.php5;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public class PhpEndOfStatementModel extends PhpStatementModel
{

    public PhpEndOfStatementModel(Term t, PhpCompileEnvironment pce)
    {
        if (t.getArity()==0) {
            embeddedHtmls = Collections.<String>emptyList();
        } else {
            embeddedHtmls = new LinkedList<String>();
            Term l = t.getSubtermAt(0).getSubtermAt(0);
            while(!l.isNil()) {
                String sc = l.getSubtermAt(0).getString();
                embeddedHtmls.add(sc);
                l = l.getSubtermAt(1);
            }
        }
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        if (!embeddedHtmls.isEmpty()) {
          try {
            for(String s: embeddedHtmls) {
                env.getIO().getOutWriter().append(s);
            }
          }catch(IOException ex){
              throw new PhpEvalException(-1,
                         "exception during html output:"+ex.getMessage(),
                         ex,this, env);
          }
        }
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException
    {
       Term[] body;
       if (embeddedHtmls.isEmpty()) {
           body = new Term[0];
       } else {
           body = new Term[1];
           body[0] = PhpTermUtils.createStringList(embeddedHtmls);
       }
       return PhpTermUtils.createContextTerm("EndOfStatement", body, this);
    }

    private List<String> embeddedHtmls;
}
