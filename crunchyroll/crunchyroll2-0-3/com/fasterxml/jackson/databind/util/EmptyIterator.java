// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.util.NoSuchElementException;
import java.util.Iterator;

public class EmptyIterator<T> implements Iterator<T>
{
    private static final EmptyIterator<?> instance;
    
    static {
        instance = new EmptyIterator<Object>();
    }
    
    public static <T> Iterator<T> instance() {
        return (Iterator<T>)EmptyIterator.instance;
    }
    
    @Override
    public boolean hasNext() {
        return false;
    }
    
    @Override
    public T next() {
        throw new NoSuchElementException();
    }
    
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
