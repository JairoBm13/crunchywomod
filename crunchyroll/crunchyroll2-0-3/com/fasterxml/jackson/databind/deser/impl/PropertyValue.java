// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import java.util.Map;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;

public abstract class PropertyValue
{
    public final PropertyValue next;
    public final Object value;
    
    protected PropertyValue(final PropertyValue next, final Object value) {
        this.next = next;
        this.value = value;
    }
    
    public abstract void assign(final Object p0) throws IOException, JsonProcessingException;
    
    static final class Any extends PropertyValue
    {
        final SettableAnyProperty _property;
        final String _propertyName;
        
        public Any(final PropertyValue propertyValue, final Object o, final SettableAnyProperty property, final String propertyName) {
            super(propertyValue, o);
            this._property = property;
            this._propertyName = propertyName;
        }
        
        @Override
        public void assign(final Object o) throws IOException, JsonProcessingException {
            this._property.set(o, this._propertyName, this.value);
        }
    }
    
    static final class Map extends PropertyValue
    {
        final Object _key;
        
        public Map(final PropertyValue propertyValue, final Object o, final Object key) {
            super(propertyValue, o);
            this._key = key;
        }
        
        @Override
        public void assign(final Object o) throws IOException, JsonProcessingException {
            ((java.util.Map)o).put(this._key, this.value);
        }
    }
    
    static final class Regular extends PropertyValue
    {
        final SettableBeanProperty _property;
        
        public Regular(final PropertyValue propertyValue, final Object o, final SettableBeanProperty property) {
            super(propertyValue, o);
            this._property = property;
        }
        
        @Override
        public void assign(final Object o) throws IOException, JsonProcessingException {
            this._property.set(o, this.value);
        }
    }
}
