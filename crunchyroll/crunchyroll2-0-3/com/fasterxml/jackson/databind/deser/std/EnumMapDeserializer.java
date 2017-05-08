// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import java.util.EnumMap;

public class EnumMapDeserializer extends StdDeserializer<EnumMap<?, ?>> implements ContextualDeserializer
{
    protected final Class<?> _enumClass;
    protected JsonDeserializer<Enum<?>> _keyDeserializer;
    protected final JavaType _mapType;
    protected JsonDeserializer<Object> _valueDeserializer;
    protected final TypeDeserializer _valueTypeDeserializer;
    
    public EnumMapDeserializer(final JavaType mapType, final JsonDeserializer<?> keyDeserializer, final JsonDeserializer<?> valueDeserializer, final TypeDeserializer valueTypeDeserializer) {
        super(EnumMap.class);
        this._mapType = mapType;
        this._enumClass = mapType.getKeyType().getRawClass();
        this._keyDeserializer = (JsonDeserializer<Enum<?>>)keyDeserializer;
        this._valueDeserializer = (JsonDeserializer<Object>)valueDeserializer;
        this._valueTypeDeserializer = valueTypeDeserializer;
    }
    
    private EnumMap<?, ?> constructMap() {
        return new EnumMap<Object, Object>(this._enumClass);
    }
    
    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
        Object o = this._keyDeserializer;
        if (o == null) {
            o = deserializationContext.findContextualValueDeserializer(this._mapType.getKeyType(), beanProperty);
        }
        final JsonDeserializer<Object> valueDeserializer = this._valueDeserializer;
        JsonDeserializer<?> jsonDeserializer;
        if (valueDeserializer == null) {
            jsonDeserializer = deserializationContext.findContextualValueDeserializer(this._mapType.getContentType(), beanProperty);
        }
        else {
            jsonDeserializer = valueDeserializer;
            if (valueDeserializer instanceof ContextualDeserializer) {
                jsonDeserializer = ((ContextualDeserializer)valueDeserializer).createContextual(deserializationContext, beanProperty);
            }
        }
        final TypeDeserializer valueTypeDeserializer = this._valueTypeDeserializer;
        TypeDeserializer forProperty;
        if ((forProperty = valueTypeDeserializer) != null) {
            forProperty = valueTypeDeserializer.forProperty(beanProperty);
        }
        return this.withResolved((JsonDeserializer<?>)o, jsonDeserializer, forProperty);
    }
    
    @Override
    public EnumMap<?, ?> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() != JsonToken.START_OBJECT) {
            throw deserializationContext.mappingException(EnumMap.class);
        }
        final EnumMap<?, ?> constructMap = this.constructMap();
        final JsonDeserializer<Object> valueDeserializer = this._valueDeserializer;
        final TypeDeserializer valueTypeDeserializer = this._valueTypeDeserializer;
        while (jsonParser.nextToken() != JsonToken.END_OBJECT) {
            final Enum<?> enum1 = this._keyDeserializer.deserialize(jsonParser, deserializationContext);
            if (enum1 == null) {
                if (!deserializationContext.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
                    while (true) {
                        while (true) {
                            Label_0173: {
                                try {
                                    if (jsonParser.hasCurrentToken()) {
                                        final String text = jsonParser.getText();
                                        throw deserializationContext.weirdStringException(text, this._enumClass, "value not one of declared Enum instance names");
                                    }
                                    break Label_0173;
                                }
                                catch (Exception ex) {
                                    final String text = null;
                                    throw deserializationContext.weirdStringException(text, this._enumClass, "value not one of declared Enum instance names");
                                }
                                break;
                            }
                            final String text = null;
                            continue;
                        }
                    }
                }
                jsonParser.nextToken();
                jsonParser.skipChildren();
            }
            else {
                Object o;
                if (jsonParser.nextToken() == JsonToken.VALUE_NULL) {
                    o = null;
                }
                else if (valueTypeDeserializer == null) {
                    o = valueDeserializer.deserialize(jsonParser, deserializationContext);
                }
                else {
                    o = valueDeserializer.deserializeWithType(jsonParser, deserializationContext, valueTypeDeserializer);
                }
                constructMap.put(enum1, o);
            }
        }
        return constructMap;
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
    }
    
    @Override
    public boolean isCachable() {
        return true;
    }
    
    public EnumMapDeserializer withResolved(final JsonDeserializer<?> jsonDeserializer, final JsonDeserializer<?> jsonDeserializer2, final TypeDeserializer typeDeserializer) {
        if (jsonDeserializer == this._keyDeserializer && jsonDeserializer2 == this._valueDeserializer && typeDeserializer == this._valueTypeDeserializer) {
            return this;
        }
        return new EnumMapDeserializer(this._mapType, jsonDeserializer, jsonDeserializer2, this._valueTypeDeserializer);
    }
}
