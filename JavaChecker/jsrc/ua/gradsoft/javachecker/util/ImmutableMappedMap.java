/*
 * ImmutableMappedMap.java
 *
 * Created on May 3, 2007, 3:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.util;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *
 * @author rssh
 */
public class ImmutableMappedMap<K,VX,VY> implements Map<K,VY> 
{
           
    /**
     * Creates a new instance of ImmutableMappedMap
     */
    public ImmutableMappedMap(Map<K,VX> origin, Function<VX, VY> f) 
    {
        origin_=origin;
        f_=f;
    }
    
    public void clear()
    { throw new UnsupportedOperationException(); }

    
    public boolean containsKey(Object key)
    {
        return origin_.containsKey(key);
    }
    
    public boolean containsValue(Object value)
    {
      try{  
        for(Map.Entry<K,VX> e : origin_.entrySet()) {
            if (f_.function(e.getValue()).equals(value)) {
                return true;
            }
        }
        return false;
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }

    public VY get(Object key)
    {
      try {  
      VX x = origin_.get(key);
      return (x==null) ? null : f_.function(x);
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
    
    public int size()
    { return origin_.size(); }
    
    public boolean isEmpty()
    { return origin_.isEmpty(); }
    
    
    public Set<K> keySet()
    { return origin_.keySet(); }
    
    public Collection<VY> values()
    {
      return new ImmutableMappedCollection<VX,VY>(
              origin_.values(), f_
              );  
    }
    
    public Set<Entry<K,VY>> entrySet()
    { if (entrySet_==null) {
          entrySet_=new MappedEntrySet();
      }
      return entrySet_;
    }
    
    public VY put(K k, VY v)
    { throw new UnsupportedOperationException(); }
    
    public void putAll(Map<? extends K,? extends VY> c)
    { throw new UnsupportedOperationException(); }
    

    public VY remove(Object key)
    { throw new UnsupportedOperationException(); }
    
    
    public final class MappedEntrySet implements Set<Map.Entry<K,VY>>
    {
        MappedEntrySet()
        {
          originEntrySet_=origin_.entrySet();  
        }

        public boolean add(Entry<K,VY> e)
        { throw new UnsupportedOperationException(); }
        
        public boolean addAll(Collection<? extends Entry<K,VY>> c)
        { throw new UnsupportedOperationException(); }
        
      
        
        public boolean contains(Object o)
        {
         try {   
           Map.Entry<K,VY> oe = (Map.Entry<K,VY>)o;
           VX v = origin_.get(oe.getKey());
           if (v==null) {
               return false;
           }
           return f_.function(v).equals(oe.getValue());
         }catch(TermWareException ex){
             throw new TermWareRuntimeException(ex);
         }
        }
        
        public boolean containsAll(Collection<?> c)
        {
            for(Object o:c) {
                if (!contains(o)) {
                    return false;
                }
            }
            return true;
        }
        
        public boolean retainAll(Collection<?> c)
        {
            throw new UnsupportedOperationException();
        }
        
        public boolean remove(Object o)
        {
           throw new UnsupportedOperationException();
        }
        
        public boolean removeAll(Collection<?> c)
        {
           throw new UnsupportedOperationException();
        }
        
        public<T>  T[] toArray(T[] x)
        { return (T[])toArray(); }
        
        public Object[] toArray()
        {
         try {   
          Object[] retval=new Object[origin_.size()];
          int i=0;
          for(Map.Entry<K,VX> e: origin_.entrySet()) {
              retval[i]=new AbstractMap.SimpleEntry<K,VY>(e.getKey(),f_.function(e.getValue()));
              ++i;
          }
          return retval;
         }catch(TermWareException ex){
             throw new TermWareRuntimeException(ex);
         }
        }
        
        public void clear()
        { throw new UnsupportedOperationException(); }
        
        public Iterator<Map.Entry<K,VY>> iterator()
        {
          return new MapEntrySetIterator(origin_.entrySet().iterator());  
        }
        
        public boolean isEmpty()
        { return origin_.isEmpty(); }
        
        public int size()
        { return origin_.size(); }
        
        private Set<Map.Entry<K,VX>> originEntrySet_;
    }
    
    class MapEntrySetIterator implements Iterator<Map.Entry<K,VY>>
    {
      MapEntrySetIterator(Iterator<Map.Entry<K,VX>> it)
      {
        it_=it;  
      }
      
      public boolean hasNext()
      { return it_.hasNext(); }
      
      public Map.Entry<K,VY> next()
      {
        try {  
          Map.Entry<K,VX> e = it_.next();
          return new AbstractMap.SimpleImmutableEntry(e.getKey(),f_.function(e.getValue()));          
        }catch(TermWareException ex){
            throw new TermWareRuntimeException(ex);
        }
      }
          
      public void remove() {
            throw new UnsupportedOperationException(); }
        
      
      private Iterator<Map.Entry<K,VX>> it_;
    }
    
    private MappedEntrySet entrySet_=null;
    
    private Map<K,VX> origin_;
    private Function<VX,VY> f_;
}
