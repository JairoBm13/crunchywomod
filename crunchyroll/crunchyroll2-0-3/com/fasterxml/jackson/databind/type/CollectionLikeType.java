// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import java.util.Collection;
import com.fasterxml.jackson.databind.JavaType;

public class CollectionLikeType extends TypeBase
{
    protected final JavaType _elementType;
    
    protected CollectionLikeType(final Class<?> clazz, final JavaType elementType, final Object o, final Object o2, final boolean b) {
        super(clazz, elementType.hashCode(), o, o2, b);
        this._elementType = elementType;
    }
    
    @Override
    protected JavaType _narrow(final Class<?> clazz) {
        return new CollectionLikeType(clazz, this._elementType, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    protected String buildCanonicalName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        if (this._elementType != null) {
            sb.append('<');
            sb.append(this._elementType.toCanonical());
            sb.append('>');
        }
        return sb.toString();
    }
    
    @Override
    public JavaType containedType(final int n) {
        if (n == 0) {
            return this._elementType;
        }
        return null;
    }
    
    @Override
    public int containedTypeCount() {
        return 1;
    }
    
    @Override
    public String containedTypeName(final int n) {
        if (n == 0) {
            return "E";
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
            final CollectionLikeType collectionLikeType = (CollectionLikeType)o;
            if (this._class != collectionLikeType._class || !this._elementType.equals(collectionLikeType._elementType)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public JavaType getContentType() {
        return this._elementType;
    }
    
    @Override
    public boolean isCollectionLikeType() {
        return true;
    }
    
    @Override
    public boolean isContainerType() {
        return true;
    }
    
    public boolean isTrueCollectionType() {
        return Collection.class.isAssignableFrom(this._class);
    }
    
    @Override
    public JavaType narrowContentsBy(final Class<?> clazz) {
        if (clazz == this._elementType.getRawClass()) {
            return this;
        }
        return new CollectionLikeType(this._class, this._elementType.narrowBy(clazz), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public String toString() {
        return "[collection-like type; class " + this._class.getName() + ", contains " + this._elementType + "]";
    }
    
    @Override
    public JavaType widenContentsBy(final Class<?> clazz) {
        if (clazz == this._elementType.getRawClass()) {
            return this;
        }
        return new CollectionLikeType(this._class, this._elementType.widenBy(clazz), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public CollectionLikeType withContentTypeHandler(final Object o) {
        return new CollectionLikeType(this._class, this._elementType.withTypeHandler(o), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public CollectionLikeType withContentValueHandler(final Object o) {
        return new CollectionLikeType(this._class, this._elementType.withValueHandler(o), this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public CollectionLikeType withTypeHandler(final Object o) {
        return new CollectionLikeType(this._class, this._elementType, this._valueHandler, o, this._asStatic);
    }
    
    @Override
    public CollectionLikeType withValueHandler(final Object o) {
        return new CollectionLikeType(this._class, this._elementType, o, this._typeHandler, this._asStatic);
    }
}
