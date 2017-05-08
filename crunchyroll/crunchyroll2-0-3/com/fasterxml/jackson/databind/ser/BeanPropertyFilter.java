// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;

public interface BeanPropertyFilter
{
    void serializeAsField(final Object p0, final JsonGenerator p1, final SerializerProvider p2, final BeanPropertyWriter p3) throws Exception;
}
