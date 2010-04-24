package ua.gradsoft.models.php5;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *Model for Interface Declaration
 * @author rssh
 */
public class PhpInterfaceDeclarationModel extends PhpStatementModel
                                       implements PhpClassOrInterfaceDeclarationModel
{

    public static PhpInterfaceDeclarationModel create(Term t, PhpCompileEnvironment env)
                                                                     throws TermWareException
    {
      return new PhpInterfaceDeclarationModel(t, env);
    }

    protected PhpInterfaceDeclarationModel(Term t,PhpCompileEnvironment env)
                                                       throws InvalidPhpTermExpression
    {
     name=t.getSubtermAt(0).getSubtermAt(0).getString();
     currentNamespace = env.currentNamespace;
     PhpClassOrInterfaceDeclarationModelHelper.addImplements(this, t.getSubtermAt(1),env);
     addBody(t.getSubtermAt(2),env);
    }


    @Override
    public void eval(PhpEvalEnvironment env) {
        /* do nothing */
    }

    public String getName()
    { return name; }

    public String getFullName()
    {
      return PhpClassOrInterfaceDeclarationModelHelper.nameWithNamespace(name,currentNamespace,false);
    }

    private void addBody(Term body, PhpCompileEnvironment pce) throws TermWareException
    {
      Term l = body.getSubtermAt(0);
      while(!l.isNil()) {
          Term t = l.getSubtermAt(0);
          l=l.getSubtermAt(1);
          if (t.getName().equals("InterfaceMethodDeclaration")) {
              addInterfaceMethodDeclaration(t,pce);
          } else if (t.getName().equals("MemberConstantDeclaration")) {
              addMemberConstantDeclaration(t,pce);
          } else {
              throw new InvalidPhpTermExpression("InterfaceMethodDeclaration or MemberConstantDeclaration required",t);
          }
      }
    }

    private void addInterfaceMethodDeclaration(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
       PhpMethodModel m = new PhpMethodModel(t,this,pce);
       methods.put(m.getName(), m);
    }

    private void addMemberConstantDeclaration(Term t, PhpCompileEnvironment pce) throws TermWareException
    {
        PhpConstantDeclarationModel m = new PhpConstantDeclarationModel(t,pce);
        constants.put(m.getName(), m);
    }

    public boolean isChildOf(PhpClassOrInterfaceDeclarationModel s)
    {
      if (s.isInterface()) {
          for(PhpInterfaceDeclarationModel c: superDeclarations) {
              if (c.getFullName().equals(s.getFullName())) {
                  return true;
              }else if (c.isChildOf(s)) {
                  return true;
              }
          }
      }
      return false;
    }


    public List<PhpInterfaceDeclarationModel> getInterfaceDeclarations() {
        return superDeclarations;
    }

    public List<String> getInterfaceNames() {
        return superNames;
    }

    public boolean isClass() {
        return false;
    }

    public boolean isInterface() {
        return true;
    }

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term[] body = new Term[3];
        body[0] = PhpTermUtils.createIdentifier(name);
        body[1] = PhpTermUtils.createList(superDeclarations, pee);
        List<PhpElementModel> el = new ArrayList<PhpElementModel>();
        el.addAll(methods.values());
        el.addAll(constants.values());
        body[2] = PhpTermUtils.createList(el, pee);
        Term retval = PhpTermUtils.createContextTerm(name, body, this);
        return retval;
    }


    private String name;
    private List<String> currentNamespace;
    private List<String> superNames;
    private List<PhpInterfaceDeclarationModel> superDeclarations;
    private Map<String,PhpConstantDeclarationModel> constants;
    private Map<String,PhpMethodModel>  methods;



}
