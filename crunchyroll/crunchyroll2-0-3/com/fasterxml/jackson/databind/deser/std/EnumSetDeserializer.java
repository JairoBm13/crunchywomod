// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import java.util.EnumSet;

public class EnumSetDeserializer extends StdDeserializer<EnumSet<?>> implements ContextualDeserializer
{
    protected final Class<Enum> _enumClass;
    protected JsonDeserializer<Enum<?>> _enumDeserializer;
    protected final JavaType _enumType;
    
    public EnumSetDeserializer(final JavaType enumType, final JsonDeserializer<?> enumDeserializer) {
        super(EnumSet.class);
        this._enumType = enumType;
        this._enumClass = (Class<Enum>)enumType.getRawClass();
        this._enumDeserializer = (JsonDeserializer<Enum<?>>)enumDeserializer;
    }
    
    private EnumSet constructSet() {
        return EnumSet.noneOf(this._enumClass);
    }
    
    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
        final JsonDeserializer<Enum<?>> enumDeserializer = this._enumDeserializer;
        Object o;
        if (enumDeserializer == null) {
            o = deserializationContext.findContextualValueDeserializer(this._enumType, beanProperty);
        }
        else {
            o = enumDeserializer;
            if (enumDeserializer instanceof ContextualDeserializer) {
                o = ((ContextualDeserializer)enumDeserializer).createContextual(deserializationContext, beanProperty);
            }
        }
        return this.withDeserializer((JsonDeserializer<?>)o);
    }
    
    @Override
    public EnumSet<?> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (!jsonParser.isExpectedStartArrayToken()) {
            throw deserializationContext.mappingException(EnumSet.class);
        }
        final EnumSet constructSet = this.constructSet();
        while (true) {
            final JsonToken nextToken = jsonParser.nextToken();
            if (nextToken == JsonToken.END_ARRAY) {
                return (EnumSet<?>)constructSet;
            }
            if (nextToken == JsonToken.VALUE_NULL) {
                throw deserializationContext.mappingException(this._enumClass);
            }
            final Enum<?> enum1 = this._enumDeserializer.deserialize(jsonParser, deserializationContext);
            if (enum1 == null) {
                continue;
            }
            constructSet.add(enum1);
        }
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
    }
    
    @Override
    public boolean isCachable() {
        return true;
    }
    
    public EnumSetDeserializer withDeserializer(final JsonDeserializer<?> jsonDeserializer) {
        if (this._enumDeserializer == jsonDeserializer) {
            return this;
        }
        return new EnumSetDeserializer(this._enumType, jsonDeserializer);
    }
}
