// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;

public abstract class TypeDeserializer
{
    public static Object deserializeIfNatural(final JsonParser jsonParser, final DeserializationContext deserializationContext, final JavaType javaType) throws IOException, JsonProcessingException {
        return deserializeIfNatural(jsonParser, deserializationContext, javaType.getRawClass());
    }
    
    public static Object deserializeIfNatural(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Class<?> clazz) throws IOException, JsonProcessingException {
        final JsonToken currentToken = jsonParser.getCurrentToken();
        if (currentToken != null) {
            switch (currentToken) {
                default: {
                    return null;
                }
                case VALUE_STRING: {
                    if (clazz.isAssignableFrom(String.class)) {
                        return jsonParser.getText();
                    }
                    break;
                }
                case VALUE_NUMBER_INT: {
                    if (clazz.isAssignableFrom(Integer.class)) {
                        return jsonParser.getIntValue();
                    }
                    break;
                }
                case VALUE_NUMBER_FLOAT: {
                    if (clazz.isAssignableFrom(Double.class)) {
                        return jsonParser.getDoubleValue();
                    }
                    break;
                }
                case VALUE_TRUE: {
                    if (clazz.isAssignableFrom(Boolean.class)) {
                        return Boolean.TRUE;
                    }
                    break;
                }
                case VALUE_FALSE: {
                    if (clazz.isAssignableFrom(Boolean.class)) {
                        return Boolean.FALSE;
                    }
                    break;
                }
            }
        }
        return null;
    }
    
    public abstract Object deserializeTypedFromAny(final JsonParser p0, final DeserializationContext p1) throws IOException, JsonProcessingException;
    
    public abstract Object deserializeTypedFromArray(final JsonParser p0, final DeserializationContext p1) throws IOException, JsonProcessingException;
    
    public abstract Object deserializeTypedFromObject(final JsonParser p0, final DeserializationContext p1) throws IOException, JsonProcessingException;
    
    public abstract Object deserializeTypedFromScalar(final JsonParser p0, final DeserializationContext p1) throws IOException, JsonProcessingException;
    
    public abstract TypeDeserializer forProperty(final BeanProperty p0);
    
    public abstract Class<?> getDefaultImpl();
    
    public abstract String getPropertyName();
    
    public abstract TypeIdResolver getTypeIdResolver();
    
    public abstract JsonTypeInfo.As getTypeInclusion();
}
