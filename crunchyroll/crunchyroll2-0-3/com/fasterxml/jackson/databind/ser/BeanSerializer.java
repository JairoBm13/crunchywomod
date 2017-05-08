// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.impl.BeanAsArraySerializer;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;

public class BeanSerializer extends BeanSerializerBase
{
    public BeanSerializer(final JavaType javaType, final BeanSerializerBuilder beanSerializerBuilder, final BeanPropertyWriter[] array, final BeanPropertyWriter[] array2) {
        super(javaType, beanSerializerBuilder, array, array2);
    }
    
    protected BeanSerializer(final BeanSerializerBase beanSerializerBase, final ObjectIdWriter objectIdWriter) {
        super(beanSerializerBase, objectIdWriter);
    }
    
    protected BeanSerializer(final BeanSerializerBase beanSerializerBase, final String[] array) {
        super(beanSerializerBase, array);
    }
    
    public static BeanSerializer createDummy(final JavaType javaType) {
        return new BeanSerializer(javaType, null, BeanSerializer.NO_PROPS, null);
    }
    
    @Override
    protected BeanSerializerBase asArraySerializer() {
        BeanSerializerBase beanSerializerBase = this;
        if (this._objectIdWriter == null) {
            beanSerializerBase = this;
            if (this._anyGetterWriter == null) {
                beanSerializerBase = this;
                if (this._propertyFilterId == null) {
                    beanSerializerBase = new BeanAsArraySerializer(this);
                }
            }
        }
        return beanSerializerBase;
    }
    
    @Override
    public final void serialize(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (this._objectIdWriter != null) {
            this._serializeWithObjectId(o, jsonGenerator, serializerProvider, true);
            return;
        }
        jsonGenerator.writeStartObject();
        if (this._propertyFilterId != null) {
            this.serializeFieldsFiltered(o, jsonGenerator, serializerProvider);
        }
        else {
            this.serializeFields(o, jsonGenerator, serializerProvider);
        }
        jsonGenerator.writeEndObject();
    }
    
    @Override
    public String toString() {
        return "BeanSerializer for " + this.handledType().getName();
    }
    
    @Override
    public JsonSerializer<Object> unwrappingSerializer(final NameTransformer nameTransformer) {
        return new UnwrappingBeanSerializer(this, nameTransformer);
    }
    
    @Override
    protected BeanSerializer withIgnorals(final String[] array) {
        return new BeanSerializer(this, array);
    }
    
    @Override
    public BeanSerializer withObjectIdWriter(final ObjectIdWriter objectIdWriter) {
        return new BeanSerializer(this, objectIdWriter);
    }
}
