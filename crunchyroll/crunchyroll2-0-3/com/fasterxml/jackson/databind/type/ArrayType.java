// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import java.lang.reflect.Type;
import java.lang.reflect.Array;
import com.fasterxml.jackson.databind.JavaType;

public final class ArrayType extends TypeBase
{
    protected final JavaType _componentType;
    protected final Object _emptyArray;
    
    private ArrayType(final JavaType componentType, final Object emptyArray, final Object o, final Object o2, final boolean b) {
        super(emptyArray.getClass(), componentType.hashCode(), o, o2, b);
        this._componentType = componentType;
        this._emptyArray = emptyArray;
    }
    
    public static ArrayType construct(final JavaType javaType, final Object o, final Object o2) {
        return new ArrayType(javaType, Array.newInstance(javaType.getRawClass(), 0), null, null, false);
    }
    
    @Override
    protected JavaType _narrow(final Class<?> clazz) {
        if (!clazz.isArray()) {
            throw new IllegalArgumentException("Incompatible narrowing operation: trying to narrow " + this.toString() + " to class " + clazz.getName());
        }
        return construct(TypeFactory.defaultInstance().constructType(clazz.getComponentType()), this._valueHandler, this._typeHandler);
    }
    
    @Override
    protected String buildCanonicalName() {
        return this._class.getName();
    }
    
    @Override
    public JavaType containedType(final int n) {
        if (n == 0) {
            return this._componentType;
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
        final boolean b = false;
        boolean b2;
        if (o == this) {
            b2 = true;
        }
        else {
            b2 = b;
            if (o != null) {
                b2 = b;
                if (o.getClass() == this.getClass()) {
                    return this._componentType.equals(((ArrayType)o)._componentType);
                }
            }
        }
        return b2;
    }
    
    @Override
    public JavaType getContentType() {
        return this._componentType;
    }
    
    @Override
    public boolean hasGenericTypes() {
        return this._componentType.hasGenericTypes();
    }
    
    @Override
    public boolean isAbstract() {
        return false;
    }
    
    @Override
    public boolean isArrayType() {
        return true;
    }
    
    @Override
    public boolean isConcrete() {
        return true;
    }
    
    @Override
    public boolean isContainerType() {
        return true;
    }
    
    @Override
    public JavaType narrowContentsBy(final Class<?> clazz) {
        if (clazz == this._componentType.getRawClass()) {
            return this;
        }
        return construct(this._componentType.narrowBy(clazz), this._valueHandler, this._typeHandler);
    }
    
    @Override
    public String toString() {
        return "[array type, component type: " + this._componentType + "]";
    }
    
    @Override
    public JavaType widenContentsBy(final Class<?> clazz) {
        if (clazz == this._componentType.getRawClass()) {
            return this;
        }
        return construct(this._componentType.widenBy(clazz), this._valueHandler, this._typeHandler);
    }
    
    @Override
    public ArrayType withContentTypeHandler(final Object o) {
        if (o == this._componentType.getTypeHandler()) {
            return this;
        }
        return new ArrayType(this._componentType.withTypeHandler(o), this._emptyArray, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public ArrayType withContentValueHandler(final Object o) {
        if (o == this._componentType.getValueHandler()) {
            return this;
        }
        return new ArrayType(this._componentType.withValueHandler(o), this._emptyArray, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    public ArrayType withTypeHandler(final Object o) {
        if (o == this._typeHandler) {
            return this;
        }
        return new ArrayType(this._componentType, this._emptyArray, this._valueHandler, o, this._asStatic);
    }
    
    @Override
    public ArrayType withValueHandler(final Object o) {
        if (o == this._valueHandler) {
            return this;
        }
        return new ArrayType(this._componentType, this._emptyArray, o, this._typeHandler, this._asStatic);
    }
}
