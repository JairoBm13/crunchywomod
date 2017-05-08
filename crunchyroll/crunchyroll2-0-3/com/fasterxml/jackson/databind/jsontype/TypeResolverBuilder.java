// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.SerializationConfig;
import java.util.Collection;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;

public interface TypeResolverBuilder<T extends TypeResolverBuilder<T>>
{
    TypeDeserializer buildTypeDeserializer(final DeserializationConfig p0, final JavaType p1, final Collection<NamedType> p2);
    
    TypeSerializer buildTypeSerializer(final SerializationConfig p0, final JavaType p1, final Collection<NamedType> p2);
    
    T defaultImpl(final Class<?> p0);
    
    Class<?> getDefaultImpl();
    
    T inclusion(final JsonTypeInfo.As p0);
    
    T init(final JsonTypeInfo.Id p0, final TypeIdResolver p1);
    
    T typeIdVisibility(final boolean p0);
    
    T typeProperty(final String p0);
}
