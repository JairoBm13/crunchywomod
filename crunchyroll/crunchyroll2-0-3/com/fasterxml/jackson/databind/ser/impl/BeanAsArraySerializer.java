// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

public class BeanAsArraySerializer extends BeanSerializerBase
{
    protected final BeanSerializerBase _defaultSerializer;
    
    public BeanAsArraySerializer(final BeanSerializerBase defaultSerializer) {
        super(defaultSerializer, (ObjectIdWriter)null);
        this._defaultSerializer = defaultSerializer;
    }
    
    protected BeanAsArraySerializer(final BeanSerializerBase defaultSerializer, final String[] array) {
        super(defaultSerializer, array);
        this._defaultSerializer = defaultSerializer;
    }
    
    private boolean hasSingleElement(final SerializerProvider serializerProvider) {
        BeanPropertyWriter[] array;
        if (this._filteredProps != null && serializerProvider.getActiveView() != null) {
            array = this._filteredProps;
        }
        else {
            array = this._props;
        }
        return array.length == 1;
    }
    
    @Override
    protected BeanSerializerBase asArraySerializer() {
        return this;
    }
    
    @Override
    public boolean isUnwrappingSerializer() {
        return false;
    }
    
    @Override
    public final void serialize(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (serializerProvider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED) && this.hasSingleElement(serializerProvider)) {
            this.serializeAsArray(o, jsonGenerator, serializerProvider);
            return;
        }
        jsonGenerator.writeStartArray();
        this.serializeAsArray(o, jsonGenerator, serializerProvider);
        jsonGenerator.writeEndArray();
    }
    
    protected final void serializeAsArray(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        Label_0074: {
            if (this._filteredProps == null || serializerProvider.getActiveView() == null) {
                break Label_0074;
            }
            BeanPropertyWriter[] array = this._filteredProps;
            while (true) {
                int n = 0;
                int n2 = 0;
                int i = 0;
                try {
                    while (i < array.length) {
                        final BeanPropertyWriter beanPropertyWriter = array[i];
                        if (beanPropertyWriter == null) {
                            n = i;
                            n2 = i;
                            jsonGenerator.writeNull();
                        }
                        else {
                            n = i;
                            n2 = i;
                            beanPropertyWriter.serializeAsColumn(o, jsonGenerator, serializerProvider);
                        }
                        ++i;
                    }
                    goto Label_0124;
                    array = this._props;
                }
                catch (Exception ex) {
                    if (n != array.length) {
                        goto Label_0125;
                    }
                    this.wrapAndThrow(serializerProvider, ex, o, "[anySetter]");
                }
                catch (StackOverflowError stackOverflowError) {
                    final JsonMappingException ex2 = new JsonMappingException("Infinite recursion (StackOverflowError)", stackOverflowError);
                    String name;
                    if (n2 == array.length) {
                        name = "[anySetter]";
                    }
                    else {
                        name = array[n2].getName();
                    }
                    ex2.prependPath(new JsonMappingException.Reference(o, name));
                    throw ex2;
                }
            }
        }
    }
    
    @Override
    public void serializeWithType(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        this._defaultSerializer.serializeWithType(o, jsonGenerator, serializerProvider, typeSerializer);
    }
    
    @Override
    public String toString() {
        return "BeanAsArraySerializer for " + this.handledType().getName();
    }
    
    @Override
    public JsonSerializer<Object> unwrappingSerializer(final NameTransformer nameTransformer) {
        return this._defaultSerializer.unwrappingSerializer(nameTransformer);
    }
    
    @Override
    protected BeanAsArraySerializer withIgnorals(final String[] array) {
        return new BeanAsArraySerializer(this, array);
    }
    
    @Override
    public BeanSerializerBase withObjectIdWriter(final ObjectIdWriter objectIdWriter) {
        return this._defaultSerializer.withObjectIdWriter(objectIdWriter);
    }
}
