// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import java.util.HashMap;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import java.util.Collection;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import java.util.HashSet;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import java.lang.reflect.Type;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import com.fasterxml.jackson.databind.ser.impl.FilteredBeanPropertyWriter;
import java.util.List;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.MapperFeature;
import java.util.ArrayList;
import java.util.Iterator;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import java.io.Serializable;

public class BeanSerializerFactory extends BasicSerializerFactory implements Serializable
{
    public static final BeanSerializerFactory instance;
    
    static {
        instance = new BeanSerializerFactory(null);
    }
    
    protected BeanSerializerFactory(final SerializerFactoryConfig serializerFactoryConfig) {
        super(serializerFactoryConfig);
    }
    
    protected BeanPropertyWriter _constructWriter(final SerializerProvider serializerProvider, final BeanPropertyDefinition beanPropertyDefinition, final TypeBindings typeBindings, final PropertyBuilder propertyBuilder, final boolean b, final AnnotatedMember annotatedMember) throws JsonMappingException {
        final String name = beanPropertyDefinition.getName();
        if (serializerProvider.canOverrideAccessModifiers()) {
            annotatedMember.fixAccess();
        }
        final JavaType type = annotatedMember.getType(typeBindings);
        final BeanProperty.Std std = new BeanProperty.Std(name, type, beanPropertyDefinition.getWrapperName(), propertyBuilder.getClassAnnotations(), annotatedMember, beanPropertyDefinition.isRequired());
        final JsonSerializer<Object> serializerFromAnnotation = this.findSerializerFromAnnotation(serializerProvider, annotatedMember);
        if (serializerFromAnnotation instanceof ResolvableSerializer) {
            ((ResolvableSerializer)serializerFromAnnotation).resolve(serializerProvider);
        }
        JsonSerializer<?> contextual = serializerFromAnnotation;
        if (serializerFromAnnotation instanceof ContextualSerializer) {
            contextual = ((ContextualSerializer)serializerFromAnnotation).createContextual(serializerProvider, std);
        }
        TypeSerializer propertyContentTypeSerializer = null;
        if (ClassUtil.isCollectionMapOrArray(type.getRawClass())) {
            propertyContentTypeSerializer = this.findPropertyContentTypeSerializer(type, serializerProvider.getConfig(), annotatedMember);
        }
        return propertyBuilder.buildWriter(beanPropertyDefinition, type, contextual, this.findPropertyTypeSerializer(type, serializerProvider.getConfig(), annotatedMember), propertyContentTypeSerializer, annotatedMember, b);
    }
    
    protected JsonSerializer<?> _createSerializer2(final SerializerProvider serializerProvider, final JavaType javaType, final BeanDescription beanDescription, final boolean b) throws JsonMappingException {
        JsonSerializer<?> serializerByAnnotations = this.findSerializerByAnnotations(serializerProvider, javaType, beanDescription);
        if (serializerByAnnotations == null) {
            final SerializationConfig config = serializerProvider.getConfig();
            boolean usesStaticTyping = false;
            Label_0073: {
                if (javaType.isContainerType()) {
                    usesStaticTyping = b;
                    if (!b) {
                        usesStaticTyping = this.usesStaticTyping(config, beanDescription, null);
                    }
                    final JsonSerializer<?> buildContainerSerializer = this.buildContainerSerializer(serializerProvider, javaType, beanDescription, usesStaticTyping);
                    if ((serializerByAnnotations = buildContainerSerializer) != null) {
                        return serializerByAnnotations;
                    }
                    serializerByAnnotations = buildContainerSerializer;
                }
                else {
                    final Iterator<Serializers> iterator = this.customSerializers().iterator();
                    JsonSerializer<?> serializer;
                    do {
                        usesStaticTyping = b;
                        if (!iterator.hasNext()) {
                            break Label_0073;
                        }
                        serializer = iterator.next().findSerializer(config, javaType, beanDescription);
                    } while ((serializerByAnnotations = serializer) == null);
                    serializerByAnnotations = serializer;
                    usesStaticTyping = b;
                }
            }
            JsonSerializer<?> jsonSerializer;
            if ((jsonSerializer = serializerByAnnotations) == null && (jsonSerializer = this.findSerializerByLookup(javaType, config, beanDescription, usesStaticTyping)) == null && (jsonSerializer = this.findSerializerByPrimaryType(serializerProvider, javaType, beanDescription, usesStaticTyping)) == null && (jsonSerializer = this.findBeanSerializer(serializerProvider, javaType, beanDescription)) == null) {
                jsonSerializer = this.findSerializerByAddonType(config, javaType, beanDescription, usesStaticTyping);
            }
            if ((serializerByAnnotations = jsonSerializer) != null) {
                serializerByAnnotations = jsonSerializer;
                if (this._factoryConfig.hasSerializerModifiers()) {
                    final Iterator<BeanSerializerModifier> iterator2 = this._factoryConfig.serializerModifiers().iterator();
                    while (iterator2.hasNext()) {
                        jsonSerializer = iterator2.next().modifySerializer(config, beanDescription, jsonSerializer);
                    }
                    return jsonSerializer;
                }
            }
        }
        return serializerByAnnotations;
    }
    
    protected JsonSerializer<Object> constructBeanSerializer(final SerializerProvider serializerProvider, final BeanDescription beanDescription) throws JsonMappingException {
        JsonSerializer<Object> unknownTypeSerializer;
        if (beanDescription.getBeanClass() == Object.class) {
            unknownTypeSerializer = serializerProvider.getUnknownTypeSerializer(Object.class);
        }
        else {
            final SerializationConfig config = serializerProvider.getConfig();
            final BeanSerializerBuilder constructBeanSerializerBuilder = this.constructBeanSerializerBuilder(beanDescription);
            constructBeanSerializerBuilder.setConfig(config);
            List<BeanPropertyWriter> list;
            if ((list = this.findBeanProperties(serializerProvider, beanDescription, constructBeanSerializerBuilder)) == null) {
                list = new ArrayList<BeanPropertyWriter>();
            }
            List<BeanPropertyWriter> list2;
            if (this._factoryConfig.hasSerializerModifiers()) {
                final Iterator<BeanSerializerModifier> iterator = this._factoryConfig.serializerModifiers().iterator();
                while (true) {
                    list2 = list;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    list = iterator.next().changeProperties(config, beanDescription, list);
                }
            }
            else {
                list2 = list;
            }
            List<BeanPropertyWriter> list3 = this.filterBeanProperties(config, beanDescription, list2);
            List<BeanPropertyWriter> properties;
            if (this._factoryConfig.hasSerializerModifiers()) {
                final Iterator<BeanSerializerModifier> iterator2 = this._factoryConfig.serializerModifiers().iterator();
                while (true) {
                    properties = list3;
                    if (!iterator2.hasNext()) {
                        break;
                    }
                    list3 = iterator2.next().orderProperties(config, beanDescription, list3);
                }
            }
            else {
                properties = list3;
            }
            constructBeanSerializerBuilder.setObjectIdWriter(this.constructObjectIdHandler(serializerProvider, beanDescription, properties));
            constructBeanSerializerBuilder.setProperties(properties);
            constructBeanSerializerBuilder.setFilterId(this.findFilterId(config, beanDescription));
            final AnnotatedMember anyGetter = beanDescription.findAnyGetter();
            if (anyGetter != null) {
                if (config.canOverrideAccessModifiers()) {
                    anyGetter.fixAccess();
                }
                final JavaType type = anyGetter.getType(beanDescription.bindingsForBeanType());
                final boolean enabled = config.isEnabled(MapperFeature.USE_STATIC_TYPING);
                final JavaType contentType = type.getContentType();
                constructBeanSerializerBuilder.setAnyGetter(new AnyGetterWriter(new BeanProperty.Std(anyGetter.getName(), contentType, null, beanDescription.getClassAnnotations(), anyGetter, false), anyGetter, MapSerializer.construct(null, type, enabled, this.createTypeSerializer(config, contentType), null, null)));
            }
            this.processViews(config, constructBeanSerializerBuilder);
            BeanSerializerBuilder beanSerializerBuilder;
            if (this._factoryConfig.hasSerializerModifiers()) {
                final Iterator<BeanSerializerModifier> iterator3 = this._factoryConfig.serializerModifiers().iterator();
                BeanSerializerBuilder updateBuilder = constructBeanSerializerBuilder;
                while (true) {
                    beanSerializerBuilder = updateBuilder;
                    if (!iterator3.hasNext()) {
                        break;
                    }
                    updateBuilder = iterator3.next().updateBuilder(config, beanDescription, updateBuilder);
                }
            }
            else {
                beanSerializerBuilder = constructBeanSerializerBuilder;
            }
            final JsonSerializer<?> build = beanSerializerBuilder.build();
            if ((unknownTypeSerializer = (JsonSerializer<Object>)build) == null) {
                unknownTypeSerializer = (JsonSerializer<Object>)build;
                if (beanDescription.hasKnownClassAnnotations()) {
                    return beanSerializerBuilder.createDummy();
                }
            }
        }
        return unknownTypeSerializer;
    }
    
    protected BeanSerializerBuilder constructBeanSerializerBuilder(final BeanDescription beanDescription) {
        return new BeanSerializerBuilder(beanDescription);
    }
    
    protected BeanPropertyWriter constructFilteredBeanWriter(final BeanPropertyWriter beanPropertyWriter, final Class<?>[] array) {
        return FilteredBeanPropertyWriter.constructViewBased(beanPropertyWriter, array);
    }
    
    protected ObjectIdWriter constructObjectIdHandler(final SerializerProvider serializerProvider, final BeanDescription beanDescription, final List<BeanPropertyWriter> list) throws JsonMappingException {
        final ObjectIdInfo objectIdInfo = beanDescription.getObjectIdInfo();
        if (objectIdInfo == null) {
            return null;
        }
        final Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
        if (generatorType == ObjectIdGenerators.PropertyGenerator.class) {
            final String propertyName = objectIdInfo.getPropertyName();
            for (int size = list.size(), i = 0; i != size; ++i) {
                final BeanPropertyWriter beanPropertyWriter = list.get(i);
                if (propertyName.equals(beanPropertyWriter.getName())) {
                    if (i > 0) {
                        list.remove(i);
                        list.add(0, beanPropertyWriter);
                    }
                    return ObjectIdWriter.construct(beanPropertyWriter.getType(), null, new PropertyBasedObjectIdGenerator(objectIdInfo, beanPropertyWriter), objectIdInfo.getAlwaysAsId());
                }
            }
            throw new IllegalArgumentException("Invalid Object Id definition for " + beanDescription.getBeanClass().getName() + ": can not find property with name '" + propertyName + "'");
        }
        return ObjectIdWriter.construct(serializerProvider.getTypeFactory().findTypeParameters(serializerProvider.constructType(generatorType), ObjectIdGenerator.class)[0], objectIdInfo.getPropertyName(), serializerProvider.objectIdGeneratorInstance(beanDescription.getClassInfo(), objectIdInfo), objectIdInfo.getAlwaysAsId());
    }
    
    protected PropertyBuilder constructPropertyBuilder(final SerializationConfig serializationConfig, final BeanDescription beanDescription) {
        return new PropertyBuilder(serializationConfig, beanDescription);
    }
    
    @Override
    public JsonSerializer<Object> createSerializer(final SerializerProvider serializerProvider, final JavaType javaType) throws JsonMappingException {
        final SerializationConfig config = serializerProvider.getConfig();
        final BeanDescription introspect = config.introspect(javaType);
        final JsonSerializer<Object> serializerFromAnnotation = this.findSerializerFromAnnotation(serializerProvider, introspect.getClassInfo());
        if (serializerFromAnnotation != null) {
            return serializerFromAnnotation;
        }
        final JavaType modifyTypeByAnnotation = this.modifyTypeByAnnotation(config, introspect.getClassInfo(), javaType);
        boolean b;
        BeanDescription beanDescription;
        if (modifyTypeByAnnotation == javaType) {
            b = false;
            beanDescription = introspect;
        }
        else if (!modifyTypeByAnnotation.hasRawClass(javaType.getRawClass())) {
            beanDescription = config.introspect(modifyTypeByAnnotation);
            b = true;
        }
        else {
            b = true;
            beanDescription = introspect;
        }
        final Converter<Object, Object> serializationConverter = beanDescription.findSerializationConverter();
        if (serializationConverter == null) {
            return (JsonSerializer<Object>)this._createSerializer2(serializerProvider, modifyTypeByAnnotation, beanDescription, b);
        }
        final JavaType outputType = serializationConverter.getOutputType(serializerProvider.getTypeFactory());
        if (!outputType.hasRawClass(modifyTypeByAnnotation.getRawClass())) {
            beanDescription = config.introspect(outputType);
        }
        return new StdDelegatingSerializer(serializationConverter, outputType, this._createSerializer2(serializerProvider, outputType, beanDescription, true));
    }
    
    @Override
    protected Iterable<Serializers> customSerializers() {
        return this._factoryConfig.serializers();
    }
    
    protected List<BeanPropertyWriter> filterBeanProperties(final SerializationConfig serializationConfig, final BeanDescription beanDescription, final List<BeanPropertyWriter> list) {
        final String[] propertiesToIgnore = serializationConfig.getAnnotationIntrospector().findPropertiesToIgnore(beanDescription.getClassInfo());
        if (propertiesToIgnore != null && propertiesToIgnore.length > 0) {
            final HashSet<String> arrayToSet = ArrayBuilders.arrayToSet(propertiesToIgnore);
            final Iterator<BeanPropertyWriter> iterator = list.iterator();
            while (iterator.hasNext()) {
                if (arrayToSet.contains(iterator.next().getName())) {
                    iterator.remove();
                }
            }
        }
        return list;
    }
    
    protected List<BeanPropertyWriter> findBeanProperties(final SerializerProvider serializerProvider, final BeanDescription beanDescription, final BeanSerializerBuilder beanSerializerBuilder) throws JsonMappingException {
        final List<BeanPropertyDefinition> properties = beanDescription.findProperties();
        final SerializationConfig config = serializerProvider.getConfig();
        this.removeIgnorableTypes(config, beanDescription, properties);
        if (config.isEnabled(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS)) {
            this.removeSetterlessGetters(config, beanDescription, properties);
        }
        if (properties.isEmpty()) {
            return null;
        }
        final boolean usesStaticTyping = this.usesStaticTyping(config, beanDescription, null);
        final PropertyBuilder constructPropertyBuilder = this.constructPropertyBuilder(config, beanDescription);
        final ArrayList list = new ArrayList<BeanPropertyWriter>(properties.size());
        final TypeBindings bindingsForBeanType = beanDescription.bindingsForBeanType();
        for (final BeanPropertyDefinition beanPropertyDefinition : properties) {
            final AnnotatedMember accessor = beanPropertyDefinition.getAccessor();
            if (beanPropertyDefinition.isTypeId()) {
                if (accessor == null) {
                    continue;
                }
                if (config.canOverrideAccessModifiers()) {
                    accessor.fixAccess();
                }
                beanSerializerBuilder.setTypeId(accessor);
            }
            else {
                final AnnotationIntrospector.ReferenceProperty referenceType = beanPropertyDefinition.findReferenceType();
                if (referenceType != null && referenceType.isBackReference()) {
                    continue;
                }
                if (accessor instanceof AnnotatedMethod) {
                    list.add(this._constructWriter(serializerProvider, beanPropertyDefinition, bindingsForBeanType, constructPropertyBuilder, usesStaticTyping, accessor));
                }
                else {
                    list.add(this._constructWriter(serializerProvider, beanPropertyDefinition, bindingsForBeanType, constructPropertyBuilder, usesStaticTyping, accessor));
                }
            }
        }
        return (List<BeanPropertyWriter>)list;
    }
    
    public JsonSerializer<Object> findBeanSerializer(final SerializerProvider serializerProvider, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        if (!this.isPotentialBeanType(javaType.getRawClass()) && !javaType.isEnumType()) {
            return null;
        }
        return this.constructBeanSerializer(serializerProvider, beanDescription);
    }
    
    protected Object findFilterId(final SerializationConfig serializationConfig, final BeanDescription beanDescription) {
        return serializationConfig.getAnnotationIntrospector().findFilterId(beanDescription.getClassInfo());
    }
    
    public TypeSerializer findPropertyContentTypeSerializer(final JavaType javaType, final SerializationConfig serializationConfig, final AnnotatedMember annotatedMember) throws JsonMappingException {
        final JavaType contentType = javaType.getContentType();
        final AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        final TypeResolverBuilder<?> propertyContentTypeResolver = annotationIntrospector.findPropertyContentTypeResolver(serializationConfig, annotatedMember, javaType);
        if (propertyContentTypeResolver == null) {
            return this.createTypeSerializer(serializationConfig, contentType);
        }
        return propertyContentTypeResolver.buildTypeSerializer(serializationConfig, contentType, serializationConfig.getSubtypeResolver().collectAndResolveSubtypes(annotatedMember, serializationConfig, annotationIntrospector, contentType));
    }
    
    public TypeSerializer findPropertyTypeSerializer(final JavaType javaType, final SerializationConfig serializationConfig, final AnnotatedMember annotatedMember) throws JsonMappingException {
        final AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        final TypeResolverBuilder<?> propertyTypeResolver = annotationIntrospector.findPropertyTypeResolver(serializationConfig, annotatedMember, javaType);
        if (propertyTypeResolver == null) {
            return this.createTypeSerializer(serializationConfig, javaType);
        }
        return propertyTypeResolver.buildTypeSerializer(serializationConfig, javaType, serializationConfig.getSubtypeResolver().collectAndResolveSubtypes(annotatedMember, serializationConfig, annotationIntrospector, javaType));
    }
    
    protected boolean isPotentialBeanType(final Class<?> clazz) {
        return ClassUtil.canBeABeanType(clazz) == null && !ClassUtil.isProxyType(clazz);
    }
    
    protected void processViews(final SerializationConfig serializationConfig, final BeanSerializerBuilder beanSerializerBuilder) {
        final List<BeanPropertyWriter> properties = beanSerializerBuilder.getProperties();
        final boolean enabled = serializationConfig.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
        final int size = properties.size();
        final BeanPropertyWriter[] filteredProperties = new BeanPropertyWriter[size];
        int i = 0;
        int n = 0;
    Label_0082_Outer:
        while (i < size) {
            final BeanPropertyWriter beanPropertyWriter = properties.get(i);
            final Class<?>[] views = beanPropertyWriter.getViews();
            while (true) {
                int n2 = 0;
                Label_0108: {
                    if (views != null) {
                        n2 = n + 1;
                        filteredProperties[i] = this.constructFilteredBeanWriter(beanPropertyWriter, views);
                        break Label_0108;
                    }
                    n2 = n;
                    if (!enabled) {
                        break Label_0108;
                    }
                    filteredProperties[i] = beanPropertyWriter;
                    ++i;
                    continue Label_0082_Outer;
                }
                n = n2;
                continue;
            }
        }
        if (enabled && n == 0) {
            return;
        }
        beanSerializerBuilder.setFilteredProperties(filteredProperties);
    }
    
    protected void removeIgnorableTypes(final SerializationConfig serializationConfig, final BeanDescription beanDescription, final List<BeanPropertyDefinition> list) {
        final AnnotationIntrospector annotationIntrospector = serializationConfig.getAnnotationIntrospector();
        final HashMap<Object, Boolean> hashMap = (HashMap<Object, Boolean>)new HashMap<Class<?>, Boolean>();
        final Iterator<BeanPropertyDefinition> iterator = list.iterator();
        while (iterator.hasNext()) {
            final AnnotatedMember accessor = iterator.next().getAccessor();
            if (accessor == null) {
                iterator.remove();
            }
            else {
                final Class<?> rawType = accessor.getRawType();
                Boolean b;
                if ((b = hashMap.get(rawType)) == null) {
                    if ((b = annotationIntrospector.isIgnorableType(serializationConfig.introspectClassAnnotations(rawType).getClassInfo())) == null) {
                        b = Boolean.FALSE;
                    }
                    hashMap.put(rawType, b);
                }
                if (!b) {
                    continue;
                }
                iterator.remove();
            }
        }
    }
    
    protected void removeSetterlessGetters(final SerializationConfig serializationConfig, final BeanDescription beanDescription, final List<BeanPropertyDefinition> list) {
        final Iterator<BeanPropertyDefinition> iterator = list.iterator();
        while (iterator.hasNext()) {
            final BeanPropertyDefinition beanPropertyDefinition = iterator.next();
            if (!beanPropertyDefinition.couldDeserialize() && !beanPropertyDefinition.isExplicitlyIncluded()) {
                iterator.remove();
            }
        }
    }
}
