// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.BeanProperty;
import java.util.Iterator;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.util.Collection;
import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;

@JacksonStdImpl
public class StringCollectionSerializer extends StaticListSerializerBase<Collection<String>> implements ContextualSerializer
{
    public static final StringCollectionSerializer instance;
    protected final JsonSerializer<String> _serializer;
    
    static {
        instance = new StringCollectionSerializer();
    }
    
    protected StringCollectionSerializer() {
        this((JsonSerializer<?>)null);
    }
    
    protected StringCollectionSerializer(final JsonSerializer<?> serializer) {
        super(Collection.class);
        this._serializer = (JsonSerializer<String>)serializer;
    }
    
    private final void _serializeUnwrapped(final Collection<String> collection, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._serializer == null) {
            this.serializeContents(collection, jsonGenerator, serializerProvider);
            return;
        }
        this.serializeUsingCustom(collection, jsonGenerator, serializerProvider);
    }
    
    private final void serializeContents(final Collection<String> collection, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._serializer != null) {
            this.serializeUsingCustom(collection, jsonGenerator, serializerProvider);
        }
        else {
            final Iterator<String> iterator = collection.iterator();
            int n = 0;
            while (iterator.hasNext()) {
                final String s = iterator.next();
                Label_0061: {
                    if (s != null) {
                        break Label_0061;
                    }
                    try {
                        serializerProvider.defaultSerializeNull(jsonGenerator);
                        break Label_0061;
                        jsonGenerator.writeString(s);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(serializerProvider, ex, collection, n);
                        continue;
                    }
                }
                ++n;
            }
        }
    }
    
    private void serializeUsingCustom(final Collection<String> collection, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        final JsonSerializer<String> serializer = this._serializer;
        for (final String s : collection) {
            if (s == null) {
                try {
                    serializerProvider.defaultSerializeNull(jsonGenerator);
                }
                catch (Exception ex) {
                    this.wrapAndThrow(serializerProvider, ex, collection, 0);
                }
            }
            else {
                serializer.serialize(s, jsonGenerator, serializerProvider);
            }
        }
    }
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        while (true) {
            Label_0132: {
                if (beanProperty == null) {
                    break Label_0132;
                }
                final AnnotatedMember member = beanProperty.getMember();
                if (member == null) {
                    break Label_0132;
                }
                final Object contentSerializer = serializerProvider.getAnnotationIntrospector().findContentSerializer(member);
                if (contentSerializer == null) {
                    break Label_0132;
                }
                final JsonSerializer<?> serializerInstance = serializerProvider.serializerInstance(member, contentSerializer);
                JsonSerializer<?> serializer = serializerInstance;
                if (serializerInstance == null) {
                    serializer = this._serializer;
                }
                final JsonSerializer<?> convertingContentSerializer = this.findConvertingContentSerializer(serializerProvider, beanProperty, serializer);
                JsonSerializer<?> jsonSerializer;
                if (convertingContentSerializer == null) {
                    jsonSerializer = serializerProvider.findValueSerializer(String.class, beanProperty);
                }
                else {
                    jsonSerializer = convertingContentSerializer;
                    if (convertingContentSerializer instanceof ContextualSerializer) {
                        jsonSerializer = ((ContextualSerializer)convertingContentSerializer).createContextual(serializerProvider, beanProperty);
                    }
                }
                JsonSerializer<?> jsonSerializer2 = jsonSerializer;
                if (this.isDefaultSerializer(jsonSerializer)) {
                    jsonSerializer2 = null;
                }
                if (jsonSerializer2 == this._serializer) {
                    return this;
                }
                return new StringCollectionSerializer(jsonSerializer2);
            }
            final JsonSerializer<?> serializerInstance = null;
            continue;
        }
    }
    
    @Override
    public void serialize(final Collection<String> collection, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (collection.size() == 1 && serializerProvider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) {
            this._serializeUnwrapped(collection, jsonGenerator, serializerProvider);
            return;
        }
        jsonGenerator.writeStartArray();
        if (this._serializer == null) {
            this.serializeContents(collection, jsonGenerator, serializerProvider);
        }
        else {
            this.serializeUsingCustom(collection, jsonGenerator, serializerProvider);
        }
        jsonGenerator.writeEndArray();
    }
    
    @Override
    public void serializeWithType(final Collection<String> collection, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        typeSerializer.writeTypePrefixForArray(collection, jsonGenerator);
        if (this._serializer == null) {
            this.serializeContents(collection, jsonGenerator, serializerProvider);
        }
        else {
            this.serializeUsingCustom(collection, jsonGenerator, serializerProvider);
        }
        typeSerializer.writeTypeSuffixForArray(collection, jsonGenerator);
    }
}
