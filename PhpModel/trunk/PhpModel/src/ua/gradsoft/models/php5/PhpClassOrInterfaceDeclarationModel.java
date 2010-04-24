
package ua.gradsoft.models.php5;

import java.util.List;

/**
 *Interface for PHP class or interface
 * @author rssh
 */
public interface PhpClassOrInterfaceDeclarationModel extends PhpElementModel
{

    boolean isClass();

    boolean isInterface();

    String  getName();

    String  getFullName();

    boolean isChildOf(PhpClassOrInterfaceDeclarationModel m);

    List<String>  getInterfaceNames();

    List<PhpInterfaceDeclarationModel> getInterfaceDeclarations();
}
