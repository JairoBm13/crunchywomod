// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.Serializable;

public final class ObjectIdReader implements Serializable
{
    public final JsonDeserializer<Object> deserializer;
    public final ObjectIdGenerator<?> generator;
    public final SettableBeanProperty idProperty;
    public final JavaType idType;
    public final String propertyName;
    
    protected ObjectIdReader(final JavaType idType, final String propertyName, final ObjectIdGenerator<?> generator, final JsonDeserializer<?> deserializer, final SettableBeanProperty idProperty) {
        this.idType = idType;
        this.propertyName = propertyName;
        this.generator = generator;
        this.deserializer = (JsonDeserializer<Object>)deserializer;
        this.idProperty = idProperty;
    }
    
    public static ObjectIdReader construct(final JavaType javaType, final String s, final ObjectIdGenerator<?> objectIdGenerator, final JsonDeserializer<?> jsonDeserializer, final SettableBeanProperty settableBeanProperty) {
        return new ObjectIdReader(javaType, s, objectIdGenerator, jsonDeserializer, settableBeanProperty);
    }
}
