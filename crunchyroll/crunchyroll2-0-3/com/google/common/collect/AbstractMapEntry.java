// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import com.google.common.base.Objects;
import java.util.Map;

abstract class AbstractMapEntry<K, V> implements Entry<K, V>
{
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
    public abstract K getKey();
    
    @Override
    public abstract V getValue();
    
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
    public V setValue(final V v) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String toString() {
        return this.getKey() + "=" + this.getValue();
    }
}
