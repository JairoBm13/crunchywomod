// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;

public abstract class NonTypedScalarSerializerBase<T> extends StdScalarSerializer<T>
{
    protected NonTypedScalarSerializerBase(final Class<T> clazz) {
        super(clazz);
    }
    
    @Override
    public final void serializeWithType(final T t, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        this.serialize(t, jsonGenerator, serializerProvider);
    }
}
