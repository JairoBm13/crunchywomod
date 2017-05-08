// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.lang.reflect.Constructor;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

public final class InnerClassProperty extends SettableBeanProperty
{
    protected final Constructor<?> _creator;
    protected final SettableBeanProperty _delegate;
    
    public InnerClassProperty(final SettableBeanProperty delegate, final Constructor<?> creator) {
        super(delegate);
        this._delegate = delegate;
        this._creator = creator;
    }
    
    protected InnerClassProperty(final InnerClassProperty innerClassProperty, final JsonDeserializer<?> jsonDeserializer) {
        super(innerClassProperty, jsonDeserializer);
        this._delegate = innerClassProperty._delegate.withValueDeserializer(jsonDeserializer);
        this._creator = innerClassProperty._creator;
    }
    
    protected InnerClassProperty(final InnerClassProperty innerClassProperty, final String s) {
        super(innerClassProperty, s);
        this._delegate = innerClassProperty._delegate.withName(s);
        this._creator = innerClassProperty._creator;
    }
    
    @Override
    public void deserializeAndSet(JsonParser o, final DeserializationContext deserializationContext, final Object o2) throws IOException, JsonProcessingException {
        Object instance = null;
        final Object o3 = null;
        if (((JsonParser)o).getCurrentToken() == JsonToken.VALUE_NULL) {
            if (this._nullProvider == null) {
                o = o3;
            }
            else {
                o = this._nullProvider.nullValue(deserializationContext);
            }
        }
        else if (this._valueTypeDeserializer != null) {
            o = this._valueDeserializer.deserializeWithType((JsonParser)o, deserializationContext, this._valueTypeDeserializer);
        }
        else {
            while (true) {
                try {
                    instance = this._creator.newInstance(o2);
                    this._valueDeserializer.deserialize((JsonParser)o, deserializationContext, instance);
                    o = instance;
                }
                catch (Exception ex) {
                    ClassUtil.unwrapAndThrowAsIAE(ex, "Failed to instantiate class " + this._creator.getDeclaringClass().getName() + ", problem: " + ex.getMessage());
                    continue;
                }
                break;
            }
        }
        this.set(o2, o);
    }
    
    @Override
    public Object deserializeSetAndReturn(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        return this.setAndReturn(o, this.deserialize(jsonParser, deserializationContext));
    }
    
    @Override
    public AnnotatedMember getMember() {
        return this._delegate.getMember();
    }
    
    @Override
    public final void set(final Object o, final Object o2) throws IOException {
        this._delegate.set(o, o2);
    }
    
    @Override
    public Object setAndReturn(final Object o, final Object o2) throws IOException {
        return this._delegate.setAndReturn(o, o2);
    }
    
    @Override
    public InnerClassProperty withName(final String s) {
        return new InnerClassProperty(this, s);
    }
    
    @Override
    public InnerClassProperty withValueDeserializer(final JsonDeserializer<?> jsonDeserializer) {
        return new InnerClassProperty(this, jsonDeserializer);
    }
}
