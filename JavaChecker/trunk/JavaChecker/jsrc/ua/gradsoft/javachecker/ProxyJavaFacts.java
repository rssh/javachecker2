/*
 * ProxyJavaFacts.java
 */

package ua.gradsoft.javachecker;

import ua.gradsoft.javachecker.models.JavaExpressionHelper;
import ua.gradsoft.javachecker.models.JavaNullTypeModel;
import ua.gradsoft.javachecker.models.JavaResolver;
import ua.gradsoft.javachecker.models.JavaTypeModel;
import ua.gradsoft.javachecker.models.JavaUnitModel;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermWareException;


/**
 *Proxy facts wich provide facts access to JavaFacts singleton.
 * @author  Ruslan Shevchenko
 */
public class ProxyJavaFacts extends DefaultFacts
{
    
    /** Creates a new instance of ProxyJavaFacts */
    public ProxyJavaFacts() throws TermWareException
    {   
    }
    
     public boolean violationDiscovered(String name,String message,Term partOfCode) throws TermWareException
    {
        return Main.getFacts().violationDiscovered(name, message, partOfCode);
    }
    
    public boolean isCheckEnabled(String name)
    {
        return Main.getFacts().isCheckEnabled(name);
    }
    
    public JavaTypeModel  getPrimitiveType(String name) throws TermWareException, EntityNotFoundException
    {
        return JavaResolver.resolveTypeModelByName(name,(JavaUnitModel)null,null,null);
    }
    
    public JavaTypeModel  getClassType(String fullName) throws TermWareException, EntityNotFoundException
    {
        return JavaResolver.resolveTypeModelByFullClassName(fullName);   
    }
    
    public JavaTypeModel  getFloatingPointLiteralType(String literal)
    {
        return JavaExpressionHelper.getFloatingPointLiteralType(literal);
    }
    
    public JavaTypeModel  getIntegerLiteralType(String literal)
    {
        return JavaExpressionHelper.getIntegerLiteralType(literal);
    }
    
    public JavaTypeModel  getNullType()
    {
        return JavaNullTypeModel.INSTANCE;
    }
    
    //public Object  javaStaticCall(Object o) throws TermWareException, EntityNotFoundException
    //{
    //    return JavaResolver.resolveTypeModelByFullClassName(fullName);   
    //}
    
    
}
