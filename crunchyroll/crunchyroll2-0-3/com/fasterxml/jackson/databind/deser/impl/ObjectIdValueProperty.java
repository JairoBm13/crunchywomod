// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

public final class ObjectIdValueProperty extends SettableBeanProperty
{
    protected final ObjectIdReader _objectIdReader;
    
    public ObjectIdValueProperty(final ObjectIdReader objectIdReader, final boolean b) {
        super(objectIdReader.propertyName, objectIdReader.idType, null, null, null, b);
        this._objectIdReader = objectIdReader;
        this._valueDeserializer = objectIdReader.deserializer;
    }
    
    protected ObjectIdValueProperty(final ObjectIdValueProperty objectIdValueProperty, final JsonDeserializer<?> jsonDeserializer) {
        super(objectIdValueProperty, jsonDeserializer);
        this._objectIdReader = objectIdValueProperty._objectIdReader;
    }
    
    protected ObjectIdValueProperty(final ObjectIdValueProperty objectIdValueProperty, final String s) {
        super(objectIdValueProperty, s);
        this._objectIdReader = objectIdValueProperty._objectIdReader;
    }
    
    @Override
    public void deserializeAndSet(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        this.deserializeSetAndReturn(jsonParser, deserializationContext, o);
    }
    
    @Override
    public Object deserializeSetAndReturn(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        final Object deserialize = this._valueDeserializer.deserialize(jsonParser, deserializationContext);
        deserializationContext.findObjectId(deserialize, this._objectIdReader.generator).bindItem(o);
        final SettableBeanProperty idProperty = this._objectIdReader.idProperty;
        Object setAndReturn = o;
        if (idProperty != null) {
            setAndReturn = idProperty.setAndReturn(o, deserialize);
        }
        return setAndReturn;
    }
    
    @Override
    public AnnotatedMember getMember() {
        return null;
    }
    
    @Override
    public void set(final Object o, final Object o2) throws IOException {
        this.setAndReturn(o, o2);
    }
    
    @Override
    public Object setAndReturn(final Object o, final Object o2) throws IOException {
        final SettableBeanProperty idProperty = this._objectIdReader.idProperty;
        if (idProperty == null) {
            throw new UnsupportedOperationException("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
        }
        return idProperty.setAndReturn(o, o2);
    }
    
    @Override
    public ObjectIdValueProperty withName(final String s) {
        return new ObjectIdValueProperty(this, s);
    }
    
    @Override
    public ObjectIdValueProperty withValueDeserializer(final JsonDeserializer<?> jsonDeserializer) {
        return new ObjectIdValueProperty(this, jsonDeserializer);
    }
}
