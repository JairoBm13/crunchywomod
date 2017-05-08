// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import com.fasterxml.jackson.core.JsonToken;
import java.util.Collection;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;

public class ArrayBlockingQueueDeserializer extends CollectionDeserializer
{
    public ArrayBlockingQueueDeserializer(final JavaType javaType, final JsonDeserializer<Object> jsonDeserializer, final TypeDeserializer typeDeserializer, final ValueInstantiator valueInstantiator, final JsonDeserializer<Object> jsonDeserializer2) {
        super(javaType, jsonDeserializer, typeDeserializer, valueInstantiator, jsonDeserializer2);
    }
    
    @Override
    public Collection<Object> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._delegateDeserializer != null) {
            return (Collection<Object>)this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
        }
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
            final String text = jsonParser.getText();
            if (text.length() == 0) {
                return (Collection<Object>)this._valueInstantiator.createFromString(deserializationContext, text);
            }
        }
        return this.deserialize(jsonParser, deserializationContext, null);
    }
    
    @Override
    public Collection<Object> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Collection<Object> collection) throws IOException, JsonProcessingException {
        if (!jsonParser.isExpectedStartArrayToken()) {
            return this.handleNonArray(jsonParser, deserializationContext, new ArrayBlockingQueue<Object>(1));
        }
        final ArrayList<Object> list = new ArrayList<Object>();
        final JsonDeserializer<Object> valueDeserializer = this._valueDeserializer;
        final TypeDeserializer valueTypeDeserializer = this._valueTypeDeserializer;
        while (true) {
            final JsonToken nextToken = jsonParser.nextToken();
            if (nextToken == JsonToken.END_ARRAY) {
                break;
            }
            Object o;
            if (nextToken == JsonToken.VALUE_NULL) {
                o = null;
            }
            else if (valueTypeDeserializer == null) {
                o = valueDeserializer.deserialize(jsonParser, deserializationContext);
            }
            else {
                o = valueDeserializer.deserializeWithType(jsonParser, deserializationContext, valueTypeDeserializer);
            }
            list.add(o);
        }
        if (collection != null) {
            collection.addAll(list);
            return collection;
        }
        return new ArrayBlockingQueue<Object>(list.size(), false, list);
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
    }
    
    @Override
    protected ArrayBlockingQueueDeserializer withResolved(final JsonDeserializer<?> jsonDeserializer, final JsonDeserializer<?> jsonDeserializer2, final TypeDeserializer typeDeserializer) {
        if (jsonDeserializer == this._delegateDeserializer && jsonDeserializer2 == this._valueDeserializer && typeDeserializer == this._valueTypeDeserializer) {
            return this;
        }
        return new ArrayBlockingQueueDeserializer(this._collectionType, (JsonDeserializer<Object>)jsonDeserializer2, typeDeserializer, this._valueInstantiator, (JsonDeserializer<Object>)jsonDeserializer);
    }
}
