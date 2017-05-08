// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;

public final class ObjectIdWriter
{
    public final boolean alwaysAsId;
    public final ObjectIdGenerator<?> generator;
    public final JavaType idType;
    public final SerializedString propertyName;
    public final JsonSerializer<Object> serializer;
    
    protected ObjectIdWriter(final JavaType idType, final SerializedString propertyName, final ObjectIdGenerator<?> generator, final JsonSerializer<?> serializer, final boolean alwaysAsId) {
        this.idType = idType;
        this.propertyName = propertyName;
        this.generator = generator;
        this.serializer = (JsonSerializer<Object>)serializer;
        this.alwaysAsId = alwaysAsId;
    }
    
    public static ObjectIdWriter construct(final JavaType javaType, final String s, final ObjectIdGenerator<?> objectIdGenerator, final boolean b) {
        SerializedString serializedString;
        if (s == null) {
            serializedString = null;
        }
        else {
            serializedString = new SerializedString(s);
        }
        return new ObjectIdWriter(javaType, serializedString, objectIdGenerator, null, b);
    }
    
    public ObjectIdWriter withAlwaysAsId(final boolean b) {
        if (b == this.alwaysAsId) {
            return this;
        }
        return new ObjectIdWriter(this.idType, this.propertyName, this.generator, this.serializer, b);
    }
    
    public ObjectIdWriter withSerializer(final JsonSerializer<?> jsonSerializer) {
        return new ObjectIdWriter(this.idType, this.propertyName, this.generator, jsonSerializer, this.alwaysAsId);
    }
}
