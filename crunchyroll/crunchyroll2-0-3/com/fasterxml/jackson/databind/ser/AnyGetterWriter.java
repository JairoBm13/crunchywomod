// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Map;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;

public class AnyGetterWriter
{
    protected final AnnotatedMember _accessor;
    protected final BeanProperty _property;
    protected MapSerializer _serializer;
    
    public AnyGetterWriter(final BeanProperty property, final AnnotatedMember accessor, final MapSerializer serializer) {
        this._accessor = accessor;
        this._property = property;
        this._serializer = serializer;
    }
    
    public void getAndSerialize(Object value, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws Exception {
        value = this._accessor.getValue(value);
        if (value == null) {
            return;
        }
        if (!(value instanceof Map)) {
            throw new JsonMappingException("Value returned by 'any-getter' (" + this._accessor.getName() + "()) not java.util.Map but " + value.getClass().getName());
        }
        this._serializer.serializeFields((Map<?, ?>)value, jsonGenerator, serializerProvider);
    }
    
    public void resolve(final SerializerProvider serializerProvider) throws JsonMappingException {
        this._serializer = (MapSerializer)this._serializer.createContextual(serializerProvider, this._property);
    }
}
