// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public final class FailingSerializer extends StdSerializer<Object>
{
    final String _msg;
    
    public FailingSerializer(final String msg) {
        super(Object.class);
        this._msg = msg;
    }
    
    @Override
    public void serialize(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        throw new JsonGenerationException(this._msg);
    }
}
