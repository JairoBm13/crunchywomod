// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.List;
import java.io.Serializable;

final class AllEqualOrdering extends Ordering<Object> implements Serializable
{
    static final AllEqualOrdering INSTANCE;
    
    static {
        INSTANCE = new AllEqualOrdering();
    }
    
    @Override
    public int compare(final Object o, final Object o2) {
        return 0;
    }
    
    @Override
    public <E> ImmutableList<E> immutableSortedCopy(final Iterable<E> iterable) {
        return ImmutableList.copyOf((Iterable<? extends E>)iterable);
    }
    
    @Override
    public <S> Ordering<S> reverse() {
        return (Ordering<S>)this;
    }
    
    @Override
    public <E> List<E> sortedCopy(final Iterable<E> iterable) {
        return (List<E>)Lists.newArrayList((Iterable<?>)iterable);
    }
    
    @Override
    public String toString() {
        return "Ordering.allEqual()";
    }
}
