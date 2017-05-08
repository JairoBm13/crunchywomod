// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

@JacksonStdImpl
public class ToStringSerializer extends StdSerializer<Object>
{
    public static final ToStringSerializer instance;
    
    static {
        instance = new ToStringSerializer();
    }
    
    public ToStringSerializer() {
        super(Object.class);
    }
    
    @Override
    public boolean isEmpty(final Object o) {
        if (o != null) {
            final String string = o.toString();
            if (string != null && string.length() != 0) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public void serialize(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        jsonGenerator.writeString(o.toString());
    }
    
    @Override
    public void serializeWithType(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        typeSerializer.writeTypePrefixForScalar(o, jsonGenerator);
        this.serialize(o, jsonGenerator, serializerProvider);
        typeSerializer.writeTypeSuffixForScalar(o, jsonGenerator);
    }
}
