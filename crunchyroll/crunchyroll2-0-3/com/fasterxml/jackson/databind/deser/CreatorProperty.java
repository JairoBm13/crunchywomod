// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;

public class CreatorProperty extends SettableBeanProperty
{
    protected final AnnotatedParameter _annotated;
    protected final int _creatorIndex;
    protected final Object _injectableValueId;
    
    protected CreatorProperty(final CreatorProperty creatorProperty, final JsonDeserializer<?> jsonDeserializer) {
        super(creatorProperty, jsonDeserializer);
        this._annotated = creatorProperty._annotated;
        this._creatorIndex = creatorProperty._creatorIndex;
        this._injectableValueId = creatorProperty._injectableValueId;
    }
    
    protected CreatorProperty(final CreatorProperty creatorProperty, final String s) {
        super(creatorProperty, s);
        this._annotated = creatorProperty._annotated;
        this._creatorIndex = creatorProperty._creatorIndex;
        this._injectableValueId = creatorProperty._injectableValueId;
    }
    
    public CreatorProperty(final String s, final JavaType javaType, final PropertyName propertyName, final TypeDeserializer typeDeserializer, final Annotations annotations, final AnnotatedParameter annotated, final int creatorIndex, final Object injectableValueId, final boolean b) {
        super(s, javaType, propertyName, typeDeserializer, annotations, b);
        this._annotated = annotated;
        this._creatorIndex = creatorIndex;
        this._injectableValueId = injectableValueId;
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
    public int getCreatorIndex() {
        return this._creatorIndex;
    }
    
    @Override
    public Object getInjectableValueId() {
        return this._injectableValueId;
    }
    
    @Override
    public AnnotatedMember getMember() {
        return this._annotated;
    }
    
    @Override
    public void set(final Object o, final Object o2) throws IOException {
        throw new IllegalStateException("Method should never be called on a " + this.getClass().getName());
    }
    
    @Override
    public Object setAndReturn(final Object o, final Object o2) throws IOException {
        return o;
    }
    
    @Override
    public String toString() {
        return "[creator property, name '" + this.getName() + "'; inject id '" + this._injectableValueId + "']";
    }
    
    @Override
    public CreatorProperty withName(final String s) {
        return new CreatorProperty(this, s);
    }
    
    @Override
    public CreatorProperty withValueDeserializer(final JsonDeserializer<?> jsonDeserializer) {
        return new CreatorProperty(this, jsonDeserializer);
    }
}
