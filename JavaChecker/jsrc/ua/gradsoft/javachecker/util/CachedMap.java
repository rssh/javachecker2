/*
 * CachedMap.java
 *
 */

package ua.gradsoft.javachecker.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *Map, which keep local cache.
 * @author rssh
 */
public class CachedMap<K,V> implements Map<K,V>
{
    
    /**
     * Creates a new instance of CachedMap
     */
    public CachedMap(Map<K,V> cache, Map<K,V> far) {
        cache_=cache;
        far_=far;
    }
    
    public void clear()
    { far_.clear(); cache_.clear(); }

    
    public boolean containsKey(Object key)
    {
        return cache_.containsKey(key) || far_.containsKey(key);
    }
    
    public boolean containsValue(Object value)
    {
        return cache_.containsValue(value) || far_.containsValue(value);
    }
    
    public boolean equals(Object o)
    { return far_.equals(o); }
    
    public int hashCode()
    { return far_.hashCode(); }
    
    public boolean isEmpty()
    { return far_.isEmpty(); }
    
    public Set<K>  keySet()
    { cache_.putAll(far_); 
      return cache_.keySet(); }
    
    public Set<Entry<K,V>> entrySet()
    {
      cache_.putAll(far_);     
      return cache_.entrySet();  
    }
    
    public V put(K k,V v)
    {
      cache_.put(k,v);
      return far_.put(k,v);
    }
    
    public void putAll(Map<? extends K, ? extends V> m)
    {
        cache_.putAll(m);
        far_.putAll(m);
    }
    
    public V get(Object k)
    {
        V retval = cache_.get(k);
        if (retval==null) {
            if (cache_.size()==far_.size()) {
                return null;
            }
            retval = far_.get(k);
            if (retval!=null) {
                cache_.put((K)k,retval);
            }
        }
        return retval;
    }
   
    public V remove(Object k)
    { 
      cache_.remove(k);
      return far_.remove(k);
    }
    
    public int size()
    { return far_.size(); }
    
    public Collection<V> values()
    {
        cache_.putAll(far_);
        return cache_.values();
    }
    
    private Map<K,V> cache_;
    private Map<K,V> far_;
    
}
