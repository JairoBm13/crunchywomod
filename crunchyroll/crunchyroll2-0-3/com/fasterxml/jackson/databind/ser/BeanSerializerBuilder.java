// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import java.util.List;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.BeanDescription;

public class BeanSerializerBuilder
{
    private static final BeanPropertyWriter[] NO_PROPERTIES;
    protected AnyGetterWriter _anyGetter;
    protected final BeanDescription _beanDesc;
    protected SerializationConfig _config;
    protected Object _filterId;
    protected BeanPropertyWriter[] _filteredProperties;
    protected ObjectIdWriter _objectIdWriter;
    protected List<BeanPropertyWriter> _properties;
    protected AnnotatedMember _typeId;
    
    static {
        NO_PROPERTIES = new BeanPropertyWriter[0];
    }
    
    public BeanSerializerBuilder(final BeanDescription beanDesc) {
        this._beanDesc = beanDesc;
    }
    
    public JsonSerializer<?> build() {
        BeanPropertyWriter[] no_PROPERTIES;
        if (this._properties == null || this._properties.isEmpty()) {
            if (this._anyGetter == null) {
                return null;
            }
            no_PROPERTIES = BeanSerializerBuilder.NO_PROPERTIES;
        }
        else {
            no_PROPERTIES = this._properties.toArray(new BeanPropertyWriter[this._properties.size()]);
        }
        return new BeanSerializer(this._beanDesc.getType(), this, no_PROPERTIES, this._filteredProperties);
    }
    
    public BeanSerializer createDummy() {
        return BeanSerializer.createDummy(this._beanDesc.getType());
    }
    
    public AnyGetterWriter getAnyGetter() {
        return this._anyGetter;
    }
    
    public BeanDescription getBeanDescription() {
        return this._beanDesc;
    }
    
    public Object getFilterId() {
        return this._filterId;
    }
    
    public ObjectIdWriter getObjectIdWriter() {
        return this._objectIdWriter;
    }
    
    public List<BeanPropertyWriter> getProperties() {
        return this._properties;
    }
    
    public AnnotatedMember getTypeId() {
        return this._typeId;
    }
    
    public void setAnyGetter(final AnyGetterWriter anyGetter) {
        this._anyGetter = anyGetter;
    }
    
    protected void setConfig(final SerializationConfig config) {
        this._config = config;
    }
    
    public void setFilterId(final Object filterId) {
        this._filterId = filterId;
    }
    
    public void setFilteredProperties(final BeanPropertyWriter[] filteredProperties) {
        this._filteredProperties = filteredProperties;
    }
    
    public void setObjectIdWriter(final ObjectIdWriter objectIdWriter) {
        this._objectIdWriter = objectIdWriter;
    }
    
    public void setProperties(final List<BeanPropertyWriter> properties) {
        this._properties = properties;
    }
    
    public void setTypeId(final AnnotatedMember typeId) {
        if (this._typeId != null) {
            throw new IllegalArgumentException("Multiple type ids specified with " + this._typeId + " and " + typeId);
        }
        this._typeId = typeId;
    }
}
