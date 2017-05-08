// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import java.util.Collection;

@JacksonStdImpl
public final class StringCollectionDeserializer extends ContainerDeserializerBase<Collection<String>> implements ContextualDeserializer
{
    protected final JavaType _collectionType;
    protected final JsonDeserializer<Object> _delegateDeserializer;
    protected final JsonDeserializer<String> _valueDeserializer;
    protected final ValueInstantiator _valueInstantiator;
    
    public StringCollectionDeserializer(final JavaType javaType, final JsonDeserializer<?> jsonDeserializer, final ValueInstantiator valueInstantiator) {
        this(javaType, valueInstantiator, null, jsonDeserializer);
    }
    
    protected StringCollectionDeserializer(final JavaType collectionType, final ValueInstantiator valueInstantiator, final JsonDeserializer<?> delegateDeserializer, final JsonDeserializer<?> valueDeserializer) {
        super(collectionType.getRawClass());
        this._collectionType = collectionType;
        this._valueDeserializer = (JsonDeserializer<String>)valueDeserializer;
        this._valueInstantiator = valueInstantiator;
        this._delegateDeserializer = (JsonDeserializer<Object>)delegateDeserializer;
    }
    
    private Collection<String> deserializeUsingCustom(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Collection<String> collection, final JsonDeserializer<String> jsonDeserializer) throws IOException, JsonProcessingException {
        while (true) {
            final JsonToken nextToken = jsonParser.nextToken();
            if (nextToken == JsonToken.END_ARRAY) {
                break;
            }
            String s;
            if (nextToken == JsonToken.VALUE_NULL) {
                s = null;
            }
            else {
                s = jsonDeserializer.deserialize(jsonParser, deserializationContext);
            }
            collection.add(s);
        }
        return collection;
    }
    
    private final Collection<String> handleNonArray(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Collection<String> collection) throws IOException, JsonProcessingException {
        if (!deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            throw deserializationContext.mappingException(this._collectionType.getRawClass());
        }
        final JsonDeserializer<String> valueDeserializer = this._valueDeserializer;
        String parseString;
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            parseString = null;
        }
        else if (valueDeserializer == null) {
            parseString = this._parseString(jsonParser, deserializationContext);
        }
        else {
            parseString = valueDeserializer.deserialize(jsonParser, deserializationContext);
        }
        collection.add(parseString);
        return collection;
    }
    
    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
        final JsonDeserializer<?> jsonDeserializer = null;
        JsonDeserializer<?> deserializer;
        if (this._valueInstantiator != null && this._valueInstantiator.getDelegateCreator() != null) {
            deserializer = this.findDeserializer(deserializationContext, this._valueInstantiator.getDelegateType(deserializationContext.getConfig()), beanProperty);
        }
        else {
            deserializer = null;
        }
        final JsonDeserializer<String> valueDeserializer = this._valueDeserializer;
        JsonDeserializer<?> jsonDeserializer2;
        if (valueDeserializer == null) {
            if ((jsonDeserializer2 = this.findConvertingContentDeserializer(deserializationContext, beanProperty, valueDeserializer)) == null) {
                jsonDeserializer2 = deserializationContext.findContextualValueDeserializer(this._collectionType.getContentType(), beanProperty);
            }
        }
        else {
            jsonDeserializer2 = valueDeserializer;
            if (valueDeserializer instanceof ContextualDeserializer) {
                jsonDeserializer2 = ((ContextualDeserializer)valueDeserializer).createContextual(deserializationContext, beanProperty);
            }
        }
        JsonDeserializer<?> jsonDeserializer3;
        if (this.isDefaultDeserializer(jsonDeserializer2)) {
            jsonDeserializer3 = jsonDeserializer;
        }
        else {
            jsonDeserializer3 = jsonDeserializer2;
        }
        return this.withResolved(deserializer, jsonDeserializer3);
    }
    
    @Override
    public Collection<String> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._delegateDeserializer != null) {
            return (Collection<String>)this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
        }
        return this.deserialize(jsonParser, deserializationContext, (Collection<String>)this._valueInstantiator.createUsingDefault(deserializationContext));
    }
    
    @Override
    public Collection<String> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Collection<String> collection) throws IOException, JsonProcessingException {
        Collection<String> handleNonArray;
        if (!jsonParser.isExpectedStartArrayToken()) {
            handleNonArray = this.handleNonArray(jsonParser, deserializationContext, collection);
        }
        else {
            if (this._valueDeserializer != null) {
                return this.deserializeUsingCustom(jsonParser, deserializationContext, collection, this._valueDeserializer);
            }
            while (true) {
                final JsonToken nextToken = jsonParser.nextToken();
                handleNonArray = collection;
                if (nextToken == JsonToken.END_ARRAY) {
                    break;
                }
                String parseString;
                if (nextToken == JsonToken.VALUE_NULL) {
                    parseString = null;
                }
                else {
                    parseString = this._parseString(jsonParser, deserializationContext);
                }
                collection.add(parseString);
            }
        }
        return handleNonArray;
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        return typeDeserializer.deserializeTypedFromArray(jsonParser, deserializationContext);
    }
    
    @Override
    public JsonDeserializer<Object> getContentDeserializer() {
        return (JsonDeserializer<Object>)this._valueDeserializer;
    }
    
    protected StringCollectionDeserializer withResolved(final JsonDeserializer<?> jsonDeserializer, final JsonDeserializer<?> jsonDeserializer2) {
        if (this._valueDeserializer == jsonDeserializer2 && this._delegateDeserializer == jsonDeserializer) {
            return this;
        }
        return new StringCollectionDeserializer(this._collectionType, this._valueInstantiator, jsonDeserializer, jsonDeserializer2);
    }
}
