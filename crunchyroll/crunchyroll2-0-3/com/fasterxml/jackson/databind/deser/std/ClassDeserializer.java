// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;

@JacksonStdImpl
public class ClassDeserializer extends StdScalarDeserializer<Class<?>>
{
    public static final ClassDeserializer instance;
    
    static {
        instance = new ClassDeserializer();
    }
    
    public ClassDeserializer() {
        super(Class.class);
    }
    
    @Override
    public Class<?> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken == JsonToken.VALUE_STRING) {
            final String trim = jsonParser.getText().trim();
            try {
                return deserializationContext.findClass(trim);
            }
            catch (Exception ex) {
                throw deserializationContext.instantiationException(this._valueClass, ClassUtil.getRootCause(ex));
            }
        }
        throw deserializationContext.mappingException(this._valueClass, currentToken);
    }
}
