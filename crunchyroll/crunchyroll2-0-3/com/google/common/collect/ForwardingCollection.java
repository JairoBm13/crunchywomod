// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Collection;

public abstract class ForwardingCollection<E> extends ForwardingObject implements Collection<E>
{
    @Override
    public boolean add(final E e) {
        return this.delegate().add(e);
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> collection) {
        return this.delegate().addAll(collection);
    }
    
    @Override
    public void clear() {
        this.delegate().clear();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.delegate().contains(o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> collection) {
        return this.delegate().containsAll(collection);
    }
    
    @Override
    protected abstract Collection<E> delegate();
    
    @Override
    public boolean isEmpty() {
        return this.delegate().isEmpty();
    }
    
    @Override
    public Iterator<E> iterator() {
        return this.delegate().iterator();
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.delegate().remove(o);
    }
    
    @Override
    public boolean removeAll(final Collection<?> collection) {
        return this.delegate().removeAll(collection);
    }
    
    @Override
    public boolean retainAll(final Collection<?> collection) {
        return this.delegate().retainAll(collection);
    }
    
    @Override
    public int size() {
        return this.delegate().size();
    }
    
    @Override
    public Object[] toArray() {
        return this.delegate().toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        return this.delegate().toArray(array);
    }
}
