// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

public class UnwrappingBeanPropertyWriter extends BeanPropertyWriter
{
    protected final NameTransformer _nameTransformer;
    
    public UnwrappingBeanPropertyWriter(final BeanPropertyWriter beanPropertyWriter, final NameTransformer nameTransformer) {
        super(beanPropertyWriter);
        this._nameTransformer = nameTransformer;
    }
    
    private UnwrappingBeanPropertyWriter(final UnwrappingBeanPropertyWriter unwrappingBeanPropertyWriter, final NameTransformer nameTransformer, final SerializedString serializedString) {
        super(unwrappingBeanPropertyWriter, serializedString);
        this._nameTransformer = nameTransformer;
    }
    
    @Override
    protected JsonSerializer<Object> _findAndAddDynamic(final PropertySerializerMap propertySerializerMap, final Class<?> clazz, final SerializerProvider serializerProvider) throws JsonMappingException {
        JsonSerializer<Object> jsonSerializer;
        if (this._nonTrivialBaseType != null) {
            jsonSerializer = serializerProvider.findValueSerializer(serializerProvider.constructSpecializedType(this._nonTrivialBaseType, clazz), this);
        }
        else {
            jsonSerializer = serializerProvider.findValueSerializer(clazz, this);
        }
        NameTransformer nameTransformer = this._nameTransformer;
        if (jsonSerializer.isUnwrappingSerializer()) {
            nameTransformer = NameTransformer.chainedTransformer(nameTransformer, ((UnwrappingBeanSerializer)jsonSerializer)._nameTransformer);
        }
        final JsonSerializer<Object> unwrappingSerializer = jsonSerializer.unwrappingSerializer(nameTransformer);
        this._dynamicSerializers = this._dynamicSerializers.newWith(clazz, unwrappingSerializer);
        return unwrappingSerializer;
    }
    
    @Override
    public void assignSerializer(final JsonSerializer<Object> jsonSerializer) {
        super.assignSerializer(jsonSerializer);
        if (this._serializer != null) {
            NameTransformer nameTransformer = this._nameTransformer;
            if (this._serializer.isUnwrappingSerializer()) {
                nameTransformer = NameTransformer.chainedTransformer(nameTransformer, ((UnwrappingBeanSerializer)this._serializer)._nameTransformer);
            }
            this._serializer = this._serializer.unwrappingSerializer(nameTransformer);
        }
    }
    
    @Override
    public UnwrappingBeanPropertyWriter rename(final NameTransformer nameTransformer) {
        return new UnwrappingBeanPropertyWriter(this, NameTransformer.chainedTransformer(nameTransformer, this._nameTransformer), new SerializedString(nameTransformer.transform(this._name.getValue())));
    }
    
    @Override
    public void serializeAsField(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws Exception {
        final Object value = this.get(o);
        if (value != null) {
            JsonSerializer<Object> jsonSerializer;
            if ((jsonSerializer = this._serializer) == null) {
                final Class<?> class1 = value.getClass();
                final PropertySerializerMap dynamicSerializers = this._dynamicSerializers;
                if ((jsonSerializer = dynamicSerializers.serializerFor(class1)) == null) {
                    jsonSerializer = this._findAndAddDynamic(dynamicSerializers, class1, serializerProvider);
                }
            }
            if (this._suppressableValue != null) {
                if (UnwrappingBeanPropertyWriter.MARKER_FOR_EMPTY == this._suppressableValue) {
                    if (jsonSerializer.isEmpty(value)) {
                        return;
                    }
                }
                else if (this._suppressableValue.equals(value)) {
                    return;
                }
            }
            if (value == o) {
                this._handleSelfReference(o, jsonSerializer);
            }
            if (!jsonSerializer.isUnwrappingSerializer()) {
                jsonGenerator.writeFieldName(this._name);
            }
            if (this._typeSerializer == null) {
                jsonSerializer.serialize(value, jsonGenerator, serializerProvider);
                return;
            }
            jsonSerializer.serializeWithType(value, jsonGenerator, serializerProvider, this._typeSerializer);
        }
    }
}
