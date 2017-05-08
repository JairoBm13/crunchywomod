// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import java.io.IOException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import java.net.InetAddress;

public class InetAddressSerializer extends StdScalarSerializer<InetAddress>
{
    public static final InetAddressSerializer instance;
    
    static {
        instance = new InetAddressSerializer();
    }
    
    public InetAddressSerializer() {
        super(InetAddress.class);
    }
    
    @Override
    public void serialize(final InetAddress inetAddress, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonGenerationException {
        final String trim = inetAddress.toString().trim();
        final int index = trim.indexOf(47);
        String s = trim;
        if (index >= 0) {
            if (index == 0) {
                s = trim.substring(1);
            }
            else {
                s = trim.substring(0, index);
            }
        }
        jsonGenerator.writeString(s);
    }
    
    @Override
    public void serializeWithType(final InetAddress inetAddress, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final TypeSerializer typeSerializer) throws IOException, JsonGenerationException {
        typeSerializer.writeTypePrefixForScalar(inetAddress, jsonGenerator, InetAddress.class);
        this.serialize(inetAddress, jsonGenerator, serializerProvider);
        typeSerializer.writeTypeSuffixForScalar(inetAddress, jsonGenerator);
    }
}
