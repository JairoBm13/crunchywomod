// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.JavaType;
import java.util.Map;
import java.io.Serializable;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class AbstractDeserializer extends JsonDeserializer<Object> implements Serializable
{
    protected final boolean _acceptBoolean;
    protected final boolean _acceptDouble;
    protected final boolean _acceptInt;
    protected final boolean _acceptString;
    protected final Map<String, SettableBeanProperty> _backRefProperties;
    protected final JavaType _baseType;
    protected final ObjectIdReader _objectIdReader;
    
    public AbstractDeserializer(final BeanDeserializerBuilder beanDeserializerBuilder, final BeanDescription beanDescription, final Map<String, SettableBeanProperty> backRefProperties) {
        final boolean b = false;
        this._baseType = beanDescription.getType();
        this._objectIdReader = beanDeserializerBuilder.getObjectIdReader();
        this._backRefProperties = backRefProperties;
        final Class<?> rawClass = this._baseType.getRawClass();
        this._acceptString = rawClass.isAssignableFrom(String.class);
        this._acceptBoolean = (rawClass == Boolean.TYPE || rawClass.isAssignableFrom(Boolean.class));
        this._acceptInt = (rawClass == Integer.TYPE || rawClass.isAssignableFrom(Integer.class));
        boolean acceptDouble = false;
        Label_0119: {
            if (rawClass != Double.TYPE) {
                acceptDouble = b;
                if (!rawClass.isAssignableFrom(Double.class)) {
                    break Label_0119;
                }
            }
            acceptDouble = true;
        }
        this._acceptDouble = acceptDouble;
    }
    
    protected Object _deserializeFromObjectId(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final Object deserialize = this._objectIdReader.deserializer.deserialize(jsonParser, deserializationContext);
        final Object item = deserializationContext.findObjectId(deserialize, this._objectIdReader.generator).item;
        if (item == null) {
            throw new IllegalStateException("Could not resolve Object Id [" + deserialize + "] -- unresolved forward-reference?");
        }
        return item;
    }
    
    protected Object _deserializeIfNatural(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        switch (jsonParser.getCurrentToken()) {
            case VALUE_STRING: {
                if (this._acceptString) {
                    return jsonParser.getText();
                }
                break;
            }
            case VALUE_NUMBER_INT: {
                if (this._acceptInt) {
                    return jsonParser.getIntValue();
                }
                break;
            }
            case VALUE_NUMBER_FLOAT: {
                if (this._acceptDouble) {
                    return jsonParser.getDoubleValue();
                }
                break;
            }
            case VALUE_TRUE: {
                if (this._acceptBoolean) {
                    return Boolean.TRUE;
                }
                break;
            }
            case VALUE_FALSE: {
                if (this._acceptBoolean) {
                    return Boolean.FALSE;
                }
                break;
            }
        }
        return null;
    }
    
    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        throw deserializationContext.instantiationException(this._baseType.getRawClass(), "abstract types either need to be mapped to concrete types, have custom deserializer, or be instantiated with additional type information");
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        Label_0037: {
            if (this._objectIdReader == null) {
                break Label_0037;
            }
            final JsonToken currentToken = jsonParser.getCurrentToken();
            if (currentToken == null || !currentToken.isScalarValue()) {
                break Label_0037;
            }
            return this._deserializeFromObjectId(jsonParser, deserializationContext);
        }
        Object o;
        if ((o = this._deserializeIfNatural(jsonParser, deserializationContext)) == null) {
            return typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
        }
        return o;
    }
    
    public SettableBeanProperty findBackReference(final String s) {
        if (this._backRefProperties == null) {
            return null;
        }
        return this._backRefProperties.get(s);
    }
    
    @Override
    public boolean isCachable() {
        return true;
    }
}
