// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;

public final class SimpleType extends TypeBase
{
    protected final String[] _typeNames;
    protected final JavaType[] _typeParameters;
    
    protected SimpleType(final Class<?> clazz) {
        this(clazz, null, null, null, null, false);
    }
    
    protected SimpleType(final Class<?> clazz, final String[] typeNames, final JavaType[] typeParameters, final Object o, final Object o2, final boolean b) {
        super(clazz, 0, o, o2, b);
        if (typeNames == null || typeNames.length == 0) {
            this._typeNames = null;
            this._typeParameters = null;
            return;
        }
        this._typeNames = typeNames;
        this._typeParameters = typeParameters;
    }
    
    public static SimpleType constructUnsafe(final Class<?> clazz) {
        return new SimpleType(clazz, null, null, null, null, false);
    }
    
    @Override
    protected JavaType _narrow(final Class<?> clazz) {
        return new SimpleType(clazz, this._typeNames, this._typeParameters, this._valueHandler, this._typeHandler, this._asStatic);
    }
    
    @Override
    protected String buildCanonicalName() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this._class.getName());
        if (this._typeParameters != null && this._typeParameters.length > 0) {
            sb.append('<');
            int n = 1;
            final JavaType[] typeParameters = this._typeParameters;
            for (int length = typeParameters.length, i = 0; i < length; ++i) {
                final JavaType javaType = typeParameters[i];
                if (n != 0) {
                    n = 0;
                }
                else {
                    sb.append(',');
                }
                sb.append(javaType.toCanonical());
            }
            sb.append('>');
        }
        return sb.toString();
    }
    
    @Override
    public JavaType containedType(final int n) {
        if (n < 0 || this._typeParameters == null || n >= this._typeParameters.length) {
            return null;
        }
        return this._typeParameters[n];
    }
    
    @Override
    public int containedTypeCount() {
        if (this._typeParameters == null) {
            return 0;
        }
        return this._typeParameters.length;
    }
    
    @Override
    public String containedTypeName(final int n) {
        if (n < 0 || this._typeNames == null || n >= this._typeNames.length) {
            return null;
        }
        return this._typeNames[n];
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
                    final SimpleType simpleType = (SimpleType)o;
                    b2 = b;
                    if (simpleType._class == this._class) {
                        final JavaType[] typeParameters = this._typeParameters;
                        final JavaType[] typeParameters2 = simpleType._typeParameters;
                        if (typeParameters == null) {
                            if (typeParameters2 != null) {
                                b2 = b;
                                if (typeParameters2.length != 0) {
                                    return b2;
                                }
                            }
                            return true;
                        }
                        b2 = b;
                        if (typeParameters2 != null) {
                            b2 = b;
                            if (typeParameters.length == typeParameters2.length) {
                                for (int length = typeParameters.length, i = 0; i < length; ++i) {
                                    b2 = b;
                                    if (!typeParameters[i].equals(typeParameters2[i])) {
                                        return b2;
                                    }
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return b2;
    }
    
    @Override
    public boolean isContainerType() {
        return false;
    }
    
    @Override
    public JavaType narrowContentsBy(final Class<?> clazz) {
        throw new IllegalArgumentException("Internal error: SimpleType.narrowContentsBy() should never be called");
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(40);
        sb.append("[simple type, class ").append(this.buildCanonicalName()).append(']');
        return sb.toString();
    }
    
    @Override
    public JavaType widenContentsBy(final Class<?> clazz) {
        throw new IllegalArgumentException("Internal error: SimpleType.widenContentsBy() should never be called");
    }
    
    @Override
    public JavaType withContentTypeHandler(final Object o) {
        throw new IllegalArgumentException("Simple types have no content types; can not call withContenTypeHandler()");
    }
    
    @Override
    public SimpleType withContentValueHandler(final Object o) {
        throw new IllegalArgumentException("Simple types have no content types; can not call withContenValueHandler()");
    }
    
    @Override
    public SimpleType withTypeHandler(final Object o) {
        return new SimpleType(this._class, this._typeNames, this._typeParameters, this._valueHandler, o, this._asStatic);
    }
    
    @Override
    public SimpleType withValueHandler(final Object o) {
        if (o == this._valueHandler) {
            return this;
        }
        return new SimpleType(this._class, this._typeNames, this._typeParameters, o, this._typeHandler, this._asStatic);
    }
}
