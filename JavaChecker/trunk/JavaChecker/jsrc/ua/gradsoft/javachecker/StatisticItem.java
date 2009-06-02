package ua.gradsoft.javachecker;

import java.io.PrintStream;
import java.util.Set;
import ua.gradsoft.termware.TermWareException;

/**
 *Statistic item is item like
 * statistic(name,expression)
 * where
 * <ul>
 *  <li> name  </li> - name of statistics.
 *  <li> expression </li> - expression, which is evaluated by general system,
 *   where atoms are changed to statistics set.
 * </ul>
 */
public abstract class StatisticItem {   

    public StatisticItem(String name, String category, 
                         String description, boolean enabled, boolean show,
                         Statistics owner)
    {
      name_=name;
      category_=category;
      description_=description;
      enabled_=enabled;
      show_=show;
      owner_=owner;
    }

    public String getName()
    { return name_; }

    public String getCategory()
    { return category_; }
    
    public String getDescription()
    { return description_; }

    public  boolean isEnabled()
    { return enabled_; }

    public  void  setEnabled(boolean enabled)
    { enabled_=enabled; }

    public boolean isShow()
    { return show_; }
    
    public void setShow(boolean show)
    {
      show_=show;  
    }

    public void report(PrintStream out, ReportFormat format, StatisticScope scope) throws TermWareException
    {
      if (enabled_) {
       switch(format)   {
           case TEXT:
               out.print(description_);
               out.println("\t:\t"+getValue(scope));
               break;
           case HTML:
               out.print("<tr><td>");
               out.print(description_);
               out.print("</td><td>");
               out.print(getValue(scope));
               out.print("</td></tr>");
               break;
           default:
               throw new RuntimeException("Invalid report format:"+format);
       }
      }
    }



    public abstract double getValue(StatisticScope scope) throws TermWareException;

    /**
     * Flush scope
     * @return
     */
    public abstract double  endScope(StatisticScope scope) throws TermWareException;

    /**
     * get names of dependencies for this value.
     *(i.e. names of StatisticItem, which we need for calcuate 'this' value)
     */
    public abstract Set<String>  getDependencies();

    protected String   name_;
    protected String   category_;
    protected String   description_;
    protected boolean  enabled_;
    protected boolean  show_;
    protected Statistics  owner_;

}
