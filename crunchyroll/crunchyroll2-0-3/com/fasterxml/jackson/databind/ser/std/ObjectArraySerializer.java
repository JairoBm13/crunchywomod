// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import java.lang.reflect.InvocationTargetException;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.ser.ContainerSerializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

@JacksonStdImpl
public class ObjectArraySerializer extends ArraySerializerBase<Object[]> implements ContextualSerializer
{
    protected PropertySerializerMap _dynamicSerializers;
    protected JsonSerializer<Object> _elementSerializer;
    protected final JavaType _elementType;
    protected final boolean _staticTyping;
    protected final TypeSerializer _valueTypeSerializer;
    
    public ObjectArraySerializer(final JavaType elementType, final boolean staticTyping, final TypeSerializer valueTypeSerializer, final JsonSerializer<Object> elementSerializer) {
        super(Object[].class, null);
        this._elementType = elementType;
        this._staticTyping = staticTyping;
        this._valueTypeSerializer = valueTypeSerializer;
        this._dynamicSerializers = PropertySerializerMap.emptyMap();
        this._elementSerializer = elementSerializer;
    }
    
    public ObjectArraySerializer(final ObjectArraySerializer objectArraySerializer, final BeanProperty beanProperty, final TypeSerializer valueTypeSerializer, final JsonSerializer<?> elementSerializer) {
        super(objectArraySerializer, beanProperty);
        this._elementType = objectArraySerializer._elementType;
        this._valueTypeSerializer = valueTypeSerializer;
        this._staticTyping = objectArraySerializer._staticTyping;
        this._dynamicSerializers = objectArraySerializer._dynamicSerializers;
        this._elementSerializer = (JsonSerializer<Object>)elementSerializer;
    }
    
    protected final JsonSerializer<Object> _findAndAddDynamic(final PropertySerializerMap propertySerializerMap, final JavaType javaType, final SerializerProvider serializerProvider) throws JsonMappingException {
        final PropertySerializerMap.SerializerAndMapResult andAddSerializer = propertySerializerMap.findAndAddSerializer(javaType, serializerProvider, this._property);
        if (propertySerializerMap != andAddSerializer.map) {
            this._dynamicSerializers = andAddSerializer.map;
        }
        return andAddSerializer.serializer;
    }
    
    protected final JsonSerializer<Object> _findAndAddDynamic(final PropertySerializerMap propertySerializerMap, final Class<?> clazz, final SerializerProvider serializerProvider) throws JsonMappingException {
        final PropertySerializerMap.SerializerAndMapResult andAddSerializer = propertySerializerMap.findAndAddSerializer(clazz, serializerProvider, this._property);
        if (propertySerializerMap != andAddSerializer.map) {
            this._dynamicSerializers = andAddSerializer.map;
        }
        return andAddSerializer.serializer;
    }
    
    public ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer typeSerializer) {
        return new ObjectArraySerializer(this._elementType, this._staticTyping, typeSerializer, this._elementSerializer);
    }
    
    @Override
    public JsonSerializer<?> createContextual(final SerializerProvider serializerProvider, final BeanProperty beanProperty) throws JsonMappingException {
        TypeSerializer typeSerializer = this._valueTypeSerializer;
        if (typeSerializer != null) {
            typeSerializer = typeSerializer.forProperty(beanProperty);
        }
        JsonSerializer<?> serializerInstance;
        final JsonSerializer<?> jsonSerializer = serializerInstance = null;
        if (beanProperty != null) {
            final AnnotatedMember member = beanProperty.getMember();
            serializerInstance = jsonSerializer;
            if (member != null) {
                final Object contentSerializer = serializerProvider.getAnnotationIntrospector().findContentSerializer(member);
                serializerInstance = jsonSerializer;
                if (contentSerializer != null) {
                    serializerInstance = serializerProvider.serializerInstance(member, contentSerializer);
                }
            }
        }
        JsonSerializer<?> elementSerializer;
        if ((elementSerializer = serializerInstance) == null) {
            elementSerializer = this._elementSerializer;
        }
        final JsonSerializer<?> convertingContentSerializer = this.findConvertingContentSerializer(serializerProvider, beanProperty, elementSerializer);
        JsonSerializer<?> jsonSerializer2;
        if (convertingContentSerializer == null) {
            jsonSerializer2 = convertingContentSerializer;
            if (this._elementType != null) {
                if (!this._staticTyping) {
                    jsonSerializer2 = convertingContentSerializer;
                    if (!this.hasContentTypeAnnotation(serializerProvider, beanProperty)) {
                        return this.withResolved(beanProperty, typeSerializer, jsonSerializer2);
                    }
                }
                jsonSerializer2 = serializerProvider.findValueSerializer(this._elementType, beanProperty);
            }
        }
        else {
            jsonSerializer2 = convertingContentSerializer;
            if (convertingContentSerializer instanceof ContextualSerializer) {
                jsonSerializer2 = ((ContextualSerializer)convertingContentSerializer).createContextual(serializerProvider, beanProperty);
            }
        }
        return this.withResolved(beanProperty, typeSerializer, jsonSerializer2);
    }
    
    @Override
    public boolean hasSingleElement(final Object[] array) {
        return array.length == 1;
    }
    
    @Override
    public boolean isEmpty(final Object[] array) {
        return array == null || array.length == 0;
    }
    
    public void serializeContents(final Object[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        final int length = array.length;
        if (length != 0) {
            if (this._elementSerializer != null) {
                this.serializeContentsUsing(array, jsonGenerator, serializerProvider, this._elementSerializer);
                return;
            }
            if (this._valueTypeSerializer != null) {
                this.serializeTypedContents(array, jsonGenerator, serializerProvider);
                return;
            }
            while (true) {
                int n = 0;
                while (true) {
                    try {
                        final PropertySerializerMap dynamicSerializers = this._dynamicSerializers;
                        if (n >= length) {
                            break;
                        }
                        final Object o = array[n];
                        if (o == null) {
                            serializerProvider.defaultSerializeNull(jsonGenerator);
                        }
                        else {
                            final Class<?> class1 = o.getClass();
                            JsonSerializer<Object> jsonSerializer;
                            if ((jsonSerializer = dynamicSerializers.serializerFor(class1)) == null) {
                                if (!this._elementType.hasGenericTypes()) {
                                    goto Label_0202;
                                }
                                jsonSerializer = this._findAndAddDynamic(dynamicSerializers, serializerProvider.constructSpecializedType(this._elementType, class1), serializerProvider);
                            }
                            jsonSerializer.serialize(o, jsonGenerator, serializerProvider);
                        }
                    }
                    catch (IOException ex) {
                        throw ex;
                    }
                    catch (Exception t) {
                        goto Label_0224;
                    }
                    ++n;
                    continue;
                }
            }
        }
    }
    
    public void serializeContentsUsing(final Object[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final JsonSerializer<Object> jsonSerializer) throws IOException, JsonGenerationException {
        final int length = array.length;
        final TypeSerializer valueTypeSerializer = this._valueTypeSerializer;
        for (int i = 0; i < length; ++i) {
            final Object o = array[i];
            Label_0039: {
                if (o != null) {
                    break Label_0039;
                }
                try {
                    serializerProvider.defaultSerializeNull(jsonGenerator);
                    continue;
                    // iftrue(Label_0059:, valueTypeSerializer != null)
                    jsonSerializer.serialize(o, jsonGenerator, serializerProvider);
                    continue;
                }
                catch (IOException ex) {
                    throw ex;
                }
                catch (Exception cause) {
                    while (cause instanceof InvocationTargetException && cause.getCause() != null) {
                        cause = cause.getCause();
                    }
                    if (cause instanceof Error) {
                        throw (Error)cause;
                    }
                    throw JsonMappingException.wrapWithPath(cause, o, i);
                }
            }
            break;
        }
    }
    
    public void serializeTypedContents(final Object[] array, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        while (true) {
            final int length = array.length;
            final TypeSerializer valueTypeSerializer = this._valueTypeSerializer;
            int n = 0;
            while (true) {
                Label_0192: {
                    try {
                        final PropertySerializerMap dynamicSerializers = this._dynamicSerializers;
                        if (n >= length) {
                            goto Label_0187;
                        }
                        final Object o = array[n];
                        if (o == null) {
                            serializerProvider.defaultSerializeNull(jsonGenerator);
                            break Label_0192;
                        }
                        final Class<?> class1 = o.getClass();
                        JsonSerializer<Object> jsonSerializer;
                        if ((jsonSerializer = dynamicSerializers.serializerFor(class1)) == null) {
                            jsonSerializer = this._findAndAddDynamic(dynamicSerializers, class1, serializerProvider);
                        }
                        jsonSerializer.serializeWithType(o, jsonGenerator, serializerProvider, valueTypeSerializer);
                        break Label_0192;
                    }
                    catch (IOException ex) {
                        throw ex;
                    }
                    catch (Exception ex2) {}
                    goto Label_0144;
                }
                ++n;
                continue;
            }
        }
    }
    
    public ObjectArraySerializer withResolved(final BeanProperty beanProperty, final TypeSerializer typeSerializer, final JsonSerializer<?> jsonSerializer) {
        if (this._property == beanProperty && jsonSerializer == this._elementSerializer && this._valueTypeSerializer == typeSerializer) {
            return this;
        }
        return new ObjectArraySerializer(this, beanProperty, typeSerializer, jsonSerializer);
    }
}
