
package ua.gradsoft.printers.dot;

import java.io.PrintWriter;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermHelper;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.printers.AbstractPrinter;

/**
 *Printer for graphwiz dot language.
 * @author rssh
 */
public class DotPrinter extends AbstractPrinter
{

 /** Creates a new instance of JavaPrinter */
    public DotPrinter(PrintWriter out, String outTag) {
        super(out,outTag);
    }

    public void writeTerm(Term t) throws TermWareException {
        writeTerm(t,0,"--");
    }

    public void writeTerm(Term t, int level, String edgeString) throws TermWareException {
        if (t.isComplexTerm()) {
            if (t.getName().equals("dotGraph")) {
                writeDotGraph(t,level);
            }else if(t.getName().equals("dotEdges")){
                writeDotEdges(t,level,edgeString);
            }else if(t.getName().equals("dotNode")){
                writeDotNode(t,level,edgeString);
            }else if(t.getName().equals("dotNodeId")){
                writeNodeId(t,level,edgeString);
            }else if(t.getName().equals("nodeId")){
                writeNodeId(t,level,edgeString);
            }else if(t.getName().equals("port")){
                writePort(t,level,edgeString);
            }else if(t.getName().equals("dotAttrStmt")){
                writeDotAttrStmt(t,level,edgeString);
            }else if(t.getName().equals("dotAttributeAssignment")){
                writeDotAttributeAssignment(t,level,edgeString);
            }else if(t.getName().equals("dotSubgraph")){
                writeDotSubgraph(t,level,edgeString);
            }else if(t.getName().equals("id")) {
                writeId(t,level);
            }else{
                t.println(out_);
            }
        }else{
            t.print(out_);
        }
    }
    
    public void flush()
    {
      out_.flush();  
    }
    
    private void writeDotGraph(Term t, int level) throws TermWareException
    {
      if (t.getArity()!=4) {
          throw new AssertException("arity of dotGraph term must be 4:"+TermHelper.termToString(t));
      }      
      boolean isStrict = t.getSubtermAt(0).getBoolean();
      if (isStrict) {
          out_.print("strict");
          out_.print(" ");
      }      
      String kind=t.getSubtermAt(1).getName(); // graph or digraph
      String edgeString="-?-";
      if (kind.equals("digraph")) {
          edgeString="->";
      }else if(kind.equals("graph")){
          edgeString="--";
      }else{
          throw new AssertException("graph or digraph atom-s expected");
      }
      out_.print(kind); 
      
      out_.print(" ");
      writeTerm(t.getSubtermAt(2),level,edgeString); // id()
      out_.print(" ");
      out_.println("{");
      writeIdent(level+1);
      Term curr = t.getSubtermAt(3);
      boolean isFirst=true;
      while(!curr.isNil()) {
          if (!isFirst) {
              out_.println(";");
              writeIdent(level+1);
          }else{
              isFirst=false;
          }
          writeTerm(curr.getSubtermAt(0),level+1,edgeString);    
          curr=curr.getSubtermAt(1);
      }
      out_.println();
      writeIdent(level);
      out_.println("}");
    }
    
    private void writeDotEdges(Term t, int level, String edgeString) throws TermWareException
    {
      boolean firstEdge=true;
      Term nodesCurr = t.getSubtermAt(0);
      while(!nodesCurr.isNil()) {
          if (!firstEdge) {
              out_.print(edgeString);
          }else{
              firstEdge=false;
          }
          Term node = nodesCurr.getSubtermAt(0);
          writeTerm(node,level,edgeString);          
          nodesCurr = nodesCurr.getSubtermAt(1);
      }
      Term attrsList = t.getSubtermAt(1);
      if (!attrsList.isNil()) {
        out_.print(" ");
        writeAttrList(attrsList,level,edgeString);     
      }
    }
    
    private void writeAttrList(Term list, int level, String edgeString) throws TermWareException
    {
       Term curr = list;
       while(!curr.isNil()) {
           out_.print(" ");
           out_.print("[");
           out_.print(" ");
           writeAttrAssignmentsList(curr.getSubtermAt(0),level,edgeString);
           out_.print(" ");
           out_.print("]");
           curr=curr.getSubtermAt(1);
       }
    }

    private void writeAttrAssignmentsList(Term list, int level, String edgeString) throws TermWareException
    {
       Term curr = list;
       boolean isFirst=true;
       while(!curr.isNil()) {
           if (!isFirst) {
               out_.print(",");
           }else{
               isFirst=false;
           }
           writeTerm(curr.getSubtermAt(0),level,edgeString);
           curr=curr.getSubtermAt(1);
       }
    }
    
    private void writeDotAttributeAssignment(Term t, int level, String edgeString) throws TermWareException
    {
        writeTerm(t.getSubtermAt(0),level,edgeString);
        out_.print("=");
        writeTerm(t.getSubtermAt(1),level,edgeString);
    }
    
    private void writeDotAttrStmt(Term t, int level, String edgeString) throws TermWareException
    {
        //dotAttrStmt(atom,attributes)
        out_.print(t.getSubtermAt(0).getName());
        out_.print(" ");
        writeAttrList(t.getSubtermAt(1),level,edgeString);
    }
      
    
 
    private void writeDotNode(Term t, int level, String edgeString) throws TermWareException
    {
      Term node = t.getSubtermAt(0);  
      writeTerm(node,level,edgeString);          
      Term attrsList = t.getSubtermAt(level);
      if (!attrsList.isNil()) {
        out_.print(" ");
        writeAttrList(attrsList,level,edgeString);     
      }
    }
        
   private void writeNodeId(Term t, int level, String edgeString) throws TermWareException    
   {
     if (t.getArity()==1) {
         writeTerm(t.getSubtermAt(0),level,edgeString);
     }else if (t.getArity()==2) {
         writeTerm(t.getSubtermAt(0),level,edgeString);
         writeTerm(t.getSubtermAt(1),level,edgeString);
     }else{
         throw new AssertException("nodeId must have arity 1 or 2, instead:"+TermHelper.termToPrettyString(t));
     } 
   }
   
    private void writePort(Term t, int level, String edgeString) throws TermWareException
    {
      if (!t.getSubtermAt(0).isNil()) {
          out_.print(":");
          writeTerm(t.getSubtermAt(0),level,edgeString);
      } 
      if (!t.getSubtermAt(1).isNil()) {
          out_.print("@");
          writeTerm(t.getSubtermAt(1),level,edgeString);
      }       
    }
    
    private void writeDotSubgraph(Term t, int level, String edgeString) throws TermWareException
    {
      out_.print("subgraph");
      out_.print(" ");
      writeTerm(t.getSubtermAt(0),level,edgeString);
      out_.print(" ");
      out_.println("{");
      Term curr = t.getSubtermAt(1);
      while(!curr.isNil()) {
        writeIdent(level+1);
        writeTerm(curr.getSubtermAt(0),level+1,edgeString);
        out_.println(";");
        curr=curr.getSubtermAt(1);
      }
      writeIdent(level);  
      out_.println("}");
    }
    
    private void writeId(Term idTerm, int level)
    {
        out_.print(idTerm.getSubtermAt(0).getString());
    }
    
    private void writeIdent(int level) {
        for(int i=0; i<level*identSize_;++i)  {
            out_.print(' ');
        }
    }

    private void writeNextLine(int level) {
        out_.println();
        writeIdent(level);
    }

    private int identSize_=2; 
    
    
}
