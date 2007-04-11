/*
 * JavaMethodModel.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ua.gradsoft.javachecker.JavaFacts;
import ua.gradsoft.termware.TermWareException;

/**
 * Model for method
 * @author  Ruslan Shevchenko
 */
public abstract class JavaMethodModel implements JavaTopLevelBlockOwnerModel 
{
    
    /**
     * Creates a new instance of JavaMethodModel
     */
    public JavaMethodModel(JavaTypeModel typeModel) 
    {
     typeModel_=typeModel;
    }
    
    public abstract String getName();    
    
    public abstract JavaModifiersModel getModifiers();
    
    public abstract List<JavaTypeVariableAbstractModel>  getTypeParameters() throws TermWareException;
 
    public abstract JavaTypeModel  getResultType() throws TermWareException;
    
    /**
     *return list of forma-parameters types.  Note, that type for varargs parameters are arrays.
     */
    public abstract List<JavaTypeModel> getFormalParametersTypes() throws TermWareException;
    
    /**
     *get list of formal parameters. 
     */
    public abstract List<JavaFormalParameterModel> getFormalParametersList() throws TermWareException;
    
    public abstract Map<String,JavaFormalParameterModel>  getFormalParametersMap() throws TermWareException;
                
    public void print(PrintWriter writer) 
    {
        List<JavaTypeVariableAbstractModel> tps;
        boolean wasError=false;
        try {
            tps=getTypeParameters();            
        }catch(TermWareException ex){
            tps=Collections.emptyList();
            writer.print("error:"+ex.getMessage());
            wasError=true;
        }
        if (!tps.isEmpty()) {
            writer.print("<");
            boolean frs=true;
            for(JavaTypeVariableAbstractModel tv:tps) {
                if (!frs) {
                    writer.print(",");
                }else{
                    frs=false;
                }
                tv.print(writer);
                writer.print(">");
            }
        }                    
        writer.print(getName());
        writer.print("(");
        boolean frs=true;
        List<JavaTypeModel> fpts;
        try {
            fpts=getFormalParametersTypes();
        }catch(TermWareException ex){
            fpts=Collections.emptyList();
            writer.print("error:"+ex.getMessage());
        }
        for(JavaTypeModel tm: fpts) {
            if (!frs) {
                writer.print(',');
            }else{
                frs=false;
            }
            writer.print(tm.getFullName());
        }
        writer.print(")");        
    }
    
    public void print(PrintStream out) 
    {
        PrintWriter writer = new PrintWriter(out);
        print(writer);
        writer.flush();
 //       writer.close();
    }
    
    public JavaFacts getJavaFacts()
    { return typeModel_.getJavaFacts(); }
    
    public JavaTypeModel getTypeModel()
    { return typeModel_; }
    
    private JavaTypeModel typeModel_;
}
