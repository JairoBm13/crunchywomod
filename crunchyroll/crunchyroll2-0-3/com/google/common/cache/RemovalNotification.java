// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.cache;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Map;

public final class RemovalNotification<K, V> implements Entry<K, V>
{
    private final RemovalCause cause;
    private final K key;
    private final V value;
    
    RemovalNotification(final K key, final V value, final RemovalCause removalCause) {
        this.key = key;
        this.value = value;
        this.cause = Preconditions.checkNotNull(removalCause);
    }
    
    @Override
    public boolean equals(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof Entry) {
            final Entry entry = (Entry)o;
            b2 = b;
            if (Objects.equal(this.getKey(), entry.getKey())) {
                b2 = b;
                if (Objects.equal(this.getValue(), entry.getValue())) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    @Override
    public K getKey() {
        return this.key;
    }
    
    @Override
    public V getValue() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        final Object key = this.getKey();
        final Object value = this.getValue();
        int hashCode2;
        if (key == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = key.hashCode();
        }
        if (value != null) {
            hashCode = value.hashCode();
        }
        return hashCode ^ hashCode2;
    }
    
    @Override
    public final V setValue(final V v) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return this.getKey() + "=" + this.getValue();
    }
}
