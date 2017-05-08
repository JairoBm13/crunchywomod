// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import java.util.List;

public class UnwrappedPropertyHandler
{
    protected final List<SettableBeanProperty> _properties;
    
    public UnwrappedPropertyHandler() {
        this._properties = new ArrayList<SettableBeanProperty>();
    }
    
    protected UnwrappedPropertyHandler(final List<SettableBeanProperty> properties) {
        this._properties = properties;
    }
    
    public void addProperty(final SettableBeanProperty settableBeanProperty) {
        this._properties.add(settableBeanProperty);
    }
    
    public Object processUnwrapped(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o, final TokenBuffer tokenBuffer) throws IOException, JsonProcessingException {
        for (int size = this._properties.size(), i = 0; i < size; ++i) {
            final SettableBeanProperty settableBeanProperty = this._properties.get(i);
            final JsonParser parser = tokenBuffer.asParser();
            parser.nextToken();
            settableBeanProperty.deserializeAndSet(parser, deserializationContext, o);
        }
        return o;
    }
    
    public UnwrappedPropertyHandler renameAll(final NameTransformer nameTransformer) {
        final ArrayList<SettableBeanProperty> list = new ArrayList<SettableBeanProperty>(this._properties.size());
        for (final SettableBeanProperty settableBeanProperty : this._properties) {
            final SettableBeanProperty withName = settableBeanProperty.withName(nameTransformer.transform(settableBeanProperty.getName()));
            final JsonDeserializer<Object> valueDeserializer = withName.getValueDeserializer();
            SettableBeanProperty withValueDeserializer = withName;
            if (valueDeserializer != null) {
                final JsonDeserializer<Object> unwrappingDeserializer = valueDeserializer.unwrappingDeserializer(nameTransformer);
                withValueDeserializer = withName;
                if (unwrappingDeserializer != valueDeserializer) {
                    withValueDeserializer = withName.withValueDeserializer(unwrappingDeserializer);
                }
            }
            list.add(withValueDeserializer);
        }
        return new UnwrappedPropertyHandler(list);
    }
}
