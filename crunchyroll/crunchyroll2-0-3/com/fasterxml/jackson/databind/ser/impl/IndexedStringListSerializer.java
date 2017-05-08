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
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import java.util.List;
import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;

@JacksonStdImpl
public final class IndexedStringListSerializer extends StaticListSerializerBase<List<String>> implements ContextualSerializer
{
    public static final IndexedStringListSerializer instance;
    protected final JsonSerializer<String> _serializer;
    
    static {
        instance = new IndexedStringListSerializer();
    }
    
    protected IndexedStringListSerializer() {
        this((JsonSerializer<?>)null);
    }
    
    public IndexedStringListSerializer(final JsonSerializer<?> serializer) {
        super(List.class);
        this._serializer = (JsonSerializer<String>)serializer;
    }
    
    private final void _serializeUnwrapped(final List<String> list, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._serializer == null) {
            this.serializeContents(list, jsonGenerator, serializerProvider, 1);
            return;
        }
        this.serializeUsingCustom(list, jsonGenerator, serializerProvider, 1);
    }
    
    private final void serializeContents(final List<String> list, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final int n) throws IOException, JsonGenerationException {
        for (int i = 0; i < n; ++i) {
            try {
                final String s = list.get(i);
                if (s == null) {
                    serializerProvider.defaultSerializeNull(jsonGenerator);
                    continue;
                }
                jsonGenerator.writeString(s);
                continue;
            }
            catch (Exception ex) {
                this.wrapAndThrow(serializerProvider, ex, list, i);
            }
            break;
        }
    }
    
    private final void serializeUsingCustom(final List<String> list, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final int n) throws IOException, JsonGenerationException {
        while (true) {
            int n2 = 0;
            int n3 = 0;
            while (true) {
                Label_0080: {
                    try {
                        final JsonSerializer<String> serializer = this._serializer;
                        if (n3 < n) {
                            n2 = n3;
                            final String s = list.get(n3);
                            if (s == null) {
                                n2 = n3;
                                serializerProvider.defaultSerializeNull(jsonGenerator);
                                break Label_0080;
                            }
                            n2 = n3;
                            serializer.serialize(s, jsonGenerator, serializerProvider);
                            break Label_0080;
                        }
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(serializerProvider, ex, list, n2);
                    }
                    break;
                }
                ++n3;
                continue;
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
                return new IndexedStringListSerializer(jsonSerializer2);
            }
            final JsonSerializer<?> serializerInstance = null;
            continue;
        }
    }
    
    @Override
    public void serialize(final List<String> list, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        final int size = list.size();
        if (size == 1 && serializerProvider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) {
            this._serializeUnwrapped(list, jsonGenerator, serializerProvider);
            return;
        }
        jsonGenerator.writeStartArray();
        if (this._serializer == null) {
            this.serializeContents(list, jsonGenerator, serializerProvider, size);
        }
        else {
            this.serializeUsingCustom(list, jsonGenerator, serializerProvider, size);
        }
        jsonGenerator.writeEndArray();
    }
    
    @Override
    public void serializeWithType(final List<String> list, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        final int size = list.size();
        typeSerializer.writeTypePrefixForArray(list, jsonGenerator);
        if (this._serializer == null) {
            this.serializeContents(list, jsonGenerator, serializerProvider, size);
        }
        else {
            this.serializeUsingCustom(list, jsonGenerator, serializerProvider, size);
        }
        typeSerializer.writeTypeSuffixForArray(list, jsonGenerator);
    }
}
