// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.DeserializationContext;

public abstract class ValueInstantiator
{
    public boolean canCreateFromBoolean() {
        return false;
    }
    
    public boolean canCreateFromDouble() {
        return false;
    }
    
    public boolean canCreateFromInt() {
        return false;
    }
    
    public boolean canCreateFromLong() {
        return false;
    }
    
    public boolean canCreateFromObjectWith() {
        return false;
    }
    
    public boolean canCreateFromString() {
        return false;
    }
    
    public boolean canCreateUsingDefault() {
        return this.getDefaultCreator() != null;
    }
    
    public boolean canCreateUsingDelegate() {
        return false;
    }
    
    public boolean canInstantiate() {
        return this.canCreateUsingDefault() || this.canCreateUsingDelegate() || this.canCreateFromObjectWith() || this.canCreateFromString() || this.canCreateFromInt() || this.canCreateFromLong() || this.canCreateFromDouble() || this.canCreateFromBoolean();
    }
    
    public Object createFromBoolean(final DeserializationContext deserializationContext, final boolean b) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " from Boolean value");
    }
    
    public Object createFromDouble(final DeserializationContext deserializationContext, final double n) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " from Floating-point number (double)");
    }
    
    public Object createFromInt(final DeserializationContext deserializationContext, final int n) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " from Integer number (int)");
    }
    
    public Object createFromLong(final DeserializationContext deserializationContext, final long n) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " from Integer number (long)");
    }
    
    public Object createFromObjectWith(final DeserializationContext deserializationContext, final Object[] array) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " with arguments");
    }
    
    public Object createFromString(final DeserializationContext deserializationContext, final String s) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " from String value");
    }
    
    public Object createUsingDefault(final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + "; no default creator found");
    }
    
    public Object createUsingDelegate(final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        throw new JsonMappingException("Can not instantiate value of type " + this.getValueTypeDesc() + " using delegate");
    }
    
    public AnnotatedWithParams getDefaultCreator() {
        return null;
    }
    
    public AnnotatedWithParams getDelegateCreator() {
        return null;
    }
    
    public JavaType getDelegateType(final DeserializationConfig deserializationConfig) {
        return null;
    }
    
    public SettableBeanProperty[] getFromObjectArguments(final DeserializationConfig deserializationConfig) {
        return null;
    }
    
    public AnnotatedParameter getIncompleteParameter() {
        return null;
    }
    
    public abstract String getValueTypeDesc();
}
