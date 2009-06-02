package ua.gradsoft.javachecker;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import ua.gradsoft.termware.DefaultFacts;
import ua.gradsoft.termware.Term;
import ua.gradsoft.termware.TermSystem;
import ua.gradsoft.termware.TermWare;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TransformationContext;
import ua.gradsoft.termware.exceptions.AssertException;
import ua.gradsoft.termware.exceptions.ConversionException;
import ua.gradsoft.termware.exceptions.RuntimeAssertException;
import ua.gradsoft.termware.strategies.BTStrategy;

/**
 *Functions here incapsulte operations with statistic items.
 * @author rssh
 */
public class Statistics {

    public class SFacts extends DefaultFacts
    {
       SFacts() throws TermWareException
       {}

        
       public void getValue(TransformationContext ctx,
                            Term x,
                            String name,
                            int scopeOrdinal) throws TermWareException
       {
          double d = Statistics.this.getValue(name, StatisticScope.values()[scopeOrdinal]);
          ctx.getCurrentSubstitution().put(x, TermWare.getInstance().getTermFactory().createDouble(d));
          //System.err.print("substitution after getValue method:");
          //ctx.getCurrentSubstitution().print(System.err);
          //System.err.println();
       }
    }

    public Statistics() throws TermWareException
    {
      termSystem_ = new TermSystem(new BTStrategy(),new SFacts());
      TermWare.addGeneralTransformers(termSystem_);
      termSystem_.addRule("V($name,$scope) -> $z [ getValue($z, $name,$scope) ] ");
      //termSystem_.setLoggingMode(true);
      //termSystem_.setLoggedEntity("All");
      valuesInScope_=new Map[StatisticScope.values().length];
    }
    
    public StatisticItem parseStatisticDefinition(Term t) throws TermWareException, ConfigException
    {
      if (t.getArity()!=5)  {
          throw new ConfigException("arity of calculate must be 5");
      }
      Term nameTerm = t.getSubtermAt(0);
      Term categoryTerm = t.getSubtermAt(1);
      Term descriptionTerm = t.getSubtermAt(2);
      Term ruleTerm = t.getSubtermAt(3);
      Term enabledTerm = t.getSubtermAt(4);
      String name = nameTerm.getAsString(TermWare.getInstance());
      String category = categoryTerm.getAsString(TermWare.getInstance());
      String description = descriptionTerm.getAsString(TermWare.getInstance());      
      boolean enabled;
      try {
         enabled = enabledTerm.getAsBoolean(TermWare.getInstance());
      }catch(ConversionException ex){
         throw new ConfigException("5-th argument of statistic definifition must be boolean, have "+enabledTerm.getName()+" for "+name);
      }

      StatisticItem si = new CalculatedStatisticItem(name,category,description,ruleTerm,enabled,Main.getStatistics());
      return si;
    }

    public void addItem(StatisticItem item)
    {
       itemsByName_.put(item.getName(), item);
       itemsTopOrdered_=null; 
    }

    public StatisticItem getItem(String name)
    {
       return itemsByName_.get(name);
    }
    
    
    public double getValue(String name, StatisticScope scope) throws TermWareException
    {
       StatisticItem item = itemsByName_.get(name);
       if (item==null) {
           throw new AssertException("Invalid statistic item name:"+name);
       }        
       return item.getValue(scope);
    }

    public TermSystem getTermSystem()
    {
      return termSystem_;
    }

    public void endOfScope(String scopeEntity, StatisticScope scope) throws TermWareException
    {
      Map<String,Double> retval = new TreeMap<String,Double>();
      for(int i=itemsTopOrdered_.size(); i>0; --i) {
          // in reverse order, becouse endOfScope reset current value
          StatisticItem item = itemsTopOrdered_.get(i-1);
          if (item.isEnabled() && item.isShow()) {
            Double d = item.endScope(scope);
            retval.put(item.getName(), d);
          } else {
              // debufg
              //
              //System.err.println("item "+item.getName()+" skip for endOfScope");
          }

      }
      if (scope.ordinal()>=Main.getStatisticDetail().ordinal()) {
          Map<String,Map<String,Double>> m = valuesInScope_[scope.ordinal()];
          if (m==null) {
              m=new TreeMap<String,Map<String,Double>>();
              valuesInScope_[scope.ordinal()]=m;
          }
          m.put(scopeEntity, retval);
      }   
    }



    /**
     * Intialize calculated values.
     *Note, that
     */
    public void init() throws ConfigException
    {
       itemsTopOrdered_ = new ArrayList<StatisticItem>();
       Set<String> inserted = new TreeSet<String>();
       for(Map.Entry<String,StatisticItem> e: itemsByName_.entrySet()) {
           if (!inserted.contains(e.getKey())) {
               insertWithDependencies(e.getValue(),inserted,e.getValue().isEnabled());
           }
       }
    }

    private void insertWithDependencies(StatisticItem item, Set<String> inserted, boolean enable) throws ConfigException
    {
        //System.err.println("insertWithDependencies("+item.getName()+","+enable+")");
        Set<String> dependencies = item.getDependencies();
        for(String s: dependencies) {
            StatisticItem childItem = itemsByName_.get(s);
            if (childItem == null ) {
                throw new ConfigException("Invalid name "+s+" in definition of "+item.getName()+" is not defined");
            }
            if (!inserted.contains(s)) {                
                insertWithDependencies(childItem,inserted,enable);
                if (inserted.contains(item.getName())) {
                  throw new ConfigException("Loop in calculate definition detected ("+item.getName()+","+s+")" );
                }
                if (enable && !childItem.isEnabled()) {
                    System.err.print("enable "+s+" check, as dependency to "+item.getName());
                    enableWithDependencies(childItem);
                }
            }else{
                // call to enableWithDependencies in two places, to prevent checking for loop in dependency inside
                if (enable && !childItem.isEnabled()) {
                    System.err.print("enable "+s+" check, as dependency to "+item.getName());
                    enableWithDependencies(childItem);                    
                }                
            }
        }
        itemsTopOrdered_.add(item);
        inserted.add(item.getName());
    }

    private void enableWithDependencies(StatisticItem item) throws ConfigException
    {
      // we know that item dependencies does not contain item.
      item.setEnabled(true);
      for(String s: item.getDependencies()) {
            StatisticItem childItem = itemsByName_.get(s);
            if (childItem == null ) {
                throw new ConfigException("Invalid name "+s+" in definition of "+item.getName()+" is not defined");
            }
            if (!childItem.isEnabled()) {
                System.err.print("enable "+s+" check, as dependency to "+item.getName());
                enableWithDependencies(item);
            }
      }
    }

    public void report(PrintStream out, ReportFormat format)
    {
        //out.println("Statistics.report");
        switch(Main.getStatisticDetail()) {
            case FILE:
                report(out,format,StatisticScope.FILE);
            case DIRECTORY:
                report(out,format,StatisticScope.DIRECTORY);
            case ALL:
                report(out,format,StatisticScope.ALL);
        }
    }

    public void report(PrintStream out, ReportFormat format, StatisticScope scope)
    {

        Map<String,Map<String,Double>> values = valuesInScope_[scope.ordinal()];

        if (values==null) {
            System.err.print("valuesInScope_ is null");
            return;
        }
        
        switch(format)
        {
            case TEXT:
                break;
            case HTML:
                if (scope!=StatisticScope.ALL) {
                  out.print("<p>Statistic (per ");
                  out.print(scope.name());
                  out.print("</p>");
                  out.print("<table>");
                  out.print("<thead>");
                  out.print("<tr>");
                  out.print(  "<th>");
                  out.print(scope.name().toLowerCase());
                  out.print(  "</th>");
                  out.print(  "<th>statistic</th>");
                  out.print("</tr>");
                  out.print("</thead>");
                }else{
                  out.print("<p>Statistic");
                  out.print("<table>");
                }
                break;
            case XML:
                out.printf("<statistic scope='%s' >\n",scope.name());
                break;
            default:
                throw new RuntimeAssertException("Invalid report format :"+format);
        }

        for(Map.Entry<String,Map<String,Double>> e: values.entrySet()) {
            switch(format) {
                case TEXT:
                   out.printf("for %s:\n",e.getKey());
                   break;
                case HTML:
                    if (scope!=StatisticScope.ALL) {
                        out.print("<tr>\n");
                        out.print("<td>\n");
                        out.print(e.getKey());
                        out.print("</td>\n");
                        out.print("<td>\n");
                        out.print("<table>\n");
                    }else{
                        out.print("<!-- for all -->");
                    }
                    break;
                case XML:
                    out.printf("<entry name='%s'>");
                    break;
                default:
                    throw new RuntimeAssertException("Invalid report format :"+format);
            }
            for(Map.Entry<String,Double> ee:e.getValue().entrySet()) {
                StatisticItem si = itemsByName_.get(ee.getKey());
                boolean isInt =  (Math.abs(ee.getValue().doubleValue()-ee.getValue().intValue())<0.00001) ;
                switch(format) {
                    case TEXT:
                        if (isInt) {
                           out.printf("%1$s (%2$s):\t %3$d\n", si.getName(), si.getDescription(), ee.getValue().intValue() );
                        }else{
                           out.printf("%1$s (%2$s):\t %3$f\n", si.getName(), si.getDescription(), ee.getValue() );
                        }
                        break;
                    case HTML:
                        if (isInt) {
                            out.printf("<tr><td>%1$s (%2$s) </td><td>%3$d</td></tr>\n",
                                si.getName(), si.getDescription(), ee.getValue().intValue() );
                        } else {
                           out.printf("<tr><td>%1$s (%2$s) </td><td>%3$f</td></tr>\n",
                                si.getName(), si.getDescription(), ee.getValue() );

                        }
                        break;
                    case XML:
                        out.printf("<value parameter='%1$s' ",si.getName());
                        out.printf(" description='%2$s' ",si.getDescription());
                        if (isInt) {
                          out.printf(" value='%3$d' />\n", ee.getValue().intValue());
                        } else {
                          out.printf(" value='%3$f' />\n", ee.getValue());
                        }
                        break;
                    default:
                       throw new RuntimeAssertException("Invalid report format :"+format);
                }
            }

            switch(format) {
                case TEXT:
                    break;
                case HTML:
                    out.println("</table>");
                    if (scope!=StatisticScope.ALL) {
                        out.println("</td></tr>");
                    }
                    break;
                case XML:
                    out.println("</entry>");
                    break;
            }

        }

        switch(format) {
            case TEXT:
                break;
            case HTML:
                out.println("</table>");
                break;
            case XML:
                out.println("</statistic>");
                break;
        }

    }



    /**
     * StatisticItems, sorted in topological order;
     */
    private ArrayList<StatisticItem>  itemsTopOrdered_;

    /**
     * StaticisItems, indexed by name
     */
    private Map<String,StatisticItem> itemsByName_ = new TreeMap<String,StatisticItem>();


    /**
     * Term system, which evaluate statistics expression.
     */
    TermSystem termSystem_;

    private Map<String,Map<String,Double>>[] valuesInScope_;


}
