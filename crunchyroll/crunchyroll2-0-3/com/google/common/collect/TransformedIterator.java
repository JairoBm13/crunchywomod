// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.Iterator;

abstract class TransformedIterator<F, T> implements Iterator<T>
{
    final Iterator<? extends F> backingIterator;
    
    TransformedIterator(final Iterator<? extends F> iterator) {
        this.backingIterator = Preconditions.checkNotNull(iterator);
    }
    
    @Override
    public final boolean hasNext() {
        return this.backingIterator.hasNext();
    }
    
    @Override
    public final T next() {
        return this.transform(this.backingIterator.next());
    }
    
    @Override
    public final void remove() {
        this.backingIterator.remove();
    }
    
    abstract T transform(final F p0);
}
