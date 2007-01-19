/*
 * LocalTypesIterable.java
 *
 * Created on νεδ³λÿ, 14, ρ³χνÿ 2007, 16:00
 *
 * Copyright (c) 2006 GradSoft  Ukraine
 * All Rights Reserved
 */


package ua.gradsoft.javachecker.models;

import java.util.Iterator;
import java.util.NoSuchElementException;
import ua.gradsoft.termware.TermWareException;
import ua.gradsoft.termware.TermWareRuntimeException;

/**
 *Iterable over local types in statement
 * @author Ruslan Shevchenko
 */
public class LocalTypesIterable implements Iterable<JavaTypeModel>
{
    
    public LocalTypesIterable(JavaStatementModel statement) {
        statement_=statement;
    }
    
    public static class LocalTypesIterator implements Iterator<JavaTypeModel>
    {
        public LocalTypesIterator(JavaStatementModel statement) {
            pos_=statement; findNext(); }
        
        private void findNext() {
            try {
                next_=pos_;
                while(next_.getLocalType()==null && (next_.getPreviousStatementModel()!=null || next_.getParentStatementModel()!=null)) {
                    if (next_.getPreviousStatementModel()!=null) {
                        next_=next_.getPreviousStatementModel();
                    } else if (next_.getParentStatementModel()!=null) {
                        next_=next_.getParentStatementModel();
                    }
                }
            }catch(TermWareException ex){
                // some error, near impossible. So, throw RuntimeException
                throw new TermWareRuntimeException(ex);
            }
        }
        
        public boolean hasNext() {
            if (next_==null) {
                findNext();
            }
            try {
                return (next_.getLocalType()!=null);
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
        public JavaTypeModel next() {
            try {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                pos_=next_;
                next_=null;
                return pos_.getLocalType();
            }catch(TermWareException ex){
                throw new TermWareRuntimeException(ex);
            }
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        private JavaStatementModel pos_;
        private JavaStatementModel next_;
        
    }
    
    public  Iterator<JavaTypeModel> iterator() {
        return new LocalTypesIterator(statement_); }
    
    private JavaStatementModel statement_;
}
