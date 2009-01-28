package ua.gradsoft.parsers.dot;


import ua.gradsoft.parsers.dot.jjt.JJTDotParserTreeConstants;
import ua.gradsoft.parsers.dot.jjt.Node;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.jj.INode;
import ua.gradsoft.termware.jj.NodeTerm;


public class DotNode extends NodeTerm implements Node {

  public DotNode(int id)
  {
    super(id,JJTDotParserTreeConstants.jjtNodeName[id]);    
  }


  public void jjtOpen() {
  }

  public void jjtClose() {
  }
  
  public void jjtSetParent(Node n) 
    { 
      jjtSetParentInternal((INode)n);
    }

  public Node jjtGetParent()
    { return (DotNode)jjtGetParentInternal(); }

  public void jjtAddChild(Node n, int i) 
    {        
      jjtAddChildInternal((DotNode)n,i); 
     }

  public Node jjtGetChild(int i) {
    return (DotNode)jjtGetChildInternal(i); 
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

