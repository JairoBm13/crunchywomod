// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import java.util.HashSet;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;

public class BeanAsArrayBuilderDeserializer extends BeanDeserializerBase
{
    protected final AnnotatedMethod _buildMethod;
    protected final BeanDeserializerBase _delegate;
    protected final SettableBeanProperty[] _orderedProperties;
    
    public BeanAsArrayBuilderDeserializer(final BeanDeserializerBase delegate, final SettableBeanProperty[] orderedProperties, final AnnotatedMethod buildMethod) {
        super(delegate);
        this._delegate = delegate;
        this._orderedProperties = orderedProperties;
        this._buildMethod = buildMethod;
    }
    
    protected Object _deserializeFromNonArray(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        throw deserializationContext.mappingException("Can not deserialize a POJO (of type " + this._beanType.getRawClass().getName() + ") from non-Array representation (token: " + jsonParser.getCurrentToken() + "): type/property designed to be serialized as JSON Array");
    }
    
    protected Object _deserializeNonVanilla(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._nonStandardCreation) {
            return this._deserializeWithCreator(jsonParser, deserializationContext);
        }
        final Object usingDefault = this._valueInstantiator.createUsingDefault(deserializationContext);
        if (this._injectables != null) {
            this.injectValues(deserializationContext, usingDefault);
        }
        Class<?> activeView;
        if (this._needViewProcesing) {
            activeView = deserializationContext.getActiveView();
        }
        else {
            activeView = null;
        }
        final SettableBeanProperty[] orderedProperties = this._orderedProperties;
        int n = 0;
        final int length = orderedProperties.length;
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            if (n == length) {
                if (!this._ignoreAllUnknown) {
                    throw deserializationContext.mappingException("Unexpected JSON values; expected at most " + length + " properties (in JSON Array)");
                }
                while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                    jsonParser.skipChildren();
                }
                return usingDefault;
            }
            else {
                final SettableBeanProperty settableBeanProperty = orderedProperties[n];
                ++n;
                Label_0188: {
                    if (settableBeanProperty != null) {
                        if (activeView != null) {
                            if (!settableBeanProperty.visibleInView(activeView)) {
                                break Label_0188;
                            }
                        }
                        try {
                            settableBeanProperty.deserializeSetAndReturn(jsonParser, deserializationContext, usingDefault);
                        }
                        catch (Exception ex) {
                            this.wrapAndThrow(ex, usingDefault, settableBeanProperty.getName(), deserializationContext);
                        }
                        continue;
                    }
                }
                jsonParser.skipChildren();
            }
        }
        return usingDefault;
    }
    
    @Override
    protected final Object _deserializeUsingPropertyBased(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final PropertyBasedCreator propertyBasedCreator = this._propertyBasedCreator;
        final PropertyValueBuffer startBuilding = propertyBasedCreator.startBuilding(jsonParser, deserializationContext, this._objectIdReader);
        final SettableBeanProperty[] orderedProperties = this._orderedProperties;
        final int length = orderedProperties.length;
        int n = 0;
        Object o = null;
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            SettableBeanProperty settableBeanProperty;
            if (n < length) {
                settableBeanProperty = orderedProperties[n];
            }
            else {
                settableBeanProperty = null;
            }
            Object o2 = null;
            Label_0071: {
                if (settableBeanProperty == null) {
                    jsonParser.skipChildren();
                    o2 = o;
                }
                else if (o != null) {
                    try {
                        o2 = settableBeanProperty.deserializeSetAndReturn(jsonParser, deserializationContext, o);
                    }
                    catch (Exception ex) {
                        this.wrapAndThrow(ex, o, settableBeanProperty.getName(), deserializationContext);
                        o2 = o;
                    }
                }
                else {
                    final String name = settableBeanProperty.getName();
                    final SettableBeanProperty creatorProperty = propertyBasedCreator.findCreatorProperty(name);
                    if (creatorProperty != null) {
                        final Object deserialize = creatorProperty.deserialize(jsonParser, deserializationContext);
                        o2 = o;
                        if (!startBuilding.assignParameter(creatorProperty.getCreatorIndex(), deserialize)) {
                            break Label_0071;
                        }
                        try {
                            o = (o2 = propertyBasedCreator.build(deserializationContext, startBuilding));
                            if (o.getClass() != this._beanType.getRawClass()) {
                                throw deserializationContext.mappingException("Can not support implicit polymorphic deserialization for POJOs-as-Arrays style: nominal type " + this._beanType.getRawClass().getName() + ", actual type " + o.getClass().getName());
                            }
                            break Label_0071;
                        }
                        catch (Exception ex2) {
                            this.wrapAndThrow(ex2, this._beanType.getRawClass(), name, deserializationContext);
                            o2 = o;
                            break Label_0071;
                        }
                    }
                    o2 = o;
                    if (!startBuilding.readIdProperty(name)) {
                        startBuilding.bufferProperty(settableBeanProperty, settableBeanProperty.deserialize(jsonParser, deserializationContext));
                        o2 = o;
                    }
                }
            }
            ++n;
            o = o2;
        }
        Object build;
        if ((build = o) != null) {
            return build;
        }
        try {
            build = propertyBasedCreator.build(deserializationContext, startBuilding);
            return build;
        }
        catch (Exception ex3) {
            this.wrapInstantiationProblem(ex3, deserializationContext);
            return null;
        }
    }
    
    protected Object _deserializeWithCreator(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._delegateDeserializer != null) {
            return this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
        }
        if (this._propertyBasedCreator != null) {
            return this._deserializeUsingPropertyBased(jsonParser, deserializationContext);
        }
        if (this._beanType.isAbstract()) {
            throw JsonMappingException.from(jsonParser, "Can not instantiate abstract type " + this._beanType + " (need to add/enable type information?)");
        }
        throw JsonMappingException.from(jsonParser, "No suitable constructor found for type " + this._beanType + ": can not instantiate from JSON object (need to add/enable type information?)");
    }
    
    @Override
    protected BeanAsArrayBuilderDeserializer asArrayDeserializer() {
        return this;
    }
    
    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() != JsonToken.START_ARRAY) {
            return this.finishBuild(deserializationContext, this._deserializeFromNonArray(jsonParser, deserializationContext));
        }
        if (!this._vanillaProcessing) {
            return this.finishBuild(deserializationContext, this._deserializeNonVanilla(jsonParser, deserializationContext));
        }
        Object o = this._valueInstantiator.createUsingDefault(deserializationContext);
        final SettableBeanProperty[] orderedProperties = this._orderedProperties;
        int n = 0;
        final int length = orderedProperties.length;
    Label_0151_Outer:
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            if (n != length) {
                final SettableBeanProperty settableBeanProperty = orderedProperties[n];
                while (true) {
                    Label_0177: {
                        if (settableBeanProperty == null) {
                            break Label_0177;
                        }
                        try {
                            o = settableBeanProperty.deserializeSetAndReturn(jsonParser, deserializationContext, o);
                            ++n;
                            continue Label_0151_Outer;
                        }
                        catch (Exception ex) {
                            this.wrapAndThrow(ex, o, settableBeanProperty.getName(), deserializationContext);
                            continue;
                        }
                    }
                    jsonParser.skipChildren();
                    continue;
                }
            }
            if (!this._ignoreAllUnknown) {
                throw deserializationContext.mappingException("Unexpected JSON values; expected at most " + length + " properties (in JSON Array)");
            }
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                jsonParser.skipChildren();
            }
            return this.finishBuild(deserializationContext, o);
        }
        return this.finishBuild(deserializationContext, o);
    }
    
    @Override
    public Object deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext, Object deserializeSetAndReturn) throws IOException, JsonProcessingException {
        if (this._injectables != null) {
            this.injectValues(deserializationContext, deserializeSetAndReturn);
        }
        final SettableBeanProperty[] orderedProperties = this._orderedProperties;
        int n = 0;
        final int length = orderedProperties.length;
    Label_0113_Outer:
        while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
            if (n != length) {
                final SettableBeanProperty settableBeanProperty = orderedProperties[n];
                while (true) {
                    Label_0140: {
                        if (settableBeanProperty == null) {
                            break Label_0140;
                        }
                        try {
                            deserializeSetAndReturn = settableBeanProperty.deserializeSetAndReturn(jsonParser, deserializationContext, deserializeSetAndReturn);
                            ++n;
                            continue Label_0113_Outer;
                        }
                        catch (Exception ex) {
                            this.wrapAndThrow(ex, deserializeSetAndReturn, settableBeanProperty.getName(), deserializationContext);
                            continue;
                        }
                    }
                    jsonParser.skipChildren();
                    continue;
                }
            }
            if (!this._ignoreAllUnknown) {
                throw deserializationContext.mappingException("Unexpected JSON values; expected at most " + length + " properties (in JSON Array)");
            }
            while (jsonParser.nextToken() != JsonToken.END_ARRAY) {
                jsonParser.skipChildren();
            }
            return this.finishBuild(deserializationContext, deserializeSetAndReturn);
        }
        return this.finishBuild(deserializationContext, deserializeSetAndReturn);
    }
    
    @Override
    public Object deserializeFromObject(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        return this._deserializeFromNonArray(jsonParser, deserializationContext);
    }
    
    protected final Object finishBuild(final DeserializationContext deserializationContext, Object invoke) throws IOException {
        try {
            invoke = this._buildMethod.getMember().invoke(invoke, new Object[0]);
            return invoke;
        }
        catch (Exception ex) {
            this.wrapInstantiationProblem(ex, deserializationContext);
            return null;
        }
    }
    
    @Override
    public JsonDeserializer<Object> unwrappingDeserializer(final NameTransformer nameTransformer) {
        return this._delegate.unwrappingDeserializer(nameTransformer);
    }
    
    @Override
    public BeanAsArrayBuilderDeserializer withIgnorableProperties(final HashSet<String> set) {
        return new BeanAsArrayBuilderDeserializer(this._delegate.withIgnorableProperties(set), this._orderedProperties, this._buildMethod);
    }
    
    @Override
    public BeanAsArrayBuilderDeserializer withObjectIdReader(final ObjectIdReader objectIdReader) {
        return new BeanAsArrayBuilderDeserializer(this._delegate.withObjectIdReader(objectIdReader), this._orderedProperties, this._buildMethod);
    }
}
