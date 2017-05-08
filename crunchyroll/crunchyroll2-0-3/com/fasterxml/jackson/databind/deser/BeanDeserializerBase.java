// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import java.util.Set;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.deser.impl.ManagedReferenceProperty;
import com.fasterxml.jackson.databind.deser.std.ContainerDeserializerBase;
import java.lang.reflect.Constructor;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;
import java.lang.reflect.Member;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.lang.reflect.InvocationTargetException;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.util.List;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.util.NameTransformer;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
import com.fasterxml.jackson.databind.deser.impl.UnwrappedPropertyHandler;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
import java.util.HashSet;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
import java.util.Map;
import java.io.Serializable;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public abstract class BeanDeserializerBase extends StdDeserializer<Object> implements ContextualDeserializer, ResolvableDeserializer, Serializable
{
    protected SettableAnyProperty _anySetter;
    protected final Map<String, SettableBeanProperty> _backRefs;
    protected final BeanPropertyMap _beanProperties;
    protected final JavaType _beanType;
    private final transient Annotations _classAnnotations;
    protected JsonDeserializer<Object> _delegateDeserializer;
    protected ExternalTypeHandler _externalTypeIdHandler;
    protected final HashSet<String> _ignorableProps;
    protected final boolean _ignoreAllUnknown;
    protected final ValueInjector[] _injectables;
    protected final boolean _needViewProcesing;
    protected boolean _nonStandardCreation;
    protected final ObjectIdReader _objectIdReader;
    protected PropertyBasedCreator _propertyBasedCreator;
    protected final JsonFormat.Shape _serializationShape;
    protected transient HashMap<ClassKey, JsonDeserializer<Object>> _subDeserializers;
    protected UnwrappedPropertyHandler _unwrappedPropertyHandler;
    protected final ValueInstantiator _valueInstantiator;
    protected boolean _vanillaProcessing;
    
    protected BeanDeserializerBase(final BeanDeserializerBase beanDeserializerBase) {
        this(beanDeserializerBase, beanDeserializerBase._ignoreAllUnknown);
    }
    
    public BeanDeserializerBase(final BeanDeserializerBase beanDeserializerBase, final ObjectIdReader objectIdReader) {
        super(beanDeserializerBase._beanType);
        this._classAnnotations = beanDeserializerBase._classAnnotations;
        this._beanType = beanDeserializerBase._beanType;
        this._valueInstantiator = beanDeserializerBase._valueInstantiator;
        this._delegateDeserializer = beanDeserializerBase._delegateDeserializer;
        this._propertyBasedCreator = beanDeserializerBase._propertyBasedCreator;
        this._backRefs = beanDeserializerBase._backRefs;
        this._ignorableProps = beanDeserializerBase._ignorableProps;
        this._ignoreAllUnknown = beanDeserializerBase._ignoreAllUnknown;
        this._anySetter = beanDeserializerBase._anySetter;
        this._injectables = beanDeserializerBase._injectables;
        this._nonStandardCreation = beanDeserializerBase._nonStandardCreation;
        this._unwrappedPropertyHandler = beanDeserializerBase._unwrappedPropertyHandler;
        this._needViewProcesing = beanDeserializerBase._needViewProcesing;
        this._serializationShape = beanDeserializerBase._serializationShape;
        this._vanillaProcessing = beanDeserializerBase._vanillaProcessing;
        this._objectIdReader = objectIdReader;
        if (objectIdReader == null) {
            this._beanProperties = beanDeserializerBase._beanProperties;
            return;
        }
        this._beanProperties = beanDeserializerBase._beanProperties.withProperty(new ObjectIdValueProperty(objectIdReader, true));
    }
    
    protected BeanDeserializerBase(final BeanDeserializerBase beanDeserializerBase, final NameTransformer nameTransformer) {
        super(beanDeserializerBase._beanType);
        this._classAnnotations = beanDeserializerBase._classAnnotations;
        this._beanType = beanDeserializerBase._beanType;
        this._valueInstantiator = beanDeserializerBase._valueInstantiator;
        this._delegateDeserializer = beanDeserializerBase._delegateDeserializer;
        this._propertyBasedCreator = beanDeserializerBase._propertyBasedCreator;
        this._backRefs = beanDeserializerBase._backRefs;
        this._ignorableProps = beanDeserializerBase._ignorableProps;
        this._ignoreAllUnknown = (nameTransformer != null || beanDeserializerBase._ignoreAllUnknown);
        this._anySetter = beanDeserializerBase._anySetter;
        this._injectables = beanDeserializerBase._injectables;
        this._objectIdReader = beanDeserializerBase._objectIdReader;
        this._nonStandardCreation = beanDeserializerBase._nonStandardCreation;
        final UnwrappedPropertyHandler unwrappedPropertyHandler = beanDeserializerBase._unwrappedPropertyHandler;
        UnwrappedPropertyHandler renameAll;
        if (nameTransformer != null) {
            if ((renameAll = unwrappedPropertyHandler) != null) {
                renameAll = unwrappedPropertyHandler.renameAll(nameTransformer);
            }
            this._beanProperties = beanDeserializerBase._beanProperties.renameAll(nameTransformer);
        }
        else {
            this._beanProperties = beanDeserializerBase._beanProperties;
            renameAll = unwrappedPropertyHandler;
        }
        this._unwrappedPropertyHandler = renameAll;
        this._needViewProcesing = beanDeserializerBase._needViewProcesing;
        this._serializationShape = beanDeserializerBase._serializationShape;
        this._vanillaProcessing = false;
    }
    
    public BeanDeserializerBase(final BeanDeserializerBase beanDeserializerBase, final HashSet<String> ignorableProps) {
        super(beanDeserializerBase._beanType);
        this._classAnnotations = beanDeserializerBase._classAnnotations;
        this._beanType = beanDeserializerBase._beanType;
        this._valueInstantiator = beanDeserializerBase._valueInstantiator;
        this._delegateDeserializer = beanDeserializerBase._delegateDeserializer;
        this._propertyBasedCreator = beanDeserializerBase._propertyBasedCreator;
        this._backRefs = beanDeserializerBase._backRefs;
        this._ignorableProps = ignorableProps;
        this._ignoreAllUnknown = beanDeserializerBase._ignoreAllUnknown;
        this._anySetter = beanDeserializerBase._anySetter;
        this._injectables = beanDeserializerBase._injectables;
        this._nonStandardCreation = beanDeserializerBase._nonStandardCreation;
        this._unwrappedPropertyHandler = beanDeserializerBase._unwrappedPropertyHandler;
        this._needViewProcesing = beanDeserializerBase._needViewProcesing;
        this._serializationShape = beanDeserializerBase._serializationShape;
        this._vanillaProcessing = beanDeserializerBase._vanillaProcessing;
        this._objectIdReader = beanDeserializerBase._objectIdReader;
        this._beanProperties = beanDeserializerBase._beanProperties;
    }
    
    protected BeanDeserializerBase(final BeanDeserializerBase beanDeserializerBase, final boolean ignoreAllUnknown) {
        super(beanDeserializerBase._beanType);
        this._classAnnotations = beanDeserializerBase._classAnnotations;
        this._beanType = beanDeserializerBase._beanType;
        this._valueInstantiator = beanDeserializerBase._valueInstantiator;
        this._delegateDeserializer = beanDeserializerBase._delegateDeserializer;
        this._propertyBasedCreator = beanDeserializerBase._propertyBasedCreator;
        this._beanProperties = beanDeserializerBase._beanProperties;
        this._backRefs = beanDeserializerBase._backRefs;
        this._ignorableProps = beanDeserializerBase._ignorableProps;
        this._ignoreAllUnknown = ignoreAllUnknown;
        this._anySetter = beanDeserializerBase._anySetter;
        this._injectables = beanDeserializerBase._injectables;
        this._objectIdReader = beanDeserializerBase._objectIdReader;
        this._nonStandardCreation = beanDeserializerBase._nonStandardCreation;
        this._unwrappedPropertyHandler = beanDeserializerBase._unwrappedPropertyHandler;
        this._needViewProcesing = beanDeserializerBase._needViewProcesing;
        this._serializationShape = beanDeserializerBase._serializationShape;
        this._vanillaProcessing = beanDeserializerBase._vanillaProcessing;
    }
    
    protected BeanDeserializerBase(final BeanDeserializerBuilder beanDeserializerBuilder, final BeanDescription beanDescription, final BeanPropertyMap beanProperties, final Map<String, SettableBeanProperty> backRefs, final HashSet<String> ignorableProps, final boolean ignoreAllUnknown, final boolean needViewProcesing) {
        final boolean b = true;
        final JsonFormat.Shape shape = null;
        super(beanDescription.getType());
        this._classAnnotations = beanDescription.getClassInfo().getAnnotations();
        this._beanType = beanDescription.getType();
        this._valueInstantiator = beanDeserializerBuilder.getValueInstantiator();
        this._beanProperties = beanProperties;
        this._backRefs = backRefs;
        this._ignorableProps = ignorableProps;
        this._ignoreAllUnknown = ignoreAllUnknown;
        this._anySetter = beanDeserializerBuilder.getAnySetter();
        final List<ValueInjector> injectables = beanDeserializerBuilder.getInjectables();
        ValueInjector[] injectables2;
        if (injectables == null || injectables.isEmpty()) {
            injectables2 = null;
        }
        else {
            injectables2 = injectables.toArray(new ValueInjector[injectables.size()]);
        }
        this._injectables = injectables2;
        this._objectIdReader = beanDeserializerBuilder.getObjectIdReader();
        this._nonStandardCreation = (this._unwrappedPropertyHandler != null || this._valueInstantiator.canCreateUsingDelegate() || this._valueInstantiator.canCreateFromObjectWith() || !this._valueInstantiator.canCreateUsingDefault());
        final JsonFormat.Value expectedFormat = beanDescription.findExpectedFormat(null);
        JsonFormat.Shape shape2;
        if (expectedFormat == null) {
            shape2 = shape;
        }
        else {
            shape2 = expectedFormat.getShape();
        }
        this._serializationShape = shape2;
        this._needViewProcesing = needViewProcesing;
        this._vanillaProcessing = (!this._nonStandardCreation && this._injectables == null && !this._needViewProcesing && this._objectIdReader != null && b);
    }
    
    private Throwable throwOrReturnThrowable(Throwable cause, final DeserializationContext deserializationContext) throws IOException {
        while (cause instanceof InvocationTargetException && cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause instanceof Error) {
            throw (Error)cause;
        }
        boolean b;
        if (deserializationContext == null || deserializationContext.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS)) {
            b = true;
        }
        else {
            b = false;
        }
        if (cause instanceof IOException) {
            if (!b || !(cause instanceof JsonProcessingException)) {
                throw (IOException)cause;
            }
        }
        else if (!b && cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
        }
        return cause;
    }
    
    protected abstract Object _deserializeUsingPropertyBased(final JsonParser p0, final DeserializationContext p1) throws IOException, JsonProcessingException;
    
    protected JsonDeserializer<Object> _findSubclassDeserializer(final DeserializationContext deserializationContext, final Object o, final TokenBuffer tokenBuffer) throws IOException, JsonProcessingException {
        synchronized (this) {
            JsonDeserializer<Object> jsonDeserializer;
            if (this._subDeserializers == null) {
                jsonDeserializer = null;
            }
            else {
                jsonDeserializer = this._subDeserializers.get(new ClassKey(o.getClass()));
            }
            // monitorexit(this)
            if (jsonDeserializer != null) {
                return jsonDeserializer;
            }
        }
        final DeserializationContext deserializationContext2;
        final JsonDeserializer<Object> rootValueDeserializer = deserializationContext2.findRootValueDeserializer(deserializationContext2.constructType(o.getClass()));
        if (rootValueDeserializer != null) {
            synchronized (this) {
                if (this._subDeserializers == null) {
                    this._subDeserializers = new HashMap<ClassKey, JsonDeserializer<Object>>();
                }
                this._subDeserializers.put(new ClassKey(o.getClass()), rootValueDeserializer);
                return rootValueDeserializer;
            }
        }
        return rootValueDeserializer;
    }
    
    protected SettableBeanProperty _resolveInnerClassValuedProperty(final DeserializationContext deserializationContext, final SettableBeanProperty settableBeanProperty) {
        final JsonDeserializer<Object> valueDeserializer = settableBeanProperty.getValueDeserializer();
        SettableBeanProperty settableBeanProperty2 = settableBeanProperty;
        if (valueDeserializer instanceof BeanDeserializerBase) {
            settableBeanProperty2 = settableBeanProperty;
            if (!((BeanDeserializerBase)valueDeserializer).getValueInstantiator().canCreateUsingDefault()) {
                final Class<?> rawClass = settableBeanProperty.getType().getRawClass();
                final Class<?> outerClass = ClassUtil.getOuterClass(rawClass);
                settableBeanProperty2 = settableBeanProperty;
                if (outerClass != null) {
                    settableBeanProperty2 = settableBeanProperty;
                    if (outerClass == this._beanType.getRawClass()) {
                        final Constructor[] constructors = rawClass.getConstructors();
                        final int length = constructors.length;
                        int n = 0;
                        while (true) {
                            settableBeanProperty2 = settableBeanProperty;
                            if (n >= length) {
                                break;
                            }
                            final Constructor constructor = constructors[n];
                            final Class[] parameterTypes = constructor.getParameterTypes();
                            if (parameterTypes.length == 1 && parameterTypes[0] == outerClass) {
                                if (deserializationContext.getConfig().canOverrideAccessModifiers()) {
                                    ClassUtil.checkAndFixAccess(constructor);
                                }
                                settableBeanProperty2 = new InnerClassProperty(settableBeanProperty, constructor);
                                break;
                            }
                            ++n;
                        }
                    }
                }
            }
        }
        return settableBeanProperty2;
    }
    
    protected SettableBeanProperty _resolveManagedReferenceProperty(final DeserializationContext deserializationContext, final SettableBeanProperty settableBeanProperty) {
        final String managedReferenceName = settableBeanProperty.getManagedReferenceName();
        if (managedReferenceName == null) {
            return settableBeanProperty;
        }
        final JsonDeserializer<Object> valueDeserializer = settableBeanProperty.getValueDeserializer();
        boolean b = false;
        SettableBeanProperty settableBeanProperty2;
        if (valueDeserializer instanceof BeanDeserializerBase) {
            settableBeanProperty2 = ((BeanDeserializerBase)valueDeserializer).findBackReference(managedReferenceName);
        }
        else if (valueDeserializer instanceof ContainerDeserializerBase) {
            final JsonDeserializer contentDeserializer = ((ContainerDeserializerBase)valueDeserializer).getContentDeserializer();
            if (!(contentDeserializer instanceof BeanDeserializerBase)) {
                String name;
                if (contentDeserializer == null) {
                    name = "NULL";
                }
                else {
                    name = ((BeanDeserializerBase)contentDeserializer).getClass().getName();
                }
                throw new IllegalArgumentException("Can not handle managed/back reference '" + managedReferenceName + "': value deserializer is of type ContainerDeserializerBase, but content type is not handled by a BeanDeserializer " + " (instead it's of type " + name + ")");
            }
            settableBeanProperty2 = ((BeanDeserializerBase)contentDeserializer).findBackReference(managedReferenceName);
            b = true;
        }
        else {
            if (!(valueDeserializer instanceof AbstractDeserializer)) {
                throw new IllegalArgumentException("Can not handle managed/back reference '" + managedReferenceName + "': type for value deserializer is not BeanDeserializer or ContainerDeserializerBase, but " + ((ContainerDeserializerBase<Object>)valueDeserializer).getClass().getName());
            }
            settableBeanProperty2 = ((AbstractDeserializer)valueDeserializer).findBackReference(managedReferenceName);
        }
        if (settableBeanProperty2 == null) {
            throw new IllegalArgumentException("Can not handle managed/back reference '" + managedReferenceName + "': no back reference property found from type " + settableBeanProperty.getType());
        }
        final JavaType beanType = this._beanType;
        final JavaType type = settableBeanProperty2.getType();
        if (!type.getRawClass().isAssignableFrom(beanType.getRawClass())) {
            throw new IllegalArgumentException("Can not handle managed/back reference '" + managedReferenceName + "': back reference type (" + type.getRawClass().getName() + ") not compatible with managed type (" + beanType.getRawClass().getName() + ")");
        }
        return new ManagedReferenceProperty(settableBeanProperty, managedReferenceName, settableBeanProperty2, this._classAnnotations, b);
    }
    
    protected SettableBeanProperty _resolveUnwrappedProperty(final DeserializationContext deserializationContext, final SettableBeanProperty settableBeanProperty) {
        final AnnotatedMember member = settableBeanProperty.getMember();
        if (member != null) {
            final NameTransformer unwrappingNameTransformer = deserializationContext.getAnnotationIntrospector().findUnwrappingNameTransformer(member);
            if (unwrappingNameTransformer != null) {
                final JsonDeserializer<Object> valueDeserializer = settableBeanProperty.getValueDeserializer();
                final JsonDeserializer<Object> unwrappingDeserializer = valueDeserializer.unwrappingDeserializer(unwrappingNameTransformer);
                if (unwrappingDeserializer != valueDeserializer && unwrappingDeserializer != null) {
                    return settableBeanProperty.withValueDeserializer(unwrappingDeserializer);
                }
            }
        }
        return null;
    }
    
    protected abstract BeanDeserializerBase asArrayDeserializer();
    
    @Override
    public JsonDeserializer<?> createContextual(final DeserializationContext deserializationContext, final BeanProperty beanProperty) throws JsonMappingException {
        final ObjectIdReader objectIdReader = this._objectIdReader;
        final AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        Annotated member;
        if (beanProperty == null || annotationIntrospector == null) {
            member = null;
        }
        else {
            member = beanProperty.getMember();
        }
        String[] array;
        ObjectIdReader objectIdReader2;
        if (beanProperty != null && annotationIntrospector != null) {
            final String[] propertiesToIgnore = annotationIntrospector.findPropertiesToIgnore(member);
            final ObjectIdInfo objectIdInfo = annotationIntrospector.findObjectIdInfo(member);
            if (objectIdInfo != null) {
                final ObjectIdInfo objectReferenceInfo = annotationIntrospector.findObjectReferenceInfo(member, objectIdInfo);
                final Class<? extends ObjectIdGenerator<?>> generatorType = objectReferenceInfo.getGeneratorType();
                SettableBeanProperty property;
                JavaType type;
                ObjectIdGenerator<?> objectIdGeneratorInstance;
                if (generatorType == ObjectIdGenerators.PropertyGenerator.class) {
                    final String propertyName = objectReferenceInfo.getPropertyName();
                    property = this.findProperty(propertyName);
                    if (property == null) {
                        throw new IllegalArgumentException("Invalid Object Id definition for " + this.getBeanClass().getName() + ": can not find property with name '" + propertyName + "'");
                    }
                    type = property.getType();
                    objectIdGeneratorInstance = new PropertyBasedObjectIdGenerator(objectReferenceInfo.getScope());
                }
                else {
                    type = deserializationContext.getTypeFactory().findTypeParameters(deserializationContext.constructType(generatorType), ObjectIdGenerator.class)[0];
                    objectIdGeneratorInstance = deserializationContext.objectIdGeneratorInstance(member, objectReferenceInfo);
                    property = null;
                }
                final ObjectIdReader construct = ObjectIdReader.construct(type, objectReferenceInfo.getPropertyName(), objectIdGeneratorInstance, deserializationContext.findRootValueDeserializer(type), property);
                array = propertiesToIgnore;
                objectIdReader2 = construct;
            }
            else {
                array = propertiesToIgnore;
                objectIdReader2 = objectIdReader;
            }
        }
        else {
            objectIdReader2 = objectIdReader;
            array = null;
        }
        BeanDeserializerBase withObjectIdReader;
        if (objectIdReader2 != null && objectIdReader2 != this._objectIdReader) {
            withObjectIdReader = this.withObjectIdReader(objectIdReader2);
        }
        else {
            withObjectIdReader = this;
        }
        BeanDeserializerBase withIgnorableProperties = withObjectIdReader;
        if (array != null) {
            withIgnorableProperties = withObjectIdReader;
            if (array.length != 0) {
                withIgnorableProperties = withObjectIdReader.withIgnorableProperties(ArrayBuilders.setAndArray(withObjectIdReader._ignorableProps, array));
            }
        }
        while (true) {
            Label_0341: {
                if (member == null) {
                    break Label_0341;
                }
                final JsonFormat.Value format = annotationIntrospector.findFormat(member);
                if (format == null) {
                    break Label_0341;
                }
                final JsonFormat.Shape shape = format.getShape();
                JsonFormat.Shape serializationShape = shape;
                if (shape == null) {
                    serializationShape = this._serializationShape;
                }
                if (serializationShape == JsonFormat.Shape.ARRAY) {
                    return withIgnorableProperties.asArrayDeserializer();
                }
                return withIgnorableProperties;
            }
            final JsonFormat.Shape shape = null;
            continue;
        }
    }
    
    public Object deserializeFromArray(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._delegateDeserializer != null) {
            try {
                final Object usingDelegate = this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
                if (this._injectables != null) {
                    this.injectValues(deserializationContext, usingDelegate);
                }
                return usingDelegate;
            }
            catch (Exception ex) {
                this.wrapInstantiationProblem(ex, deserializationContext);
            }
        }
        throw deserializationContext.mappingException(this.getBeanClass());
    }
    
    public Object deserializeFromBoolean(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        if (this._delegateDeserializer != null && !this._valueInstantiator.canCreateFromBoolean()) {
            final Object usingDelegate = this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
            if (this._injectables != null) {
                this.injectValues(deserializationContext, usingDelegate);
            }
            return usingDelegate;
        }
        return this._valueInstantiator.createFromBoolean(deserializationContext, jsonParser.getCurrentToken() == JsonToken.VALUE_TRUE);
    }
    
    public Object deserializeFromDouble(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Object o = null;
        switch (jsonParser.getNumberType()) {
            default: {
                if (this._delegateDeserializer != null) {
                    o = this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
                    break;
                }
                throw deserializationContext.instantiationException(this.getBeanClass(), "no suitable creator method found to deserialize from JSON floating-point number");
            }
            case FLOAT:
            case DOUBLE: {
                if (this._delegateDeserializer == null || this._valueInstantiator.canCreateFromDouble()) {
                    return this._valueInstantiator.createFromDouble(deserializationContext, jsonParser.getDoubleValue());
                }
                final Object o2 = o = this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
                if (this._injectables != null) {
                    this.injectValues(deserializationContext, o2);
                    return o2;
                }
                break;
            }
        }
        return o;
    }
    
    public Object deserializeFromNumber(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Object o = null;
        if (this._objectIdReader != null) {
            o = this.deserializeFromObjectId(jsonParser, deserializationContext);
        }
        else {
            switch (jsonParser.getNumberType()) {
                default: {
                    if (this._delegateDeserializer == null) {
                        throw deserializationContext.instantiationException(this.getBeanClass(), "no suitable creator method found to deserialize from JSON integer number");
                    }
                    final Object o2 = o = this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
                    if (this._injectables != null) {
                        this.injectValues(deserializationContext, o2);
                        return o2;
                    }
                    break;
                }
                case INT: {
                    if (this._delegateDeserializer == null || this._valueInstantiator.canCreateFromInt()) {
                        return this._valueInstantiator.createFromInt(deserializationContext, jsonParser.getIntValue());
                    }
                    final Object o3 = o = this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
                    if (this._injectables != null) {
                        this.injectValues(deserializationContext, o3);
                        return o3;
                    }
                    break;
                }
                case LONG: {
                    if (this._delegateDeserializer == null || this._valueInstantiator.canCreateFromInt()) {
                        return this._valueInstantiator.createFromLong(deserializationContext, jsonParser.getLongValue());
                    }
                    final Object o4 = o = this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
                    if (this._injectables != null) {
                        this.injectValues(deserializationContext, o4);
                        return o4;
                    }
                    break;
                }
            }
        }
        return o;
    }
    
    public abstract Object deserializeFromObject(final JsonParser p0, final DeserializationContext p1) throws IOException, JsonProcessingException;
    
    protected Object deserializeFromObjectId(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final Object deserialize = this._objectIdReader.deserializer.deserialize(jsonParser, deserializationContext);
        final Object item = deserializationContext.findObjectId(deserialize, this._objectIdReader.generator).item;
        if (item == null) {
            throw new IllegalStateException("Could not resolve Object Id [" + deserialize + "] (for " + this._beanType + ") -- unresolved forward-reference?");
        }
        return item;
    }
    
    protected Object deserializeFromObjectUsingNonDefault(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
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
    
    public Object deserializeFromString(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        Object o;
        if (this._objectIdReader != null) {
            o = this.deserializeFromObjectId(jsonParser, deserializationContext);
        }
        else {
            if (this._delegateDeserializer == null || this._valueInstantiator.canCreateFromString()) {
                return this._valueInstantiator.createFromString(deserializationContext, jsonParser.getText());
            }
            final Object o2 = o = this._valueInstantiator.createUsingDelegate(deserializationContext, this._delegateDeserializer.deserialize(jsonParser, deserializationContext));
            if (this._injectables != null) {
                this.injectValues(deserializationContext, o2);
                return o2;
            }
        }
        return o;
    }
    
    protected Object deserializeWithObjectId(JsonParser parser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final String propertyName = this._objectIdReader.propertyName;
        if (propertyName.equals(parser.getCurrentName())) {
            return this.deserializeFromObject(parser, deserializationContext);
        }
        TokenBuffer tokenBuffer = new TokenBuffer(parser.getCodec());
        TokenBuffer tokenBuffer2 = null;
        while (parser.getCurrentToken() != JsonToken.END_OBJECT) {
            final String currentName = parser.getCurrentName();
            if (tokenBuffer2 == null) {
                if (propertyName.equals(currentName)) {
                    tokenBuffer2 = new TokenBuffer(parser.getCodec());
                    tokenBuffer2.writeFieldName(currentName);
                    parser.nextToken();
                    tokenBuffer2.copyCurrentStructure(parser);
                    tokenBuffer2.append(tokenBuffer);
                    tokenBuffer = null;
                }
                else {
                    tokenBuffer.writeFieldName(currentName);
                    parser.nextToken();
                    tokenBuffer.copyCurrentStructure(parser);
                }
            }
            else {
                tokenBuffer2.writeFieldName(currentName);
                parser.nextToken();
                tokenBuffer2.copyCurrentStructure(parser);
            }
            parser.nextToken();
        }
        if (tokenBuffer2 != null) {
            tokenBuffer = tokenBuffer2;
        }
        tokenBuffer.writeEndObject();
        parser = tokenBuffer.asParser();
        parser.nextToken();
        return this.deserializeFromObject(parser, deserializationContext);
    }
    
    @Override
    public final Object deserializeWithType(final JsonParser jsonParser, final DeserializationContext deserializationContext, final TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
        if (this._objectIdReader != null) {
            final JsonToken currentToken = jsonParser.getCurrentToken();
            if (currentToken != null && currentToken.isScalarValue()) {
                return this.deserializeFromObjectId(jsonParser, deserializationContext);
            }
        }
        return typeDeserializer.deserializeTypedFromObject(jsonParser, deserializationContext);
    }
    
    public SettableBeanProperty findBackReference(final String s) {
        if (this._backRefs == null) {
            return null;
        }
        return this._backRefs.get(s);
    }
    
    protected JsonDeserializer<Object> findConvertingDeserializer(final DeserializationContext deserializationContext, final SettableBeanProperty settableBeanProperty) throws JsonMappingException {
        final AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        if (annotationIntrospector != null) {
            final Object deserializationConverter = annotationIntrospector.findDeserializationConverter(settableBeanProperty.getMember());
            if (deserializationConverter != null) {
                final Converter<Object, Object> converterInstance = deserializationContext.converterInstance(settableBeanProperty.getMember(), deserializationConverter);
                final JavaType inputType = converterInstance.getInputType(deserializationContext.getTypeFactory());
                return new StdDelegatingDeserializer<Object>(converterInstance, inputType, deserializationContext.findContextualValueDeserializer(inputType, settableBeanProperty));
            }
        }
        return null;
    }
    
    public SettableBeanProperty findProperty(final String s) {
        SettableBeanProperty find;
        if (this._beanProperties == null) {
            find = null;
        }
        else {
            find = this._beanProperties.find(s);
        }
        SettableBeanProperty creatorProperty = find;
        if (find == null) {
            creatorProperty = find;
            if (this._propertyBasedCreator != null) {
                creatorProperty = this._propertyBasedCreator.findCreatorProperty(s);
            }
        }
        return creatorProperty;
    }
    
    public final Class<?> getBeanClass() {
        return this._beanType.getRawClass();
    }
    
    @Override
    public Collection<Object> getKnownPropertyNames() {
        final ArrayList<String> list = (ArrayList<String>)new ArrayList<Object>();
        final Iterator<SettableBeanProperty> iterator = this._beanProperties.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next().getName());
        }
        return (Collection<Object>)list;
    }
    
    public ValueInstantiator getValueInstantiator() {
        return this._valueInstantiator;
    }
    
    protected Object handlePolymorphic(final JsonParser jsonParser, final DeserializationContext deserializationContext, Object deserialize, final TokenBuffer tokenBuffer) throws IOException, JsonProcessingException {
        final JsonDeserializer<Object> findSubclassDeserializer = this._findSubclassDeserializer(deserializationContext, deserialize, tokenBuffer);
        if (findSubclassDeserializer != null) {
            Object deserialize2;
            if (tokenBuffer != null) {
                tokenBuffer.writeEndObject();
                final JsonParser parser = tokenBuffer.asParser();
                parser.nextToken();
                deserialize2 = findSubclassDeserializer.deserialize(parser, deserializationContext, deserialize);
            }
            else {
                deserialize2 = deserialize;
            }
            deserialize = deserialize2;
            if (jsonParser != null) {
                deserialize = findSubclassDeserializer.deserialize(jsonParser, deserializationContext, deserialize2);
            }
        }
        else {
            Object handleUnknownProperties;
            if (tokenBuffer != null) {
                handleUnknownProperties = this.handleUnknownProperties(deserializationContext, deserialize, tokenBuffer);
            }
            else {
                handleUnknownProperties = deserialize;
            }
            deserialize = handleUnknownProperties;
            if (jsonParser != null) {
                return this.deserialize(jsonParser, deserializationContext, handleUnknownProperties);
            }
        }
        return deserialize;
    }
    
    protected Object handleUnknownProperties(final DeserializationContext deserializationContext, final Object o, final TokenBuffer tokenBuffer) throws IOException, JsonProcessingException {
        tokenBuffer.writeEndObject();
        final JsonParser parser = tokenBuffer.asParser();
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            final String currentName = parser.getCurrentName();
            parser.nextToken();
            this.handleUnknownProperty(parser, deserializationContext, o, currentName);
        }
        return o;
    }
    
    @Override
    protected void handleUnknownProperty(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o, final String s) throws IOException, JsonProcessingException {
        if (this._ignoreAllUnknown || (this._ignorableProps != null && this._ignorableProps.contains(s))) {
            jsonParser.skipChildren();
            return;
        }
        super.handleUnknownProperty(jsonParser, deserializationContext, o, s);
    }
    
    protected void handleUnknownVanilla(final JsonParser jsonParser, final DeserializationContext deserializationContext, final Object o, final String s) throws IOException, JsonProcessingException {
        if (this._ignorableProps != null && this._ignorableProps.contains(s)) {
            jsonParser.skipChildren();
            return;
        }
        if (this._anySetter != null) {
            try {
                this._anySetter.deserializeAndSet(jsonParser, deserializationContext, o, s);
                return;
            }
            catch (Exception ex) {
                this.wrapAndThrow(ex, o, s, deserializationContext);
                return;
            }
        }
        this.handleUnknownProperty(jsonParser, deserializationContext, o, s);
    }
    
    protected void injectValues(final DeserializationContext deserializationContext, final Object o) throws IOException, JsonProcessingException {
        final ValueInjector[] injectables = this._injectables;
        for (int length = injectables.length, i = 0; i < length; ++i) {
            injectables[i].inject(deserializationContext, o);
        }
    }
    
    @Override
    public boolean isCachable() {
        return true;
    }
    
    @Override
    public void resolve(final DeserializationContext deserializationContext) throws JsonMappingException {
        final boolean b = false;
        ExternalTypeHandler.Builder builder2;
        if (this._valueInstantiator.canCreateFromObjectWith()) {
            this._propertyBasedCreator = PropertyBasedCreator.construct(deserializationContext, this._valueInstantiator, this._valueInstantiator.getFromObjectArguments(deserializationContext.getConfig()));
            final Iterator<SettableBeanProperty> iterator = this._propertyBasedCreator.properties().iterator();
            ExternalTypeHandler.Builder builder = null;
            while (true) {
                builder2 = builder;
                if (!iterator.hasNext()) {
                    break;
                }
                final SettableBeanProperty settableBeanProperty = iterator.next();
                if (!settableBeanProperty.hasValueTypeDeserializer()) {
                    continue;
                }
                final TypeDeserializer valueTypeDeserializer = settableBeanProperty.getValueTypeDeserializer();
                if (valueTypeDeserializer.getTypeInclusion() != JsonTypeInfo.As.EXTERNAL_PROPERTY) {
                    continue;
                }
                ExternalTypeHandler.Builder builder3;
                if ((builder3 = builder) == null) {
                    builder3 = new ExternalTypeHandler.Builder();
                }
                builder3.addExternal(settableBeanProperty, valueTypeDeserializer);
                builder = builder3;
            }
        }
        else {
            builder2 = null;
        }
        final Iterator<SettableBeanProperty> iterator2 = this._beanProperties.iterator();
        final UnwrappedPropertyHandler unwrappedPropertyHandler = null;
        ExternalTypeHandler.Builder builder4 = builder2;
        UnwrappedPropertyHandler unwrappedPropertyHandler2 = unwrappedPropertyHandler;
        while (iterator2.hasNext()) {
            final SettableBeanProperty settableBeanProperty2 = iterator2.next();
            SettableBeanProperty settableBeanProperty3 = null;
            Label_0236: {
                if (!settableBeanProperty2.hasValueDeserializer()) {
                    JsonDeserializer<Object> jsonDeserializer;
                    if ((jsonDeserializer = this.findConvertingDeserializer(deserializationContext, settableBeanProperty2)) == null) {
                        jsonDeserializer = this.findDeserializer(deserializationContext, settableBeanProperty2.getType(), settableBeanProperty2);
                    }
                    settableBeanProperty3 = settableBeanProperty2.withValueDeserializer(jsonDeserializer);
                }
                else {
                    final JsonDeserializer<Object> valueDeserializer = settableBeanProperty2.getValueDeserializer();
                    if (valueDeserializer instanceof ContextualDeserializer) {
                        final JsonDeserializer<?> contextual = ((ContextualDeserializer)valueDeserializer).createContextual(deserializationContext, settableBeanProperty2);
                        if (contextual != valueDeserializer) {
                            settableBeanProperty3 = settableBeanProperty2.withValueDeserializer(contextual);
                            break Label_0236;
                        }
                    }
                    settableBeanProperty3 = settableBeanProperty2;
                }
            }
            final SettableBeanProperty resolveManagedReferenceProperty = this._resolveManagedReferenceProperty(deserializationContext, settableBeanProperty3);
            final SettableBeanProperty resolveUnwrappedProperty = this._resolveUnwrappedProperty(deserializationContext, resolveManagedReferenceProperty);
            if (resolveUnwrappedProperty != null) {
                if (unwrappedPropertyHandler2 == null) {
                    unwrappedPropertyHandler2 = new UnwrappedPropertyHandler();
                }
                unwrappedPropertyHandler2.addProperty(resolveUnwrappedProperty);
            }
            else {
                final SettableBeanProperty resolveInnerClassValuedProperty = this._resolveInnerClassValuedProperty(deserializationContext, resolveManagedReferenceProperty);
                if (resolveInnerClassValuedProperty != settableBeanProperty2) {
                    this._beanProperties.replace(resolveInnerClassValuedProperty);
                }
                if (!resolveInnerClassValuedProperty.hasValueTypeDeserializer()) {
                    continue;
                }
                final TypeDeserializer valueTypeDeserializer2 = resolveInnerClassValuedProperty.getValueTypeDeserializer();
                if (valueTypeDeserializer2.getTypeInclusion() != JsonTypeInfo.As.EXTERNAL_PROPERTY) {
                    continue;
                }
                if (builder4 == null) {
                    builder4 = new ExternalTypeHandler.Builder();
                }
                builder4.addExternal(resolveInnerClassValuedProperty, valueTypeDeserializer2);
                this._beanProperties.remove(resolveInnerClassValuedProperty);
            }
        }
        if (this._anySetter != null && !this._anySetter.hasValueDeserializer()) {
            this._anySetter = this._anySetter.withValueDeserializer(this.findDeserializer(deserializationContext, this._anySetter.getType(), this._anySetter.getProperty()));
        }
        if (this._valueInstantiator.canCreateUsingDelegate()) {
            final JavaType delegateType = this._valueInstantiator.getDelegateType(deserializationContext.getConfig());
            if (delegateType == null) {
                throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._beanType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
            }
            this._delegateDeserializer = this.findDeserializer(deserializationContext, delegateType, new BeanProperty.Std(null, delegateType, null, this._classAnnotations, this._valueInstantiator.getDelegateCreator(), false));
        }
        if (builder4 != null) {
            this._externalTypeIdHandler = builder4.build();
            this._nonStandardCreation = true;
        }
        if ((this._unwrappedPropertyHandler = unwrappedPropertyHandler2) != null) {
            this._nonStandardCreation = true;
        }
        boolean vanillaProcessing = b;
        if (this._vanillaProcessing) {
            vanillaProcessing = b;
            if (!this._nonStandardCreation) {
                vanillaProcessing = true;
            }
        }
        this._vanillaProcessing = vanillaProcessing;
    }
    
    @Override
    public abstract JsonDeserializer<Object> unwrappingDeserializer(final NameTransformer p0);
    
    public abstract BeanDeserializerBase withIgnorableProperties(final HashSet<String> p0);
    
    public abstract BeanDeserializerBase withObjectIdReader(final ObjectIdReader p0);
    
    public void wrapAndThrow(final Throwable t, final Object o, final String s, final DeserializationContext deserializationContext) throws IOException {
        throw JsonMappingException.wrapWithPath(this.throwOrReturnThrowable(t, deserializationContext), o, s);
    }
    
    protected void wrapInstantiationProblem(Throwable cause, final DeserializationContext deserializationContext) throws IOException {
        while (cause instanceof InvocationTargetException && cause.getCause() != null) {
            cause = cause.getCause();
        }
        if (cause instanceof Error) {
            throw (Error)cause;
        }
        final boolean b = deserializationContext == null || deserializationContext.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS);
        if (cause instanceof IOException) {
            throw (IOException)cause;
        }
        if (!b && cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
        }
        throw deserializationContext.instantiationException(this._beanType.getRawClass(), cause);
    }
}
