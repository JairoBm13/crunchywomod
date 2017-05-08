// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;

public final class WritableObjectId
{
    public final ObjectIdGenerator<?> generator;
    public Object id;
    protected boolean idWritten;
    
    public WritableObjectId(final ObjectIdGenerator<?> generator) {
        this.idWritten = false;
        this.generator = generator;
    }
    
    public Object generateId(Object generateId) {
        generateId = this.generator.generateId(generateId);
        return this.id = generateId;
    }
    
    public void writeAsField(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final ObjectIdWriter objectIdWriter) throws IOException, JsonGenerationException {
        final SerializedString propertyName = objectIdWriter.propertyName;
        this.idWritten = true;
        if (propertyName != null) {
            jsonGenerator.writeFieldName(propertyName);
            objectIdWriter.serializer.serialize(this.id, jsonGenerator, serializerProvider);
        }
    }
    
    public boolean writeAsId(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final ObjectIdWriter objectIdWriter) throws IOException, JsonGenerationException {
        if (this.id != null && (this.idWritten || objectIdWriter.alwaysAsId)) {
            objectIdWriter.serializer.serialize(this.id, jsonGenerator, serializerProvider);
            return true;
        }
        return false;
    }
}
