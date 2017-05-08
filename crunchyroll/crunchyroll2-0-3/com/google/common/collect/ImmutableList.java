// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.ListIterator;
import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.RandomAccess;
import java.util.List;

public abstract class ImmutableList<E> extends ImmutableCollection<E> implements List<E>, RandomAccess
{
    static <E> ImmutableList<E> asImmutableList(final Object[] array) {
        switch (array.length) {
            default: {
                return (ImmutableList<E>)construct(array);
            }
            case 0: {
                return of();
            }
            case 1: {
                return new SingletonImmutableList<E>((E)array[0]);
            }
        }
    }
    
    public static <E> Builder<E> builder() {
        return new Builder<E>();
    }
    
    private static <E> ImmutableList<E> construct(final Object... array) {
        for (int i = 0; i < array.length; ++i) {
            ObjectArrays.checkElementNotNull(array[i], i);
        }
        return new RegularImmutableList<E>(array);
    }
    
    private static <E> ImmutableList<E> copyFromCollection(final Collection<? extends E> collection) {
        return asImmutableList(collection.toArray());
    }
    
    public static <E> ImmutableList<E> copyOf(final Iterable<? extends E> iterable) {
        Preconditions.checkNotNull(iterable);
        if (iterable instanceof Collection) {
            return copyOf((Collection<? extends E>)Collections2.cast((Iterable<? extends E>)iterable));
        }
        return copyOf((Iterator<? extends E>)iterable.iterator());
    }
    
    public static <E> ImmutableList<E> copyOf(final Collection<? extends E> collection) {
        if (collection instanceof ImmutableCollection) {
            List<E> list2;
            final ImmutableList<? extends E> list = (ImmutableList<? extends E>)(list2 = (List<E>)((ImmutableCollection<? extends E>)collection).asList());
            if (list.isPartialView()) {
                list2 = (List<E>)copyFromCollection((Collection<?>)list);
            }
            return (ImmutableList<E>)list2;
        }
        return (ImmutableList<E>)copyFromCollection((Collection<?>)collection);
    }
    
    public static <E> ImmutableList<E> copyOf(final Iterator<? extends E> iterator) {
        if (!iterator.hasNext()) {
            return of();
        }
        final E next = (E)iterator.next();
        if (!iterator.hasNext()) {
            return of(next);
        }
        return new Builder<E>().add(next).addAll(iterator).build();
    }
    
    public static <E> ImmutableList<E> copyOf(final E[] array) {
        switch (array.length) {
            default: {
                return (ImmutableList<E>)construct((Object[])array.clone());
            }
            case 0: {
                return of();
            }
            case 1: {
                return new SingletonImmutableList<E>(array[0]);
            }
        }
    }
    
    public static <E> ImmutableList<E> of() {
        return (ImmutableList<E>)EmptyImmutableList.INSTANCE;
    }
    
    public static <E> ImmutableList<E> of(final E e) {
        return new SingletonImmutableList<E>(e);
    }
    
    public static <E> ImmutableList<E> of(final E e, final E e2) {
        return construct(e, e2);
    }
    
    @Override
    public final void add(final int n, final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final boolean addAll(final int n, final Collection<? extends E> collection) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ImmutableList<E> asList() {
        return this;
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.indexOf(o) >= 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        return Lists.equalsImpl(this, o);
    }
    
    @Override
    public int hashCode() {
        return Lists.hashCodeImpl(this);
    }
    
    @Override
    public int indexOf(final Object o) {
        if (o == null) {
            return -1;
        }
        return Lists.indexOfImpl(this, o);
    }
    
    @Override
    public UnmodifiableIterator<E> iterator() {
        return this.listIterator();
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        if (o == null) {
            return -1;
        }
        return Lists.lastIndexOfImpl(this, o);
    }
    
    @Override
    public UnmodifiableListIterator<E> listIterator() {
        return this.listIterator(0);
    }
    
    @Override
    public UnmodifiableListIterator<E> listIterator(final int n) {
        return new AbstractIndexedListIterator<E>(this.size(), n) {
            @Override
            protected E get(final int n) {
                return ImmutableList.this.get(n);
            }
        };
    }
    
    @Override
    public final E remove(final int n) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public final E set(final int n, final E e) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ImmutableList<E> subList(final int n, final int n2) {
        Preconditions.checkPositionIndexes(n, n2, this.size());
        switch (n2 - n) {
            default: {
                return this.subListUnchecked(n, n2);
            }
            case 0: {
                return of();
            }
            case 1: {
                return of(this.get(n));
            }
        }
    }
    
    ImmutableList<E> subListUnchecked(final int n, final int n2) {
        return new SubList(n, n2 - n);
    }
    
    public static final class Builder<E> extends ImmutableCollection.Builder<E>
    {
        private Object[] contents;
        private int size;
        
        public Builder() {
            this(4);
        }
        
        Builder(final int n) {
            this.contents = new Object[n];
            this.size = 0;
        }
        
        public Builder<E> add(final E e) {
            Preconditions.checkNotNull(e);
            this.expandFor(1);
            this.contents[this.size++] = e;
            return this;
        }
        
        public Builder<E> addAll(final Iterable<? extends E> iterable) {
            if (iterable instanceof Collection) {
                this.expandFor(((Collection<? extends E>)iterable).size());
            }
            super.addAll(iterable);
            return this;
        }
        
        public Builder<E> addAll(final Iterator<? extends E> iterator) {
            super.addAll(iterator);
            return this;
        }
        
        public ImmutableList<E> build() {
            switch (this.size) {
                default: {
                    if (this.size == this.contents.length) {
                        return new RegularImmutableList<E>(this.contents);
                    }
                    return new RegularImmutableList<E>(ObjectArrays.arraysCopyOf(this.contents, this.size));
                }
                case 0: {
                    return ImmutableList.of();
                }
                case 1: {
                    return ImmutableList.of(this.contents[0]);
                }
            }
        }
        
        Builder<E> expandFor(int n) {
            n += this.size;
            if (this.contents.length < n) {
                this.contents = ObjectArrays.arraysCopyOf(this.contents, ImmutableCollection.Builder.expandedCapacity(this.contents.length, n));
            }
            return this;
        }
    }
    
    class SubList extends ImmutableList<E>
    {
        final transient int length;
        final transient int offset;
        
        SubList(final int offset, final int length) {
            this.offset = offset;
            this.length = length;
        }
        
        @Override
        public E get(final int n) {
            Preconditions.checkElementIndex(n, this.length);
            return ImmutableList.this.get(this.offset + n);
        }
        
        @Override
        boolean isPartialView() {
            return true;
        }
        
        @Override
        public int size() {
            return this.length;
        }
        
        @Override
        public ImmutableList<E> subList(final int n, final int n2) {
            Preconditions.checkPositionIndexes(n, n2, this.length);
            return ImmutableList.this.subList(this.offset + n, this.offset + n2);
        }
    }
}
