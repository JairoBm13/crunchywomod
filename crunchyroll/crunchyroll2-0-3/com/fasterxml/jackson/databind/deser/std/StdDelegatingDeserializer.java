// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;

public class StdDelegatingDeserializer<T> extends StdDeserializer<T> implements ContextualDeserializer, ResolvableDeserializer
{
    protected final Converter<Object, T> _converter;
    protected final JsonDeserializer<Object> _delegateDeserializer;
    protected final JavaType _delegateType;
    
    public StdDelegatingDeserializer(final Converter<Object, T> converter, final JavaType delegateType, final JsonDeserializer<?> delegateDeserializer) {
        super(delegateType);
        this._converter = converter;
        this._delegateType = delegateType;
        this._delegateDeserializer = (JsonDeserializer<Object>)delegateDeserializer;
    }
    
    protected T convertValue(final Object o) {
        return this._converter.convert(o);
    }
    
    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
        if (this._delegateDeserializer != null) {
            StdDelegatingDeserializer withDelegate = this;
            if (this._delegateDeserializer instanceof ContextualDeserializer) {
                final JsonDeserializer<?> contextual = ((ContextualDeserializer)this._delegateDeserializer).createContextual(deserializationContext, beanProperty);
                withDelegate = this;
                if (contextual != this._delegateDeserializer) {
                    withDelegate = this.withDelegate(this._converter, this._delegateType, contextual);
                }
            }
            return withDelegate;
        }
        final JavaType inputType = this._converter.getInputType(deserializationContext.getTypeFactory());
        return this.withDelegate(this._converter, inputType, deserializationContext.findContextualValueDeserializer(inputType, beanProperty));
    }
    
    @Override
    public T deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final Object deserialize = this._delegateDeserializer.deserialize(jsonParser, deserializationContext);
        if (deserialize == null) {
            return null;
        }
        return this.convertValue(deserialize);
    }
    
    @Override
    public Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        final Object deserializeWithType = this._delegateDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        if (deserializeWithType == null) {
            return null;
        }
        return this.convertValue(deserializeWithType);
    }
    
    @Override
    public void resolve(final DeserializationContext deserializationContext) throws JsonMappingException {
        if (this._delegateDeserializer != null && this._delegateDeserializer instanceof ResolvableDeserializer) {
            ((ResolvableDeserializer)this._delegateDeserializer).resolve(deserializationContext);
        }
    }
    
    protected StdDelegatingDeserializer<T> withDelegate(final Converter<Object, T> converter, final JavaType javaType, final JsonDeserializer<?> jsonDeserializer) {
        if (this.getClass() != StdDelegatingDeserializer.class) {
            throw new IllegalStateException("Sub-class " + this.getClass().getName() + " must override 'withDelegate'");
        }
        return new StdDelegatingDeserializer<T>(converter, javaType, jsonDeserializer);
    }
}
