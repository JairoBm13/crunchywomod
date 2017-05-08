// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.lang.reflect.Method;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

public final class SetterlessProperty extends SettableBeanProperty
{
    protected final AnnotatedMethod _annotated;
    protected final Method _getter;
    
    protected SetterlessProperty(final SetterlessProperty setterlessProperty, final JsonDeserializer<?> jsonDeserializer) {
        super(setterlessProperty, jsonDeserializer);
        this._annotated = setterlessProperty._annotated;
        this._getter = setterlessProperty._getter;
    }
    
    protected SetterlessProperty(final SetterlessProperty setterlessProperty, final String s) {
        super(setterlessProperty, s);
        this._annotated = setterlessProperty._annotated;
        this._getter = setterlessProperty._getter;
    }
    
    public SetterlessProperty(final BeanPropertyDefinition beanPropertyDefinition, final JavaType javaType, final TypeDeserializer typeDeserializer, final Annotations annotations, final AnnotatedMethod annotated) {
        super(beanPropertyDefinition, javaType, typeDeserializer, annotations);
        this._annotated = annotated;
        this._getter = annotated.getAnnotated();
    }
    
    @Override
    public final void deserializeAndSet(final JsonParser jsonParser, final DeserializationContext deserializationContext, Object invoke) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            return;
        }
        try {
            invoke = this._getter.invoke(invoke, new Object[0]);
            if (invoke == null) {
                throw new JsonMappingException("Problem deserializing 'setterless' property '" + this.getName() + "': get method returned null");
            }
        }
        catch (Exception ex) {
            this._throwAsIOE(ex);
            return;
        }
        this._valueDeserializer.deserialize(jsonParser, deserializationContext, invoke);
    }
    
    @Override
    public Object deserializeSetAndReturn(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        this.deserializeAndSet(jsonParser, deserializationContext, o);
        return o;
    }
    
    @Override
    public AnnotatedMember getMember() {
        return this._annotated;
    }
    
    @Override
    public final void set(final Object o, final Object o2) throws IOException {
        throw new UnsupportedOperationException("Should never call 'set' on setterless property");
    }
    
    @Override
    public Object setAndReturn(final Object o, final Object o2) throws IOException {
        this.set(o, o2);
        return null;
    }
    
    @Override
    public SetterlessProperty withName(final String s) {
        return new SetterlessProperty(this, s);
    }
    
    @Override
    public SetterlessProperty withValueDeserializer(final JsonDeserializer<?> jsonDeserializer) {
        return new SetterlessProperty(this, jsonDeserializer);
    }
}
