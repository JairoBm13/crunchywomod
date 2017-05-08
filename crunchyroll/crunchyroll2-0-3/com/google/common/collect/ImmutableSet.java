// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import java.util.Collection;
import java.util.Arrays;
import com.google.common.base.Preconditions;
import java.util.Set;

public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E>
{
    private static final int CUTOFF;
    
    static {
        CUTOFF = (int)Math.floor(7.516192768E8);
    }
    
    public static <E> Builder<E> builder() {
        return new Builder<E>();
    }
    
    static int chooseTableSize(final int n) {
        int n2 = 1073741824;
        if (n < ImmutableSet.CUTOFF) {
            int n3 = Integer.highestOneBit(n - 1) << 1;
            while (true) {
                n2 = n3;
                if (n3 * 0.7 >= n) {
                    break;
                }
                n3 <<= 1;
            }
        }
        else {
            Preconditions.checkArgument(n < 1073741824, (Object)"collection too large");
        }
        return n2;
    }
    
    private static <E> ImmutableSet<E> construct(final int n, Object... arraysCopy) {
        switch (n) {
            default: {
                final int chooseTableSize = chooseTableSize(n);
                final Object[] array = new Object[chooseTableSize];
                final int n2 = chooseTableSize - 1;
                int n3 = 0;
                int i = 0;
                int n4 = 0;
                while (i < n) {
                    final Object checkElementNotNull = ObjectArrays.checkElementNotNull(arraysCopy[i], i);
                    final int hashCode = checkElementNotNull.hashCode();
                    int smear = Hashing.smear(hashCode);
                    while (true) {
                        final int n5 = smear & n2;
                        final Object o = array[n5];
                        if (o == null) {
                            final int n6 = n4 + 1;
                            array[n5] = (arraysCopy[n4] = checkElementNotNull);
                            n3 += hashCode;
                            n4 = n6;
                            break;
                        }
                        if (o.equals(checkElementNotNull)) {
                            break;
                        }
                        ++smear;
                    }
                    ++i;
                }
                Arrays.fill(arraysCopy, n4, n, null);
                if (n4 == 1) {
                    return new SingletonImmutableSet<E>(arraysCopy[0], n3);
                }
                if (chooseTableSize != chooseTableSize(n4)) {
                    return (ImmutableSet<E>)construct(n4, arraysCopy);
                }
                if (n4 < arraysCopy.length) {
                    arraysCopy = ObjectArrays.arraysCopyOf(arraysCopy, n4);
                }
                return new RegularImmutableSet<E>(arraysCopy, n3, array, n2);
            }
            case 0: {
                return of();
            }
            case 1: {
                return of(arraysCopy[0]);
            }
        }
    }
    
    private static <E> ImmutableSet<E> copyFromCollection(final Collection<? extends E> collection) {
        final Object[] array = collection.toArray();
        switch (array.length) {
            default: {
                return (ImmutableSet<E>)construct(array.length, array);
            }
            case 0: {
                return of();
            }
            case 1: {
                return of(array[0]);
            }
        }
    }
    
    public static <E> ImmutableSet<E> copyOf(final Iterable<? extends E> iterable) {
        if (iterable instanceof Collection) {
            return copyOf((Collection<? extends E>)Collections2.cast((Iterable<? extends E>)iterable));
        }
        return copyOf((Iterator<? extends E>)iterable.iterator());
    }
    
    public static <E> ImmutableSet<E> copyOf(final Collection<? extends E> collection) {
        if (collection instanceof ImmutableSet && !(collection instanceof ImmutableSortedSet)) {
            final ImmutableSet<E> set = (ImmutableSet<E>)collection;
            if (!set.isPartialView()) {
                return set;
            }
        }
        return (ImmutableSet<E>)copyFromCollection((Collection<?>)collection);
    }
    
    public static <E> ImmutableSet<E> copyOf(final Iterator<? extends E> iterator) {
        if (!iterator.hasNext()) {
            return of();
        }
        final E next = (E)iterator.next();
        if (!iterator.hasNext()) {
            return of(next);
        }
        return new Builder<E>().add(next).addAll(iterator).build();
    }
    
    public static <E> ImmutableSet<E> of() {
        return (ImmutableSet<E>)EmptyImmutableSet.INSTANCE;
    }
    
    public static <E> ImmutableSet<E> of(final E e) {
        return new SingletonImmutableSet<E>(e);
    }
    
    public static <E> ImmutableSet<E> of(final E e, final E e2, final E e3, final E e4, final E e5, final E e6, final E... array) {
        final Object[] array2 = new Object[array.length + 6];
        array2[0] = e;
        array2[1] = e2;
        array2[2] = e3;
        array2[3] = e4;
        array2[4] = e5;
        array2[5] = e6;
        System.arraycopy(array, 0, array2, 6, array.length);
        return (ImmutableSet<E>)construct(array2.length, array2);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o == this || ((!(o instanceof ImmutableSet) || !this.isHashCodeFast() || !((ImmutableSet)o).isHashCodeFast() || this.hashCode() == o.hashCode()) && Sets.equalsImpl(this, o));
    }
    
    @Override
    public int hashCode() {
        return Sets.hashCodeImpl(this);
    }
    
    boolean isHashCodeFast() {
        return false;
    }
    
    @Override
    public abstract UnmodifiableIterator<E> iterator();
    
    abstract static class ArrayImmutableSet<E> extends ImmutableSet<E>
    {
        final transient Object[] elements;
        
        ArrayImmutableSet(final Object[] elements) {
            this.elements = elements;
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            if (collection != this) {
                if (!(collection instanceof ArrayImmutableSet)) {
                    return super.containsAll(collection);
                }
                if (collection.size() > this.size()) {
                    return false;
                }
                final Object[] elements = ((ArrayImmutableSet<?>)collection).elements;
                for (int length = elements.length, i = 0; i < length; ++i) {
                    if (!this.contains(elements[i])) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        @Override
        ImmutableList<E> createAsList() {
            return new RegularImmutableAsList<E>(this, this.elements);
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        boolean isPartialView() {
            return false;
        }
        
        @Override
        public UnmodifiableIterator<E> iterator() {
            return this.asList().iterator();
        }
        
        @Override
        public int size() {
            return this.elements.length;
        }
        
        @Override
        public Object[] toArray() {
            return this.asList().toArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.asList().toArray(array);
        }
    }
    
    public static class Builder<E> extends ImmutableCollection.Builder<E>
    {
        Object[] contents;
        int size;
        
        public Builder() {
            this(4);
        }
        
        Builder(final int n) {
            Preconditions.checkArgument(n >= 0, "capacity must be >= 0 but was %s", n);
            this.contents = new Object[n];
            this.size = 0;
        }
        
        public Builder<E> add(final E e) {
            this.expandFor(1);
            this.contents[this.size++] = Preconditions.checkNotNull(e);
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
        
        public ImmutableSet<E> build() {
            final ImmutableSet<Object> access$000 = construct(this.size, this.contents);
            this.size = access$000.size();
            return (ImmutableSet<E>)access$000;
        }
        
        Builder<E> expandFor(int n) {
            n += this.size;
            if (this.contents.length < n) {
                this.contents = ObjectArrays.arraysCopyOf(this.contents, ImmutableCollection.Builder.expandedCapacity(this.contents.length, n));
            }
            return this;
        }
    }
}
