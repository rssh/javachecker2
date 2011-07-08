/*
 * ImmutableMappedCollection.java
 *
 */

package ua.gradsoft.javachecker.util;

import java.util.Collection;
import java.util.Iterator;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *represent collection of X as collection of Y
 * @author rssh
 */
public class ImmutableMappedCollection<X,Y> implements Collection<Y>
{
    
    /** Creates a new instance of ImmutableMappedCollection */
    public ImmutableMappedCollection(Collection<X> origin,Function<X,Y> f) {
        origin_=origin;
        f_=f;                
    }
    
        public boolean add(Y e) {
            throw new UnsupportedOperationException(); }
        
        public boolean addAll(Collection<? extends Y> c) {
            throw new UnsupportedOperationException(); }
        
        public void clear() {
            throw new UnsupportedOperationException(); }
        
        public boolean contains(Object o) {
            try {
                return origin_.contains(f_.function((X)o));
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
        
        public Iterator<Y> iterator() {
            return new HereIterator(); }
        
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
                Iterator<X> it = origin_.iterator();
                int i=0;
                while(it.hasNext()) {            
                    retval[i]=(T)f_.function(it.next());
                    ++i;
                }
                return retval;
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
    class HereIterator implements Iterator<Y>
    {
        HereIterator()
        { xit_=origin_.iterator(); }
        
        public boolean hasNext()
        { return xit_.hasNext(); }
        
        public Y next()
        { try {
              return f_.function(xit_.next()); 
          }catch(TermWareException ex){
              throw new TermWareRuntimeException(ex);
          }
        }
        
          public void remove() {
            throw new UnsupportedOperationException(); }
        
        private Iterator<X> xit_;
    }
    
    protected Collection<X> origin_;
    protected Function<X,Y> f_;
    
}
