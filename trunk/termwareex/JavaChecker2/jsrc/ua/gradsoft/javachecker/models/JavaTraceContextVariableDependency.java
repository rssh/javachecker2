/*
 * JavaTraceContextVariableDependency.java
 *
 */

package ua.gradsoft.javachecker.models;

import java.util.TreeMap;

/**
 *Dependency matrix between variables.
 * @author rssh
 */
class JavaTraceContextVariableDependency {
    
    JavaTraceContextVariableDependency()
    {
        matrix_=new TreeMap<JavaVariableModel, TreeMap<JavaVariableModel, Boolean>>(JavaVariableModelComparator.INSTANCE);
    }
    
    /**
     * add dependency from x to y.
     */
    public void addDependency(JavaVariableModel x, JavaVariableModel y)
    {
        TreeMap<JavaVariableModel, Boolean> xRow = matrix_.get(x);
        if (xRow==null) {
            xRow = new TreeMap<JavaVariableModel,Boolean>();
            matrix_.put(x,xRow);
        }
        Boolean b = xRow.get(y);
        boolean needCorrection=false;
        if (b==null) {
            b=true;
            xRow.put(y,true);
            needCorrection=true;
        }else if (b==false){
            xRow.put(y,true);
            needCorrection=true;
        }
        TreeMap<JavaVariableModel, Boolean> yRow = matrix_.get(y);
        if (yRow==null) {
            yRow = new TreeMap<JavaVariableModel,Boolean>();
            matrix_.put(y,yRow);          
        }
        needCorrection_=needCorrection;
    }
    
    public void checkCorrection()
    {
      if (needCorrection_) {
          // do transitive closure by FloydWarshall
          for(JavaVariableModel k: matrix_.keySet()) {
              TreeMap<JavaVariableModel,Boolean> kRow = matrix_.get(k);               
              for(JavaVariableModel i: matrix_.keySet()) {
                  TreeMap<JavaVariableModel,Boolean> iRow = matrix_.get(i);               
                  for(JavaVariableModel j: matrix_.keySet()) {
                      // path[i][j] = OR(path[i][j],AND(path[i][k],path[k,j]))
                      Boolean ij = iRow.get(j);
                      if (ij==null) {
                          Boolean ik = iRow.get(k);
                          if (ik==null) {
                              // do nothing -- result in this step is false.
                          }else{
                              if (ik.booleanValue()==true) {
                                  Boolean kj=kRow.get(j);
                                  if (kj!=null) {
                                      if (kj==true) {
                                          iRow.put(j,true);
                                      }
                                  }
                              }
                          }                          
                      }else{
                          if (ij.booleanValue()!=true) {
                              Boolean ik = iRow.get(k);
                              if (ik!=null) {
                                  if (ik.booleanValue()==true) {
                                      Boolean kj = kRow.get(j);
                                      if (kj!=null) {
                                          if (kj.booleanValue()==true) {
                                              iRow.put(j,true);
                                          }
                                      }
                                  }
                              }
                          }
                      }
                  }
              }
          }          
      }  
    }
    
    private boolean needCorrection_=false;
    private TreeMap<JavaVariableModel, TreeMap<JavaVariableModel, Boolean>> matrix_;
    
}
