// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

import java.util.Map;

abstract class ImmutableMapEntrySet<K, V> extends ImmutableSet<Map.Entry<K, V>>
{
    @Override
    public boolean contains(final Object o) {
        boolean b2;
        final boolean b = b2 = false;
        if (o instanceof Map.Entry) {
            final Map.Entry entry = (Map.Entry)o;
            final V value = this.map().get(entry.getKey());
            b2 = b;
            if (value != null) {
                b2 = b;
                if (value.equals(entry.getValue())) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    @Override
    boolean isPartialView() {
        return this.map().isPartialView();
    }
    
    abstract ImmutableMap<K, V> map();
    
    @Override
    public int size() {
        return this.map().size();
    }
}
