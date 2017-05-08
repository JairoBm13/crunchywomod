// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;

public class StdDelegatingSerializer extends StdSerializer<Object> implements JsonFormatVisitable, SchemaAware, ContextualSerializer, ResolvableSerializer
{
    protected final Converter<Object, ?> _converter;
    protected final JsonSerializer<Object> _delegateSerializer;
    protected final JavaType _delegateType;
    
    public StdDelegatingSerializer(final Converter<Object, ?> converter, final JavaType delegateType, final JsonSerializer<?> delegateSerializer) {
        super(delegateType);
        this._converter = converter;
        this._delegateType = delegateType;
        this._delegateSerializer = (JsonSerializer<Object>)delegateSerializer;
    }
    
    protected Object convertValue(final Object o) {
        return this._converter.convert(o);
    }
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        if (this._delegateSerializer != null) {
            if (this._delegateSerializer instanceof ContextualSerializer) {
                final JsonSerializer<?> contextual = ((ContextualSerializer)this._delegateSerializer).createContextual(serializerProvider, beanProperty);
                if (contextual != this._delegateSerializer) {
                    return this.withDelegate(this._converter, this._delegateType, contextual);
                }
            }
            return this;
        }
        JavaType javaType;
        if ((javaType = this._delegateType) == null) {
            javaType = this._converter.getOutputType(serializerProvider.getTypeFactory());
        }
        return this.withDelegate(this._converter, javaType, serializerProvider.findValueSerializer(javaType, beanProperty));
    }
    
    @Override
    public boolean isEmpty(Object convertValue) {
        convertValue = this.convertValue(convertValue);
        return this._delegateSerializer.isEmpty(convertValue);
    }
    
    @Override
    public void resolve(final SerializerProvider serializerProvider) throws JsonMappingException {
        if (this._delegateSerializer != null && this._delegateSerializer instanceof ResolvableSerializer) {
            ((ResolvableSerializer)this._delegateSerializer).resolve(serializerProvider);
        }
    }
    
    @Override
    public void serialize(Object convertValue, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        convertValue = this.convertValue(convertValue);
        if (convertValue == null) {
            serializerProvider.defaultSerializeNull(jsonGenerator);
            return;
        }
        this._delegateSerializer.serialize(convertValue, jsonGenerator, serializerProvider);
    }
    
    @Override
    public void serializeWithType(Object convertValue, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonProcessingException {
        convertValue = this.convertValue(convertValue);
        this._delegateSerializer.serializeWithType(convertValue, jsonGenerator, serializerProvider, typeSerializer);
    }
    
    protected StdDelegatingSerializer withDelegate(final Converter<Object, ?> converter, final JavaType javaType, final JsonSerializer<?> jsonSerializer) {
        if (this.getClass() != StdDelegatingSerializer.class) {
            throw new IllegalStateException("Sub-class " + this.getClass().getName() + " must override 'withDelegate'");
        }
        return new StdDelegatingSerializer(converter, javaType, jsonSerializer);
    }
}
