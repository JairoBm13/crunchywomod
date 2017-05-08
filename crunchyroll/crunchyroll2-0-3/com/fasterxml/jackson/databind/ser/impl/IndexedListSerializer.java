// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import java.util.List;
import com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase;

@JacksonStdImpl
public final class IndexedListSerializer extends AsArraySerializerBase<List<?>>
{
    public IndexedListSerializer(final JavaType javaType, final boolean b, final TypeSerializer typeSerializer, final BeanProperty beanProperty, final JsonSerializer<Object> jsonSerializer) {
        super(List.class, javaType, b, typeSerializer, beanProperty, jsonSerializer);
    }
    
    public IndexedListSerializer(final IndexedListSerializer indexedListSerializer, final BeanProperty beanProperty, final TypeSerializer typeSerializer, final JsonSerializer<?> jsonSerializer) {
        super(indexedListSerializer, beanProperty, typeSerializer, jsonSerializer);
    }
    
    public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
        return new IndexedListSerializer(this._elementType, this._staticTyping, typeSerializer, this._property, this._elementSerializer);
    }
    
    @Override
    public boolean hasSingleElement(final List<?> list) {
        return list.size() == 1;
    }
    
    @Override
    public boolean isEmpty(final List<?> list) {
        return list == null || list.isEmpty();
    }
    
    public void serializeContents(final List<?> list, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._elementSerializer != null) {
            this.serializeContentsUsing(list, jsonGenerator, serializerProvider, this._elementSerializer);
        }
        else {
            if (this._valueTypeSerializer != null) {
                this.serializeTypedContents(list, jsonGenerator, serializerProvider);
                return;
            }
            final int size = list.size();
            if (size != 0) {
                while (true) {
                    int n = 0;
                    int n2 = 0;
                    while (true) {
                        Label_0234: {
                            while (true) {
                                PropertySerializerMap dynamicSerializers = null;
                                Class<?> class1 = null;
                                Label_0216: {
                                    try {
                                        dynamicSerializers = this._dynamicSerializers;
                                        if (n2 >= size) {
                                            break;
                                        }
                                        n = n2;
                                        final Object value = list.get(n2);
                                        if (value == null) {
                                            n = n2;
                                            serializerProvider.defaultSerializeNull(jsonGenerator);
                                            break Label_0234;
                                        }
                                        n = n2;
                                        class1 = value.getClass();
                                        n = n2;
                                        final JsonSerializer<Object> serializer = dynamicSerializers.serializerFor(class1);
                                        PropertySerializerMap dynamicSerializers2 = dynamicSerializers;
                                        JsonSerializer<Object> jsonSerializer;
                                        if ((jsonSerializer = serializer) == null) {
                                            n = n2;
                                            if (!this._elementType.hasGenericTypes()) {
                                                break Label_0216;
                                            }
                                            n = n2;
                                            final JsonSerializer<Object> jsonSerializer2 = this._findAndAddDynamic(dynamicSerializers, serializerProvider.constructSpecializedType(this._elementType, class1), serializerProvider);
                                            n = n2;
                                            dynamicSerializers2 = this._dynamicSerializers;
                                            jsonSerializer = jsonSerializer2;
                                        }
                                        n = n2;
                                        jsonSerializer.serialize(value, jsonGenerator, serializerProvider);
                                        dynamicSerializers = dynamicSerializers2;
                                        break Label_0234;
                                    }
                                    catch (Exception ex) {
                                        this.wrapAndThrow(serializerProvider, ex, list, n);
                                        return;
                                    }
                                }
                                n = n2;
                                final JsonSerializer<Object> jsonSerializer2 = this._findAndAddDynamic(dynamicSerializers, class1, serializerProvider);
                                continue;
                            }
                        }
                        ++n2;
                        continue;
                    }
                }
            }
        }
    }
    
    public void serializeContentsUsing(final List<?> list, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final JsonSerializer<Object> jsonSerializer) throws IOException, JsonGenerationException {
        final int size = list.size();
        if (size != 0) {
            final TypeSerializer valueTypeSerializer = this._valueTypeSerializer;
        Block_4_Outer:
            for (int i = 0; i < size; ++i) {
                final Object value = list.get(i);
                Label_0053: {
                    if (value != null) {
                        break Label_0053;
                    }
                    try {
                        serializerProvider.defaultSerializeNull(jsonGenerator);
                        continue Block_4_Outer;
                        while (true) {
                            jsonSerializer.serialize(value, jsonGenerator, serializerProvider);
                            continue Block_4_Outer;
                            continue;
                        }
                    }
                    // iftrue(Label_0085:, valueTypeSerializer != null)
                    catch (Exception ex) {
                        this.wrapAndThrow(serializerProvider, ex, list, i);
                    }
                }
                continue;
                Label_0085: {
                    jsonSerializer.serializeWithType(value, jsonGenerator, serializerProvider, valueTypeSerializer);
                }
            }
        }
    }
    
    public void serializeTypedContents(final List<?> list, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        final int size = list.size();
        if (size != 0) {
            while (true) {
                final int n = 0;
                int n2 = 0;
                int n3 = n;
                while (true) {
                    Label_0217: {
                        while (true) {
                            PropertySerializerMap dynamicSerializers = null;
                            Class<?> class1 = null;
                            Label_0199: {
                                try {
                                    final TypeSerializer valueTypeSerializer = this._valueTypeSerializer;
                                    n3 = n;
                                    dynamicSerializers = this._dynamicSerializers;
                                    if (n2 >= size) {
                                        break;
                                    }
                                    n3 = n2;
                                    final Object value = list.get(n2);
                                    if (value == null) {
                                        n3 = n2;
                                        serializerProvider.defaultSerializeNull(jsonGenerator);
                                        break Label_0217;
                                    }
                                    n3 = n2;
                                    class1 = value.getClass();
                                    n3 = n2;
                                    final JsonSerializer<Object> serializer = dynamicSerializers.serializerFor(class1);
                                    PropertySerializerMap dynamicSerializers2 = dynamicSerializers;
                                    JsonSerializer<Object> jsonSerializer;
                                    if ((jsonSerializer = serializer) == null) {
                                        n3 = n2;
                                        if (!this._elementType.hasGenericTypes()) {
                                            break Label_0199;
                                        }
                                        n3 = n2;
                                        final JsonSerializer<Object> jsonSerializer2 = this._findAndAddDynamic(dynamicSerializers, serializerProvider.constructSpecializedType(this._elementType, class1), serializerProvider);
                                        n3 = n2;
                                        dynamicSerializers2 = this._dynamicSerializers;
                                        jsonSerializer = jsonSerializer2;
                                    }
                                    n3 = n2;
                                    jsonSerializer.serializeWithType(value, jsonGenerator, serializerProvider, valueTypeSerializer);
                                    dynamicSerializers = dynamicSerializers2;
                                    break Label_0217;
                                }
                                catch (Exception ex) {
                                    this.wrapAndThrow(serializerProvider, ex, list, n3);
                                    return;
                                }
                            }
                            n3 = n2;
                            final JsonSerializer<Object> jsonSerializer2 = this._findAndAddDynamic(dynamicSerializers, class1, serializerProvider);
                            continue;
                        }
                    }
                    ++n2;
                    continue;
                }
            }
        }
    }
    
    @Override
    public IndexedListSerializer withResolved(final BeanProperty beanProperty, final TypeSerializer typeSerializer, final JsonSerializer<?> jsonSerializer) {
        return new IndexedListSerializer(this, beanProperty, typeSerializer, jsonSerializer);
    }
}
