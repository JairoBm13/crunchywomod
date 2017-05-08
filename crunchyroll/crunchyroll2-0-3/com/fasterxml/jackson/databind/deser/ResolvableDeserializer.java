// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;

public interface ResolvableDeserializer
{
    void resolve(final DeserializationContext p0) throws JsonMappingException;
}
