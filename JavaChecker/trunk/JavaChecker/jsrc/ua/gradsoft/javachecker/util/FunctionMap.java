/*
 * FunctionMap.java
 *
 */

package ua.gradsoft.javachecker.util;

import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Representation of
 *  1). function K->V
 *  2). range of K
 *as immutable map
 * @author rssh
 */
public class FunctionMap<K,V> implements Map<K,V>
{
    
    /** Creates a new instance of FunctionMap */
    public FunctionMap(Collection<K> origin,Function<K,V> f) {
        origin_=origin;
        f_=f;
    }
    
    public void clear() {
        throw new UnsupportedOperationException(); }
    
    
    public boolean containsKey(Object key) {
        return origin_.contains(key);
    }
    
    public boolean containsValue(Object value) {
        try {
            for(K key: origin_) {
                if (f_.function(key).equals(value)) {
                    return true;
                }
            }
            return false;
        }catch(TermWareException ex){
            throw new TermWareRuntimeException(ex);
        }
    }
    
    public boolean equals(Object o) {
        if (o instanceof FunctionMap) {
            FunctionMap xo = (FunctionMap)o;
            return origin_.equals(xo.origin_) && f_.equals(xo.f_);
        }else{
            return false;
        }
    }
    
    public int hashCode() {
        return origin_.hashCode() + f_.hashCode(); }
    
    public boolean isEmpty() {
        return origin_.isEmpty(); }
    
    public Set<K>  keySet() {
        return new HereKeySet(); }
    
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
            return f_.function((K)k);
        }catch(TermWareException ex){
            throw new TermWareRuntimeException(ex);
        }
    }
    
    public V remove(Object k) {
        throw new UnsupportedOperationException();
    }
    
    public int size() {
        return origin_.size(); }
    
    public Collection<V> values() {        
        ArrayList<V> retval=new ArrayList<V>();
        try {
            for(K k: origin_) {
                retval.add(f_.function(k));
            }
        }catch(TermWareException ex){
            throw new TermWareRuntimeException(ex);
        }
        return retval;
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
                return origin_.contains(e.getKey()) && f_.function(e.getKey()).equals(e.getValue());
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
            return origin_.isEmpty(); }
        
        public Iterator<Entry<K,V>> iterator() {
            return new HereEntrySetIterator(origin_.iterator()); }
        
        public boolean remove(Object o) {
            throw new UnsupportedOperationException(); }
        
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException(); }
        
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException(); }
        
        public int size() {
            return origin_.size(); }
        
        public Object[] toArray() {
            return toArray(new Object[0]);
        }
        
        public <T> T[] toArray(T[] x) {
            try {
                T[] retval = (T[])new Object[origin_.size()];
                int i=0;
                for(K k:origin_) {
                    retval[i]=(T)new AbstractMap.SimpleImmutableEntry<K,V>(k,f_.function(k));
                }
                return retval;
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
    }
    
    class HereEntrySetIterator implements Iterator<Entry<K,V>>
    {
        HereEntrySetIterator(Iterator<K> iterator) {
            iterator_=iterator;
        }
        
        public boolean hasNext() {
            return iterator_.hasNext(); }
        
        public Entry<K,V> next() {
            try {
                K k = iterator_.next();
                return new SimpleImmutableEntry<K,V>(k,f_.function(k));
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
        public void remove() {
            throw new UnsupportedOperationException(); }
        
        private Iterator<K> iterator_;
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
               return origin_.contains(o);
        }
        
        public boolean containsAll(Collection<?> c) {
            for(Object o: c) {
                if (!contains(o)) return false;
            }
            return true;
        }
        
        public boolean isEmpty() {
            return origin_.isEmpty(); }
        
        public Iterator<K> iterator() {
            return new HereKeySetIterator(); }
        
        public boolean remove(Object o) {
            throw new UnsupportedOperationException(); }
        
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException(); }
        
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException(); }
        
        public int size() {
            return origin_.size(); }
        
        public Object[] toArray() {
            return toArray(new Object[0]);
        }
        
        public <T> T[] toArray(T[] x) {            
                T[] retval = (T[])new Object[origin_.size()];
                int i=0;
                for(K k: origin_) {
                    retval[i]=(T)k;
                    ++i;
                }
                return retval;
        }
        
    }
        
    class HereKeySetIterator implements Iterator<K>
    {
        HereKeySetIterator() {
            originIterator_=origin_.iterator();
        }
        
        public boolean hasNext() {
            return originIterator_.hasNext(); }
        
        public K next() {
            return originIterator_.next();
        }
        
        public void remove() {
            throw new UnsupportedOperationException(); }
        
        private Iterator<K> originIterator_;
    }
    
    
    
    private Collection<K> origin_;
    private Function<K,V> f_;
    
}
