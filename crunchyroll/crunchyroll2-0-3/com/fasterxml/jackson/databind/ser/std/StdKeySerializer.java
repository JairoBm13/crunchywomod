// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import java.util.Date;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;

public class StdKeySerializer extends StdSerializer<Object>
{
    static final StdKeySerializer instace;
    
    static {
        instace = new StdKeySerializer();
    }
    
    public StdKeySerializer() {
        super(Object.class);
    }
    
    @Override
    public void serialize(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        if (o instanceof Date) {
            serializerProvider.defaultSerializeDateKey((Date)o, jsonGenerator);
            return;
        }
        jsonGenerator.writeFieldName(o.toString());
    }
}
