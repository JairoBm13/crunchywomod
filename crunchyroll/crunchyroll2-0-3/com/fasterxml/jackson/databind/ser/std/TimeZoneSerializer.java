// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.util.TimeZone;

public class TimeZoneSerializer extends StdScalarSerializer<TimeZone>
{
    public static final TimeZoneSerializer instance;
    
    static {
        instance = new TimeZoneSerializer();
    }
    
    public TimeZoneSerializer() {
        super(TimeZone.class);
    }
    
    @Override
    public void serialize(final TimeZone timeZone, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jsonGenerator.writeString(timeZone.getID());
    }
    
    @Override
    public void serializeWithType(final TimeZone timeZone, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        typeSerializer.writeTypePrefixForScalar(timeZone, jsonGenerator, TimeZone.class);
        this.serialize(timeZone, jsonGenerator, serializerProvider);
        typeSerializer.writeTypeSuffixForScalar(timeZone, jsonGenerator);
    }
}
