/*
 * ViolationSummary.java
 *
 *
 */

package ua.gradsoft.javachecker;

import java.io.*;
import java.util.Collections;
import java.util.Set;

/**
 * Type of violation
 */
public class TypeOfViolation extends StatisticItem
{
    
    /** Creates a new instance of ViolationSummary 
     *@param name   - name of such statistics
     *@param category - category of checks.
     */
    public TypeOfViolation(String name, String category, String description, boolean enabledByDefault, boolean show, Statistics statistics) {
        super(name,category,description,enabledByDefault,show,statistics);
        counters_=new int[StatisticScope.values().length];
    }
    
    public  void readPreferences(JavaFacts facts)
    {
      enabled_=facts.isCheckEnabled(name_);
    }
        
    @Deprecated
    public  int getCounter()
    { return counters_[StatisticScope.ALL.ordinal()]; }

    public  int getCounter(StatisticScope scope)
    { return counters_[scope.ordinal()]; }


    public  void  increment()
    { 
      for(int i=0; i<counters_.length; ++i) {
          ++counters_[i];
      }
      if (!name_.equals("*")) {
          ((TypeOfViolation)owner_.getItem("*")).increment();
      }
    }

    @Override
    public double getValue(StatisticScope scope)
    {
      return counters_[scope.ordinal()];
    }

    @Override
    public double endScope(StatisticScope scope)
    {
      double retval = counters_[scope.ordinal()];
      counters_[scope.ordinal()]=0;
      return retval;
    }

    @Override
    public Set<String>  getDependencies()
    {
        return Collections.<String>emptySet();
    }

    
     
    private int[]    counters_;
  
    
}
