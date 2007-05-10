/*
 * ImmutableMappedList.java
 *
 * Created on May 3, 2007, 4:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ua.gradsoft.javachecker.util;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Immutable mapped list, which return mapping of original x by function.
 * @author rssh
 */
public class ImmutableMappedList<X,Y> extends ImmutableMappedCollection<X,Y> implements List<Y>
{
    
    /** Creates a new instance of ImmutableMappedList */
    public ImmutableMappedList(List<X> origin, Function<X,Y> f) {
        super(origin,f);
        lorigin_=origin;
    }
    
    public void add(int index, Y element) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, Collection<? extends Y> elements) {
        throw new UnsupportedOperationException();
    }
    
    
    public Y get(int pos)
    { try {
          return f_.function(lorigin_.get(pos)); 
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
    
    public Y set(int index, Y element) {
        throw new UnsupportedOperationException();
    }
    
    
    
    public Y  remove(int x) {
        throw new UnsupportedOperationException();}
    
    public int indexOf(Object o) {
        for(int i=0; i<lorigin_.size(); ++i) {
          try {  
            if (f_.function(lorigin_.get(i)).equals(o)) {
                return i;
            }
          }catch(TermWareException ex){
              throw new TermWareRuntimeException(ex);
          }
        }
        return -1;
    }
    
    
    public int lastIndexOf(Object o) {
      try{  
        for(int i=lorigin_.size(); i>0; --i) {            
            if (f_.function(lorigin_.get(i-1)).equals(o)) {
                return i;
            }
        }
        return -1;
      }catch(TermWareException ex){
          throw new TermWareRuntimeException(ex);
      }
    }
    
    public ListIterator<Y>  listIterator() {
        return new MappedListIterator(lorigin_.listIterator()); }
    
    public ListIterator<Y>  listIterator(int i) {
        return new MappedListIterator(lorigin_.listIterator(i)); }
    
    public class MappedListIterator implements ListIterator<Y>
    {
        MappedListIterator(ListIterator<X> xit) {
            xit_=xit; }
        
        public void add(Y e) {
            throw new UnsupportedOperationException();
        }
        
        public boolean hasNext()  { return xit_.hasNext(); }
        
        public boolean hasPrevious() { return xit_.hasPrevious(); }
        
        public Y next() { 
            try {
                return f_.function(xit_.next()); 
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
        public int nextIndex() { return xit_.nextIndex(); }
        
        public Y previous() { 
          try {  
            return f_.function(xit_.previous()); 
          }catch(TermWareException ex){
              throw new TermWareRuntimeException(ex);
          }
        }
        
        public int previousIndex() { return xit_.previousIndex(); }
        
        public void remove() { throw new UnsupportedOperationException(); }
        
        public void set(Y e) { throw new UnsupportedOperationException(); }
        
        
        private ListIterator<X> xit_;
    }
    
    public List<Y> subList(int l, int h) {
        return new ImmutableMappedList<X,Y>(lorigin_.subList(l,h),f_); }
    
    protected List<X> lorigin_;
    
    
}
