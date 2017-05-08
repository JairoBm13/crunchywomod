// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import java.util.Iterator;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JavaType;
import java.util.EnumSet;

public class EnumSetSerializer extends AsArraySerializerBase<EnumSet<? extends Enum<?>>>
{
    public EnumSetSerializer(final JavaType javaType, final BeanProperty beanProperty) {
        super(EnumSet.class, javaType, true, null, beanProperty, null);
    }
    
    public EnumSetSerializer(final EnumSetSerializer enumSetSerializer, final BeanProperty beanProperty, final TypeSerializer typeSerializer, final JsonSerializer<?> jsonSerializer) {
        super(enumSetSerializer, beanProperty, typeSerializer, jsonSerializer);
    }
    
    public EnumSetSerializer _withValueTypeSerializer(final TypeSerializer typeSerializer) {
        return this;
    }
    
    @Override
    public boolean hasSingleElement(final EnumSet<? extends Enum<?>> set) {
        return set.size() == 1;
    }
    
    @Override
    public boolean isEmpty(final EnumSet<? extends Enum<?>> set) {
        return set == null || set.isEmpty();
    }
    
    public void serializeContents(final EnumSet<? extends Enum<?>> set, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        final JsonSerializer<Object> elementSerializer = this._elementSerializer;
        final Iterator<Enum<?>> iterator = set.iterator();
        JsonFormatVisitable jsonFormatVisitable = elementSerializer;
        while (iterator.hasNext()) {
            final Enum<?> enum1 = iterator.next();
            JsonFormatVisitable valueSerializer;
            if ((valueSerializer = jsonFormatVisitable) == null) {
                valueSerializer = serializerProvider.findValueSerializer(enum1.getDeclaringClass(), this._property);
            }
            ((JsonSerializer<Enum<?>>)valueSerializer).serialize(enum1, jsonGenerator, serializerProvider);
            jsonFormatVisitable = valueSerializer;
        }
    }
    
    @Override
    public EnumSetSerializer withResolved(final BeanProperty beanProperty, final TypeSerializer typeSerializer, final JsonSerializer<?> jsonSerializer) {
        return new EnumSetSerializer(this, beanProperty, typeSerializer, jsonSerializer);
    }
}
