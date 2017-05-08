// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.util.Iterator;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.io.Serializable;
import java.util.AbstractSet;
import java.util.LinkedHashSet;
import java.util.HashSet;

public final class $Sets
{
    public static <E> HashSet<E> newHashSet() {
        return new HashSet<E>();
    }
    
    public static <E> LinkedHashSet<E> newLinkedHashSet() {
        return new LinkedHashSet<E>();
    }
    
    private static class SetFromMap<E> extends AbstractSet<E> implements Serializable, Set<E>
    {
        private final Map<E, Boolean> m;
        private transient Set<E> s;
        
        @Override
        public boolean add(final E e) {
            return this.m.put(e, Boolean.TRUE) == null;
        }
        
        @Override
        public void clear() {
            this.m.clear();
        }
        
        @Override
        public boolean contains(final Object o) {
            return this.m.containsKey(o);
        }
        
        @Override
        public boolean containsAll(final Collection<?> collection) {
            return this.s.containsAll(collection);
        }
        
        @Override
        public boolean equals(@$Nullable final Object o) {
            return this == o || this.s.equals(o);
        }
        
        @Override
        public int hashCode() {
            return this.s.hashCode();
        }
        
        @Override
        public boolean isEmpty() {
            return this.m.isEmpty();
        }
        
        @Override
        public Iterator<E> iterator() {
            return this.s.iterator();
        }
        
        @Override
        public boolean remove(final Object o) {
            return this.m.remove(o) != null;
        }
        
        @Override
        public boolean removeAll(final Collection<?> collection) {
            return this.s.removeAll(collection);
        }
        
        @Override
        public boolean retainAll(final Collection<?> collection) {
            return this.s.retainAll(collection);
        }
        
        @Override
        public int size() {
            return this.m.size();
        }
        
        @Override
        public Object[] toArray() {
            return this.s.toArray();
        }
        
        @Override
        public <T> T[] toArray(final T[] array) {
            return this.s.toArray(array);
        }
        
        @Override
        public String toString() {
            return this.s.toString();
        }
    }
}
