// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype;

import com.fasterxml.jackson.databind.JavaType;

public interface TypeIdResolver
{
    String idFromBaseType();
    
    String idFromValue(final Object p0);
    
    String idFromValueAndType(final Object p0, final Class<?> p1);
    
    void init(final JavaType p0);
    
    JavaType typeFromId(final String p0);
}
