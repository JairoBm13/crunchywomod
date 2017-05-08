// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import java.util.Set;
import java.io.Serializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanPropertyFilter;

public abstract class SimpleBeanPropertyFilter implements BeanPropertyFilter
{
    protected abstract boolean include(final BeanPropertyWriter p0);
    
    @Override
    public void serializeAsField(final Object o, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider, final BeanPropertyWriter beanPropertyWriter) throws Exception {
        if (this.include(beanPropertyWriter)) {
            beanPropertyWriter.serializeAsField(o, jsonGenerator, serializerProvider);
        }
    }
    
    public static class FilterExceptFilter extends SimpleBeanPropertyFilter implements Serializable
    {
        protected final Set<String> _propertiesToInclude;
        
        @Override
        protected boolean include(final BeanPropertyWriter beanPropertyWriter) {
            return this._propertiesToInclude.contains(beanPropertyWriter.getName());
        }
    }
    
    public static class SerializeExceptFilter extends SimpleBeanPropertyFilter
    {
        protected final Set<String> _propertiesToExclude;
        
        @Override
        protected boolean include(final BeanPropertyWriter beanPropertyWriter) {
            return !this._propertiesToExclude.contains(beanPropertyWriter.getName());
        }
    }
}
