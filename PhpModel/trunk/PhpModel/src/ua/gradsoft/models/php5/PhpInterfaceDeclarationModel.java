package ua.gradsoft.models.php5;

import ua.gradsoft.termware.Term;

/**
 *Model for Interface Declaration
 * @author rssh
 */
public class PhpInterfaceDeclarationModel extends PhpStatementModel
{

    public static PhpInterfaceDeclarationModel create(Term t, PhpCompileEnvironment env)
    {
      return new PhpInterfaceDeclarationModel(t);
    }

    protected PhpInterfaceDeclarationModel(Term t)
    {
     name=t.getSubtermAt(0).getSubtermAt(0).getString();
     term=t;
    }


    @Override
    public void compile(PhpCompileEnvironment env) {
        env.addInterfaceDeclarationModel(this);
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        /* do nothing */
    }

    public String getName()
    { return name; }
    

    private String name;
    private Term   term;

}
