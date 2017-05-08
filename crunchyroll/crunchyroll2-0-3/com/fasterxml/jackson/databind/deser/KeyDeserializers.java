// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;

public interface KeyDeserializers
{
    KeyDeserializer findKeyDeserializer(final JavaType p0, final DeserializationConfig p1, final BeanDescription p2) throws JsonMappingException;
}
