package ua.gradsoft.parsers.java5;


import ua.gradsoft.parsers.java5.jjt.JJTJavaParserTreeConstants;
import ua.gradsoft.parsers.java5.jjt.Node;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.jj.INode;
import ua.gradsoft.termware.jj.NodeTerm;


public class JavaNode extends NodeTerm implements Node {

  public JavaNode(int id)
  {
    super(id,JJTJavaParserTreeConstants.jjtNodeName[id]);    
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
    { return (JavaNode)jjtGetParentInternal(); }

  public void jjtAddChild(Node n, int i) 
    {        
    //  String s=TermHelper.termToString((JavaNode)n);      
    //  System.err.println("jjtAddChild, n="+s+",i="+i);
      jjtAddChildInternal((JavaNode)n,i); 
     }

  public Node jjtGetChild(int i) {
    return (JavaNode)jjtGetChildInternal(i); 
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

