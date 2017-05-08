// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import java.util.Iterator;
import java.util.Collection;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import java.util.LinkedHashMap;
import com.fasterxml.jackson.databind.DeserializationConfig;
import java.util.Map;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
import java.util.List;
import java.util.HashSet;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.BeanDescription;
import java.util.HashMap;

public class BeanDeserializerBuilder
{
    protected SettableAnyProperty _anySetter;
    protected HashMap<String, SettableBeanProperty> _backRefProperties;
    protected final BeanDescription _beanDesc;
    protected AnnotatedMethod _buildMethod;
    protected JsonPOJOBuilder.Value _builderConfig;
    protected final boolean _defaultViewInclusion;
    protected HashSet<String> _ignorableProps;
    protected boolean _ignoreAllUnknown;
    protected List<ValueInjector> _injectables;
    protected ObjectIdReader _objectIdReader;
    protected final Map<String, SettableBeanProperty> _properties;
    protected ValueInstantiator _valueInstantiator;
    
    public BeanDeserializerBuilder(final BeanDescription beanDesc, final DeserializationConfig deserializationConfig) {
        this._properties = new LinkedHashMap<String, SettableBeanProperty>();
        this._beanDesc = beanDesc;
        this._defaultViewInclusion = deserializationConfig.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
    }
    
    public void addBackReferenceProperty(final String s, final SettableBeanProperty settableBeanProperty) {
        if (this._backRefProperties == null) {
            this._backRefProperties = new HashMap<String, SettableBeanProperty>(4);
        }
        this._backRefProperties.put(s, settableBeanProperty);
        if (this._properties != null) {
            this._properties.remove(settableBeanProperty.getName());
        }
    }
    
    public void addCreatorProperty(final SettableBeanProperty settableBeanProperty) {
        this.addProperty(settableBeanProperty);
    }
    
    public void addIgnorable(final String s) {
        if (this._ignorableProps == null) {
            this._ignorableProps = new HashSet<String>();
        }
        this._ignorableProps.add(s);
    }
    
    public void addInjectable(final String s, final JavaType javaType, final Annotations annotations, final AnnotatedMember annotatedMember, final Object o) {
        if (this._injectables == null) {
            this._injectables = new ArrayList<ValueInjector>();
        }
        this._injectables.add(new ValueInjector(s, javaType, annotations, annotatedMember, o));
    }
    
    public void addOrReplaceProperty(final SettableBeanProperty settableBeanProperty, final boolean b) {
        this._properties.put(settableBeanProperty.getName(), settableBeanProperty);
    }
    
    public void addProperty(final SettableBeanProperty settableBeanProperty) {
        final SettableBeanProperty settableBeanProperty2 = this._properties.put(settableBeanProperty.getName(), settableBeanProperty);
        if (settableBeanProperty2 != null && settableBeanProperty2 != settableBeanProperty) {
            throw new IllegalArgumentException("Duplicate property '" + settableBeanProperty.getName() + "' for " + this._beanDesc.getType());
        }
    }
    
    public JsonDeserializer<?> build() {
        final Collection<SettableBeanProperty> values = this._properties.values();
        final BeanPropertyMap beanPropertyMap = new BeanPropertyMap(values);
        beanPropertyMap.assignIndexes();
        boolean b2;
        final boolean b = b2 = !this._defaultViewInclusion;
        Label_0076: {
            if (!b) {
                final Iterator<SettableBeanProperty> iterator = values.iterator();
                do {
                    b2 = b;
                    if (iterator.hasNext()) {
                        continue;
                    }
                    break Label_0076;
                } while (!iterator.next().hasViews());
                b2 = true;
            }
        }
        BeanPropertyMap withProperty = beanPropertyMap;
        if (this._objectIdReader != null) {
            withProperty = beanPropertyMap.withProperty(new ObjectIdValueProperty(this._objectIdReader, true));
        }
        return new BeanDeserializer(this, this._beanDesc, withProperty, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, b2);
    }
    
    public AbstractDeserializer buildAbstract() {
        return new AbstractDeserializer(this, this._beanDesc, this._backRefProperties);
    }
    
    public JsonDeserializer<?> buildBuilderBased(final JavaType javaType, final String s) {
        if (this._buildMethod == null) {
            throw new IllegalArgumentException("Builder class " + this._beanDesc.getBeanClass().getName() + " does not have build method '" + s + "()'");
        }
        final Class<?> rawReturnType = this._buildMethod.getRawReturnType();
        if (!javaType.getRawClass().isAssignableFrom(rawReturnType)) {
            throw new IllegalArgumentException("Build method '" + this._buildMethod.getFullName() + " has bad return type (" + rawReturnType.getName() + "), not compatible with POJO type (" + javaType.getRawClass().getName() + ")");
        }
        final Collection<SettableBeanProperty> values = this._properties.values();
        final BeanPropertyMap beanPropertyMap = new BeanPropertyMap(values);
        beanPropertyMap.assignIndexes();
        boolean b2;
        final boolean b = b2 = !this._defaultViewInclusion;
        Label_0218: {
            if (!b) {
                final Iterator<SettableBeanProperty> iterator = values.iterator();
                do {
                    b2 = b;
                    if (iterator.hasNext()) {
                        continue;
                    }
                    break Label_0218;
                } while (!iterator.next().hasViews());
                b2 = true;
            }
        }
        BeanPropertyMap withProperty = beanPropertyMap;
        if (this._objectIdReader != null) {
            withProperty = beanPropertyMap.withProperty(new ObjectIdValueProperty(this._objectIdReader, true));
        }
        return new BuilderBasedDeserializer(this, this._beanDesc, withProperty, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, b2);
    }
    
    public SettableBeanProperty findProperty(final String s) {
        return this._properties.get(s);
    }
    
    public SettableAnyProperty getAnySetter() {
        return this._anySetter;
    }
    
    public AnnotatedMethod getBuildMethod() {
        return this._buildMethod;
    }
    
    public List<ValueInjector> getInjectables() {
        return this._injectables;
    }
    
    public ObjectIdReader getObjectIdReader() {
        return this._objectIdReader;
    }
    
    public ValueInstantiator getValueInstantiator() {
        return this._valueInstantiator;
    }
    
    public void setAnySetter(final SettableAnyProperty anySetter) {
        if (this._anySetter != null && anySetter != null) {
            throw new IllegalStateException("_anySetter already set to non-null");
        }
        this._anySetter = anySetter;
    }
    
    public void setIgnoreUnknownProperties(final boolean ignoreAllUnknown) {
        this._ignoreAllUnknown = ignoreAllUnknown;
    }
    
    public void setObjectIdReader(final ObjectIdReader objectIdReader) {
        this._objectIdReader = objectIdReader;
    }
    
    public void setPOJOBuilder(final AnnotatedMethod buildMethod, final JsonPOJOBuilder.Value builderConfig) {
        this._buildMethod = buildMethod;
        this._builderConfig = builderConfig;
    }
    
    public void setValueInstantiator(final ValueInstantiator valueInstantiator) {
        this._valueInstantiator = valueInstantiator;
    }
}
