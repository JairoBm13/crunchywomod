// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import com.google.common.base.Predicate;
import java.util.Iterator;
import com.google.common.base.Preconditions;

public abstract class FluentIterable<E> implements Iterable<E>
{
    private final Iterable<E> iterable;
    
    protected FluentIterable() {
        this.iterable = this;
    }
    
    FluentIterable(final Iterable<E> iterable) {
        this.iterable = Preconditions.checkNotNull(iterable);
    }
    
    public static <E> FluentIterable<E> from(final Iterable<E> iterable) {
        if (iterable instanceof FluentIterable) {
            return (FluentIterable<E>)iterable;
        }
        return new FluentIterable<E>(iterable) {
            @Override
            public Iterator<E> iterator() {
                return iterable.iterator();
            }
        };
    }
    
    public final FluentIterable<E> filter(final Predicate<? super E> predicate) {
        return from((Iterable<E>)Iterables.filter((Iterable<E>)this.iterable, (Predicate<? super E>)predicate));
    }
    
    public final ImmutableSet<E> toImmutableSet() {
        return ImmutableSet.copyOf((Iterable<? extends E>)this.iterable);
    }
    
    @Override
    public String toString() {
        return Iterables.toString(this.iterable);
    }
}
