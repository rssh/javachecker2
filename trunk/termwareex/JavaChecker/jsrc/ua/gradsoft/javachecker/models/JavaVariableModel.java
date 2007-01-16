/*
 * JavaVariableModel.java
 *
 * Created on вівторок, 9, січня 2007, 0:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.models;

import ua.gradsoft.termware.TermWareException;

/**
 *Model of java variable, which we can see in code.
 * @author Ruslan Shevchenko
 */
public interface JavaVariableModel {
    
    public String getName();
    
    public JavaVariableKind getKind();
    
    public JavaTypeModel getTypeModel() throws TermWareException;
    
    
}
