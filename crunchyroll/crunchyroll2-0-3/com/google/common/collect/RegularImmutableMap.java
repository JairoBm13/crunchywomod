// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Iterator;
import com.google.common.base.Preconditions;
import java.util.Map;

final class RegularImmutableMap<K, V> extends ImmutableMap<K, V>
{
    private final transient LinkedEntry<K, V>[] entries;
    private final transient int keySetHashCode;
    private final transient int mask;
    private final transient LinkedEntry<K, V>[] table;
    
    RegularImmutableMap(final Entry<?, ?>... array) {
        final int length = array.length;
        this.entries = this.createEntryArray(length);
        final int chooseTableSize = chooseTableSize(length);
        this.table = this.createEntryArray(chooseTableSize);
        this.mask = chooseTableSize - 1;
        int keySetHashCode = 0;
        for (int i = 0; i < length; ++i) {
            final Entry<?, ?> entry = array[i];
            final Object key = entry.getKey();
            final int hashCode = key.hashCode();
            keySetHashCode += hashCode;
            final int n = Hashing.smear(hashCode) & this.mask;
            LinkedEntry<K, ?> next = this.table[n];
            final LinkedEntry<K, ?> linkedEntry = newLinkedEntry((K)key, entry.getValue(), next);
            this.table[n] = (LinkedEntry<K, V>)linkedEntry;
            this.entries[i] = (LinkedEntry<K, V>)linkedEntry;
            while (next != null) {
                Preconditions.checkArgument(!key.equals(next.getKey()), "duplicate key: %s", key);
                next = next.next();
            }
        }
        this.keySetHashCode = keySetHashCode;
    }
    
    private static int chooseTableSize(final int n) {
        int highestOneBit;
        final int n2 = highestOneBit = Integer.highestOneBit(n);
        if (n / n2 > 1.2) {
            highestOneBit = n2 << 1;
            Preconditions.checkArgument(highestOneBit > 0, "table too large: %s", n);
        }
        return highestOneBit;
    }
    
    private LinkedEntry<K, V>[] createEntryArray(final int n) {
        return (LinkedEntry<K, V>[])new LinkedEntry[n];
    }
    
    private static <K, V> LinkedEntry<K, V> newLinkedEntry(final K k, final V v, final LinkedEntry<K, V> linkedEntry) {
        if (linkedEntry == null) {
            return (LinkedEntry<K, V>)new TerminalEntry(k, v);
        }
        return (LinkedEntry<K, V>)new NonTerminalEntry(k, v, (LinkedEntry<Object, Object>)linkedEntry);
    }
    
    @Override
    public boolean containsValue(final Object o) {
        if (o != null) {
            final LinkedEntry<K, V>[] entries = this.entries;
            for (int length = entries.length, i = 0; i < length; ++i) {
                if (entries[i].getValue().equals(o)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    ImmutableSet<Entry<K, V>> createEntrySet() {
        return (ImmutableSet<Entry<K, V>>)new EntrySet();
    }
    
    @Override
    ImmutableSet<K> createKeySet() {
        return (ImmutableSet<K>)new ImmutableMapKeySet<K, V>(this.entrySet(), this.keySetHashCode) {
            @Override
            ImmutableMap<K, V> map() {
                return (ImmutableMap<K, V>)RegularImmutableMap.this;
            }
        };
    }
    
    @Override
    public V get(final Object o) {
        if (o != null) {
            for (Object next = this.table[Hashing.smear(o.hashCode()) & this.mask]; next != null; next = ((LinkedEntry<Object, Object>)next).next()) {
                if (o.equals(((Map.Entry<Object, V>)next).getKey())) {
                    return ((Map.Entry<Object, V>)next).getValue();
                }
            }
        }
        return null;
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
    public int size() {
        return this.entries.length;
    }
    
    @Override
    public String toString() {
        final StringBuilder append = Collections2.newStringBuilderForCollection(this.size()).append('{');
        Collections2.STANDARD_JOINER.appendTo(append, this.entries);
        return append.append('}').toString();
    }
    
    private class EntrySet extends ImmutableMapEntrySet<K, V>
    {
        @Override
        ImmutableList<Entry<K, V>> createAsList() {
            return new RegularImmutableAsList<Entry<K, V>>((ImmutableCollection<Entry<K, V>>)this, RegularImmutableMap.this.entries);
        }
        
        @Override
        public UnmodifiableIterator<Entry<K, V>> iterator() {
            return (UnmodifiableIterator<Entry<K, V>>)this.asList().iterator();
        }
        
        @Override
        ImmutableMap<K, V> map() {
            return (ImmutableMap<K, V>)RegularImmutableMap.this;
        }
    }
    
    private interface LinkedEntry<K, V> extends Entry<K, V>
    {
        LinkedEntry<K, V> next();
    }
    
    private static final class NonTerminalEntry<K, V> extends ImmutableEntry<K, V> implements LinkedEntry<K, V>
    {
        final LinkedEntry<K, V> next;
        
        NonTerminalEntry(final K k, final V v, final LinkedEntry<K, V> next) {
            super(k, v);
            this.next = next;
        }
        
        @Override
        public LinkedEntry<K, V> next() {
            return this.next;
        }
    }
    
    private static final class TerminalEntry<K, V> extends ImmutableEntry<K, V> implements LinkedEntry<K, V>
    {
        TerminalEntry(final K k, final V v) {
            super(k, v);
        }
        
        @Override
        public LinkedEntry<K, V> next() {
            return null;
        }
    }
}
