
package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermFactory;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for attributes of member variable.
 */
public class PhpMemberVariableAttributesModel implements PhpElementModel
{

    public PhpMemberVariableAttributesModel(Term t) throws TermWareException
    {
       Term l = t.getSubtermAt(0);
       staticFlag=false;
       visibility=PhpVisibility.PUBLIC;
       while(!l.isNil()) {
           Term ct = l.getSubtermAt(0);
           l=l.getSubtermAt(1);
           if (ct.getName().equals("static")) {
               staticFlag=true;
           }else if (ct.getName().equals("public")) {
               visibility=PhpVisibility.PUBLIC;
           }else if (ct.getName().equals("protected")) {
               visibility=PhpVisibility.PROTECTED;
           }else if (ct.getName().equals("private")) {
               visibility=PhpVisibility.PRIVATE;
           }else if (ct.getName().equals("var")) {
               // do nothing.
           }else{
               throw new AssertException("Unknown attribute:"+t.getName());
           }
       }
        
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body=new Term[1];
        TermFactory tf = PhpTermUtils.getTermFactory();
        Term l = PhpTermUtils.createNil();
        l = tf.createTerm("cons",tf.createAtom("var"),l);
        l = tf.createTerm("cons",tf.createAtom(visibility.name()),l);
        if (staticFlag) {
            l = tf.createTerm("cons",tf.createAtom("static"),l);
        }
        body[0]=l;
        return PhpTermUtils.createContextTerm("SMemberVariablesAttributes",body, this);
    }



    public PhpVisibility getVisibility()
    { return visibility; }

    public boolean isStatic()
    { return staticFlag; }

    private PhpVisibility visibility;
    private boolean    staticFlag;
}
