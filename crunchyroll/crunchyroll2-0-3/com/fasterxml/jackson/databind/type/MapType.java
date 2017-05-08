// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

public final class MapType extends MapLikeType
{
    private MapType(final Class<?> clazz, final JavaType javaType, final JavaType javaType2, final Object o, final Object o2, final boolean b) {
        super(clazz, javaType, javaType2, o, o2, b);
    }
    
    public static MapType construct(final Class<?> clazz, final JavaType javaType, final JavaType javaType2) {
        return new MapType(clazz, javaType, javaType2, null, null, false);
    }
    
    @Override
    protected JavaType _narrow(final Class<?> clazz) {
        return new MapType(clazz, this._keyType, this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public JavaType narrowContentsBy(final Class<?> clazz) {
        if (clazz == this._valueType.getRawClass()) {
            return this;
        }
        return new MapType(this._class, this._keyType, this._valueType.narrowBy(clazz), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public JavaType narrowKey(final Class<?> clazz) {
        if (clazz == this._keyType.getRawClass()) {
            return this;
        }
        return new MapType(this._class, this._keyType.narrowBy(clazz), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public String toString() {
        return "[map type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + "]";
    }
    
    @Override
    public JavaType widenContentsBy(final Class<?> clazz) {
        if (clazz == this._valueType.getRawClass()) {
            return this;
        }
        return new MapType(this._class, this._keyType, this._valueType.widenBy(clazz), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public JavaType widenKey(final Class<?> clazz) {
        if (clazz == this._keyType.getRawClass()) {
            return this;
        }
        return new MapType(this._class, this._keyType.widenBy(clazz), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public MapType withContentTypeHandler(final Object o) {
        return new MapType(this._class, this._keyType, this._valueType.withTypeHandler(o), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public MapType withContentValueHandler(final Object o) {
        return new MapType(this._class, this._keyType, this._valueType.withValueHandler(o), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public MapType withKeyValueHandler(final Object o) {
        return new MapType(this._class, this._keyType.withValueHandler(o), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public MapType withTypeHandler(final Object o) {
        return new MapType(this._class, this._keyType, this._valueType, this._valueHandler, o, this._asStatic);
    }
    
    @Override
    public MapType withValueHandler(final Object o) {
        return new MapType(this._class, this._keyType, this._valueType, o, this._typeHandler, this._asStatic);
    }
}
