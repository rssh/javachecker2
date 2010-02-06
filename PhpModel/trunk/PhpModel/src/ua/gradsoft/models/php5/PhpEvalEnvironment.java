package ua.gradsoft.models.php5;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class PhpEvalEnvironment extends PhpCompileEnvironment
{
 
    public EvalState getEvalState()
    { return evalState; }
    
    public void setEvalState(EvalState evalState)
    { this.evalState=evalState; }

    public int       getBreakOrContinueDepth()
    { return breakOrContinueDepth; }
    
    public void      setBreakOrContinueDepth(int breakOrContinueDeptch)
    { this.breakOrContinueDepth=breakOrContinueDeptch; }

    public int     decBreakOrContinueDepth()
    {
      return --breakOrContinueDepth;
    }

    public PhpFlatVarContext  getLocals()
    {
      if (locals==null) {
          locals=new LinkedList<PhpFlatVarContext>();
          locals.push(new PhpFlatVarContext());
      }
      return locals.peek();
    }

    public Map<String,PhpValueModel> getCaseSensitiveConstants()
    {
      if (caseSensitiveConstants==null) {
          caseSensitiveConstants = new HashMap<String,PhpValueModel>();
      }
      return caseSensitiveConstants;
    }

    public Map<String,PhpValueModel> getCaseInsensitiveConstants()
    {
      if (caseInsensitiveConstants==null) {
          caseInsensitiveConstants = new HashMap<String,PhpValueModel>();
      }
      return caseInsensitiveConstants;
    }

    public Map<String,PhpFunctionModel>  getFunctions()
    {
        if (functions==null) {
            functions = new TreeMap<String,PhpFunctionModel>();
        }
        return functions;
    }

    public PhpValueModel popLastReturn()
    {
      if (rets==null) {
          rets = new LinkedList<PhpValueModel>();
      }  
      if (rets.size()==0) {
          return null;
      } else {
          return rets.pop();
      }
    }

    public  void pushLastReturn(PhpValueModel x)
    {
      if (rets==null) {
          rets = new LinkedList<PhpValueModel>();
      }
      rets.push(x);
    }

    public String getGotoIdentifier()
    {
       return gotoIdentifier;
    }

    public void setGotoIdentifier(String identifier)
    {
       this.gotoIdentifier = identifier ;
    }

    public boolean isSupressWarnings()
    {
        return supressWarnings;
    }

    public void  setSupressWarnings(boolean value)
    {
        supressWarnings=value;
    }
    
    public void warning(String message)
    {
       if (!supressWarnings) {
          System.out.println("warning:"+message);
       }
    }
    
    public void error(String message)
    {
       //this.getIO().getErrWriter()
       System.out.println("error:"+message);
       evalState = EvalState.THROW;
    }
    
    public Set<String>  getIncludedInRequest()
    {
       if (includedInRequest==null) {
           includedInRequest = new TreeSet<String>();
       }
       return includedInRequest;     
    }

    public PhpIOEnv getIO()
    {
      if (io==null) {
          io = new PhpIOEnv();
      }
      return io;
    }

    public PhpClassDeclarationModel getCurrentClassDeclaration() {
        return currentClassDeclaration;
    }

    public void setCurrentClassDeclaration(PhpClassDeclarationModel currentClassDeclaration) {
        this.currentClassDeclaration = currentClassDeclaration;
    }

    public List<String> getCurrentNamespace()
    { return currentNamespace; }

    public void setCurrentNamespace(List<String> currentNamespace)
    { this.currentNamespace = currentNamespace; }

    public PhpValueModel evalVariable(String name)
    {
        PhpValueModel m = this.getLocals().get(name);
        if (m==null) {
            if (!supressWarnings) {
              warning("use of undefined variable:" + name);
            }
            m = new PhpStringModel("");
            this.getLocals().bind(name, m);
        }
        return m;
    }


    private EvalState evalState;
    private int       breakOrContinueDepth;

    private PhpClassDeclarationModel  currentClassDeclaration;

    private LinkedList<PhpFlatVarContext> locals;
    //private LinkedList<PhpFlatVarContext> params;
    private Map<String,PhpValueModel> caseSensitiveConstants;
    private Map<String,PhpValueModel> caseInsensitiveConstants;

    private Map<String,PhpFunctionModel> functions;

    private LinkedList<PhpValueModel> rets;

    private String gotoIdentifier;

    private Set<String>  includedInRequest;
    private boolean      supressWarnings;
    private List<String> currentNamespace=Collections.emptyList();


    private PhpIOEnv     io;



}
