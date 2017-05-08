// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import java.util.Iterator;
import java.util.Map;
import java.util.Collection;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

public final class ManagedReferenceProperty extends SettableBeanProperty
{
    protected final SettableBeanProperty _backProperty;
    protected final boolean _isContainer;
    protected final SettableBeanProperty _managedProperty;
    protected final String _referenceName;
    
    public ManagedReferenceProperty(final SettableBeanProperty managedProperty, final String referenceName, final SettableBeanProperty backProperty, final Annotations annotations, final boolean isContainer) {
        super(managedProperty.getName(), managedProperty.getType(), managedProperty.getWrapperName(), managedProperty.getValueTypeDeserializer(), annotations, managedProperty.isRequired());
        this._referenceName = referenceName;
        this._managedProperty = managedProperty;
        this._backProperty = backProperty;
        this._isContainer = isContainer;
    }
    
    protected ManagedReferenceProperty(final ManagedReferenceProperty managedReferenceProperty, final JsonDeserializer<?> jsonDeserializer) {
        super(managedReferenceProperty, jsonDeserializer);
        this._referenceName = managedReferenceProperty._referenceName;
        this._isContainer = managedReferenceProperty._isContainer;
        this._managedProperty = managedReferenceProperty._managedProperty;
        this._backProperty = managedReferenceProperty._backProperty;
    }
    
    protected ManagedReferenceProperty(final ManagedReferenceProperty managedReferenceProperty, final String s) {
        super(managedReferenceProperty, s);
        this._referenceName = managedReferenceProperty._referenceName;
        this._isContainer = managedReferenceProperty._isContainer;
        this._managedProperty = managedReferenceProperty._managedProperty;
        this._backProperty = managedReferenceProperty._backProperty;
    }
    
    @Override
    public void deserializeAndSet(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        this.set(o, this._managedProperty.deserialize(jsonParser, deserializationContext));
    }
    
    @Override
    public Object deserializeSetAndReturn(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        return this.setAndReturn(o, this.deserialize(jsonParser, deserializationContext));
    }
    
    @Override
    public AnnotatedMember getMember() {
        return this._managedProperty.getMember();
    }
    
    @Override
    public final void set(final Object o, final Object o2) throws IOException {
        this.setAndReturn(o, o2);
    }
    
    @Override
    public Object setAndReturn(final Object o, final Object o2) throws IOException {
        final Object setAndReturn = this._managedProperty.setAndReturn(o, o2);
        if (o2 != null) {
            if (this._isContainer) {
                if (o2 instanceof Object[]) {
                    final Object[] array = (Object[])o2;
                    for (int length = array.length, i = 0; i < length; ++i) {
                        final Object o3 = array[i];
                        if (o3 != null) {
                            this._backProperty.set(o3, o);
                        }
                    }
                }
                else if (o2 instanceof Collection) {
                    for (final Object next : (Collection)o2) {
                        if (next != null) {
                            this._backProperty.set(next, o);
                        }
                    }
                }
                else {
                    if (!(o2 instanceof Map)) {
                        throw new IllegalStateException("Unsupported container type (" + o2.getClass().getName() + ") when resolving reference '" + this._referenceName + "'");
                    }
                    for (final Object next2 : ((Map)o2).values()) {
                        if (next2 != null) {
                            this._backProperty.set(next2, o);
                        }
                    }
                }
            }
            else {
                this._backProperty.set(o2, o);
            }
        }
        return setAndReturn;
    }
    
    @Override
    public ManagedReferenceProperty withName(final String s) {
        return new ManagedReferenceProperty(this, s);
    }
    
    @Override
    public ManagedReferenceProperty withValueDeserializer(final JsonDeserializer<?> jsonDeserializer) {
        return new ManagedReferenceProperty(this, jsonDeserializer);
    }
}
