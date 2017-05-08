// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.SortedSet;
import com.google.common.base.Preconditions;
import java.util.Iterator;
import java.util.Comparator;
import java.util.NavigableSet;

public abstract class ImmutableSortedSet<E> extends ImmutableSortedSetFauxverideShim<E> implements SortedIterable<E>, NavigableSet<E>
{
    private static final ImmutableSortedSet<Comparable> NATURAL_EMPTY_SET;
    private static final Comparator<Comparable> NATURAL_ORDER;
    final transient Comparator<? super E> comparator;
    transient ImmutableSortedSet<E> descendingSet;
    
    static {
        NATURAL_ORDER = Ordering.natural();
        NATURAL_EMPTY_SET = new EmptyImmutableSortedSet<Comparable>(ImmutableSortedSet.NATURAL_ORDER);
    }
    
    ImmutableSortedSet(final Comparator<? super E> comparator) {
        this.comparator = comparator;
    }
    
    private static <E> ImmutableSortedSet<E> emptySet() {
        return (ImmutableSortedSet<E>)ImmutableSortedSet.NATURAL_EMPTY_SET;
    }
    
    static <E> ImmutableSortedSet<E> emptySet(final Comparator<? super E> comparator) {
        if (ImmutableSortedSet.NATURAL_ORDER.equals(comparator)) {
            return emptySet();
        }
        return new EmptyImmutableSortedSet<E>(comparator);
    }
    
    @Override
    public E ceiling(final E e) {
        return Iterables.getFirst((Iterable<? extends E>)this.tailSet(e, true), (E)null);
    }
    
    @Override
    public Comparator<? super E> comparator() {
        return this.comparator;
    }
    
    abstract ImmutableSortedSet<E> createDescendingSet();
    
    @Override
    public UnmodifiableIterator<E> descendingIterator() {
        return this.descendingSet().iterator();
    }
    
    @Override
    public ImmutableSortedSet<E> descendingSet() {
        ImmutableSortedSet<E> descendingSet;
        if ((descendingSet = this.descendingSet) == null) {
            descendingSet = this.createDescendingSet();
            this.descendingSet = descendingSet;
            descendingSet.descendingSet = this;
        }
        return descendingSet;
    }
    
    @Override
    public E floor(final E e) {
        return Iterables.getFirst((Iterable<? extends E>)this.headSet(e, true).descendingSet(), (E)null);
    }
    
    @Override
    public ImmutableSortedSet<E> headSet(final E e) {
        return this.headSet(e, false);
    }
    
    @Override
    public ImmutableSortedSet<E> headSet(final E e, final boolean b) {
        return this.headSetImpl(Preconditions.checkNotNull(e), b);
    }
    
    abstract ImmutableSortedSet<E> headSetImpl(final E p0, final boolean p1);
    
    @Override
    public E higher(final E e) {
        return Iterables.getFirst((Iterable<? extends E>)this.tailSet(e, false), (E)null);
    }
    
    @Override
    public abstract UnmodifiableIterator<E> iterator();
    
    @Override
    public E lower(final E e) {
        return Iterables.getFirst((Iterable<? extends E>)this.headSet(e, false).descendingSet(), (E)null);
    }
    
    @Override
    public final E pollFirst() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final E pollLast() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ImmutableSortedSet<E> subSet(final E e, final E e2) {
        return this.subSet(e, true, e2, false);
    }
    
    @Override
    public ImmutableSortedSet<E> subSet(final E e, final boolean b, final E e2, final boolean b2) {
        Preconditions.checkNotNull(e);
        Preconditions.checkNotNull(e2);
        Preconditions.checkArgument(this.comparator.compare((Object)e, (Object)e2) <= 0);
        return this.subSetImpl(e, b, e2, b2);
    }
    
    abstract ImmutableSortedSet<E> subSetImpl(final E p0, final boolean p1, final E p2, final boolean p3);
    
    @Override
    public ImmutableSortedSet<E> tailSet(final E e) {
        return this.tailSet(e, true);
    }
    
    @Override
    public ImmutableSortedSet<E> tailSet(final E e, final boolean b) {
        return this.tailSetImpl(Preconditions.checkNotNull(e), b);
    }
    
    abstract ImmutableSortedSet<E> tailSetImpl(final E p0, final boolean p1);
}
