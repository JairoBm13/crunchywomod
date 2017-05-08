// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;

public abstract class FromStringDeserializer<T> extends StdScalarDeserializer<T>
{
    protected FromStringDeserializer(final Class<?> clazz) {
        super(clazz);
    }
    
    protected abstract T _deserialize(final String p0, final DeserializationContext p1) throws IOException, JsonProcessingException;
    
    protected T _deserializeEmbedded(final Object o, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        throw deserializationContext.mappingException("Don't know how to convert embedded Object of type " + o.getClass().getName() + " into " + this._valueClass.getName());
    }
    
    @Override
    public final T deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final T t = null;
        Object o = jsonParser.getValueAsString();
        Label_0069: {
            if (o == null) {
                break Label_0069;
            }
            T deserialize = t;
            Label_0042: {
                if (((String)o).length() != 0) {
                    o = ((String)o).trim();
                    if (((String)o).length() != 0) {
                        break Label_0042;
                    }
                    deserialize = t;
                }
                Label_0040: {
                    return deserialize;
                }
                try {
                    if ((deserialize = this._deserialize((String)o, deserializationContext)) == null) {
                        throw deserializationContext.weirdStringException((String)o, this._valueClass, "not a valid textual representation");
                    }
                    return deserialize;
                    Label_0118:
                    throw deserializationContext.mappingException(this._valueClass);
                    // iftrue(Label_0040:, o == null)
                    // iftrue(Label_0118:, jsonParser.getCurrentToken() != JsonToken.VALUE_EMBEDDED_OBJECT)
                    Block_6: {
                        while (true) {
                            o = jsonParser.getEmbeddedObject();
                            deserialize = t;
                            break Block_6;
                            continue;
                        }
                        Label_0110:
                        return this._deserializeEmbedded(o, deserializationContext);
                    }
                    // iftrue(Label_0110:, !this._valueClass.isAssignableFrom((Class<?>)o.getClass()))
                    return (T)o;
                }
                catch (IllegalArgumentException ex) {
                    throw deserializationContext.weirdStringException((String)o, this._valueClass, "not a valid textual representation");
                }
            }
        }
    }
}
