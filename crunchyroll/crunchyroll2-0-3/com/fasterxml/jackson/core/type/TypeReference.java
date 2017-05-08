// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> implements Comparable<TypeReference<T>>
{
    protected final Type _type;
    
    protected TypeReference() {
        final Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without actual type information");
        }
        this._type = ((ParameterizedType)genericSuperclass).getActualTypeArguments()[0];
    }
    
    @Override
    public int compareTo(final TypeReference<T> typeReference) {
        return 0;
    }
    
    public Type getType() {
        return this._type;
    }
}
