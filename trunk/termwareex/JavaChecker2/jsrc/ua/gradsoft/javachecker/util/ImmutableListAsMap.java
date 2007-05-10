/*
 * ImmutableListAsMap.java
 */

package ua.gradsoft.javachecker.util;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Representation of list (where all is filled) and indexing function  as immutable map
 * @author rssh
 */
public class ImmutableListAsMap<K,V> implements Map<K,V>
{
    
    public ImmutableListAsMap(List<V> list, Function<K,Integer> indexFunction, Function<Integer,K> keyFunction)
    {
        list_=list;
        indexFunction_=indexFunction;
        keyFunction_=keyFunction;
    }
    
    public void clear() {
        throw new UnsupportedOperationException(); }
    
    
    public boolean containsKey(Object key) {
        try {
          return indexFunction_.function((K)key) < list_.size();
        }catch(TermWareException ex){
            throw new TermWareRuntimeException(ex);
        }
    }
    
    public boolean containsValue(Object value) {
        return list_.contains(value);
    }
    
    public boolean equals(Object o) {
        if (o instanceof ImmutableListAsMap) {
            ImmutableListAsMap xo = (ImmutableListAsMap)o;
            return list_.equals(xo.list_) && indexFunction_.equals(xo.indexFunction_);
        }else{
            return false;
        }
    }
    
    public int hashCode() {
        return list_.hashCode() + indexFunction_.hashCode(); }
    
    public boolean isEmpty() {
        return list_.isEmpty(); }
    
    public Set<K>  keySet() {
        return new HereKeySet();                     
    }
    
    public Set<Entry<K,V>> entrySet() {
        return new HereEntrySet();
    }
    
    public V put(K k,V v) {
        throw new UnsupportedOperationException();
    }
    
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }
    
    public V get(Object k) {
        try {
            Integer index = indexFunction_.function((K)k);
            if (index==null) {
                return null;
            }else{
               return list_.get(index);
            }
        }catch(TermWareException ex){
            throw new TermWareRuntimeException(ex);
        }
    }
    
    public V remove(Object k) {
        throw new UnsupportedOperationException();
    }
    
    public int size() {
        return list_.size(); }
    
    public Collection<V> values() {        
        return list_;
    }
    
    class HereEntrySet implements Set<Map.Entry<K,V>>
    {
        public boolean add(Map.Entry<K,V> e) {
            throw new UnsupportedOperationException(); }
        
        public boolean addAll(Collection<? extends Map.Entry<K,V>> c) {
            throw new UnsupportedOperationException(); }
        
        public void clear() {
            throw new UnsupportedOperationException(); }
        
        public boolean contains(Object o) {
            try {
                Map.Entry<K,V> e = (Map.Entry<K,V>)o;
                Integer index = indexFunction_.function(e.getKey());
                if (index==null) {
                    return false;
                }else{
                    return list_.get(index).equals(e.getValue());
                }
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
        public boolean containsAll(Collection<?> c) {
            for(Object o: c) {
                if (!contains(o)) return false;
            }
            return true;
        }
        
        public boolean isEmpty() {
            return list_.isEmpty(); }
        
        public Iterator<Entry<K,V>> iterator() {
            return new HereEntrySetIterator(); }
        
        public boolean remove(Object o) {
            throw new UnsupportedOperationException(); }
        
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException(); }
        
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException(); }
        
        public int size() {
            return list_.size(); }
        
        public Object[] toArray() {
            return toArray(new Object[0]);
        }
        
        public <T> T[] toArray(T[] x) {
            try {
                T[] retval = (T[])new Object[list_.size()];
                for(int i=0;i<list_.size();++i) {            
                    retval[i]=(T)new SimpleImmutableEntry<K,V>(keyFunction_.function(i),list_.get(i));
                }
                return retval;
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
    }
        
    class HereEntrySetIterator implements Iterator<Entry<K,V>>
    {
        HereEntrySetIterator() {
            i=0;
        }
        
        public boolean hasNext() {
            return i<list_.size(); }
        
        public Entry<K,V> next() {
            try {
                ++i;
                return new SimpleImmutableEntry<K,V>(keyFunction_.function(i),list_.get(i));
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
        public void remove() {
            throw new UnsupportedOperationException(); }
        
        private int i;
    }
    
    class HereKeySet implements Set<K>
    {
        public boolean add(K k) {
            throw new UnsupportedOperationException(); }
        
        public boolean addAll(Collection<? extends K> c) {
            throw new UnsupportedOperationException(); }
        
        public void clear() {
            throw new UnsupportedOperationException(); }
        
        public boolean contains(Object o) {
            try {
                int index = indexFunction_.function((K)o);
                return index >0 && index < list_.size();
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
        public boolean containsAll(Collection<?> c) {
            for(Object o: c) {
                if (!contains(o)) return false;
            }
            return true;
        }
        
        public boolean isEmpty() {
            return list_.isEmpty(); }
        
        public Iterator<K> iterator() {
            return new HereKeySetIterator(); }
        
        public boolean remove(Object o) {
            throw new UnsupportedOperationException(); }
        
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException(); }
        
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException(); }
        
        public int size() {
            return list_.size(); }
        
        public Object[] toArray() {
            return toArray(new Object[0]);
        }
        
        public <T> T[] toArray(T[] x) {
            try {
                T[] retval = (T[])new Object[list_.size()];
                for(int i=0;i<list_.size();++i) {            
                    retval[i]=(T)keyFunction_.function(i);
                }
                return retval;
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
    }
        
    class HereKeySetIterator implements Iterator<K>
    {
        HereKeySetIterator() {
            i=0;
        }
        
        public boolean hasNext() {
            return i<list_.size(); }
        
        public K next() {
            try {
                ++i;
                return keyFunction_.function(i);
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
        public void remove() {
            throw new UnsupportedOperationException(); }
        
        private int i;
    }
    
    
    private Function<K,Integer> indexFunction_;
    private Function<Integer,K> keyFunction_;
    private List<V> list_;
}
