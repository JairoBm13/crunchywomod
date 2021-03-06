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
import java.lang.reflect.Field;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

public final class FieldProperty extends SettableBeanProperty
{
    protected final AnnotatedField _annotated;
    protected final transient Field _field;
    
    protected FieldProperty(final FieldProperty fieldProperty, final JsonDeserializer<?> jsonDeserializer) {
        super(fieldProperty, jsonDeserializer);
        this._annotated = fieldProperty._annotated;
        this._field = fieldProperty._field;
    }
    
    protected FieldProperty(final FieldProperty fieldProperty, final String s) {
        super(fieldProperty, s);
        this._annotated = fieldProperty._annotated;
        this._field = fieldProperty._field;
    }
    
    public FieldProperty(final BeanPropertyDefinition beanPropertyDefinition, final JavaType javaType, final TypeDeserializer typeDeserializer, final Annotations annotations, final AnnotatedField annotated) {
        super(beanPropertyDefinition, javaType, typeDeserializer, annotations);
        this._annotated = annotated;
        this._field = annotated.getAnnotated();
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
            this._field.set(o, o2);
        }
        catch (Exception ex) {
            this._throwAsIOE(ex, o2);
        }
    }
    
    @Override
    public Object setAndReturn(final Object o, final Object o2) throws IOException {
        try {
            this._field.set(o, o2);
            return o;
        }
        catch (Exception ex) {
            this._throwAsIOE(ex, o2);
            return o;
        }
    }
    
    @Override
    public FieldProperty withName(final String s) {
        return new FieldProperty(this, s);
    }
    
    @Override
    public FieldProperty withValueDeserializer(final JsonDeserializer<?> jsonDeserializer) {
        return new FieldProperty(this, jsonDeserializer);
    }
}
