

package ua.gradsoft.models.php5;

import java.util.List;
import ua.gradsoft.termware.Term;

/**
 *Helper for class or declaration
 * @author rssh
 */
public class PhpClassOrInterfaceDeclarationModelHelper {


   static void addImplements(PhpClassOrInterfaceDeclarationModel decl,
                              Term t,
                              PhpCompileEnvironment pce)
    {
      Term l=t;
      List<String> implementsInterfaceNames = decl.getInterfaceNames();
      List<PhpInterfaceDeclarationModel> interfaceDeclarations =
                                                    decl.getInterfaceDeclarations();
      while(!l.isNil()) {
          String iname = l.getSubtermAt(0).getSubtermAt(0).getString();
          implementsInterfaceNames.add(iname);
          PhpInterfaceDeclarationModel id = pce.findInterfaceDeclarationModel(iname);
          interfaceDeclarations.add(id);
          l=l.getSubtermAt(1);
      }
    }

   static String nameWithNamespace(String name, List<String> namespaces, boolean absolute)
   {
     if (namespaces==null || namespaces.isEmpty()) {
         if (absolute) {
             return "\\"+name;
         } else {
             return name;
         }
     }
     StringBuilder sb = new StringBuilder();
     boolean isFirst=true;
     for(String ns: namespaces) {
         if (!isFirst || absolute) {
             sb.append("\\");
         }
         isFirst=false;
         sb.append(ns);
     }
     sb.append("\\");
     sb.append(name);
     return sb.toString();
   }


}
