/*
 * IntegerOrderList.java
 *
 * Created on May 3, 2007, 5:35 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author rssh
 */
public class IntegerOrderList implements List<Integer> {
    
    /** Creates a new instance of IntegerOrderList */
    public IntegerOrderList(int size) {
        this(0,size);
    }
    
    /** Creates a new instance of IntegerOrderList */
    public IntegerOrderList(int lbound, int hbound) {
        lbound_=lbound;
        hbound_=hbound;
    }
    
    public boolean add(Integer v)
    { throw new UnsupportedOperationException(); }
    
    public void add(int index, Integer v)
    { throw new UnsupportedOperationException(); }
    
    public boolean addAll(Collection<? extends Integer> c)
    { throw new UnsupportedOperationException(); }

    public boolean addAll(int index, Collection<? extends Integer> c)
    { throw new UnsupportedOperationException(); }

    public void clear()
    { throw new UnsupportedOperationException(); }
    
    public boolean contains(Object o)
    {
        int x = (Integer)o;
        return x>=lbound_ && x<hbound_;
    }
    
    public boolean containsAll(Collection<?> c)
    {
        if (c instanceof IntegerOrderList) {
            return containsAllSame((IntegerOrderList)c);
        }else{
            for(Object o:c) {
                if (!contains(o)) return false;
            }
            return true;
        }
    }
    
    public boolean containsAllSame(IntegerOrderList l)
    {
        return l.lbound_ >= lbound_ && l.hbound_<=hbound_;
    }
    
    public boolean equals(Object o)
    {
      if (o instanceof IntegerOrderList) {
          IntegerOrderList lo=(IntegerOrderList)o;
          return (lo.hbound_==hbound_) && (lo.lbound_==lbound_);
      }else{
          return false;
      }  
    }
    
    public int hashCode()
    { return (lbound_)+hbound_; }
    
    public Integer get(int index)
    {
        return lbound_+index;
    }
    
    public int indexOf(Object o)
    {
       if (o instanceof Integer) {
           Integer io=(Integer)o;
           if (io>=hbound_) {
               return -1;
           }else if (io<lbound_){
               return -1;
           }else{
               return io-lbound_;
           }       
       }else{
           return -1;
       } 
    }
    
    public int lastIndexOf(Object o)
    { return indexOf(o); }
    
    public boolean isEmpty()
    { return lbound_==hbound_; }
    
    public int size()
    { return hbound_-lbound_; }
    
    public Integer set(int index, Integer element)
    { throw new UnsupportedOperationException(); }
    
    public Integer remove(int i)
    { throw new UnsupportedOperationException(); }
    
    public boolean  remove(Object o)
    { throw new UnsupportedOperationException(); }
    
     public boolean removeAll(Collection<?> c)
    { throw new UnsupportedOperationException(); }
    
     public boolean retainAll(Collection<?> c)
    { throw new UnsupportedOperationException(); }
     
    public List<Integer> subList(int x1, int x2)
    {
        return new IntegerOrderList(lbound_+x1,x2-x1+1);
    }
    
    public Iterator<Integer>  iterator()
    { return new HereIterator(lbound_); }
    
    public ListIterator<Integer>  listIterator()
    { return new HereIterator(lbound_); }
    
    public ListIterator<Integer>  listIterator(int index)
    { return new HereIterator(lbound_+index); }
    
    public Object[] toArray()
    { Integer[] retval = new Integer[size()];
      for(int i=lbound_;i<hbound_;++i) {
          retval[i-lbound_]=i;
      }
      return retval;
    }
    
    public <T>  T[] toArray(T[] x)
    { return (T[])toArray(); }
    
    class HereIterator implements ListIterator<Integer>
    {
     public HereIterator(Integer index)
     { index_=index-1; }
     
     public void add(Integer e)
     { throw new UnsupportedOperationException(); }
     
     public boolean hasNext()
     {          
         return index_ < hbound_-1; 
     }
     
     public boolean hasPrevious()
     { return index_ > lbound_; }
     
     public Integer next()
     { return ++index_; }
     
     public int nextIndex()
     { return index_-lbound_+1; }
     
     
     public Integer previous()
     { return --index_; }
     
     public int previousIndex()
     { return index_-lbound_-1; }
     
     public void remove()
     { throw new UnsupportedOperationException(); }
     
     public void set(Integer e)
     { throw new UnsupportedOperationException(); }
                
     
     int index_;   
    }
    
    private int lbound_;
    private int hbound_;
}
