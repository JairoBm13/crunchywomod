// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

@JacksonStdImpl
public final class BooleanSerializer extends NonTypedScalarSerializerBase<Boolean>
{
    final boolean _forPrimitive;
    
    public BooleanSerializer(final boolean forPrimitive) {
        super(Boolean.class);
        this._forPrimitive = forPrimitive;
    }
    
    @Override
    public void serialize(final Boolean b, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jsonGenerator.writeBoolean(b);
    }
}
