package ua.gradsoft.models.php5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;

/**
 *Model for include statement
 */
public class PhpIncludeStatementModel extends PhpStatementModel
{

    public static PhpIncludeStatementModel create(Term t, PhpCompileEnvironment pce)
            throws TermWareException
    {
      return new PhpIncludeStatementModel(t,pce);  
    }

    public PhpIncludeStatementModel(Term t, PhpCompileEnvironment pce)
                                                 throws TermWareException
    {
      supressWarnings=t.getSubtermAt(0).getBoolean();
      String sIncludeType = t.getSubtermAt(1).getString();
      if (sIncludeType.equals("include")) {
          includeType=PhpIncludeType.INCLUDE;
      } else if (sIncludeType.equals("include_once")) {
          includeType=PhpIncludeType.INCLUDE_ONCE;
      } else if (sIncludeType.equals("require")) {
          includeType=PhpIncludeType.REQUIRE;
      } else if (sIncludeType.equals("require_once")) {
          includeType=PhpIncludeType.REQUIRE_ONCE;
      } else {
          throw new AssertException("invalid include type: "+sIncludeType);
      }
      expression = PhpExpressionModelHelper.create(t.getSubtermAt(2), pce);  
    }

    @Override
    public void compile(PhpCompileEnvironment env) {
        /* do noting */
    }

    @Override
    public void eval(PhpEvalEnvironment env) {
        boolean prevSupressWarnings = env.isSupressWarnings();
        if (supressWarnings) {
           env.setSupressWarnings(supressWarnings);
        }
        try {
          PhpValueModel v = expression.eval(env);
          //EvalState.GOTO
          boolean isError=false;
          String  errorMessage=null;
          switch (env.getEvalState()) {
            case BREAK:
            case CONTINUE:
                  isError=true;
                  errorMessage="break or continue not in loop";
                  break;
            case GOTO:
                  isError=true;
                  errorMessage="goto out of scope";
                  break;
            case RETURN:
                  isError=true;
                  errorMessage="return out of scope";
                  break;
            case THROW:
                  return;
            case OK:
                  break;
            default:
                throw new PhpEvalException("Invalid eval state:"+env.getEvalState());
          }
          if (isError) {
            if (includeType.isRequire()) {
                env.error(errorMessage);
            }else{
                env.warning(errorMessage);
            }
            return;
          }
          String fname = v.getString(env);
          File f = env.getIO().findIncludeFile(fname);
          if (f==null) {
            errorMessage="include file "+fname+" is not found";
            if (includeType.isRequire()) {
                env.error(errorMessage);
            } else {
                env.warning(errorMessage);
            }
            return;
          }
          if (includeType.isOnce()) {
              if (env.getIncludedInRequest().contains(fname)) {
                  // already was included, do nothing
                  return;
              }
          }
          env.getIncludedInRequest().add(fname);
          Reader r = new BufferedReader(new FileReader(fname));
          Term t = TermWare.getInstance().getParserFactory("PHP").createParser(r, fname, null, null).readTerm();
          PhpPageModel pageModel = new PhpPageModel(t,env);
          pageModel.eval(env);

        }finally{
          if (supressWarnings) {
              env.setSupressWarnings(prevSupressWarnings);
          }
        }
    }


    
    private boolean supressWarnings;
    private PhpIncludeType includeType;
    private PhpExpressionModel expression;

}
