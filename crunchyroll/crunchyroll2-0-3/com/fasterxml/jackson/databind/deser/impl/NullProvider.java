// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import java.io.Serializable;

public final class NullProvider implements Serializable
{
    private final boolean _isPrimitive;
    private final Object _nullValue;
    private final Class<?> _rawType;
    
    public NullProvider(final JavaType javaType, final Object nullValue) {
        this._nullValue = nullValue;
        this._isPrimitive = javaType.isPrimitive();
        this._rawType = javaType.getRawClass();
    }
    
    public Object nullValue(final DeserializationContext deserializationContext) throws JsonProcessingException {
        if (this._isPrimitive && deserializationContext.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
            throw deserializationContext.mappingException("Can not map JSON null into type " + this._rawType.getName() + " (set DeserializationConfig.DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES to 'false' to allow)");
        }
        return this._nullValue;
    }
}
