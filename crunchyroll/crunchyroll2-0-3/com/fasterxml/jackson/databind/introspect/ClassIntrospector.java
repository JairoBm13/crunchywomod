// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;

public abstract class ClassIntrospector
{
    public abstract BeanDescription forClassAnnotations(final MapperConfig<?> p0, final JavaType p1, final MixInResolver p2);
    
    public abstract BeanDescription forCreation(final DeserializationConfig p0, final JavaType p1, final MixInResolver p2);
    
    public abstract BeanDescription forDeserialization(final DeserializationConfig p0, final JavaType p1, final MixInResolver p2);
    
    public abstract BeanDescription forDeserializationWithBuilder(final DeserializationConfig p0, final JavaType p1, final MixInResolver p2);
    
    public abstract BeanDescription forSerialization(final SerializationConfig p0, final JavaType p1, final MixInResolver p2);
    
    public interface MixInResolver
    {
        Class<?> findMixInClassFor(final Class<?> p0);
    }
}
