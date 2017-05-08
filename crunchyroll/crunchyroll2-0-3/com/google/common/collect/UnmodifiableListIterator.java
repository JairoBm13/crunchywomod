// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.ListIterator;

public abstract class UnmodifiableListIterator<E> extends UnmodifiableIterator<E> implements ListIterator<E>
{
    @Override
    public final void add(final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final void set(final E e) {
        throw new UnsupportedOperationException();
    }
}
