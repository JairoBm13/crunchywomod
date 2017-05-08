// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.module;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.HashMap;
import java.io.Serializable;
import com.fasterxml.jackson.databind.deser.KeyDeserializers;

public class SimpleKeyDeserializers implements KeyDeserializers, Serializable
{
    protected HashMap<ClassKey, KeyDeserializer> _classMappings;
    
    public SimpleKeyDeserializers() {
        this._classMappings = null;
    }
    
    @Override
    public KeyDeserializer findKeyDeserializer(final JavaType javaType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) {
        if (this._classMappings == null) {
            return null;
        }
        return this._classMappings.get(new ClassKey(javaType.getRawClass()));
    }
}
