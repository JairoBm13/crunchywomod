// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.ext.OptionalHandlerFactory;
import java.util.HashMap;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.deser.impl.FieldProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.deser.std.ThrowableDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.lang.reflect.Member;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.reflect.Type;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import java.util.List;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import java.util.HashSet;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import java.util.Map;
import java.util.Collection;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import java.util.Set;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Iterator;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import java.io.Serializable;

public class BeanDeserializerFactory extends BasicDeserializerFactory implements Serializable
{
    private static final Class<?>[] INIT_CAUSE_PARAMS;
    private static final Class<?>[] NO_VIEWS;
    public static final BeanDeserializerFactory instance;
    
    static {
        INIT_CAUSE_PARAMS = new Class[] { Throwable.class };
        NO_VIEWS = new Class[0];
        instance = new BeanDeserializerFactory(new DeserializerFactoryConfig());
    }
    
    public BeanDeserializerFactory(final DeserializerFactoryConfig deserializerFactoryConfig) {
        super(deserializerFactoryConfig);
    }
    
    protected JsonDeserializer<Object> _findCustomBeanDeserializer(final JavaType javaType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
        final Iterator<Deserializers> iterator = this._factoryConfig.deserializers().iterator();
        while (iterator.hasNext()) {
            final JsonDeserializer<?> beanDeserializer = iterator.next().findBeanDeserializer(javaType, deserializationConfig, beanDescription);
            if (beanDeserializer != null) {
                return (JsonDeserializer<Object>)beanDeserializer;
            }
        }
        return null;
    }
    
    protected void addBeanProps(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final BeanDeserializerBuilder beanDeserializerBuilder) throws JsonMappingException {
        final SettableBeanProperty[] fromObjectArguments = beanDeserializerBuilder.getValueInstantiator().getFromObjectArguments(deserializationContext.getConfig());
        final AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        final Boolean ignoreUnknownProperties = annotationIntrospector.findIgnoreUnknownProperties(beanDescription.getClassInfo());
        if (ignoreUnknownProperties != null) {
            beanDeserializerBuilder.setIgnoreUnknownProperties(ignoreUnknownProperties);
        }
        final HashSet<String> arrayToSet = ArrayBuilders.arrayToSet(annotationIntrospector.findPropertiesToIgnore(beanDescription.getClassInfo()));
        final Iterator<Object> iterator = arrayToSet.iterator();
        while (iterator.hasNext()) {
            beanDeserializerBuilder.addIgnorable(iterator.next());
        }
        final AnnotatedMethod anySetter = beanDescription.findAnySetter();
        if (anySetter != null) {
            beanDeserializerBuilder.setAnySetter(this.constructAnySetter(deserializationContext, beanDescription, anySetter));
        }
        if (anySetter == null) {
            final Set<String> ignoredPropertyNames = beanDescription.getIgnoredPropertyNames();
            if (ignoredPropertyNames != null) {
                final Iterator<Object> iterator2 = ignoredPropertyNames.iterator();
                while (iterator2.hasNext()) {
                    beanDeserializerBuilder.addIgnorable(iterator2.next());
                }
            }
        }
        final boolean b = deserializationContext.isEnabled(MapperFeature.USE_GETTERS_AS_SETTERS) && deserializationContext.isEnabled(MapperFeature.AUTO_DETECT_GETTERS);
        List<BeanPropertyDefinition> list = this.filterBeanProps(deserializationContext, beanDescription, beanDeserializerBuilder, beanDescription.findProperties(), arrayToSet);
        List<BeanPropertyDefinition> list2;
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator3 = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                list2 = list;
                if (!iterator3.hasNext()) {
                    break;
                }
                list = iterator3.next().updateProperties(deserializationContext.getConfig(), beanDescription, list);
            }
        }
        else {
            list2 = list;
        }
        for (final BeanPropertyDefinition beanPropertyDefinition : list2) {
            final SettableBeanProperty settableBeanProperty = null;
            final SettableBeanProperty settableBeanProperty2 = null;
            if (beanPropertyDefinition.hasConstructorParameter()) {
                final String name = beanPropertyDefinition.getName();
                SettableBeanProperty settableBeanProperty3 = settableBeanProperty2;
                if (fromObjectArguments != null) {
                    final int length = fromObjectArguments.length;
                    int n = 0;
                    while (true) {
                        settableBeanProperty3 = settableBeanProperty2;
                        if (n >= length) {
                            break;
                        }
                        settableBeanProperty3 = fromObjectArguments[n];
                        if (name.equals(settableBeanProperty3.getName())) {
                            break;
                        }
                        ++n;
                    }
                }
                if (settableBeanProperty3 == null) {
                    throw deserializationContext.mappingException("Could not find creator property with name '" + name + "' (in class " + beanDescription.getBeanClass().getName() + ")");
                }
                beanDeserializerBuilder.addCreatorProperty(settableBeanProperty3);
            }
            else {
                SettableBeanProperty settableBeanProperty4 = null;
                Label_0475: {
                    if (beanPropertyDefinition.hasSetter()) {
                        settableBeanProperty4 = this.constructSettableProperty(deserializationContext, beanDescription, beanPropertyDefinition, beanPropertyDefinition.getSetter().getGenericParameterType(0));
                    }
                    else if (beanPropertyDefinition.hasField()) {
                        settableBeanProperty4 = this.constructSettableProperty(deserializationContext, beanDescription, beanPropertyDefinition, beanPropertyDefinition.getField().getGenericType());
                    }
                    else {
                        settableBeanProperty4 = settableBeanProperty;
                        if (b) {
                            settableBeanProperty4 = settableBeanProperty;
                            if (beanPropertyDefinition.hasGetter()) {
                                final Class<?> rawType = beanPropertyDefinition.getGetter().getRawType();
                                if (!Collection.class.isAssignableFrom(rawType)) {
                                    settableBeanProperty4 = settableBeanProperty;
                                    if (!Map.class.isAssignableFrom(rawType)) {
                                        break Label_0475;
                                    }
                                }
                                settableBeanProperty4 = this.constructSetterlessProperty(deserializationContext, beanDescription, beanPropertyDefinition);
                            }
                        }
                    }
                }
                if (settableBeanProperty4 == null) {
                    continue;
                }
                final Class<?>[] views = beanPropertyDefinition.findViews();
                Class<?>[] no_VIEWS;
                if ((no_VIEWS = views) == null) {
                    no_VIEWS = views;
                    if (!deserializationContext.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
                        no_VIEWS = BeanDeserializerFactory.NO_VIEWS;
                    }
                }
                settableBeanProperty4.setViews(no_VIEWS);
                beanDeserializerBuilder.addProperty(settableBeanProperty4);
            }
        }
    }
    
    protected void addInjectables(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final BeanDeserializerBuilder beanDeserializerBuilder) throws JsonMappingException {
        final Map<Object, AnnotatedMember> injectables = beanDescription.findInjectables();
        if (injectables != null) {
            final boolean canOverrideAccessModifiers = deserializationContext.canOverrideAccessModifiers();
            for (final Map.Entry<K, AnnotatedMember> entry : injectables.entrySet()) {
                final AnnotatedMember annotatedMember = entry.getValue();
                if (canOverrideAccessModifiers) {
                    annotatedMember.fixAccess();
                }
                beanDeserializerBuilder.addInjectable(annotatedMember.getName(), beanDescription.resolveType(annotatedMember.getGenericType()), beanDescription.getClassAnnotations(), annotatedMember, entry.getKey());
            }
        }
    }
    
    protected void addObjectIdReader(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final BeanDeserializerBuilder beanDeserializerBuilder) throws JsonMappingException {
        final ObjectIdInfo objectIdInfo = beanDescription.getObjectIdInfo();
        if (objectIdInfo == null) {
            return;
        }
        final Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
        SettableBeanProperty property;
        JavaType type;
        ObjectIdGenerator<?> objectIdGeneratorInstance;
        if (generatorType == ObjectIdGenerators.PropertyGenerator.class) {
            final String propertyName = objectIdInfo.getPropertyName();
            property = beanDeserializerBuilder.findProperty(propertyName);
            if (property == null) {
                throw new IllegalArgumentException("Invalid Object Id definition for " + beanDescription.getBeanClass().getName() + ": can not find property with name '" + propertyName + "'");
            }
            type = property.getType();
            objectIdGeneratorInstance = new PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
        }
        else {
            type = deserializationContext.getTypeFactory().findTypeParameters(deserializationContext.constructType(generatorType), ObjectIdGenerator.class)[0];
            property = null;
            objectIdGeneratorInstance = deserializationContext.objectIdGeneratorInstance(beanDescription.getClassInfo(), objectIdInfo);
        }
        beanDeserializerBuilder.setObjectIdReader(ObjectIdReader.construct(type, objectIdInfo.getPropertyName(), objectIdGeneratorInstance, deserializationContext.findRootValueDeserializer(type), property));
    }
    
    protected void addReferenceProperties(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final BeanDeserializerBuilder beanDeserializerBuilder) throws JsonMappingException {
        final Map<String, AnnotatedMember> backReferenceProperties = beanDescription.findBackReferenceProperties();
        if (backReferenceProperties != null) {
            for (final Map.Entry<String, AnnotatedMember> entry : backReferenceProperties.entrySet()) {
                final String s = entry.getKey();
                final AnnotatedMember annotatedMember = entry.getValue();
                Type type;
                if (annotatedMember instanceof AnnotatedMethod) {
                    type = ((AnnotatedMethod)annotatedMember).getGenericParameterType(0);
                }
                else {
                    type = annotatedMember.getRawType();
                }
                beanDeserializerBuilder.addBackReferenceProperty(s, this.constructSettableProperty(deserializationContext, beanDescription, SimpleBeanPropertyDefinition.construct(deserializationContext.getConfig(), annotatedMember), type));
            }
        }
    }
    
    public JsonDeserializer<Object> buildBeanDeserializer(final DeserializationContext deserializationContext, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        final ValueInstantiator valueInstantiator = this.findValueInstantiator(deserializationContext, beanDescription);
        BeanDeserializerBuilder constructBeanDeserializerBuilder = this.constructBeanDeserializerBuilder(deserializationContext, beanDescription);
        constructBeanDeserializerBuilder.setValueInstantiator(valueInstantiator);
        this.addBeanProps(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        this.addObjectIdReader(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        this.addReferenceProperties(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        this.addInjectables(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        final DeserializationConfig config = deserializationContext.getConfig();
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator = this._factoryConfig.deserializerModifiers().iterator();
            BeanDeserializerBuilder updateBuilder = constructBeanDeserializerBuilder;
            while (true) {
                constructBeanDeserializerBuilder = updateBuilder;
                if (!iterator.hasNext()) {
                    break;
                }
                updateBuilder = iterator.next().updateBuilder(config, beanDescription, updateBuilder);
            }
        }
        JsonDeserializer<?> jsonDeserializer;
        if (javaType.isAbstract() && !valueInstantiator.canInstantiate()) {
            jsonDeserializer = constructBeanDeserializerBuilder.buildAbstract();
        }
        else {
            jsonDeserializer = constructBeanDeserializerBuilder.build();
        }
        JsonDeserializer<Object> jsonDeserializer2;
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator2 = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                jsonDeserializer2 = (JsonDeserializer<Object>)jsonDeserializer;
                if (!iterator2.hasNext()) {
                    break;
                }
                jsonDeserializer = iterator2.next().modifyDeserializer(config, beanDescription, jsonDeserializer);
            }
        }
        else {
            jsonDeserializer2 = (JsonDeserializer<Object>)jsonDeserializer;
        }
        return jsonDeserializer2;
    }
    
    protected JsonDeserializer<Object> buildBuilderBasedDeserializer(final DeserializationContext deserializationContext, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        final ValueInstantiator valueInstantiator = this.findValueInstantiator(deserializationContext, beanDescription);
        final DeserializationConfig config = deserializationContext.getConfig();
        final BeanDeserializerBuilder constructBeanDeserializerBuilder = this.constructBeanDeserializerBuilder(deserializationContext, beanDescription);
        constructBeanDeserializerBuilder.setValueInstantiator(valueInstantiator);
        this.addBeanProps(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        this.addObjectIdReader(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        this.addReferenceProperties(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        this.addInjectables(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        final JsonPOJOBuilder.Value pojoBuilderConfig = beanDescription.findPOJOBuilderConfig();
        String buildMethodName;
        if (pojoBuilderConfig == null) {
            buildMethodName = "build";
        }
        else {
            buildMethodName = pojoBuilderConfig.buildMethodName;
        }
        final AnnotatedMethod method = beanDescription.findMethod(buildMethodName, null);
        if (method != null && config.canOverrideAccessModifiers()) {
            ClassUtil.checkAndFixAccess(method.getMember());
        }
        constructBeanDeserializerBuilder.setPOJOBuilder(method, pojoBuilderConfig);
        BeanDeserializerBuilder beanDeserializerBuilder = constructBeanDeserializerBuilder;
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator = this._factoryConfig.deserializerModifiers().iterator();
            BeanDeserializerBuilder updateBuilder = constructBeanDeserializerBuilder;
            while (true) {
                beanDeserializerBuilder = updateBuilder;
                if (!iterator.hasNext()) {
                    break;
                }
                updateBuilder = iterator.next().updateBuilder(config, beanDescription, updateBuilder);
            }
        }
        JsonDeserializer<?> jsonDeserializer = beanDeserializerBuilder.buildBuilderBased(javaType, buildMethodName);
        JsonDeserializer<Object> jsonDeserializer2;
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator2 = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                jsonDeserializer2 = (JsonDeserializer<Object>)jsonDeserializer;
                if (!iterator2.hasNext()) {
                    break;
                }
                jsonDeserializer = iterator2.next().modifyDeserializer(config, beanDescription, jsonDeserializer);
            }
        }
        else {
            jsonDeserializer2 = (JsonDeserializer<Object>)jsonDeserializer;
        }
        return jsonDeserializer2;
    }
    
    public JsonDeserializer<Object> buildThrowableDeserializer(final DeserializationContext deserializationContext, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        BeanDeserializerBuilder constructBeanDeserializerBuilder = this.constructBeanDeserializerBuilder(deserializationContext, beanDescription);
        constructBeanDeserializerBuilder.setValueInstantiator(this.findValueInstantiator(deserializationContext, beanDescription));
        this.addBeanProps(deserializationContext, beanDescription, constructBeanDeserializerBuilder);
        final AnnotatedMethod method = beanDescription.findMethod("initCause", BeanDeserializerFactory.INIT_CAUSE_PARAMS);
        if (method != null) {
            final SettableBeanProperty constructSettableProperty = this.constructSettableProperty(deserializationContext, beanDescription, SimpleBeanPropertyDefinition.construct(deserializationContext.getConfig(), method, "cause"), method.getGenericParameterType(0));
            if (constructSettableProperty != null) {
                constructBeanDeserializerBuilder.addOrReplaceProperty(constructSettableProperty, true);
            }
        }
        constructBeanDeserializerBuilder.addIgnorable("localizedMessage");
        constructBeanDeserializerBuilder.addIgnorable("suppressed");
        constructBeanDeserializerBuilder.addIgnorable("message");
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator = this._factoryConfig.deserializerModifiers().iterator();
            BeanDeserializerBuilder updateBuilder = constructBeanDeserializerBuilder;
            while (true) {
                constructBeanDeserializerBuilder = updateBuilder;
                if (!iterator.hasNext()) {
                    break;
                }
                updateBuilder = iterator.next().updateBuilder(config, beanDescription, updateBuilder);
            }
        }
        JsonDeserializer<?> jsonDeserializer2;
        final JsonDeserializer<?> jsonDeserializer = jsonDeserializer2 = constructBeanDeserializerBuilder.build();
        if (jsonDeserializer instanceof BeanDeserializer) {
            jsonDeserializer2 = new ThrowableDeserializer((BeanDeserializer)jsonDeserializer);
        }
        JsonDeserializer<Object> jsonDeserializer3;
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator2 = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                jsonDeserializer3 = (JsonDeserializer<Object>)jsonDeserializer2;
                if (!iterator2.hasNext()) {
                    break;
                }
                jsonDeserializer2 = iterator2.next().modifyDeserializer(config, beanDescription, jsonDeserializer2);
            }
        }
        else {
            jsonDeserializer3 = (JsonDeserializer<Object>)jsonDeserializer2;
        }
        return jsonDeserializer3;
    }
    
    protected SettableAnyProperty constructAnySetter(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final AnnotatedMethod annotatedMethod) throws JsonMappingException {
        if (deserializationContext.canOverrideAccessModifiers()) {
            annotatedMethod.fixAccess();
        }
        final JavaType resolveType = beanDescription.bindingsForBeanType().resolveType(annotatedMethod.getGenericParameterType(1));
        final BeanProperty.Std std = new BeanProperty.Std(annotatedMethod.getName(), resolveType, null, beanDescription.getClassAnnotations(), annotatedMethod, false);
        final JavaType resolveType2 = this.resolveType(deserializationContext, beanDescription, resolveType, annotatedMethod);
        final JsonDeserializer<Object> deserializerFromAnnotation = this.findDeserializerFromAnnotation(deserializationContext, annotatedMethod);
        if (deserializerFromAnnotation != null) {
            return new SettableAnyProperty(std, annotatedMethod, resolveType2, deserializerFromAnnotation);
        }
        return new SettableAnyProperty(std, annotatedMethod, this.modifyTypeByAnnotation(deserializationContext, annotatedMethod, resolveType2), null);
    }
    
    protected BeanDeserializerBuilder constructBeanDeserializerBuilder(final DeserializationContext deserializationContext, final BeanDescription beanDescription) {
        return new BeanDeserializerBuilder(beanDescription, deserializationContext.getConfig());
    }
    
    protected SettableBeanProperty constructSettableProperty(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final BeanPropertyDefinition beanPropertyDefinition, final Type type) throws JsonMappingException {
        final AnnotatedMember mutator = beanPropertyDefinition.getMutator();
        if (deserializationContext.canOverrideAccessModifiers()) {
            mutator.fixAccess();
        }
        final JavaType resolveType = beanDescription.resolveType(type);
        final BeanProperty.Std std = new BeanProperty.Std(beanPropertyDefinition.getName(), resolveType, beanPropertyDefinition.getWrapperName(), beanDescription.getClassAnnotations(), mutator, beanPropertyDefinition.isRequired());
        final JavaType resolveType2 = this.resolveType(deserializationContext, beanDescription, resolveType, mutator);
        if (resolveType2 != resolveType) {
            std.withType(resolveType2);
        }
        final JsonDeserializer<Object> deserializerFromAnnotation = this.findDeserializerFromAnnotation(deserializationContext, mutator);
        final JavaType modifyTypeByAnnotation = this.modifyTypeByAnnotation(deserializationContext, mutator, resolveType2);
        final TypeDeserializer typeDeserializer = modifyTypeByAnnotation.getTypeHandler();
        SettableBeanProperty settableBeanProperty;
        if (mutator instanceof AnnotatedMethod) {
            settableBeanProperty = new MethodProperty(beanPropertyDefinition, modifyTypeByAnnotation, typeDeserializer, beanDescription.getClassAnnotations(), (AnnotatedMethod)mutator);
        }
        else {
            settableBeanProperty = new FieldProperty(beanPropertyDefinition, modifyTypeByAnnotation, typeDeserializer, beanDescription.getClassAnnotations(), (AnnotatedField)mutator);
        }
        SettableBeanProperty withValueDeserializer = settableBeanProperty;
        if (deserializerFromAnnotation != null) {
            withValueDeserializer = settableBeanProperty.withValueDeserializer(deserializerFromAnnotation);
        }
        final AnnotationIntrospector.ReferenceProperty referenceType = beanPropertyDefinition.findReferenceType();
        if (referenceType != null && referenceType.isManagedReference()) {
            withValueDeserializer.setManagedReferenceName(referenceType.getName());
        }
        return withValueDeserializer;
    }
    
    protected SettableBeanProperty constructSetterlessProperty(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final BeanPropertyDefinition beanPropertyDefinition) throws JsonMappingException {
        final AnnotatedMethod getter = beanPropertyDefinition.getGetter();
        if (deserializationContext.canOverrideAccessModifiers()) {
            getter.fixAccess();
        }
        final JavaType type = getter.getType(beanDescription.bindingsForBeanType());
        final JsonDeserializer<Object> deserializerFromAnnotation = this.findDeserializerFromAnnotation(deserializationContext, getter);
        final JavaType modifyTypeByAnnotation = this.modifyTypeByAnnotation(deserializationContext, getter, type);
        SettableBeanProperty withValueDeserializer;
        final SetterlessProperty setterlessProperty = (SetterlessProperty)(withValueDeserializer = new SetterlessProperty(beanPropertyDefinition, modifyTypeByAnnotation, modifyTypeByAnnotation.getTypeHandler(), beanDescription.getClassAnnotations(), getter));
        if (deserializerFromAnnotation != null) {
            withValueDeserializer = setterlessProperty.withValueDeserializer(deserializerFromAnnotation);
        }
        return withValueDeserializer;
    }
    
    @Override
    public JsonDeserializer<Object> createBeanDeserializer(final DeserializationContext deserializationContext, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        JsonDeserializer<?> jsonDeserializer = this._findCustomBeanDeserializer(javaType, config, beanDescription);
        if (jsonDeserializer == null) {
            if (javaType.isThrowable()) {
                return this.buildThrowableDeserializer(deserializationContext, javaType, beanDescription);
            }
            if (javaType.isAbstract()) {
                final JavaType materializeAbstractType = this.materializeAbstractType(deserializationContext, javaType, beanDescription);
                if (materializeAbstractType != null) {
                    return this.buildBeanDeserializer(deserializationContext, materializeAbstractType, config.introspect(materializeAbstractType));
                }
            }
            if ((jsonDeserializer = this.findStdDeserializer(deserializationContext, javaType, beanDescription)) == null) {
                if (!this.isPotentialBeanType(javaType.getRawClass())) {
                    return null;
                }
                return this.buildBeanDeserializer(deserializationContext, javaType, beanDescription);
            }
        }
        return (JsonDeserializer<Object>)jsonDeserializer;
    }
    
    @Override
    public JsonDeserializer<Object> createBuilderBasedDeserializer(final DeserializationContext deserializationContext, final JavaType javaType, final BeanDescription beanDescription, final Class<?> clazz) throws JsonMappingException {
        return this.buildBuilderBasedDeserializer(deserializationContext, javaType, deserializationContext.getConfig().introspectForBuilder(deserializationContext.constructType(clazz)));
    }
    
    protected List<BeanPropertyDefinition> filterBeanProps(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final BeanDeserializerBuilder beanDeserializerBuilder, final List<BeanPropertyDefinition> list, final Set<String> set) throws JsonMappingException {
        final ArrayList<BeanPropertyDefinition> list2 = new ArrayList<BeanPropertyDefinition>(Math.max(4, list.size()));
        final HashMap<Class<?>, Boolean> hashMap = new HashMap<Class<?>, Boolean>();
        for (final BeanPropertyDefinition beanPropertyDefinition : list) {
            final String name = beanPropertyDefinition.getName();
            if (!set.contains(name)) {
                if (!beanPropertyDefinition.hasConstructorParameter()) {
                    Class<?> clazz = null;
                    if (beanPropertyDefinition.hasSetter()) {
                        clazz = beanPropertyDefinition.getSetter().getRawParameterType(0);
                    }
                    else if (beanPropertyDefinition.hasField()) {
                        clazz = beanPropertyDefinition.getField().getRawType();
                    }
                    if (clazz != null && this.isIgnorableType(deserializationContext.getConfig(), beanDescription, clazz, hashMap)) {
                        beanDeserializerBuilder.addIgnorable(name);
                        continue;
                    }
                }
                list2.add(beanPropertyDefinition);
            }
        }
        return list2;
    }
    
    protected JsonDeserializer<?> findOptionalStdDeserializer(final DeserializationContext deserializationContext, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        return OptionalHandlerFactory.instance.findDeserializer(javaType, deserializationContext.getConfig(), beanDescription);
    }
    
    protected JsonDeserializer<?> findStdDeserializer(final DeserializationContext deserializationContext, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        final JsonDeserializer<?> defaultDeserializer = this.findDefaultDeserializer(deserializationContext, javaType, beanDescription);
        if (defaultDeserializer != null) {
            return defaultDeserializer;
        }
        if (AtomicReference.class.isAssignableFrom(javaType.getRawClass())) {
            final JavaType[] typeParameters = deserializationContext.getTypeFactory().findTypeParameters(javaType, AtomicReference.class);
            JavaType unknownType;
            if (typeParameters == null || typeParameters.length < 1) {
                unknownType = TypeFactory.unknownType();
            }
            else {
                unknownType = typeParameters[0];
            }
            return new JdkDeserializers.AtomicReferenceDeserializer(unknownType);
        }
        return this.findOptionalStdDeserializer(deserializationContext, javaType, beanDescription);
    }
    
    protected boolean isIgnorableType(final DeserializationConfig deserializationConfig, BeanDescription introspectClassAnnotations, final Class<?> clazz, final Map<Class<?>, Boolean> map) {
        Boolean b;
        if ((b = map.get(clazz)) == null) {
            introspectClassAnnotations = deserializationConfig.introspectClassAnnotations(clazz);
            if ((b = deserializationConfig.getAnnotationIntrospector().isIgnorableType(introspectClassAnnotations.getClassInfo())) == null) {
                b = Boolean.FALSE;
            }
        }
        return b;
    }
    
    protected boolean isPotentialBeanType(final Class<?> clazz) {
        final String canBeABeanType = ClassUtil.canBeABeanType(clazz);
        if (canBeABeanType != null) {
            throw new IllegalArgumentException("Can not deserialize Class " + clazz.getName() + " (of type " + canBeABeanType + ") as a Bean");
        }
        if (ClassUtil.isProxyType(clazz)) {
            throw new IllegalArgumentException("Can not deserialize Proxy class " + clazz.getName() + " as a Bean");
        }
        final String localType = ClassUtil.isLocalType(clazz, true);
        if (localType != null) {
            throw new IllegalArgumentException("Can not deserialize Class " + clazz.getName() + " (of type " + localType + ") as a Bean");
        }
        return true;
    }
    
    protected JavaType materializeAbstractType(final DeserializationContext deserializationContext, JavaType type, final BeanDescription beanDescription) throws JsonMappingException {
        type = beanDescription.getType();
        final Iterator<AbstractTypeResolver> iterator = this._factoryConfig.abstractTypeResolvers().iterator();
        while (iterator.hasNext()) {
            final JavaType resolveAbstractType = iterator.next().resolveAbstractType(deserializationContext.getConfig(), type);
            if (resolveAbstractType != null) {
                return resolveAbstractType;
            }
        }
        return null;
    }
}
