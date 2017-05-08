// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.JsonDeserializer;

public abstract class ContainerDeserializerBase<T> extends StdDeserializer<T>
{
    protected ContainerDeserializerBase(final Class<?> clazz) {
        super(clazz);
    }
    
    public abstract JsonDeserializer<Object> getContentDeserializer();
}
