/*
 * JavaAccessLevelModel.java
 *
 * Created on понеділок, 23, лютого 2004, 11:03
 */

package ua.kiev.gradsoft.JavaChecker;

import ua.kiev.gradsoft.TermWare.*;
import ua.kiev.gradsoft.TermWare.exceptions.*;

/**
 *Model for access level.
 * @author  Ruslan Shevchenko
 */
public class JavaAccessLevelModel {
    
    
    public final int PACKAGE_LEVEL=0;
    public final int PRIVATE=1;
    public final int PROTECTED=2;
    public final int PUBLIC=3;
    
    /** Creates a new instance of JavaAccessLevelModel */
    public JavaAccessLevelModel(ITerm propertiesList) throws TermWareException
    {
        if (propertiesList.isAtom() && propertiesList.getName().equals("empty_list")) {
            value_=PACKAGE_LEVEL;
        }else{
            boolean found=false;
            while(!propertiesList.isNil()) {
                if (propertiesList.getName().equals("cons")) {
                    ITerm t=propertiesList.getSubtermAt(0);
                    if (t.getName().equals("java_public")) {
                        value_=PUBLIC;
                        found=true;
                        break;
                    }else if(t.getName().equals("java_protected")) {
                        value_=PROTECTED;
                        found=true;
                        break;
                    }else if(t.getName().equals("java_private")){
                        value_=PRIVATE;
                        found=true;
                        break;
                    }else{
                        propertiesList=propertiesList.getSubtermAt(1);
                    }
                }else{
                    throw new AssertException("Invalid properties list");
                }
            }
            if (!found) {
                value_=PACKAGE_LEVEL;
            }
        }
    }
    
    public boolean isPrivate()
    { return value_==PRIVATE; }
    
    public boolean isProtected()
    { return value_==PROTECTED; }
    
    public boolean isPublic()
    { return value_==PUBLIC; }
    
    public boolean isPackageLevel()
    { return value_==PACKAGE_LEVEL; }
    
    private int value_;
    
}
