// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.databind.JavaType;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonSerializer;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ser.SerializerCache;

public final class ReadOnlyClassToSerializerMap
{
    protected SerializerCache.TypeKey _cacheKey;
    protected final JsonSerializerMap _map;
    
    private ReadOnlyClassToSerializerMap(final JsonSerializerMap map) {
        this._cacheKey = null;
        this._map = map;
    }
    
    public static ReadOnlyClassToSerializerMap from(final HashMap<SerializerCache.TypeKey, JsonSerializer<Object>> hashMap) {
        return new ReadOnlyClassToSerializerMap(new JsonSerializerMap(hashMap));
    }
    
    public ReadOnlyClassToSerializerMap instance() {
        return new ReadOnlyClassToSerializerMap(this._map);
    }
    
    public JsonSerializer<Object> typedValueSerializer(final JavaType javaType) {
        if (this._cacheKey == null) {
            this._cacheKey = new SerializerCache.TypeKey(javaType, true);
        }
        else {
            this._cacheKey.resetTyped(javaType);
        }
        return this._map.find(this._cacheKey);
    }
    
    public JsonSerializer<Object> typedValueSerializer(final Class<?> clazz) {
        if (this._cacheKey == null) {
            this._cacheKey = new SerializerCache.TypeKey(clazz, true);
        }
        else {
            this._cacheKey.resetTyped(clazz);
        }
        return this._map.find(this._cacheKey);
    }
    
    public JsonSerializer<Object> untypedValueSerializer(final JavaType javaType) {
        if (this._cacheKey == null) {
            this._cacheKey = new SerializerCache.TypeKey(javaType, false);
        }
        else {
            this._cacheKey.resetUntyped(javaType);
        }
        return this._map.find(this._cacheKey);
    }
    
    public JsonSerializer<Object> untypedValueSerializer(final Class<?> clazz) {
        if (this._cacheKey == null) {
            this._cacheKey = new SerializerCache.TypeKey(clazz, false);
        }
        else {
            this._cacheKey.resetUntyped(clazz);
        }
        return this._map.find(this._cacheKey);
    }
}
