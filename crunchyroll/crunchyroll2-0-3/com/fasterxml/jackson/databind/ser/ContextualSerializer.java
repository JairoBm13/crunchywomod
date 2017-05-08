// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;

public interface ContextualSerializer
{
    JsonSerializer<?> createContextual(final SerializerProvider p0, final BeanProperty p1) throws JsonMappingException;
}
