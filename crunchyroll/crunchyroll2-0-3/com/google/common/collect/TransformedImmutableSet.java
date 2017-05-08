// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

abstract class TransformedImmutableSet<D, E> extends ImmutableSet<E>
{
    final int hashCode;
    final ImmutableCollection<D> source;
    
    TransformedImmutableSet(final ImmutableCollection<D> source) {
        this.source = source;
        this.hashCode = Sets.hashCodeImpl(this);
    }
    
    TransformedImmutableSet(final ImmutableCollection<D> source, final int hashCode) {
        this.source = source;
        this.hashCode = hashCode;
    }
    
    @Override
    public final int hashCode() {
        return this.hashCode;
    }
    
    @Override
    public boolean isEmpty() {
        return false;
    }
    
    @Override
    boolean isHashCodeFast() {
        return true;
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return new UnmodifiableIterator<E>() {
            final /* synthetic */ Iterator val$backingIterator = TransformedImmutableSet.this.source.iterator();
            
            @Override
            public boolean hasNext() {
                return this.val$backingIterator.hasNext();
            }
            
            @Override
            public E next() {
                return TransformedImmutableSet.this.transform(this.val$backingIterator.next());
            }
        };
    }
    
    @Override
    public int size() {
        return this.source.size();
    }
    
    @Override
    public Object[] toArray() {
        return this.toArray(new Object[this.size()]);
    }
    
    @Override
    public <T> T[] toArray(final T[] array) {
        return ObjectArrays.toArrayImpl(this, array);
    }
    
    abstract E transform(final D p0);
}
