// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.io.Serializable;
import com.fasterxml.jackson.core.type.ResolvedType;

public abstract class JavaType extends ResolvedType implements Serializable, Type
{
    protected final boolean _asStatic;
    protected final Class<?> _class;
    protected final int _hashCode;
    protected final Object _typeHandler;
    protected final Object _valueHandler;
    
    protected JavaType(final Class<?> class1, final int n, final Object valueHandler, final Object typeHandler, final boolean asStatic) {
        this._class = class1;
        this._hashCode = class1.getName().hashCode() + n;
        this._valueHandler = valueHandler;
        this._typeHandler = typeHandler;
        this._asStatic = asStatic;
    }
    
    protected void _assertSubclass(final Class<?> clazz, final Class<?> clazz2) {
        if (!this._class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("Class " + clazz.getName() + " is not assignable to " + this._class.getName());
        }
    }
    
    protected abstract JavaType _narrow(final Class<?> p0);
    
    protected JavaType _widen(final Class<?> clazz) {
        return this._narrow(clazz);
    }
    
    public JavaType containedType(final int n) {
        return null;
    }
    
    public int containedTypeCount() {
        return 0;
    }
    
    public String containedTypeName(final int n) {
        return null;
    }
    
    @Override
    public abstract boolean equals(final Object p0);
    
    public JavaType forcedNarrowBy(final Class<?> clazz) {
        if (clazz == this._class) {
            return this;
        }
        JavaType javaType2;
        final JavaType javaType = javaType2 = this._narrow(clazz);
        if (this._valueHandler != javaType.getValueHandler()) {
            javaType2 = javaType.withValueHandler(this._valueHandler);
        }
        JavaType withTypeHandler = javaType2;
        if (this._typeHandler != javaType2.getTypeHandler()) {
            withTypeHandler = javaType2.withTypeHandler(this._typeHandler);
        }
        return withTypeHandler;
    }
    
    public JavaType getContentType() {
        return null;
    }
    
    public JavaType getKeyType() {
        return null;
    }
    
    public final Class<?> getRawClass() {
        return this._class;
    }
    
    public <T> T getTypeHandler() {
        return (T)this._typeHandler;
    }
    
    public <T> T getValueHandler() {
        return (T)this._valueHandler;
    }
    
    public boolean hasGenericTypes() {
        return this.containedTypeCount() > 0;
    }
    
    public final boolean hasRawClass(final Class<?> clazz) {
        return this._class == clazz;
    }
    
    @Override
    public final int hashCode() {
        return this._hashCode;
    }
    
    public boolean isAbstract() {
        return Modifier.isAbstract(this._class.getModifiers());
    }
    
    public boolean isArrayType() {
        return false;
    }
    
    public boolean isCollectionLikeType() {
        return false;
    }
    
    public boolean isConcrete() {
        return (this._class.getModifiers() & 0x600) == 0x0 || this._class.isPrimitive();
    }
    
    public abstract boolean isContainerType();
    
    public final boolean isEnumType() {
        return this._class.isEnum();
    }
    
    public final boolean isFinal() {
        return Modifier.isFinal(this._class.getModifiers());
    }
    
    public final boolean isInterface() {
        return this._class.isInterface();
    }
    
    public boolean isMapLikeType() {
        return false;
    }
    
    public final boolean isPrimitive() {
        return this._class.isPrimitive();
    }
    
    public boolean isThrowable() {
        return Throwable.class.isAssignableFrom(this._class);
    }
    
    public JavaType narrowBy(final Class<?> clazz) {
        if (clazz == this._class) {
            return this;
        }
        this._assertSubclass(clazz, this._class);
        JavaType javaType2;
        final JavaType javaType = javaType2 = this._narrow(clazz);
        if (this._valueHandler != javaType.getValueHandler()) {
            javaType2 = javaType.withValueHandler(this._valueHandler);
        }
        JavaType withTypeHandler = javaType2;
        if (this._typeHandler != javaType2.getTypeHandler()) {
            withTypeHandler = javaType2.withTypeHandler(this._typeHandler);
        }
        return withTypeHandler;
    }
    
    public abstract JavaType narrowContentsBy(final Class<?> p0);
    
    @Override
    public abstract String toString();
    
    public final boolean useStaticType() {
        return this._asStatic;
    }
    
    public JavaType widenBy(final Class<?> clazz) {
        if (clazz == this._class) {
            return this;
        }
        this._assertSubclass(this._class, clazz);
        return this._widen(clazz);
    }
    
    public abstract JavaType widenContentsBy(final Class<?> p0);
    
    public abstract JavaType withContentTypeHandler(final Object p0);
    
    public abstract JavaType withContentValueHandler(final Object p0);
    
    public abstract JavaType withTypeHandler(final Object p0);
    
    public abstract JavaType withValueHandler(final Object p0);
}
