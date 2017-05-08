// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

abstract class ImmutableAsList<E> extends ImmutableList<E>
{
    @Override
    public boolean contains(final Object o) {
        return this.delegateCollection().contains(o);
    }
    
    abstract ImmutableCollection<E> delegateCollection();
    
    @Override
    public boolean isEmpty() {
        return this.delegateCollection().isEmpty();
    }
    
    @Override
    boolean isPartialView() {
        return this.delegateCollection().isPartialView();
    }
    
    @Override
    public int size() {
        return this.delegateCollection().size();
    }
}
