// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

public class UnwrappingBeanSerializer extends BeanSerializerBase
{
    protected final NameTransformer _nameTransformer;
    
    public UnwrappingBeanSerializer(final UnwrappingBeanSerializer unwrappingBeanSerializer, final ObjectIdWriter objectIdWriter) {
        super(unwrappingBeanSerializer, objectIdWriter);
        this._nameTransformer = unwrappingBeanSerializer._nameTransformer;
    }
    
    protected UnwrappingBeanSerializer(final UnwrappingBeanSerializer unwrappingBeanSerializer, final String[] array) {
        super(unwrappingBeanSerializer, array);
        this._nameTransformer = unwrappingBeanSerializer._nameTransformer;
    }
    
    public UnwrappingBeanSerializer(final BeanSerializerBase beanSerializerBase, final NameTransformer nameTransformer) {
        super(beanSerializerBase, nameTransformer);
        this._nameTransformer = nameTransformer;
    }
    
    @Override
    protected BeanSerializerBase asArraySerializer() {
        return this;
    }
    
    @Override
    public boolean isUnwrappingSerializer() {
        return true;
    }
    
    @Override
    public final void serialize(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._objectIdWriter != null) {
            this._serializeWithObjectId(o, jsonGenerator, serializerProvider, false);
            return;
        }
        if (this._propertyFilterId != null) {
            this.serializeFieldsFiltered(o, jsonGenerator, serializerProvider);
            return;
        }
        this.serializeFields(o, jsonGenerator, serializerProvider);
    }
    
    @Override
    public String toString() {
        return "UnwrappingBeanSerializer for " + this.handledType().getName();
    }
    
    @Override
    public JsonSerializer<Object> unwrappingSerializer(final NameTransformer nameTransformer) {
        return new UnwrappingBeanSerializer(this, nameTransformer);
    }
    
    @Override
    protected UnwrappingBeanSerializer withIgnorals(final String[] array) {
        return new UnwrappingBeanSerializer(this, array);
    }
    
    @Override
    public UnwrappingBeanSerializer withObjectIdWriter(final ObjectIdWriter objectIdWriter) {
        return new UnwrappingBeanSerializer(this, objectIdWriter);
    }
}
