// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.util.Map;
import java.io.Serializable;
import java.util.LinkedHashMap;

public class LRUMap<K, V> extends LinkedHashMap<K, V> implements Serializable
{
    protected final int _maxEntries;
    
    public LRUMap(final int n, final int maxEntries) {
        super(n, 0.8f, true);
        this._maxEntries = maxEntries;
    }
    
    @Override
    protected boolean removeEldestEntry(final Map.Entry<K, V> entry) {
        return this.size() > this._maxEntries;
    }
}
