
package ua.gradsoft.models.php5;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author rssh
 */
public class PhpCompileEnvironment
{

  public PhpClassDeclarationModel  findClassDeclarationModel(String name)
  {
    return classModels_.get(name);  
  }

  public void addClassDeclarationModel(PhpClassDeclarationModel model)
  {
    classModels_.put(model.getName(), model);
  }

  public PhpInterfaceDeclarationModel  findInterfaceDeclarationModel(String name)
  {
    return interfaceModels_.get(name);
  }

  public void addInterfaceDeclarationModel(PhpInterfaceDeclarationModel model)
  {
    interfaceModels_.put(model.getName(), model);
  }

  public List<String> getCurrentNamespace()
    { return currentNamespace; }

  public void setCurrentNamespace(List<String> currentNamespace)
    { this.currentNamespace = currentNamespace; }
  

  protected Map<String,PhpClassDeclarationModel> classModels_=new HashMap<String,PhpClassDeclarationModel>();
  protected Map<String,PhpInterfaceDeclarationModel> interfaceModels_=new HashMap<String,PhpInterfaceDeclarationModel>();
  protected List<String> currentNamespace=Collections.emptyList();



}
