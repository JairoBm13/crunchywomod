// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.NoSuchElementException;

public abstract class AbstractSequentialIterator<T> extends UnmodifiableIterator<T>
{
    private T nextOrNull;
    
    protected AbstractSequentialIterator(final T nextOrNull) {
        this.nextOrNull = nextOrNull;
    }
    
    protected abstract T computeNext(final T p0);
    
    @Override
    public final boolean hasNext() {
        return this.nextOrNull != null;
    }
    
    @Override
    public final T next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return this.nextOrNull;
        }
        finally {
            this.nextOrNull = this.computeNext(this.nextOrNull);
        }
    }
}
