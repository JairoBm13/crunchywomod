// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.JavaType;

public class AsExternalTypeDeserializer extends AsArrayTypeDeserializer
{
    public AsExternalTypeDeserializer(final JavaType javaType, final TypeIdResolver typeIdResolver, final String s, final boolean b, final Class<?> clazz) {
        super(javaType, typeIdResolver, s, b, clazz);
    }
    
    public AsExternalTypeDeserializer(final AsExternalTypeDeserializer asExternalTypeDeserializer, final BeanProperty beanProperty) {
        super(asExternalTypeDeserializer, beanProperty);
    }
    
    @Override
    public TypeDeserializer forProperty(final BeanProperty beanProperty) {
        if (beanProperty == this._property) {
            return this;
        }
        return new AsExternalTypeDeserializer(this, beanProperty);
    }
    
    @Override
    public JsonTypeInfo.As getTypeInclusion() {
        return JsonTypeInfo.As.EXTERNAL_PROPERTY;
    }
}
