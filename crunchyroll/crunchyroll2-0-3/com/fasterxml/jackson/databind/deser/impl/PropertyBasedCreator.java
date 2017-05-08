// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonParser;
import java.util.Collection;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import java.util.HashMap;

public final class PropertyBasedCreator
{
    protected final Object[] _defaultValues;
    protected final HashMap<String, SettableBeanProperty> _properties;
    protected final SettableBeanProperty[] _propertiesWithInjectables;
    protected final int _propertyCount;
    protected final ValueInstantiator _valueInstantiator;
    
    protected PropertyBasedCreator(final ValueInstantiator valueInstantiator, final SettableBeanProperty[] array, final Object[] defaultValues) {
        this._valueInstantiator = valueInstantiator;
        this._properties = new HashMap<String, SettableBeanProperty>();
        final int length = array.length;
        this._propertyCount = length;
        SettableBeanProperty[] propertiesWithInjectables = null;
        SettableBeanProperty[] array2;
        for (int i = 0; i < length; ++i, propertiesWithInjectables = array2) {
            final SettableBeanProperty settableBeanProperty = array[i];
            this._properties.put(settableBeanProperty.getName(), settableBeanProperty);
            array2 = propertiesWithInjectables;
            if (settableBeanProperty.getInjectableValueId() != null) {
                if ((array2 = propertiesWithInjectables) == null) {
                    array2 = new SettableBeanProperty[length];
                }
                array2[i] = settableBeanProperty;
            }
        }
        this._defaultValues = defaultValues;
        this._propertiesWithInjectables = propertiesWithInjectables;
    }
    
    public static PropertyBasedCreator construct(final DeserializationContext deserializationContext, final ValueInstantiator valueInstantiator, final SettableBeanProperty[] array) throws JsonMappingException {
        final int length = array.length;
        final SettableBeanProperty[] array2 = new SettableBeanProperty[length];
        int i = 0;
        Object[] array3 = null;
        while (i < length) {
            SettableBeanProperty withValueDeserializer;
            final SettableBeanProperty settableBeanProperty = withValueDeserializer = array[i];
            if (!settableBeanProperty.hasValueDeserializer()) {
                withValueDeserializer = settableBeanProperty.withValueDeserializer(deserializationContext.findContextualValueDeserializer(settableBeanProperty.getType(), settableBeanProperty));
            }
            array2[i] = withValueDeserializer;
            final JsonDeserializer<Object> valueDeserializer = withValueDeserializer.getValueDeserializer();
            Object nullValue;
            if (valueDeserializer == null) {
                nullValue = null;
            }
            else {
                nullValue = valueDeserializer.getNullValue();
            }
            Object defaultValue;
            if (nullValue == null && withValueDeserializer.getType().isPrimitive()) {
                defaultValue = ClassUtil.defaultValue(withValueDeserializer.getType().getRawClass());
            }
            else {
                defaultValue = nullValue;
            }
            Object[] array4 = array3;
            if (defaultValue != null) {
                if ((array4 = array3) == null) {
                    array4 = new Object[length];
                }
                array4[i] = defaultValue;
            }
            ++i;
            array3 = array4;
        }
        return new PropertyBasedCreator(valueInstantiator, array2, array3);
    }
    
    public Object build(final DeserializationContext deserializationContext, final PropertyValueBuffer propertyValueBuffer) throws IOException {
        final Object handleIdValue = propertyValueBuffer.handleIdValue(deserializationContext, this._valueInstantiator.createFromObjectWith(deserializationContext, propertyValueBuffer.getParameters(this._defaultValues)));
        for (PropertyValue propertyValue = propertyValueBuffer.buffered(); propertyValue != null; propertyValue = propertyValue.next) {
            propertyValue.assign(handleIdValue);
        }
        return handleIdValue;
    }
    
    public SettableBeanProperty findCreatorProperty(final String s) {
        return this._properties.get(s);
    }
    
    public Collection<SettableBeanProperty> properties() {
        return this._properties.values();
    }
    
    public PropertyValueBuffer startBuilding(final JsonParser jsonParser, final DeserializationContext deserializationContext, final ObjectIdReader objectIdReader) {
        final PropertyValueBuffer propertyValueBuffer = new PropertyValueBuffer(jsonParser, deserializationContext, this._propertyCount, objectIdReader);
        if (this._propertiesWithInjectables != null) {
            propertyValueBuffer.inject(this._propertiesWithInjectables);
        }
        return propertyValueBuffer;
    }
}
