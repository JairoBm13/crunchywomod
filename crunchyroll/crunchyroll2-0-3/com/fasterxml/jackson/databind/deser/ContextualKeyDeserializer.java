// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;

public interface ContextualKeyDeserializer
{
    KeyDeserializer createContextual(final DeserializationContext p0, final BeanProperty p1) throws JsonMappingException;
}
