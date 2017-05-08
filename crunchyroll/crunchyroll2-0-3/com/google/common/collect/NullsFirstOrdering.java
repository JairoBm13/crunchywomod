// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.io.Serializable;

final class NullsFirstOrdering<T> extends Ordering<T> implements Serializable
{
    final Ordering<? super T> ordering;
    
    NullsFirstOrdering(final Ordering<? super T> ordering) {
        this.ordering = ordering;
    }
    
    @Override
    public int compare(final T t, final T t2) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return -1;
        }
        if (t2 == null) {
            return 1;
        }
        return this.ordering.compare((Object)t, (Object)t2);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || (o instanceof NullsFirstOrdering && this.ordering.equals(((NullsFirstOrdering)o).ordering));
    }
    
    @Override
    public int hashCode() {
        return this.ordering.hashCode() ^ 0x39153A74;
    }
    
    @Override
    public <S extends T> Ordering<S> nullsFirst() {
        return (Ordering<S>)this;
    }
    
    @Override
    public <S extends T> Ordering<S> nullsLast() {
        return this.ordering.nullsLast();
    }
    
    @Override
    public <S extends T> Ordering<S> reverse() {
        return this.ordering.reverse().nullsLast();
    }
    
    @Override
    public String toString() {
        return this.ordering + ".nullsFirst()";
    }
}
