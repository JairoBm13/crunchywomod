// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;

public abstract class TypeIdResolverBase implements TypeIdResolver
{
    protected final JavaType _baseType;
    protected final TypeFactory _typeFactory;
    
    protected TypeIdResolverBase(final JavaType baseType, final TypeFactory typeFactory) {
        this._baseType = baseType;
        this._typeFactory = typeFactory;
    }
    
    @Override
    public String idFromBaseType() {
        return this.idFromValueAndType(null, this._baseType.getRawClass());
    }
    
    @Override
    public void init(final JavaType javaType) {
    }
}
