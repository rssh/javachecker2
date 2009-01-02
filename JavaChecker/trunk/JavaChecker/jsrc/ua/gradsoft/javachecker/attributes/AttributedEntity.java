/*
 * Attributed.java
 *
 */

package ua.gradsoft.javachecker.attributes;

import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;

/**
 *General interface for attributes
 * @author rssh
 */
public interface AttributedEntity {       
   
   public Term getAttribute(String name)  throws TermWareException;
   
   public void setAttribute(String name, Term value)  throws TermWareException;
     
   public AttributedEntity  getChildAttributes(String childName) throws TermWareException;
    
}
