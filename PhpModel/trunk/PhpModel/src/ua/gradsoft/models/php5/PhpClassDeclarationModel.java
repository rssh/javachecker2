
package ua.gradsoft.models.php5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for class
 * @author rssh
 */
public class PhpClassDeclarationModel extends PhpStatementModel
                  implements PhpClassOrInterfaceDeclarationModel
{

    public PhpClassDeclarationModel(Term t, PhpCompileEnvironment e) throws TermWareException
    {
      if(t.getName().equals("ClassDeclaration")) {
          Term attrTerm = t.getSubtermAt(0);
          if (t.getName().equals("abstract")) {
              abstractFlag=true;
              finalFlag=false;
          }else if (t.getName().equals("final")) {
              finalFlag=true;
              abstractFlag=false;
          }else if (t.isNil()) {
              finalFlag=false;
              abstractFlag=false;
          }else{
              throw new AssertException("Invalid class attribute:"+TermHelper.termToString(attrTerm));
          }
          Term idTerm = t.getSubtermAt(1);
          name=idTerm.getSubtermAt(0).getString();
          addSuper(t.getSubtermAt(2),e);
          PhpClassOrInterfaceDeclarationModelHelper.addImplements(this, t.getSubtermAt(3), e);
      }else{
        throw new AssertException("ClassDeclaration exprected");
      }
      Term sclassMembers = t.getSubtermAt(4);
      Term ct = sclassMembers.getSubtermAt(0);
      while(!ct.isNil()) {
          Term mb = ct.getSubtermAt(0);
          ct=ct.getSubtermAt(1);
          addMember(mb,e);
      }
      this.currentNamespace = e.getCurrentNamespace();
    }

    public boolean isClass() {
        return true;
    }

    public boolean isInterface() {
        return false;
    }



    public String getName()
    { return name; }

    @Override
    public void eval(PhpEvalEnvironment env) {
        env.addClassDeclarationModel(this);
        evalGlobals(env);
        evalSuperPtr(env);
        evalImplementsPtr(env);
    }

    public boolean isAbstract() {
        return abstractFlag;
    }

    public Map<String, PhpConstantDeclarationModel> getConstants() {
        return constants;
    }

    public boolean isFinal() {
        return finalFlag;
    }

    public List<PhpVariableModel> getGlobalDecls() {
        return globalDecls;
    }

    public Set<String> getGlobals() {
        return globals;
    }

    public List<String> getInterfaceNames() {
        if (implementsInterfaceNames==null) {
            implementsInterfaceNames=new ArrayList<String>();
        }
        return implementsInterfaceNames;
    }

    public List<PhpInterfaceDeclarationModel> getInterfaceDeclarations() {
        if (interfaceDeclarations==null) {
            interfaceDeclarations=new LinkedList<PhpInterfaceDeclarationModel>();
        }
        return interfaceDeclarations;
    }

    public Map<String, PhpVariableDeclarationModel> getMemberVariables() {
        return memberVariables;
    }

    public Map<String, PhpMethodModel> getMethods() {
        return methods;
    }

    public PhpClassDeclarationModel getSuperClassDeclaration() {
        return superClassDeclaration;
    }

    public String getSuperClassName() {
        return superClassName;
    }





    private void addMember(Term md, PhpCompileEnvironment pce) throws TermWareException
    {
      if (md.getName().equals("MemberFunctionDeclaration")) {
          addMemberFunctionDeclaration(md,pce);
      } else if (md.getName().equals("MemberConstantDeclaration")) {
          addMemberConstantDeclaration(md,pce);
      } else if (md.getName().equals("MemberVariablesDeclaration")) {
          addMemberVariablesDeclaration(md,pce);
      } else if (md.getName().equals("MemberGlobalsDeclaration")) {
          addMemberGlobalsDeclaration(md,pce);
      } else if (md.getName().equals("MemberAbstractFunctionDeclaration")) {
          addMemberFunctionDeclaration(md,pce);
      } else {
          throw new AssertException("Invalid class member:"+TermHelper.termToString(md));
      }
    }

    private void addMemberFunctionDeclaration(Term md, PhpCompileEnvironment pce) throws TermWareException
    {
      PhpMethodModel m = new PhpMethodModel(md,this,pce);
      if (methods==null) {
          methods=new HashMap<String,PhpMethodModel>();
      }
      methods.put(m.getName(), m);
    }

    private void addMemberConstantDeclaration(Term mcd, PhpCompileEnvironment pce) throws TermWareException
    {
      PhpConstantDeclarationModel cdm = new PhpConstantDeclarationModel(mcd,pce);
      if (constants==null) {
          constants=new HashMap<String,PhpConstantDeclarationModel>();
      }
      constants.put(cdm.getName(), cdm);
    }

    private void addMemberVariablesDeclaration(Term mcd, PhpCompileEnvironment pce) throws TermWareException
    {
       Term attributesTerm = mcd.getSubtermAt(0);
       PhpMemberVariableAttributesModel attributes = new PhpMemberVariableAttributesModel(attributesTerm);
       Term l = mcd.getSubtermAt(1);
       while(!l.isNil()) {
           Term ct = l.getSubtermAt(0);
           l=l.getSubtermAt(1);
           addMemberVariableDeclaration(attributes,ct,pce);
       }
    }

    private void addMemberVariableDeclaration(PhpMemberVariableAttributesModel attributes,
                                              Term mcd,
                                              PhpCompileEnvironment pce) throws TermWareException
    {
      PhpVariableDeclarationModel cdm = new PhpVariableDeclarationModel(mcd,pce);
      if (memberVariables==null) {
          memberVariables=new HashMap<String,PhpVariableDeclarationModel>();
      }
      memberVariables.put(cdm.getName(), cdm);
    }

    private void addMemberGlobalsDeclaration(Term d, PhpCompileEnvironment pce)
    {
        Term l = d.getSubtermAt(0);
        globalDecls = new LinkedList<PhpVariableModel>();
        while(!l.isNil()) {
            Term ct = l.getSubtermAt(0);
            l = l.getSubtermAt(1);
            globalDecls.add(new PhpVariableModel(ct,pce));
        }
    }

    private void addSuper(Term t, PhpCompileEnvironment pce)
    {
       if (t.isNil()) {
           superClassName = null;
       } else {
           superClassName = t.getSubtermAt(0).getString();
           superClassDeclaration = pce.findClassDeclarationModel(superClassName);
       }
    }

   

    public Term getTerm(PhpEvalEnvironment pee) throws TermWareException {
        Term body[] = new Term[5];
        if (finalFlag) {
            body[0]=PhpTermUtils.createAtom("final");
        }else if (abstractFlag) {
            body[0]=PhpTermUtils.createAtom("abstract");
        }else{
            body[0]=PhpTermUtils.createNil();
        }
        body[1]=PhpTermUtils.createIdentifier(name);
        if (superClassName==null) {
            body[2]=PhpTermUtils.createNil();
        }else{
            body[2]=PhpTermUtils.createIdentifier(superClassName);
        }
        List<Term> implementsList = new LinkedList<Term>();
        for(String s: implementsInterfaceNames) {
            implementsList.add(PhpTermUtils.createIdentifier(name));
        }
        body[3]=PhpTermUtils.createList(implementsList);
        List<Term> clList = new LinkedList<Term>();
        if (methods!=null) {
            for(PhpMethodModel m: methods.values()) {
                clList.add(m.getTerm(pee));
            }
        }
        if (globalDecls!=null) {
            for(PhpVariableModel v:globalDecls) {
                clList.add(v.getTerm(pee));
            }
        }
        if (constants!=null) {
            for(PhpConstantDeclarationModel x: constants.values()) {
                clList.add(x.getTerm(pee));
            }
        }
        if (memberVariables!=null) {
            for(PhpVariableDeclarationModel x: memberVariables.values()) {
                clList.add(x.getTerm(pee));
            }
        }
        Term clTerm = PhpTermUtils.getTermFactory().createTerm("SClassMembers", PhpTermUtils.createList(clList));
        body[4]=clTerm;
        return PhpTermUtils.createContextTerm("ClassDeclaration", body, this);
    }


    private void evalGlobals(PhpEvalEnvironment pee)
    {
      globals = new TreeSet<String>();
      for(PhpVariableModel vm: globalDecls)
      {
        globals.add(vm.getLastVarname(pee));
      }
    }

    private void evalSuperPtr(PhpEvalEnvironment pee)
    {
      if (superClassName!=null) {
          superClassDeclaration = pee.findClassDeclarationModel(superClassName);
      }
    }

    private void evalImplementsPtr(PhpEvalEnvironment pee)
    {
        interfaceDeclarations = new ArrayList<PhpInterfaceDeclarationModel>(implementsInterfaceNames.size());
        for(String s : implementsInterfaceNames) {
            interfaceDeclarations.add(pee.findInterfaceDeclarationModel(s));
        }
    }


    public PhpMethodModel findMethod(String name)
    {
        PhpMethodModel retval = methods.get(name);
        if (retval==null) {
            if(superClassDeclaration!=null) {
                retval = superClassDeclaration.findMethod(name);
            }
        }
        return retval;
    }

    public String getFullName()
    {
      return PhpClassOrInterfaceDeclarationModelHelper.nameWithNamespace(name,currentNamespace,false);
    }

    public boolean isChildOf(PhpClassOrInterfaceDeclarationModel m)
    {
      if (superClassDeclaration!=null) {
        if (m.getFullName().equals(superClassDeclaration.getFullName())) {
           return true;
        } else if (superClassDeclaration.isChildOf(m)) {
           return true;
        }
      }
      if (m.isInterface()) {
          for(PhpInterfaceDeclarationModel s: interfaceDeclarations) {
              if (m.getFullName().equals(s.getFullName())) {
                  return true;
              }
              if (s.isChildOf(m)) {
                  return true;
              }
          }
      }
      return false;
    }


    private boolean finalFlag;
    private boolean abstractFlag;
    private String name;
    //private Term term;
    private String superClassName;
    private PhpClassDeclarationModel superClassDeclaration;
    private List<String> implementsInterfaceNames;
    private List<PhpInterfaceDeclarationModel> interfaceDeclarations;
    private Map<String,PhpConstantDeclarationModel> constants;
    private Map<String,PhpMethodModel>  methods;
    private Map<String,PhpVariableDeclarationModel> memberVariables;
    private List<PhpVariableModel> globalDecls;
    private Set<String>  globals;
    private List<String> currentNamespace;
}
