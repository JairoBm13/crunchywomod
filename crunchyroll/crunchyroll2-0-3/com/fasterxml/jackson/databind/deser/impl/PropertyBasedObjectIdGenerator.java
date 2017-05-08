// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

public class PropertyBasedObjectIdGenerator extends PropertyGenerator
{
    public PropertyBasedObjectIdGenerator(final Class<?> clazz) {
        super(clazz);
    }
    
    @Override
    public ObjectIdGenerator<Object> forScope(final Class<?> clazz) {
        if (clazz == this._scope) {
            return this;
        }
        return new PropertyBasedObjectIdGenerator(clazz);
    }
    
    @Override
    public Object generateId(final Object o) {
        throw new UnsupportedOperationException();
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
