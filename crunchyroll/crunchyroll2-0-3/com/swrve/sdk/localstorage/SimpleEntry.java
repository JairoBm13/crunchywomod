// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.localstorage;

import java.util.Map;

public class SimpleEntry<K, V> implements Entry<K, V>
{
    private K key;
    private V value;
    
    public SimpleEntry(final K key, final V value) {
        this.key = key;
        this.value = value;
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
    public V setValue(final V value) {
        return this.value = value;
    }
}
