// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.SerializerCache;
import java.util.Map;

public class JsonSerializerMap
{
    private final Bucket[] _buckets;
    private final int _size;
    
    public JsonSerializerMap(final Map<SerializerCache.TypeKey, JsonSerializer<Object>> map) {
        final int size = findSize(map.size());
        this._size = size;
        final Bucket[] buckets = new Bucket[size];
        for (final Map.Entry<SerializerCache.TypeKey, JsonSerializer<Object>> entry : map.entrySet()) {
            final SerializerCache.TypeKey typeKey = entry.getKey();
            final int n = typeKey.hashCode() & size - 1;
            buckets[n] = new Bucket(buckets[n], typeKey, entry.getValue());
        }
        this._buckets = buckets;
    }
    
    private static final int findSize(int n) {
        if (n <= 64) {
            n += n;
        }
        else {
            n += n >> 2;
        }
        int i;
        for (i = 8; i < n; i += i) {}
        return i;
    }
    
    public JsonSerializer<Object> find(final SerializerCache.TypeKey typeKey) {
        final Bucket bucket = this._buckets[typeKey.hashCode() & this._buckets.length - 1];
        if (bucket == null) {
            return null;
        }
        Bucket bucket2 = bucket;
        if (typeKey.equals(bucket.key)) {
            return bucket.value;
        }
        Bucket next;
        do {
            next = bucket2.next;
            if (next == null) {
                return null;
            }
            bucket2 = next;
        } while (!typeKey.equals(next.key));
        return next.value;
    }
    
    private static final class Bucket
    {
        public final SerializerCache.TypeKey key;
        public final Bucket next;
        public final JsonSerializer<Object> value;
        
        public Bucket(final Bucket next, final SerializerCache.TypeKey key, final JsonSerializer<Object> value) {
            this.next = next;
            this.key = key;
            this.value = value;
        }
    }
}
