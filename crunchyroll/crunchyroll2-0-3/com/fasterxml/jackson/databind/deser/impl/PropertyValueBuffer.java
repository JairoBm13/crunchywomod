// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.databind.BeanProperty;
import java.io.IOException;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;

public final class PropertyValueBuffer
{
    private PropertyValue _buffered;
    protected final DeserializationContext _context;
    protected final Object[] _creatorParameters;
    private Object _idValue;
    protected final ObjectIdReader _objectIdReader;
    private int _paramsNeeded;
    protected final JsonParser _parser;
    
    public PropertyValueBuffer(final JsonParser parser, final DeserializationContext context, final int paramsNeeded, final ObjectIdReader objectIdReader) {
        this._parser = parser;
        this._context = context;
        this._paramsNeeded = paramsNeeded;
        this._objectIdReader = objectIdReader;
        this._creatorParameters = new Object[paramsNeeded];
    }
    
    public boolean assignParameter(int paramsNeeded, final Object o) {
        this._creatorParameters[paramsNeeded] = o;
        paramsNeeded = this._paramsNeeded - 1;
        this._paramsNeeded = paramsNeeded;
        return paramsNeeded <= 0;
    }
    
    public void bufferAnyProperty(final SettableAnyProperty settableAnyProperty, final String s, final Object o) {
        this._buffered = new PropertyValue.Any(this._buffered, o, settableAnyProperty, s);
    }
    
    public void bufferMapProperty(final Object o, final Object o2) {
        this._buffered = new PropertyValue.Map(this._buffered, o2, o);
    }
    
    public void bufferProperty(final SettableBeanProperty settableBeanProperty, final Object o) {
        this._buffered = new PropertyValue.Regular(this._buffered, o, settableBeanProperty);
    }
    
    protected PropertyValue buffered() {
        return this._buffered;
    }
    
    protected final Object[] getParameters(final Object[] array) {
        if (array != null) {
            for (int i = 0; i < this._creatorParameters.length; ++i) {
                if (this._creatorParameters[i] == null) {
                    final Object o = array[i];
                    if (o != null) {
                        this._creatorParameters[i] = o;
                    }
                }
            }
        }
        return this._creatorParameters;
    }
    
    public Object handleIdValue(final DeserializationContext deserializationContext, final Object o) throws IOException {
        Object setAndReturn = o;
        if (this._objectIdReader != null) {
            setAndReturn = o;
            if (this._idValue != null) {
                deserializationContext.findObjectId(this._idValue, this._objectIdReader.generator).bindItem(o);
                final SettableBeanProperty idProperty = this._objectIdReader.idProperty;
                setAndReturn = o;
                if (idProperty != null) {
                    setAndReturn = idProperty.setAndReturn(o, this._idValue);
                }
            }
        }
        return setAndReturn;
    }
    
    public void inject(final SettableBeanProperty[] array) {
        for (int i = 0; i < array.length; ++i) {
            final SettableBeanProperty settableBeanProperty = array[i];
            if (settableBeanProperty != null) {
                this._creatorParameters[i] = this._context.findInjectableValue(settableBeanProperty.getInjectableValueId(), settableBeanProperty, null);
            }
        }
    }
    
    public boolean readIdProperty(final String s) throws IOException {
        if (this._objectIdReader != null && s.equals(this._objectIdReader.propertyName)) {
            this._idValue = this._objectIdReader.deserializer.deserialize(this._parser, this._context);
            return true;
        }
        return false;
    }
}
