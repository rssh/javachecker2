/*
 * Function.java
 *
 * Created on May 3, 2007, 3:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.util;

import ua.gradsoft.termware.TermWareException;

/**
 *
 * @author rssh
 */
public interface Function<X,Y>
{
    
     Y function(X x) throws TermWareException;
        
}
