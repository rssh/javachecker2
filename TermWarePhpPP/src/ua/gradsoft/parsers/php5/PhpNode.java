package ua.gradsoft.parsers.php5;

import ua.gradsoft.termware.*;
import ua.gradsoft.termware.jj.*;
import ua.gradsoft.parsers.php5.jjt.*;

public class PhpNode extends NodeTerm implements Node
{
  public PhpNode(int id)
  {
   super(id,PHPTreeConstants.jjtNodeName[id]);
  }


  public void jjtOpen() {}
  public void jjtClose() {}

  public void jjtSetParent(Node n)
    {
      jjtSetParentInternal((INode)n);
    }

  public Node jjtGetParent()
    { return (PhpNode)jjtGetParentInternal(); }


  public void jjtAddChild(Node n, int i) 
    {        
      jjtAddChildInternal((PhpNode)n,i); 
     }

  public Node jjtGetChild(int i) {
    return (PhpNode)jjtGetChildInternal(i); 
  }

  public void  addTerm(Term t, int i) {
      jjtInsertChildInternal(new NodeTerm(t), i);
  }

  /* You can override these two methods in subclasses of SimpleNode to
     customize the way the node appears when the tree is dumped.  If
     your output uses more than one line you should override
     toString(String), otherwise overriding toString() is probably all
     you need to do. */

  public String toString() 
    {       
      return super.toString(); 
    }
  
  public String toString(String prefix) { return prefix + toString(); }

}

