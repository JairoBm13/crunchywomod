// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import java.util.Map;
import com.fasterxml.jackson.databind.JavaType;

public class MapLikeType extends TypeBase
{
    protected final JavaType _keyType;
    protected final JavaType _valueType;
    
    protected MapLikeType(final Class<?> clazz, final JavaType keyType, final JavaType valueType, final Object o, final Object o2, final boolean b) {
        super(clazz, keyType.hashCode() ^ valueType.hashCode(), o, o2, b);
        this._keyType = keyType;
        this._valueType = valueType;
    }
    
    @Override
    protected JavaType _narrow(final Class<?> clazz) {
        return new MapLikeType(clazz, this._keyType, this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    protected String buildCanonicalName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        if (this._keyType != null) {
            sb.append('<');
            sb.append(this._keyType.toCanonical());
            sb.append(',');
            sb.append(this._valueType.toCanonical());
            sb.append('>');
        }
        return sb.toString();
    }
    
    @Override
    public JavaType containedType(final int n) {
        if (n == 0) {
            return this._keyType;
        }
        if (n == 1) {
            return this._valueType;
        }
        return null;
    }
    
    @Override
    public int containedTypeCount() {
        return 2;
    }
    
    @Override
    public String containedTypeName(final int n) {
        if (n == 0) {
            return "K";
        }
        if (n == 1) {
            return "V";
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o != this) {
            if (o == null) {
                return false;
            }
            if (o.getClass() != this.getClass()) {
                return false;
            }
            final MapLikeType mapLikeType = (MapLikeType)o;
            if (this._class != mapLikeType._class || !this._keyType.equals(mapLikeType._keyType) || !this._valueType.equals(mapLikeType._valueType)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public JavaType getContentType() {
        return this._valueType;
    }
    
    @Override
    public JavaType getKeyType() {
        return this._keyType;
    }
    
    @Override
    public boolean isContainerType() {
        return true;
    }
    
    @Override
    public boolean isMapLikeType() {
        return true;
    }
    
    public boolean isTrueMapType() {
        return Map.class.isAssignableFrom(this._class);
    }
    
    @Override
    public JavaType narrowContentsBy(final Class<?> clazz) {
        if (clazz == this._valueType.getRawClass()) {
            return this;
        }
        return new MapLikeType(this._class, this._keyType, this._valueType.narrowBy(clazz), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    public JavaType narrowKey(final Class<?> clazz) {
        if (clazz == this._keyType.getRawClass()) {
            return this;
        }
        return new MapLikeType(this._class, this._keyType.narrowBy(clazz), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public String toString() {
        return "[map-like type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + "]";
    }
    
    @Override
    public JavaType widenContentsBy(final Class<?> clazz) {
        if (clazz == this._valueType.getRawClass()) {
            return this;
        }
        return new MapLikeType(this._class, this._keyType, this._valueType.widenBy(clazz), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    public JavaType widenKey(final Class<?> clazz) {
        if (clazz == this._keyType.getRawClass()) {
            return this;
        }
        return new MapLikeType(this._class, this._keyType.widenBy(clazz), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public MapLikeType withContentTypeHandler(final Object o) {
        return new MapLikeType(this._class, this._keyType, this._valueType.withTypeHandler(o), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public MapLikeType withContentValueHandler(final Object o) {
        return new MapLikeType(this._class, this._keyType, this._valueType.withValueHandler(o), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    public MapLikeType withKeyValueHandler(final Object o) {
        return new MapLikeType(this._class, this._keyType.withValueHandler(o), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public MapLikeType withTypeHandler(final Object o) {
        return new MapLikeType(this._class, this._keyType, this._valueType, this._valueHandler, o, this._asStatic);
    }
    
    @Override
    public MapLikeType withValueHandler(final Object o) {
        return new MapLikeType(this._class, this._keyType, this._valueType, o, this._typeHandler, this._asStatic);
    }
}
