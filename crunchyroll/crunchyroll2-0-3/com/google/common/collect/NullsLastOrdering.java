// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.io.Serializable;

final class NullsLastOrdering<T> extends Ordering<T> implements Serializable
{
    final Ordering<? super T> ordering;
    
    NullsLastOrdering(final Ordering<? super T> ordering) {
        this.ordering = ordering;
    }
    
    @Override
    public int compare(final T t, final T t2) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return 1;
        }
        if (t2 == null) {
            return -1;
        }
        return this.ordering.compare((Object)t, (Object)t2);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof NullsLastOrdering && this.ordering.equals(((NullsLastOrdering)o).ordering));
    }
    
    @Override
    public int hashCode() {
        return this.ordering.hashCode() ^ 0xC9177248;
    }
    
    @Override
    public <S extends T> Ordering<S> nullsFirst() {
        return this.ordering.nullsFirst();
    }
    
    @Override
    public <S extends T> Ordering<S> nullsLast() {
        return (Ordering<S>)this;
    }
    
    @Override
    public <S extends T> Ordering<S> reverse() {
        return this.ordering.reverse().nullsFirst();
    }
    
    @Override
    public String toString() {
        return this.ordering + ".nullsLast()";
    }
}
