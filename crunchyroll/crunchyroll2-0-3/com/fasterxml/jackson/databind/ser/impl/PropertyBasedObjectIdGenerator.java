// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

public class PropertyBasedObjectIdGenerator extends PropertyGenerator
{
    protected final BeanPropertyWriter _property;
    
    public PropertyBasedObjectIdGenerator(final ObjectIdInfo objectIdInfo, final BeanPropertyWriter beanPropertyWriter) {
        this(objectIdInfo.getScope(), beanPropertyWriter);
    }
    
    protected PropertyBasedObjectIdGenerator(final Class<?> clazz, final BeanPropertyWriter property) {
        super(clazz);
        this._property = property;
    }
    
    @Override
    public boolean canUseFor(final ObjectIdGenerator<?> objectIdGenerator) {
        boolean b2;
        final boolean b = b2 = false;
        if (((PropertyBasedObjectIdGenerator)objectIdGenerator).getClass() == this.getClass()) {
            final PropertyBasedObjectIdGenerator propertyBasedObjectIdGenerator = (PropertyBasedObjectIdGenerator)objectIdGenerator;
            b2 = b;
            if (((ObjectIdGenerators.Base)propertyBasedObjectIdGenerator).getScope() == this._scope) {
                b2 = b;
                if (propertyBasedObjectIdGenerator._property == this._property) {
                    b2 = true;
                }
            }
        }
        return b2;
    }
    
    @Override
    public ObjectIdGenerator<Object> forScope(final Class<?> clazz) {
        if (clazz == this._scope) {
            return this;
        }
        return new PropertyBasedObjectIdGenerator(clazz, this._property);
    }
    
    @Override
    public Object generateId(Object value) {
        try {
            value = this._property.get(value);
            return value;
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new IllegalStateException("Problem accessing property '" + this._property.getName() + "': " + ex2.getMessage(), ex2);
        }
    }
    
    @Override
    public IdKey key(final Object o) {
        return new IdKey(this.getClass(), this._scope, o);
    }
    
    @Override
    public ObjectIdGenerator<Object> newForSerialization(final Object o) {
        return this;
    }
}
