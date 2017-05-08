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
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import java.util.Collection;

@JacksonStdImpl
public class CollectionDeserializer extends ContainerDeserializerBase<Collection<Object>> implements ContextualDeserializer
{
    protected final JavaType _collectionType;
    protected final JsonDeserializer<Object> _delegateDeserializer;
    protected final JsonDeserializer<Object> _valueDeserializer;
    protected final ValueInstantiator _valueInstantiator;
    protected final TypeDeserializer _valueTypeDeserializer;
    
    public CollectionDeserializer(final JavaType javaType, final JsonDeserializer<Object> jsonDeserializer, final TypeDeserializer typeDeserializer, final ValueInstantiator valueInstantiator) {
        this(javaType, jsonDeserializer, typeDeserializer, valueInstantiator, null);
    }
    
    protected CollectionDeserializer(final JavaType collectionType, final JsonDeserializer<Object> valueDeserializer, final TypeDeserializer valueTypeDeserializer, final ValueInstantiator valueInstantiator, final JsonDeserializer<Object> delegateDeserializer) {
        super(collectionType.getRawClass());
        this._collectionType = collectionType;
        this._valueDeserializer = valueDeserializer;
        this._valueTypeDeserializer = valueTypeDeserializer;
        this._valueInstantiator = valueInstantiator;
        this._delegateDeserializer = delegateDeserializer;
    }
    
    @Override
    public CollectionDeserializer createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
        JsonDeserializer<?> deserializer;
        if (this._valueInstantiator != null && this._valueInstantiator.canCreateUsingDelegate()) {
            final JavaType delegateType = this._valueInstantiator.getDelegateType(deserializationContext.getConfig());
            if (delegateType == null) {
                throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._collectionType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
            }
            deserializer = this.findDeserializer(deserializationContext, delegateType, beanProperty);
        }
        else {
            deserializer = null;
        }
        final JsonDeserializer<?> convertingContentDeserializer = this.findConvertingContentDeserializer(deserializationContext, beanProperty, this._valueDeserializer);
        JsonDeserializer<?> jsonDeserializer;
        if (convertingContentDeserializer == null) {
            jsonDeserializer = deserializationContext.findContextualValueDeserializer(this._collectionType.getContentType(), beanProperty);
        }
        else {
            jsonDeserializer = convertingContentDeserializer;
            if (convertingContentDeserializer instanceof ContextualDeserializer) {
                jsonDeserializer = ((ContextualDeserializer)convertingContentDeserializer).createContextual(deserializationContext, beanProperty);
            }
        }
        final TypeDeserializer valueTypeDeserializer = this._valueTypeDeserializer;
        TypeDeserializer forProperty;
        if ((forProperty = valueTypeDeserializer) != null) {
            forProperty = valueTypeDeserializer.forProperty(beanProperty);
        }
        return this.withResolved(deserializer, jsonDeserializer, forProperty);
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
        return this.deserialize(jsonParser, deserializationContext, (Collection<Object>)this._valueInstantiator.createUsingDefault(deserializationContext));
    }
    
    @Override
    public Collection<Object> deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Collection<Object> collection) throws IOException, JsonProcessingException {
        Collection<Object> handleNonArray;
        if (!jsonParser.isExpectedStartArrayToken()) {
            handleNonArray = this.handleNonArray(jsonParser, deserializationContext, collection);
        }
        else {
            final JsonDeserializer<Object> valueDeserializer = this._valueDeserializer;
            final TypeDeserializer valueTypeDeserializer = this._valueTypeDeserializer;
            while (true) {
                final JsonToken nextToken = jsonParser.nextToken();
                handleNonArray = collection;
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
                collection.add(o);
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
        return this._valueDeserializer;
    }
    
    protected final Collection<Object> handleNonArray(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Collection<Object> collection) throws IOException, JsonProcessingException {
        if (!deserializationContext.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
            throw deserializationContext.mappingException(this._collectionType.getRawClass());
        }
        final JsonDeserializer<Object> valueDeserializer = this._valueDeserializer;
        final TypeDeserializer valueTypeDeserializer = this._valueTypeDeserializer;
        Object o;
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            o = null;
        }
        else if (valueTypeDeserializer == null) {
            o = valueDeserializer.deserialize(jsonParser, deserializationContext);
        }
        else {
            o = valueDeserializer.deserializeWithType(jsonParser, deserializationContext, valueTypeDeserializer);
        }
        collection.add(o);
        return collection;
    }
    
    protected CollectionDeserializer withResolved(final JsonDeserializer<?> jsonDeserializer, final JsonDeserializer<?> jsonDeserializer2, final TypeDeserializer typeDeserializer) {
        if (jsonDeserializer == this._delegateDeserializer && jsonDeserializer2 == this._valueDeserializer && typeDeserializer == this._valueTypeDeserializer) {
            return this;
        }
        return new CollectionDeserializer(this._collectionType, (JsonDeserializer<Object>)jsonDeserializer2, typeDeserializer, this._valueInstantiator, (JsonDeserializer<Object>)jsonDeserializer);
    }
}
