// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
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

public final class MethodProperty extends SettableBeanProperty
{
    protected final AnnotatedMethod _annotated;
    protected final transient Method _setter;
    
    protected MethodProperty(final MethodProperty methodProperty, final JsonDeserializer<?> jsonDeserializer) {
        super(methodProperty, jsonDeserializer);
        this._annotated = methodProperty._annotated;
        this._setter = methodProperty._setter;
    }
    
    protected MethodProperty(final MethodProperty methodProperty, final String s) {
        super(methodProperty, s);
        this._annotated = methodProperty._annotated;
        this._setter = methodProperty._setter;
    }
    
    public MethodProperty(final BeanPropertyDefinition beanPropertyDefinition, final JavaType javaType, final TypeDeserializer typeDeserializer, final Annotations annotations, final AnnotatedMethod annotated) {
        super(beanPropertyDefinition, javaType, typeDeserializer, annotations);
        this._annotated = annotated;
        this._setter = annotated.getAnnotated();
    }
    
    @Override
    public void deserializeAndSet(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        this.set(o, this.deserialize(jsonParser, deserializationContext));
    }
    
    @Override
    public Object deserializeSetAndReturn(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        return this.setAndReturn(o, this.deserialize(jsonParser, deserializationContext));
    }
    
    @Override
    public AnnotatedMember getMember() {
        return this._annotated;
    }
    
    @Override
    public final void set(final Object o, final Object o2) throws IOException {
        try {
            this._setter.invoke(o, o2);
        }
        catch (Exception ex) {
            this._throwAsIOE(ex, o2);
        }
    }
    
    @Override
    public Object setAndReturn(final Object o, final Object o2) throws IOException {
        try {
            final Object invoke = this._setter.invoke(o, o2);
            if (invoke == null) {
                return o;
            }
            return invoke;
        }
        catch (Exception ex) {
            this._throwAsIOE(ex, o2);
            return null;
        }
    }
    
    @Override
    public MethodProperty withName(final String s) {
        return new MethodProperty(this, s);
    }
    
    @Override
    public MethodProperty withValueDeserializer(final JsonDeserializer<?> jsonDeserializer) {
        return new MethodProperty(this, jsonDeserializer);
    }
}
