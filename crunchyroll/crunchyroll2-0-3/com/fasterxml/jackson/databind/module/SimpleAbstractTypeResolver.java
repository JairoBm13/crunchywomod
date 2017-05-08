// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.module;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.HashMap;
import java.io.Serializable;
import com.fasterxml.jackson.databind.AbstractTypeResolver;

public class SimpleAbstractTypeResolver extends AbstractTypeResolver implements Serializable
{
    protected final HashMap<ClassKey, Class<?>> _mappings;
    
    public SimpleAbstractTypeResolver() {
        this._mappings = new HashMap<ClassKey, Class<?>>();
    }
    
    @Override
    public JavaType findTypeMapping(final DeserializationConfig deserializationConfig, final JavaType javaType) {
        final Class<?> clazz = this._mappings.get(new ClassKey(javaType.getRawClass()));
        if (clazz == null) {
            return null;
        }
        return javaType.narrowBy(clazz);
    }
    
    @Override
    public JavaType resolveAbstractType(final DeserializationConfig deserializationConfig, final JavaType javaType) {
        return null;
    }
}
