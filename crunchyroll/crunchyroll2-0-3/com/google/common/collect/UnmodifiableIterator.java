// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;

public abstract class UnmodifiableIterator<E> implements Iterator<E>
{
    @Override
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
