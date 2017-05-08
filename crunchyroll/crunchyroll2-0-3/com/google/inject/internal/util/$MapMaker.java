// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal.util;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.TimerTask;
import java.lang.ref.WeakReference;
import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

public final class $MapMaker
{
    private static final ValueReference<Object, Object> COMPUTING;
    private final $CustomConcurrentHashMap.Builder builder;
    private long expirationNanos;
    private Strength keyStrength;
    private boolean useCustomMap;
    private Strength valueStrength;
    
    static {
        COMPUTING = (ValueReference)new ValueReference<Object, Object>() {
            @Override
            public ValueReference<Object, Object> copyFor(final ReferenceEntry<Object, Object> referenceEntry) {
                throw new AssertionError();
            }
            
            @Override
            public Object get() {
                return null;
            }
            
            @Override
            public Object waitForValue() {
                throw new AssertionError();
            }
        };
    }
    
    public $MapMaker() {
        this.keyStrength = Strength.STRONG;
        this.valueStrength = Strength.STRONG;
        this.expirationNanos = 0L;
        this.builder = new $CustomConcurrentHashMap.Builder();
    }
    
    private static <K, V> ValueReference<K, V> computing() {
        return (ValueReference<K, V>)$MapMaker.COMPUTING;
    }
    
    private $MapMaker setKeyStrength(final Strength keyStrength) {
        if (this.keyStrength != Strength.STRONG) {
            throw new IllegalStateException("Key strength was already set to " + this.keyStrength + ".");
        }
        this.keyStrength = keyStrength;
        this.useCustomMap = true;
        return this;
    }
    
    private $MapMaker setValueStrength(final Strength valueStrength) {
        if (this.valueStrength != Strength.STRONG) {
            throw new IllegalStateException("Value strength was already set to " + this.valueStrength + ".");
        }
        this.valueStrength = valueStrength;
        this.useCustomMap = true;
        return this;
    }
    
    public <K, V> ConcurrentMap<K, V> makeComputingMap(final $Function<? super K, ? extends V> $Function) {
        return (ConcurrentMap<K, V>)new StrategyImpl(this, ($Function<? super K, ? extends V>)$Function).map;
    }
    
    public $MapMaker weakKeys() {
        return this.setKeyStrength(Strength.WEAK);
    }
    
    public $MapMaker weakValues() {
        return this.setValueStrength(Strength.WEAK);
    }
    
    private static class ComputationExceptionReference<K, V> implements ValueReference<K, V>
    {
        final Throwable t;
        
        ComputationExceptionReference(final Throwable t) {
            this.t = t;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceEntry<K, V> referenceEntry) {
            return this;
        }
        
        @Override
        public V get() {
            return null;
        }
        
        @Override
        public V waitForValue() {
            throw new $AsynchronousComputationException(this.t);
        }
    }
    
    private static class LinkedSoftEntry<K, V> extends SoftEntry<K, V>
    {
        final ReferenceEntry<K, V> next;
        
        LinkedSoftEntry(final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, final K k, final int n, final ReferenceEntry<K, V> next) {
            super(internals, k, n);
            this.next = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }
    
    private static class LinkedStrongEntry<K, V> extends StrongEntry<K, V>
    {
        final ReferenceEntry<K, V> next;
        
        LinkedStrongEntry(final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, final K k, final int n, final ReferenceEntry<K, V> next) {
            super(internals, k, n);
            this.next = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }
    
    private static class LinkedWeakEntry<K, V> extends WeakEntry<K, V>
    {
        final ReferenceEntry<K, V> next;
        
        LinkedWeakEntry(final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, final K k, final int n, final ReferenceEntry<K, V> next) {
            super(internals, k, n);
            this.next = next;
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return this.next;
        }
    }
    
    private static class NullOutputExceptionReference<K, V> implements ValueReference<K, V>
    {
        final String message;
        
        NullOutputExceptionReference(final String message) {
            this.message = message;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceEntry<K, V> referenceEntry) {
            return this;
        }
        
        @Override
        public V get() {
            return null;
        }
        
        @Override
        public V waitForValue() {
            throw new $NullOutputException(this.message);
        }
    }
    
    private static class QueueHolder
    {
        static final $FinalizableReferenceQueue queue;
        
        static {
            queue = new $FinalizableReferenceQueue();
        }
    }
    
    private interface ReferenceEntry<K, V>
    {
        int getHash();
        
        K getKey();
        
        ReferenceEntry<K, V> getNext();
        
        ValueReference<K, V> getValueReference();
        
        void setValueReference(final ValueReference<K, V> p0);
        
        void valueReclaimed();
    }
    
    private static class SoftEntry<K, V> extends $FinalizableSoftReference<K> implements ReferenceEntry<K, V>
    {
        final int hash;
        final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        volatile ValueReference<K, V> valueReference;
        
        SoftEntry(final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, final K k, final int hash) {
            super(k, QueueHolder.queue);
            this.valueReference = (ValueReference<K, V>)computing();
            this.internals = internals;
            this.hash = hash;
        }
        
        @Override
        public void finalizeReferent() {
            this.internals.removeEntry(this);
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public K getKey() {
            return this.get();
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return null;
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }
        
        @Override
        public void valueReclaimed() {
            this.internals.removeEntry(this, null);
        }
    }
    
    private static class SoftValueReference<K, V> extends $FinalizableSoftReference<V> implements ValueReference<K, V>
    {
        final ReferenceEntry<K, V> entry;
        
        SoftValueReference(final V v, final ReferenceEntry<K, V> entry) {
            super(v, QueueHolder.queue);
            this.entry = entry;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceEntry<K, V> referenceEntry) {
            return new SoftValueReference(this.get(), (ReferenceEntry<Object, Object>)referenceEntry);
        }
        
        @Override
        public void finalizeReferent() {
            this.entry.valueReclaimed();
        }
        
        @Override
        public V waitForValue() {
            return this.get();
        }
    }
    
    private static class StrategyImpl<K, V> implements ComputingStrategy<K, V, ReferenceEntry<K, V>>, Serializable
    {
        final long expirationNanos;
        Internals<K, V, ReferenceEntry<K, V>> internals;
        final Strength keyStrength;
        final ConcurrentMap<K, V> map;
        final Strength valueStrength;
        
        StrategyImpl(final $MapMaker $MapMaker, final $Function<? super K, ? extends V> $Function) {
            this.keyStrength = $MapMaker.keyStrength;
            this.valueStrength = $MapMaker.valueStrength;
            this.expirationNanos = $MapMaker.expirationNanos;
            this.map = $MapMaker.builder.buildComputingMap((ComputingStrategy<K, V, Object>)this, $Function);
        }
        
        public V compute(final K k, final ReferenceEntry<K, V> referenceEntry, final $Function<? super K, ? extends V> $Function) {
            V apply;
            try {
                apply = (V)$Function.apply(k);
                if (apply == null) {
                    final String string = $Function + " returned null for key " + k + ".";
                    this.setValueReference(referenceEntry, new NullOutputExceptionReference<K, V>(string));
                    throw new $NullOutputException(string);
                }
            }
            catch (Throwable t) {
                this.setValueReference(referenceEntry, new ComputationExceptionReference<K, V>(t));
                throw new $ComputationException(t);
            }
            this.setValue(referenceEntry, apply);
            return apply;
        }
        
        public ReferenceEntry<K, V> copyEntry(final K k, final ReferenceEntry<K, V> referenceEntry, final ReferenceEntry<K, V> referenceEntry2) {
            final ValueReference<K, V> valueReference = referenceEntry.getValueReference();
            if (valueReference == $MapMaker.COMPUTING) {
                final ReferenceEntry<K, V> entry = this.newEntry(k, referenceEntry.getHash(), referenceEntry2);
                entry.setValueReference(new FutureValueReference(referenceEntry, entry));
                return entry;
            }
            final ReferenceEntry<K, V> entry2 = this.newEntry(k, referenceEntry.getHash(), referenceEntry2);
            entry2.setValueReference(valueReference.copyFor(entry2));
            return entry2;
        }
        
        @Override
        public boolean equalKeys(final K k, final Object o) {
            return this.keyStrength.equal(k, o);
        }
        
        @Override
        public boolean equalValues(final V v, final Object o) {
            return this.valueStrength.equal(v, o);
        }
        
        public int getHash(final ReferenceEntry referenceEntry) {
            return referenceEntry.getHash();
        }
        
        public K getKey(final ReferenceEntry<K, V> referenceEntry) {
            return referenceEntry.getKey();
        }
        
        public ReferenceEntry<K, V> getNext(final ReferenceEntry<K, V> referenceEntry) {
            return referenceEntry.getNext();
        }
        
        public V getValue(final ReferenceEntry<K, V> referenceEntry) {
            return referenceEntry.getValueReference().get();
        }
        
        @Override
        public int hashKey(final Object o) {
            return this.keyStrength.hash(o);
        }
        
        public ReferenceEntry<K, V> newEntry(final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
            return this.keyStrength.newEntry(this.internals, k, n, referenceEntry);
        }
        
        void scheduleRemoval(final K k, final V v) {
            $ExpirationTimer.instance.schedule(new TimerTask() {
                final /* synthetic */ WeakReference val$keyReference = new WeakReference((T)k);
                final /* synthetic */ WeakReference val$valueReference = new WeakReference((T)v);
                
                @Override
                public void run() {
                    final Object value = this.val$keyReference.get();
                    if (value != null) {
                        StrategyImpl.this.map.remove(value, this.val$valueReference.get());
                    }
                }
            }, TimeUnit.NANOSECONDS.toMillis(this.expirationNanos));
        }
        
        @Override
        public void setInternals(final Internals<K, V, ReferenceEntry<K, V>> internals) {
            this.internals = internals;
        }
        
        public void setValue(final ReferenceEntry<K, V> referenceEntry, final V v) {
            this.setValueReference(referenceEntry, this.valueStrength.referenceValue(referenceEntry, v));
            if (this.expirationNanos > 0L) {
                this.scheduleRemoval(referenceEntry.getKey(), v);
            }
        }
        
        void setValueReference(final ReferenceEntry<K, V> referenceEntry, final ValueReference<K, V> valueReference) {
            int n;
            if (referenceEntry.getValueReference() == $MapMaker.COMPUTING) {
                n = 1;
            }
            else {
                n = 0;
            }
            referenceEntry.setValueReference(valueReference);
            if (n != 0) {
                synchronized (referenceEntry) {
                    referenceEntry.notifyAll();
                }
            }
        }
        
        public V waitForValue(final ReferenceEntry<K, V> referenceEntry) throws InterruptedException {
            final ValueReference<K, V> valueReference;
            if ((valueReference = referenceEntry.getValueReference()) == $MapMaker.COMPUTING) {
                synchronized (referenceEntry) {
                    while (referenceEntry.getValueReference() == $MapMaker.COMPUTING) {
                        referenceEntry.wait();
                    }
                }
            }
            // monitorexit(referenceEntry)
            return valueReference.waitForValue();
        }
        
        private static class Fields
        {
            static final Field expirationNanos;
            static final Field internals;
            static final Field keyStrength;
            static final Field map;
            static final Field valueStrength;
            
            static {
                keyStrength = findField("keyStrength");
                valueStrength = findField("valueStrength");
                expirationNanos = findField("expirationNanos");
                internals = findField("internals");
                map = findField("map");
            }
            
            static Field findField(final String s) {
                try {
                    final Field declaredField = StrategyImpl.class.getDeclaredField(s);
                    declaredField.setAccessible(true);
                    return declaredField;
                }
                catch (NoSuchFieldException ex) {
                    throw new AssertionError((Object)ex);
                }
            }
        }
        
        private class FutureValueReference implements ValueReference<K, V>
        {
            final ReferenceEntry<K, V> newEntry;
            final ReferenceEntry<K, V> original;
            
            FutureValueReference(final ReferenceEntry<K, V> original, final ReferenceEntry<K, V> newEntry) {
                this.original = original;
                this.newEntry = newEntry;
            }
            
            @Override
            public ValueReference<K, V> copyFor(final ReferenceEntry<K, V> referenceEntry) {
                return new FutureValueReference(this.original, referenceEntry);
            }
            
            @Override
            public V get() {
                try {
                    final V value = this.original.getValueReference().get();
                    if (!true) {
                        this.removeEntry();
                    }
                    return value;
                }
                finally {
                    if (!false) {
                        this.removeEntry();
                    }
                }
            }
            
            void removeEntry() {
                StrategyImpl.this.internals.removeEntry(this.newEntry);
            }
            
            @Override
            public V waitForValue() throws InterruptedException {
                try {
                    final Object waitForValue = StrategyImpl.this.waitForValue(this.original);
                    if (!true) {
                        this.removeEntry();
                    }
                    return (V)waitForValue;
                }
                finally {
                    if (!false) {
                        this.removeEntry();
                    }
                }
            }
        }
    }
    
    private enum Strength
    {
        SOFT {
            @Override
            boolean equal(final Object o, final Object o2) {
                return o == o2;
            }
            
            @Override
            int hash(final Object o) {
                return System.identityHashCode(o);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                if (referenceEntry == null) {
                    return new SoftEntry<K, V>(internals, k, n);
                }
                return new LinkedSoftEntry<K, V>(internals, k, n, referenceEntry);
            }
            
            @Override
             <K, V> ValueReference<K, V> referenceValue(final ReferenceEntry<K, V> referenceEntry, final V v) {
                return new SoftValueReference<K, V>(v, referenceEntry);
            }
        }, 
        STRONG {
            @Override
            boolean equal(final Object o, final Object o2) {
                return o.equals(o2);
            }
            
            @Override
            int hash(final Object o) {
                return o.hashCode();
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                if (referenceEntry == null) {
                    return new StrongEntry<K, V>(internals, k, n);
                }
                return new LinkedStrongEntry<K, V>(internals, k, n, referenceEntry);
            }
            
            @Override
             <K, V> ValueReference<K, V> referenceValue(final ReferenceEntry<K, V> referenceEntry, final V v) {
                return new StrongValueReference<K, V>(v);
            }
        }, 
        WEAK {
            @Override
            boolean equal(final Object o, final Object o2) {
                return o == o2;
            }
            
            @Override
            int hash(final Object o) {
                return System.identityHashCode(o);
            }
            
            @Override
             <K, V> ReferenceEntry<K, V> newEntry(final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, final K k, final int n, final ReferenceEntry<K, V> referenceEntry) {
                if (referenceEntry == null) {
                    return new WeakEntry<K, V>(internals, k, n);
                }
                return new LinkedWeakEntry<K, V>(internals, k, n, referenceEntry);
            }
            
            @Override
             <K, V> ValueReference<K, V> referenceValue(final ReferenceEntry<K, V> referenceEntry, final V v) {
                return new WeakValueReference<K, V>(v, referenceEntry);
            }
        };
        
        abstract boolean equal(final Object p0, final Object p1);
        
        abstract int hash(final Object p0);
        
        abstract <K, V> ReferenceEntry<K, V> newEntry(final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> p0, final K p1, final int p2, final ReferenceEntry<K, V> p3);
        
        abstract <K, V> ValueReference<K, V> referenceValue(final ReferenceEntry<K, V> p0, final V p1);
    }
    
    private static class StrongEntry<K, V> implements ReferenceEntry<K, V>
    {
        final int hash;
        final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        final K key;
        volatile ValueReference<K, V> valueReference;
        
        StrongEntry(final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, final K key, final int hash) {
            this.valueReference = (ValueReference<K, V>)computing();
            this.internals = internals;
            this.key = key;
            this.hash = hash;
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public K getKey() {
            return this.key;
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return null;
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }
        
        @Override
        public void valueReclaimed() {
            this.internals.removeEntry(this, null);
        }
    }
    
    private static class StrongValueReference<K, V> implements ValueReference<K, V>
    {
        final V referent;
        
        StrongValueReference(final V referent) {
            this.referent = referent;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceEntry<K, V> referenceEntry) {
            return this;
        }
        
        @Override
        public V get() {
            return this.referent;
        }
        
        @Override
        public V waitForValue() {
            return this.get();
        }
    }
    
    private interface ValueReference<K, V>
    {
        ValueReference<K, V> copyFor(final ReferenceEntry<K, V> p0);
        
        V get();
        
        V waitForValue() throws InterruptedException;
    }
    
    private static class WeakEntry<K, V> extends $FinalizableWeakReference<K> implements ReferenceEntry<K, V>
    {
        final int hash;
        final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals;
        volatile ValueReference<K, V> valueReference;
        
        WeakEntry(final $CustomConcurrentHashMap.Internals<K, V, ReferenceEntry<K, V>> internals, final K k, final int hash) {
            super(k, QueueHolder.queue);
            this.valueReference = (ValueReference<K, V>)computing();
            this.internals = internals;
            this.hash = hash;
        }
        
        @Override
        public void finalizeReferent() {
            this.internals.removeEntry(this);
        }
        
        @Override
        public int getHash() {
            return this.hash;
        }
        
        @Override
        public K getKey() {
            return this.get();
        }
        
        @Override
        public ReferenceEntry<K, V> getNext() {
            return null;
        }
        
        @Override
        public ValueReference<K, V> getValueReference() {
            return this.valueReference;
        }
        
        @Override
        public void setValueReference(final ValueReference<K, V> valueReference) {
            this.valueReference = valueReference;
        }
        
        @Override
        public void valueReclaimed() {
            this.internals.removeEntry(this, null);
        }
    }
    
    private static class WeakValueReference<K, V> extends $FinalizableWeakReference<V> implements ValueReference<K, V>
    {
        final ReferenceEntry<K, V> entry;
        
        WeakValueReference(final V v, final ReferenceEntry<K, V> entry) {
            super(v, QueueHolder.queue);
            this.entry = entry;
        }
        
        @Override
        public ValueReference<K, V> copyFor(final ReferenceEntry<K, V> referenceEntry) {
            return new WeakValueReference(this.get(), (ReferenceEntry<Object, Object>)referenceEntry);
        }
        
        @Override
        public void finalizeReferent() {
            this.entry.valueReclaimed();
        }
        
        @Override
        public V waitForValue() throws InterruptedException {
            return this.get();
        }
    }
}
