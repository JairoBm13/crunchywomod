// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.JsonSerializable;

@JacksonStdImpl
public class SerializableSerializer extends StdSerializer<JsonSerializable>
{
    private static final AtomicReference<ObjectMapper> _mapperReference;
    public static final SerializableSerializer instance;
    
    static {
        instance = new SerializableSerializer();
        _mapperReference = new AtomicReference<ObjectMapper>();
    }
    
    protected SerializableSerializer() {
        super(JsonSerializable.class);
    }
    
    @Override
    public void serialize(final JsonSerializable jsonSerializable, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jsonSerializable.serialize(jsonGenerator, serializerProvider);
    }
    
    @Override
    public final void serializeWithType(final JsonSerializable jsonSerializable, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        jsonSerializable.serializeWithType(jsonGenerator, serializerProvider, typeSerializer);
    }
}
