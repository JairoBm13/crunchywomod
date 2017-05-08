// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Collection;
import java.util.Set;

public abstract class ForwardingSet<E> extends ForwardingCollection<E> implements Set<E>
{
    @Override
    protected abstract Set<E> delegate();
    
    @Override
    public boolean equals(final Object o) {
        return o == this || this.delegate().equals(o);
    }
    
    @Override
    public int hashCode() {
        return this.delegate().hashCode();
    }
}
