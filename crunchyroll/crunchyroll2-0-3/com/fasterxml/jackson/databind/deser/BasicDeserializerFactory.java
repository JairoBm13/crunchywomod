// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.deser.std.JdkDeserializers;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.deser.std.UntypedObjectDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import com.fasterxml.jackson.databind.deser.std.EnumMapDeserializer;
import java.util.EnumMap;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringCollectionDeserializer;
import com.fasterxml.jackson.databind.deser.std.ArrayBlockingQueueDeserializer;
import java.util.concurrent.ArrayBlockingQueue;
import com.fasterxml.jackson.databind.deser.std.EnumSetDeserializer;
import java.util.EnumSet;
import com.fasterxml.jackson.databind.deser.std.ObjectArrayDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.deser.std.PrimitiveArrayDeserializers;
import java.lang.reflect.Method;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
import com.fasterxml.jackson.databind.deser.impl.CreatorCollector;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.AbstractTypeResolver;
import com.fasterxml.jackson.databind.deser.std.JacksonDeserializers;
import com.fasterxml.jackson.databind.JsonMappingException;
import java.util.Iterator;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import java.lang.reflect.Member;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializers;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.DeserializationContext;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.LinkedHashMap;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import java.util.Map;
import java.util.Collection;
import java.util.HashMap;
import java.io.Serializable;

public abstract class BasicDeserializerFactory extends DeserializerFactory implements Serializable
{
    private static final Class<?> CLASS_CHAR_BUFFER;
    private static final Class<?> CLASS_ITERABLE;
    private static final Class<?> CLASS_OBJECT;
    private static final Class<?> CLASS_STRING;
    static final HashMap<String, Class<? extends Collection>> _collectionFallbacks;
    static final HashMap<String, Class<? extends Map>> _mapFallbacks;
    protected final DeserializerFactoryConfig _factoryConfig;
    
    static {
        CLASS_OBJECT = Object.class;
        CLASS_STRING = String.class;
        CLASS_CHAR_BUFFER = CharSequence.class;
        CLASS_ITERABLE = Iterable.class;
        (_mapFallbacks = new HashMap<String, Class<? extends Map>>()).put(Map.class.getName(), LinkedHashMap.class);
        BasicDeserializerFactory._mapFallbacks.put(ConcurrentMap.class.getName(), ConcurrentHashMap.class);
        BasicDeserializerFactory._mapFallbacks.put(SortedMap.class.getName(), TreeMap.class);
        BasicDeserializerFactory._mapFallbacks.put("java.util.NavigableMap", TreeMap.class);
        while (true) {
            try {
                BasicDeserializerFactory._mapFallbacks.put(ConcurrentNavigableMap.class.getName(), ConcurrentSkipListMap.class);
                (_collectionFallbacks = new HashMap<String, Class<? extends Collection>>()).put(Collection.class.getName(), ArrayList.class);
                BasicDeserializerFactory._collectionFallbacks.put(List.class.getName(), ArrayList.class);
                BasicDeserializerFactory._collectionFallbacks.put(Set.class.getName(), HashSet.class);
                BasicDeserializerFactory._collectionFallbacks.put(SortedSet.class.getName(), TreeSet.class);
                BasicDeserializerFactory._collectionFallbacks.put(Queue.class.getName(), LinkedList.class);
                BasicDeserializerFactory._collectionFallbacks.put("java.util.Deque", LinkedList.class);
                BasicDeserializerFactory._collectionFallbacks.put("java.util.NavigableSet", TreeSet.class);
            }
            catch (Throwable t) {
                System.err.println("Problems with (optional) types: " + t);
                continue;
            }
            break;
        }
    }
    
    protected BasicDeserializerFactory(final DeserializerFactoryConfig factoryConfig) {
        this._factoryConfig = factoryConfig;
    }
    
    private KeyDeserializer _createEnumKeyDeserializer(final DeserializationContext deserializationContext, final JavaType javaType) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        final BeanDescription introspect = config.introspect(javaType);
        final JsonDeserializer<Object> deserializerFromAnnotation = this.findDeserializerFromAnnotation(deserializationContext, introspect.getClassInfo());
        if (deserializerFromAnnotation != null) {
            return StdKeyDeserializers.constructDelegatingKeyDeserializer(config, javaType, deserializerFromAnnotation);
        }
        final Class<?> rawClass = javaType.getRawClass();
        if (this._findCustomEnumDeserializer(rawClass, config, introspect) != null) {
            return StdKeyDeserializers.constructDelegatingKeyDeserializer(config, javaType, deserializerFromAnnotation);
        }
        final EnumResolver<?> constructEnumResolver = this.constructEnumResolver(rawClass, config, introspect.findJsonValueMethod());
        for (final AnnotatedMethod annotatedMethod : introspect.getFactoryMethods()) {
            if (config.getAnnotationIntrospector().hasCreatorAnnotation(annotatedMethod)) {
                if (annotatedMethod.getParameterCount() != 1 || !annotatedMethod.getRawReturnType().isAssignableFrom(rawClass)) {
                    throw new IllegalArgumentException("Unsuitable method (" + annotatedMethod + ") decorated with @JsonCreator (for Enum type " + rawClass.getName() + ")");
                }
                if (annotatedMethod.getGenericParameterType(0) != String.class) {
                    throw new IllegalArgumentException("Parameter #0 type for factory method (" + annotatedMethod + ") not suitable, must be java.lang.String");
                }
                if (config.canOverrideAccessModifiers()) {
                    ClassUtil.checkAndFixAccess(annotatedMethod.getMember());
                }
                return StdKeyDeserializers.constructEnumKeyDeserializer(constructEnumResolver, annotatedMethod);
            }
        }
        return StdKeyDeserializers.constructEnumKeyDeserializer(constructEnumResolver);
    }
    
    private ValueInstantiator _findStdValueInstantiator(final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
        return JacksonDeserializers.findValueInstantiator(deserializationConfig, beanDescription);
    }
    
    private JavaType _mapAbstractType2(final DeserializationConfig deserializationConfig, final JavaType javaType) throws JsonMappingException {
        final Class<?> rawClass = javaType.getRawClass();
        if (this._factoryConfig.hasAbstractTypeResolvers()) {
            final Iterator<AbstractTypeResolver> iterator = this._factoryConfig.abstractTypeResolvers().iterator();
            while (iterator.hasNext()) {
                final JavaType typeMapping = iterator.next().findTypeMapping(deserializationConfig, javaType);
                if (typeMapping != null && typeMapping.getRawClass() != rawClass) {
                    return typeMapping;
                }
            }
        }
        return null;
    }
    
    protected void _addDeserializerConstructors(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final VisibilityChecker<?> visibilityChecker, final AnnotationIntrospector annotationIntrospector, final CreatorCollector creatorCollector) throws JsonMappingException {
        final AnnotatedConstructor defaultConstructor = beanDescription.findDefaultConstructor();
        if (defaultConstructor != null && (!creatorCollector.hasDefaultCreator() || annotationIntrospector.hasCreatorAnnotation(defaultConstructor))) {
            creatorCollector.setDefaultCreator(defaultConstructor);
        }
        String[] array = null;
        AnnotatedConstructor annotatedConstructor = null;
        for (final BeanPropertyDefinition beanPropertyDefinition : beanDescription.findProperties()) {
            if (beanPropertyDefinition.getConstructorParameter() != null) {
                final AnnotatedParameter constructorParameter = beanPropertyDefinition.getConstructorParameter();
                final AnnotatedWithParams owner = constructorParameter.getOwner();
                if (!(owner instanceof AnnotatedConstructor)) {
                    continue;
                }
                AnnotatedConstructor annotatedConstructor2;
                String[] array2;
                if (annotatedConstructor == null) {
                    annotatedConstructor2 = (AnnotatedConstructor)owner;
                    array2 = new String[annotatedConstructor2.getParameterCount()];
                }
                else {
                    annotatedConstructor2 = annotatedConstructor;
                    array2 = array;
                }
                array2[constructorParameter.getIndex()] = beanPropertyDefinition.getName();
                annotatedConstructor = annotatedConstructor2;
                array = array2;
            }
        }
        for (final AnnotatedConstructor annotatedConstructor3 : beanDescription.getConstructors()) {
            final int parameterCount = annotatedConstructor3.getParameterCount();
            final boolean b = annotationIntrospector.hasCreatorAnnotation(annotatedConstructor3) || annotatedConstructor3 == annotatedConstructor;
            final boolean creatorVisible = visibilityChecker.isCreatorVisible(annotatedConstructor3);
            if (parameterCount == 1) {
                String s;
                if (annotatedConstructor3 == annotatedConstructor) {
                    s = array[0];
                }
                else {
                    s = null;
                }
                this._handleSingleArgumentConstructor(deserializationContext, beanDescription, visibilityChecker, annotationIntrospector, creatorCollector, annotatedConstructor3, b, creatorVisible, s);
            }
            else {
                if (!b && !creatorVisible) {
                    continue;
                }
                AnnotatedParameter annotatedParameter = null;
                int n = 0;
                int n2 = 0;
                final CreatorProperty[] array3 = new CreatorProperty[parameterCount];
                int n3;
                AnnotatedParameter annotatedParameter2;
                int n4;
                for (int i = 0; i < parameterCount; ++i, annotatedParameter = annotatedParameter2, n2 = n4, n = n3) {
                    final AnnotatedParameter parameter = annotatedConstructor3.getParameter(i);
                    String s2 = null;
                    if (annotatedConstructor3 == annotatedConstructor) {
                        s2 = array[i];
                    }
                    String s3;
                    if ((s3 = s2) == null) {
                        PropertyName nameForDeserialization;
                        if (parameter == null) {
                            nameForDeserialization = null;
                        }
                        else {
                            nameForDeserialization = annotationIntrospector.findNameForDeserialization(parameter);
                        }
                        String simpleName;
                        if (nameForDeserialization == null) {
                            simpleName = null;
                        }
                        else {
                            simpleName = nameForDeserialization.getSimpleName();
                        }
                        s3 = simpleName;
                    }
                    final Object injectableValueId = annotationIntrospector.findInjectableValueId(parameter);
                    if (s3 != null && s3.length() > 0) {
                        n3 = n + 1;
                        array3[i] = this.constructCreatorProperty(deserializationContext, beanDescription, s3, i, parameter, injectableValueId);
                        annotatedParameter2 = annotatedParameter;
                        n4 = n2;
                    }
                    else if (injectableValueId != null) {
                        n4 = n2 + 1;
                        array3[i] = this.constructCreatorProperty(deserializationContext, beanDescription, s3, i, parameter, injectableValueId);
                        n3 = n;
                        annotatedParameter2 = annotatedParameter;
                    }
                    else {
                        n4 = n2;
                        n3 = n;
                        annotatedParameter2 = parameter;
                        if (annotatedParameter != null) {
                            n4 = n2;
                            n3 = n;
                            annotatedParameter2 = annotatedParameter;
                        }
                    }
                }
                if (!b && n <= 0 && n2 <= 0) {
                    continue;
                }
                if (n + n2 == parameterCount) {
                    creatorCollector.addPropertyCreator(annotatedConstructor3, array3);
                }
                else if (n == 0 && n2 + 1 == parameterCount) {
                    creatorCollector.addDelegatingCreator(annotatedConstructor3, array3);
                }
                else {
                    creatorCollector.addIncompeteParameter(annotatedParameter);
                }
            }
        }
    }
    
    protected void _addDeserializerFactoryMethods(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final VisibilityChecker<?> visibilityChecker, final AnnotationIntrospector annotationIntrospector, final CreatorCollector creatorCollector) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        for (final AnnotatedMethod defaultCreator : beanDescription.getFactoryMethods()) {
            final boolean hasCreatorAnnotation = annotationIntrospector.hasCreatorAnnotation(defaultCreator);
            final int parameterCount = defaultCreator.getParameterCount();
            if (parameterCount == 0) {
                if (!hasCreatorAnnotation) {
                    continue;
                }
                creatorCollector.setDefaultCreator(defaultCreator);
            }
            else {
                if (parameterCount == 1) {
                    final AnnotatedParameter parameter = defaultCreator.getParameter(0);
                    PropertyName nameForDeserialization;
                    if (parameter == null) {
                        nameForDeserialization = null;
                    }
                    else {
                        nameForDeserialization = annotationIntrospector.findNameForDeserialization(parameter);
                    }
                    String simpleName;
                    if (nameForDeserialization == null) {
                        simpleName = null;
                    }
                    else {
                        simpleName = nameForDeserialization.getSimpleName();
                    }
                    if (annotationIntrospector.findInjectableValueId(parameter) == null && (simpleName == null || simpleName.length() == 0)) {
                        this._handleSingleArgumentFactory(config, beanDescription, visibilityChecker, annotationIntrospector, creatorCollector, defaultCreator, hasCreatorAnnotation);
                        continue;
                    }
                }
                else if (!annotationIntrospector.hasCreatorAnnotation(defaultCreator)) {
                    continue;
                }
                AnnotatedParameter annotatedParameter = null;
                final CreatorProperty[] array = new CreatorProperty[parameterCount];
                int n = 0;
                int n2 = 0;
                int n3;
                AnnotatedParameter annotatedParameter2;
                int n4;
                for (int i = 0; i < parameterCount; ++i, annotatedParameter = annotatedParameter2, n2 = n4, n = n3) {
                    final AnnotatedParameter parameter2 = defaultCreator.getParameter(i);
                    PropertyName nameForDeserialization2;
                    if (parameter2 == null) {
                        nameForDeserialization2 = null;
                    }
                    else {
                        nameForDeserialization2 = annotationIntrospector.findNameForDeserialization(parameter2);
                    }
                    String simpleName2;
                    if (nameForDeserialization2 == null) {
                        simpleName2 = null;
                    }
                    else {
                        simpleName2 = nameForDeserialization2.getSimpleName();
                    }
                    final Object injectableValueId = annotationIntrospector.findInjectableValueId(parameter2);
                    if (simpleName2 != null && simpleName2.length() > 0) {
                        n3 = n + 1;
                        array[i] = this.constructCreatorProperty(deserializationContext, beanDescription, simpleName2, i, parameter2, injectableValueId);
                        annotatedParameter2 = annotatedParameter;
                        n4 = n2;
                    }
                    else if (injectableValueId != null) {
                        n4 = n2 + 1;
                        array[i] = this.constructCreatorProperty(deserializationContext, beanDescription, simpleName2, i, parameter2, injectableValueId);
                        n3 = n;
                        annotatedParameter2 = annotatedParameter;
                    }
                    else {
                        n4 = n2;
                        n3 = n;
                        annotatedParameter2 = parameter2;
                        if (annotatedParameter != null) {
                            n4 = n2;
                            n3 = n;
                            annotatedParameter2 = annotatedParameter;
                        }
                    }
                }
                if (!hasCreatorAnnotation && n <= 0 && n2 <= 0) {
                    continue;
                }
                if (n + n2 == parameterCount) {
                    creatorCollector.addPropertyCreator(defaultCreator, array);
                }
                else {
                    if (n != 0 || n2 + 1 != parameterCount) {
                        throw new IllegalArgumentException("Argument #" + annotatedParameter.getIndex() + " of factory method " + defaultCreator + " has no property name annotation; must have name when multiple-paramater constructor annotated as Creator");
                    }
                    creatorCollector.addDelegatingCreator(defaultCreator, array);
                }
            }
        }
    }
    
    protected ValueInstantiator _constructDefaultValueInstantiator(final DeserializationContext deserializationContext, final BeanDescription beanDescription) throws JsonMappingException {
        final CreatorCollector creatorCollector = new CreatorCollector(beanDescription, deserializationContext.canOverrideAccessModifiers());
        final AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        final DeserializationConfig config = deserializationContext.getConfig();
        final VisibilityChecker<?> autoDetectVisibility = annotationIntrospector.findAutoDetectVisibility(beanDescription.getClassInfo(), config.getDefaultVisibilityChecker());
        this._addDeserializerFactoryMethods(deserializationContext, beanDescription, autoDetectVisibility, annotationIntrospector, creatorCollector);
        if (beanDescription.getType().isConcrete()) {
            this._addDeserializerConstructors(deserializationContext, beanDescription, autoDetectVisibility, annotationIntrospector, creatorCollector);
        }
        return creatorCollector.constructValueInstantiator(config);
    }
    
    protected JsonDeserializer<?> _findCustomArrayDeserializer(final ArrayType arrayType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        final Iterator<Deserializers> iterator = this._factoryConfig.deserializers().iterator();
        while (iterator.hasNext()) {
            final JsonDeserializer<?> arrayDeserializer = iterator.next().findArrayDeserializer(arrayType, deserializationConfig, beanDescription, typeDeserializer, jsonDeserializer);
            if (arrayDeserializer != null) {
                return arrayDeserializer;
            }
        }
        return null;
    }
    
    protected JsonDeserializer<?> _findCustomCollectionDeserializer(final CollectionType collectionType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        final Iterator<Deserializers> iterator = this._factoryConfig.deserializers().iterator();
        while (iterator.hasNext()) {
            final JsonDeserializer<?> collectionDeserializer = iterator.next().findCollectionDeserializer(collectionType, deserializationConfig, beanDescription, typeDeserializer, jsonDeserializer);
            if (collectionDeserializer != null) {
                return collectionDeserializer;
            }
        }
        return null;
    }
    
    protected JsonDeserializer<?> _findCustomCollectionLikeDeserializer(final CollectionLikeType collectionLikeType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        final Iterator<Deserializers> iterator = this._factoryConfig.deserializers().iterator();
        while (iterator.hasNext()) {
            final JsonDeserializer<?> collectionLikeDeserializer = iterator.next().findCollectionLikeDeserializer(collectionLikeType, deserializationConfig, beanDescription, typeDeserializer, jsonDeserializer);
            if (collectionLikeDeserializer != null) {
                return collectionLikeDeserializer;
            }
        }
        return null;
    }
    
    protected JsonDeserializer<?> _findCustomEnumDeserializer(final Class<?> clazz, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
        final Iterator<Deserializers> iterator = this._factoryConfig.deserializers().iterator();
        while (iterator.hasNext()) {
            final JsonDeserializer<?> enumDeserializer = iterator.next().findEnumDeserializer(clazz, deserializationConfig, beanDescription);
            if (enumDeserializer != null) {
                return enumDeserializer;
            }
        }
        return null;
    }
    
    protected JsonDeserializer<?> _findCustomMapDeserializer(final MapType mapType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final KeyDeserializer keyDeserializer, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        final Iterator<Deserializers> iterator = this._factoryConfig.deserializers().iterator();
        while (iterator.hasNext()) {
            final JsonDeserializer<?> mapDeserializer = iterator.next().findMapDeserializer(mapType, deserializationConfig, beanDescription, keyDeserializer, typeDeserializer, jsonDeserializer);
            if (mapDeserializer != null) {
                return mapDeserializer;
            }
        }
        return null;
    }
    
    protected JsonDeserializer<?> _findCustomMapLikeDeserializer(final MapLikeType mapLikeType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final KeyDeserializer keyDeserializer, final TypeDeserializer typeDeserializer, final JsonDeserializer<?> jsonDeserializer) throws JsonMappingException {
        final Iterator<Deserializers> iterator = this._factoryConfig.deserializers().iterator();
        while (iterator.hasNext()) {
            final JsonDeserializer<?> mapLikeDeserializer = iterator.next().findMapLikeDeserializer(mapLikeType, deserializationConfig, beanDescription, keyDeserializer, typeDeserializer, jsonDeserializer);
            if (mapLikeDeserializer != null) {
                return mapLikeDeserializer;
            }
        }
        return null;
    }
    
    protected JsonDeserializer<?> _findCustomTreeNodeDeserializer(final Class<? extends JsonNode> clazz, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
        final Iterator<Deserializers> iterator = this._factoryConfig.deserializers().iterator();
        while (iterator.hasNext()) {
            final JsonDeserializer<?> treeNodeDeserializer = iterator.next().findTreeNodeDeserializer(clazz, deserializationConfig, beanDescription);
            if (treeNodeDeserializer != null) {
                return treeNodeDeserializer;
            }
        }
        return null;
    }
    
    protected boolean _handleSingleArgumentConstructor(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final VisibilityChecker<?> visibilityChecker, final AnnotationIntrospector annotationIntrospector, final CreatorCollector creatorCollector, final AnnotatedConstructor annotatedConstructor, final boolean b, final boolean b2, final String s) throws JsonMappingException {
        final AnnotatedParameter parameter = annotatedConstructor.getParameter(0);
        String simpleName;
        if (s == null) {
            PropertyName nameForDeserialization;
            if (parameter == null) {
                nameForDeserialization = null;
            }
            else {
                nameForDeserialization = annotationIntrospector.findNameForDeserialization(parameter);
            }
            if (nameForDeserialization == null) {
                simpleName = null;
            }
            else {
                simpleName = nameForDeserialization.getSimpleName();
            }
        }
        else {
            simpleName = s;
        }
        final Object injectableValueId = annotationIntrospector.findInjectableValueId(parameter);
        if (injectableValueId != null || (simpleName != null && simpleName.length() > 0)) {
            creatorCollector.addPropertyCreator(annotatedConstructor, new CreatorProperty[] { this.constructCreatorProperty(deserializationContext, beanDescription, simpleName, 0, parameter, injectableValueId) });
            return true;
        }
        final Class<?> rawParameterType = annotatedConstructor.getRawParameterType(0);
        if (rawParameterType == String.class) {
            if (b || b2) {
                creatorCollector.addStringCreator(annotatedConstructor);
            }
            return true;
        }
        if (rawParameterType == Integer.TYPE || rawParameterType == Integer.class) {
            if (b || b2) {
                creatorCollector.addIntCreator(annotatedConstructor);
            }
            return true;
        }
        if (rawParameterType == Long.TYPE || rawParameterType == Long.class) {
            if (b || b2) {
                creatorCollector.addLongCreator(annotatedConstructor);
            }
            return true;
        }
        if (rawParameterType == Double.TYPE || rawParameterType == Double.class) {
            if (b || b2) {
                creatorCollector.addDoubleCreator(annotatedConstructor);
            }
            return true;
        }
        if (b) {
            creatorCollector.addDelegatingCreator(annotatedConstructor, null);
            return true;
        }
        return false;
    }
    
    protected boolean _handleSingleArgumentFactory(final DeserializationConfig deserializationConfig, final BeanDescription beanDescription, final VisibilityChecker<?> visibilityChecker, final AnnotationIntrospector annotationIntrospector, final CreatorCollector creatorCollector, final AnnotatedMethod annotatedMethod, final boolean b) throws JsonMappingException {
        final Class<?> rawParameterType = annotatedMethod.getRawParameterType(0);
        if (rawParameterType == String.class) {
            if (b || visibilityChecker.isCreatorVisible(annotatedMethod)) {
                creatorCollector.addStringCreator(annotatedMethod);
            }
        }
        else if (rawParameterType == Integer.TYPE || rawParameterType == Integer.class) {
            if (b || visibilityChecker.isCreatorVisible(annotatedMethod)) {
                creatorCollector.addIntCreator(annotatedMethod);
                return true;
            }
        }
        else if (rawParameterType == Long.TYPE || rawParameterType == Long.class) {
            if (b || visibilityChecker.isCreatorVisible(annotatedMethod)) {
                creatorCollector.addLongCreator(annotatedMethod);
                return true;
            }
        }
        else if (rawParameterType == Double.TYPE || rawParameterType == Double.class) {
            if (b || visibilityChecker.isCreatorVisible(annotatedMethod)) {
                creatorCollector.addDoubleCreator(annotatedMethod);
                return true;
            }
        }
        else if (rawParameterType == Boolean.TYPE || rawParameterType == Boolean.class) {
            if (b || visibilityChecker.isCreatorVisible(annotatedMethod)) {
                creatorCollector.addBooleanCreator(annotatedMethod);
                return true;
            }
        }
        else {
            if (annotationIntrospector.hasCreatorAnnotation(annotatedMethod)) {
                creatorCollector.addDelegatingCreator(annotatedMethod, null);
                return true;
            }
            return false;
        }
        return true;
    }
    
    protected CollectionType _mapAbstractCollectionType(final JavaType javaType, final DeserializationConfig deserializationConfig) {
        final Class<? extends Collection> clazz = BasicDeserializerFactory._collectionFallbacks.get(javaType.getRawClass().getName());
        if (clazz == null) {
            return null;
        }
        return (CollectionType)deserializationConfig.constructSpecializedType(javaType, clazz);
    }
    
    public ValueInstantiator _valueInstantiatorInstance(final DeserializationConfig deserializationConfig, final Annotated annotated, final Object o) throws JsonMappingException {
        if (o == null) {
            return null;
        }
        if (o instanceof ValueInstantiator) {
            return (ValueInstantiator)o;
        }
        if (!(o instanceof Class)) {
            throw new IllegalStateException("AnnotationIntrospector returned key deserializer definition of type " + o.getClass().getName() + "; expected type KeyDeserializer or Class<KeyDeserializer> instead");
        }
        final Class clazz = (Class)o;
        if (clazz == NoClass.class) {
            return null;
        }
        if (!ValueInstantiator.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException("AnnotationIntrospector returned Class " + clazz.getName() + "; expected Class<ValueInstantiator>");
        }
        final HandlerInstantiator handlerInstantiator = deserializationConfig.getHandlerInstantiator();
        if (handlerInstantiator != null) {
            final ValueInstantiator valueInstantiatorInstance = handlerInstantiator.valueInstantiatorInstance(deserializationConfig, annotated, clazz);
            if (valueInstantiatorInstance != null) {
                return valueInstantiatorInstance;
            }
        }
        return ClassUtil.createInstance((Class<ValueInstantiator>)clazz, deserializationConfig.canOverrideAccessModifiers());
    }
    
    protected CreatorProperty constructCreatorProperty(final DeserializationContext deserializationContext, final BeanDescription beanDescription, final String s, final int n, final AnnotatedParameter annotatedParameter, final Object o) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        final AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
        Boolean hasRequiredMarker;
        if (annotationIntrospector == null) {
            hasRequiredMarker = null;
        }
        else {
            hasRequiredMarker = annotationIntrospector.hasRequiredMarker(annotatedParameter);
        }
        final boolean b = hasRequiredMarker != null && hasRequiredMarker;
        final JavaType constructType = config.getTypeFactory().constructType(annotatedParameter.getParameterType(), beanDescription.bindingsForBeanType());
        BeanProperty.Std withType = new BeanProperty.Std(s, constructType, annotationIntrospector.findWrapperName(annotatedParameter), beanDescription.getClassAnnotations(), annotatedParameter, b);
        final JavaType resolveType = this.resolveType(deserializationContext, beanDescription, constructType, annotatedParameter);
        if (resolveType != constructType) {
            withType = withType.withType(resolveType);
        }
        final JsonDeserializer<Object> deserializerFromAnnotation = this.findDeserializerFromAnnotation(deserializationContext, annotatedParameter);
        final JavaType modifyTypeByAnnotation = this.modifyTypeByAnnotation(deserializationContext, annotatedParameter, resolveType);
        TypeDeserializer typeDeserializer = modifyTypeByAnnotation.getTypeHandler();
        if (typeDeserializer == null) {
            typeDeserializer = this.findTypeDeserializer(config, modifyTypeByAnnotation);
        }
        CreatorProperty withValueDeserializer = new CreatorProperty(s, modifyTypeByAnnotation, withType.getWrapperName(), typeDeserializer, beanDescription.getClassAnnotations(), annotatedParameter, n, o, withType.isRequired());
        if (deserializerFromAnnotation != null) {
            withValueDeserializer = withValueDeserializer.withValueDeserializer(deserializerFromAnnotation);
        }
        return withValueDeserializer;
    }
    
    protected EnumResolver<?> constructEnumResolver(final Class<?> clazz, final DeserializationConfig deserializationConfig, final AnnotatedMethod annotatedMethod) {
        if (annotatedMethod != null) {
            final Method annotated = annotatedMethod.getAnnotated();
            if (deserializationConfig.canOverrideAccessModifiers()) {
                ClassUtil.checkAndFixAccess(annotated);
            }
            return EnumResolver.constructUnsafeUsingMethod(clazz, annotated);
        }
        if (deserializationConfig.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING)) {
            return EnumResolver.constructUnsafeUsingToString(clazz);
        }
        return EnumResolver.constructUnsafe(clazz, deserializationConfig.getAnnotationIntrospector());
    }
    
    @Override
    public JsonDeserializer<?> createArrayDeserializer(final DeserializationContext deserializationContext, final ArrayType arrayType, final BeanDescription beanDescription) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        final JavaType contentType = arrayType.getContentType();
        final JsonDeserializer<?> jsonDeserializer = contentType.getValueHandler();
        TypeDeserializer typeDeserializer = contentType.getTypeHandler();
        if (typeDeserializer == null) {
            typeDeserializer = this.findTypeDeserializer(config, contentType);
        }
        JsonDeserializer<?> jsonDeserializer3;
        final JsonDeserializer<?> jsonDeserializer2 = jsonDeserializer3 = this._findCustomArrayDeserializer(arrayType, config, beanDescription, typeDeserializer, jsonDeserializer);
        Label_0128: {
            if (jsonDeserializer2 != null) {
                break Label_0128;
            }
            if (jsonDeserializer == null) {
                final Class<?> rawClass = contentType.getRawClass();
                if (contentType.isPrimitive()) {
                    return PrimitiveArrayDeserializers.forType(rawClass);
                }
                if (rawClass == String.class) {
                    return StringArrayDeserializer.instance;
                }
            }
            if ((jsonDeserializer3 = jsonDeserializer2) == null) {
                jsonDeserializer3 = new ObjectArrayDeserializer(arrayType, (JsonDeserializer<Object>)jsonDeserializer, typeDeserializer);
            }
            break Label_0128;
        }
        if (!this._factoryConfig.hasDeserializerModifiers()) {
            return jsonDeserializer3;
        }
        final Iterator<BeanDeserializerModifier> iterator = this._factoryConfig.deserializerModifiers().iterator();
        while (true) {
            final JsonDeserializer<?> forType = jsonDeserializer3;
            if (!iterator.hasNext()) {
                return forType;
            }
            jsonDeserializer3 = iterator.next().modifyArrayDeserializer(config, arrayType, beanDescription, jsonDeserializer3);
        }
    }
    
    @Override
    public JsonDeserializer<?> createCollectionDeserializer(final DeserializationContext deserializationContext, CollectionType collectionType, BeanDescription introspectForCreation) throws JsonMappingException {
        final JavaType contentType = collectionType.getContentType();
        final JsonDeserializer<Object> jsonDeserializer = contentType.getValueHandler();
        final DeserializationConfig config = deserializationContext.getConfig();
        TypeDeserializer typeDeserializer = contentType.getTypeHandler();
        if (typeDeserializer == null) {
            typeDeserializer = this.findTypeDeserializer(config, contentType);
        }
        JsonDeserializer<?> findCustomCollectionDeserializer;
        final JsonDeserializer<?> jsonDeserializer2 = findCustomCollectionDeserializer = this._findCustomCollectionDeserializer(collectionType, config, introspectForCreation, typeDeserializer, jsonDeserializer);
        if (jsonDeserializer2 == null) {
            final Class<?> rawClass = collectionType.getRawClass();
            findCustomCollectionDeserializer = jsonDeserializer2;
            if (jsonDeserializer == null) {
                findCustomCollectionDeserializer = jsonDeserializer2;
                if (EnumSet.class.isAssignableFrom(rawClass)) {
                    findCustomCollectionDeserializer = new EnumSetDeserializer(contentType, null);
                }
            }
        }
        Object modifyCollectionDeserializer;
        if (findCustomCollectionDeserializer == null) {
            if (collectionType.isInterface() || collectionType.isAbstract()) {
                final CollectionType mapAbstractCollectionType = this._mapAbstractCollectionType(collectionType, config);
                if (mapAbstractCollectionType == null) {
                    throw new IllegalArgumentException("Can not find a deserializer for non-concrete Collection type " + collectionType);
                }
                introspectForCreation = config.introspectForCreation(mapAbstractCollectionType);
                collectionType = mapAbstractCollectionType;
            }
            final ValueInstantiator valueInstantiator = this.findValueInstantiator(deserializationContext, introspectForCreation);
            if (!valueInstantiator.canCreateUsingDefault() && collectionType.getRawClass() == ArrayBlockingQueue.class) {
                return new ArrayBlockingQueueDeserializer(collectionType, jsonDeserializer, typeDeserializer, valueInstantiator, null);
            }
            if (contentType.getRawClass() == String.class) {
                modifyCollectionDeserializer = new StringCollectionDeserializer(collectionType, jsonDeserializer, valueInstantiator);
            }
            else {
                modifyCollectionDeserializer = new CollectionDeserializer(collectionType, jsonDeserializer, typeDeserializer, valueInstantiator);
            }
        }
        else {
            modifyCollectionDeserializer = findCustomCollectionDeserializer;
        }
        JsonDeserializer<?> jsonDeserializer3;
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                jsonDeserializer3 = (JsonDeserializer<?>)modifyCollectionDeserializer;
                if (!iterator.hasNext()) {
                    break;
                }
                modifyCollectionDeserializer = iterator.next().modifyCollectionDeserializer(config, collectionType, introspectForCreation, (JsonDeserializer<?>)modifyCollectionDeserializer);
            }
        }
        else {
            jsonDeserializer3 = (JsonDeserializer<?>)modifyCollectionDeserializer;
        }
        return jsonDeserializer3;
    }
    
    @Override
    public JsonDeserializer<?> createCollectionLikeDeserializer(final DeserializationContext deserializationContext, final CollectionLikeType collectionLikeType, final BeanDescription beanDescription) throws JsonMappingException {
        final JavaType contentType = collectionLikeType.getContentType();
        final JsonDeserializer<?> jsonDeserializer = contentType.getValueHandler();
        final DeserializationConfig config = deserializationContext.getConfig();
        TypeDeserializer typeDeserializer = contentType.getTypeHandler();
        if (typeDeserializer == null) {
            typeDeserializer = this.findTypeDeserializer(config, contentType);
        }
        JsonDeserializer<?> findCustomCollectionLikeDeserializer;
        JsonDeserializer<?> modifyCollectionLikeDeserializer = findCustomCollectionLikeDeserializer = this._findCustomCollectionLikeDeserializer(collectionLikeType, config, beanDescription, typeDeserializer, jsonDeserializer);
        if (modifyCollectionLikeDeserializer != null) {
            findCustomCollectionLikeDeserializer = modifyCollectionLikeDeserializer;
            if (this._factoryConfig.hasDeserializerModifiers()) {
                final Iterator<BeanDeserializerModifier> iterator = this._factoryConfig.deserializerModifiers().iterator();
                while (iterator.hasNext()) {
                    modifyCollectionLikeDeserializer = iterator.next().modifyCollectionLikeDeserializer(config, collectionLikeType, beanDescription, modifyCollectionLikeDeserializer);
                }
                findCustomCollectionLikeDeserializer = modifyCollectionLikeDeserializer;
            }
        }
        return findCustomCollectionLikeDeserializer;
    }
    
    @Override
    public JsonDeserializer<?> createEnumDeserializer(final DeserializationContext deserializationContext, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        final Class<?> rawClass = javaType.getRawClass();
        JsonDeserializer<?> jsonDeserializer = this._findCustomEnumDeserializer(rawClass, config, beanDescription);
        JsonDeserializer<?> modifyEnumDeserializer = null;
        Label_0134: {
            if (jsonDeserializer == null) {
                while (true) {
                    for (final AnnotatedMethod annotatedMethod : beanDescription.getFactoryMethods()) {
                        if (deserializationContext.getAnnotationIntrospector().hasCreatorAnnotation(annotatedMethod)) {
                            if (annotatedMethod.getParameterCount() != 1 || !annotatedMethod.getRawReturnType().isAssignableFrom(rawClass)) {
                                throw new IllegalArgumentException("Unsuitable method (" + annotatedMethod + ") decorated with @JsonCreator (for Enum type " + rawClass.getName() + ")");
                            }
                            jsonDeserializer = EnumDeserializer.deserializerForCreator(config, rawClass, annotatedMethod);
                            modifyEnumDeserializer = jsonDeserializer;
                            if (jsonDeserializer == null) {
                                modifyEnumDeserializer = new EnumDeserializer(this.constructEnumResolver(rawClass, config, beanDescription.findJsonValueMethod()));
                            }
                            break Label_0134;
                        }
                    }
                    continue;
                }
            }
            modifyEnumDeserializer = jsonDeserializer;
        }
        JsonDeserializer<?> jsonDeserializer2;
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator2 = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                jsonDeserializer2 = modifyEnumDeserializer;
                if (!iterator2.hasNext()) {
                    break;
                }
                modifyEnumDeserializer = iterator2.next().modifyEnumDeserializer(config, javaType, beanDescription, modifyEnumDeserializer);
            }
        }
        else {
            jsonDeserializer2 = modifyEnumDeserializer;
        }
        return jsonDeserializer2;
    }
    
    @Override
    public KeyDeserializer createKeyDeserializer(final DeserializationContext deserializationContext, final JavaType javaType) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        KeyDeserializer keyDeserializer = null;
        final KeyDeserializer keyDeserializer2 = null;
        if (this._factoryConfig.hasKeyDeserializers()) {
            final BeanDescription introspectClassAnnotations = config.introspectClassAnnotations(javaType.getRawClass());
            final Iterator<KeyDeserializers> iterator = this._factoryConfig.keyDeserializers().iterator();
            keyDeserializer = keyDeserializer2;
            while (iterator.hasNext()) {
                final KeyDeserializer keyDeserializer3 = iterator.next().findKeyDeserializer(javaType, config, introspectClassAnnotations);
                if ((keyDeserializer = keyDeserializer3) != null) {
                    keyDeserializer = keyDeserializer3;
                    break;
                }
            }
        }
        KeyDeserializer keyDeserializer4 = null;
        Label_0123: {
            if ((keyDeserializer4 = keyDeserializer) != null) {
                break Label_0123;
            }
            if (!javaType.isEnumType()) {
                keyDeserializer4 = StdKeyDeserializers.findStringBasedKeyDeserializer(config, javaType);
                break Label_0123;
            }
            return this._createEnumKeyDeserializer(deserializationContext, javaType);
        }
        KeyDeserializer createEnumKeyDeserializer;
        if ((createEnumKeyDeserializer = keyDeserializer4) == null) {
            return createEnumKeyDeserializer;
        }
        createEnumKeyDeserializer = keyDeserializer4;
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator2 = this._factoryConfig.deserializerModifiers().iterator();
            while (iterator2.hasNext()) {
                keyDeserializer4 = iterator2.next().modifyKeyDeserializer(config, javaType, keyDeserializer4);
            }
            return keyDeserializer4;
        }
        return createEnumKeyDeserializer;
    }
    
    @Override
    public JsonDeserializer<?> createMapDeserializer(final DeserializationContext deserializationContext, MapType mapType, BeanDescription introspectForCreation) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        final JavaType keyType = mapType.getKeyType();
        final JavaType contentType = mapType.getContentType();
        final JsonDeserializer<?> jsonDeserializer = contentType.getValueHandler();
        final KeyDeserializer keyDeserializer = keyType.getValueHandler();
        TypeDeserializer typeDeserializer = contentType.getTypeHandler();
        if (typeDeserializer == null) {
            typeDeserializer = this.findTypeDeserializer(config, contentType);
        }
        JsonDeserializer<?> jsonDeserializer3;
        JsonDeserializer<?> jsonDeserializer2 = jsonDeserializer3 = this._findCustomMapDeserializer(mapType, config, introspectForCreation, keyDeserializer, typeDeserializer, jsonDeserializer);
        MapType mapType2 = mapType;
        BeanDescription beanDescription = introspectForCreation;
        if (jsonDeserializer2 == null) {
            final Class<?> rawClass = mapType.getRawClass();
            if (EnumMap.class.isAssignableFrom(rawClass)) {
                final Class<?> rawClass2 = keyType.getRawClass();
                if (rawClass2 == null || !rawClass2.isEnum()) {
                    throw new IllegalArgumentException("Can not construct EnumMap; generic (key) type not available");
                }
                jsonDeserializer2 = new EnumMapDeserializer(mapType, null, jsonDeserializer, typeDeserializer);
            }
            jsonDeserializer3 = jsonDeserializer2;
            mapType2 = mapType;
            beanDescription = introspectForCreation;
            if (jsonDeserializer2 == null) {
                if (mapType.isInterface() || mapType.isAbstract()) {
                    final Class<? extends Map> clazz = BasicDeserializerFactory._mapFallbacks.get(rawClass.getName());
                    if (clazz == null) {
                        throw new IllegalArgumentException("Can not find a deserializer for non-concrete Map type " + mapType);
                    }
                    mapType = (MapType)config.constructSpecializedType(mapType, clazz);
                    introspectForCreation = config.introspectForCreation(mapType);
                }
                jsonDeserializer3 = new MapDeserializer(mapType, this.findValueInstantiator(deserializationContext, introspectForCreation), keyDeserializer, (JsonDeserializer<Object>)jsonDeserializer, typeDeserializer);
                ((MapDeserializer)jsonDeserializer3).setIgnorableProperties(config.getAnnotationIntrospector().findPropertiesToIgnore(introspectForCreation.getClassInfo()));
                beanDescription = introspectForCreation;
                mapType2 = mapType;
            }
        }
        JsonDeserializer<?> jsonDeserializer4 = jsonDeserializer3;
        if (this._factoryConfig.hasDeserializerModifiers()) {
            final Iterator<BeanDeserializerModifier> iterator = this._factoryConfig.deserializerModifiers().iterator();
            while (true) {
                jsonDeserializer4 = jsonDeserializer3;
                if (!iterator.hasNext()) {
                    break;
                }
                jsonDeserializer3 = iterator.next().modifyMapDeserializer(config, mapType2, beanDescription, jsonDeserializer3);
            }
        }
        return jsonDeserializer4;
    }
    
    @Override
    public JsonDeserializer<?> createMapLikeDeserializer(final DeserializationContext deserializationContext, final MapLikeType mapLikeType, final BeanDescription beanDescription) throws JsonMappingException {
        final JavaType keyType = mapLikeType.getKeyType();
        final JavaType contentType = mapLikeType.getContentType();
        final DeserializationConfig config = deserializationContext.getConfig();
        final JsonDeserializer<?> jsonDeserializer = contentType.getValueHandler();
        final KeyDeserializer keyDeserializer = keyType.getValueHandler();
        TypeDeserializer typeDeserializer = contentType.getTypeHandler();
        if (typeDeserializer == null) {
            typeDeserializer = this.findTypeDeserializer(config, contentType);
        }
        JsonDeserializer<?> findCustomMapLikeDeserializer;
        JsonDeserializer<?> modifyMapLikeDeserializer = findCustomMapLikeDeserializer = this._findCustomMapLikeDeserializer(mapLikeType, config, beanDescription, keyDeserializer, typeDeserializer, jsonDeserializer);
        if (modifyMapLikeDeserializer != null) {
            findCustomMapLikeDeserializer = modifyMapLikeDeserializer;
            if (this._factoryConfig.hasDeserializerModifiers()) {
                final Iterator<BeanDeserializerModifier> iterator = this._factoryConfig.deserializerModifiers().iterator();
                while (iterator.hasNext()) {
                    modifyMapLikeDeserializer = iterator.next().modifyMapLikeDeserializer(config, mapLikeType, beanDescription, modifyMapLikeDeserializer);
                }
                findCustomMapLikeDeserializer = modifyMapLikeDeserializer;
            }
        }
        return findCustomMapLikeDeserializer;
    }
    
    @Override
    public JsonDeserializer<?> createTreeDeserializer(final DeserializationConfig deserializationConfig, final JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        final Class<?> rawClass = javaType.getRawClass();
        final JsonDeserializer<?> findCustomTreeNodeDeserializer = this._findCustomTreeNodeDeserializer((Class<? extends JsonNode>)rawClass, deserializationConfig, beanDescription);
        if (findCustomTreeNodeDeserializer != null) {
            return findCustomTreeNodeDeserializer;
        }
        return JsonNodeDeserializer.getDeserializer(rawClass);
    }
    
    public JsonDeserializer<?> findDefaultDeserializer(final DeserializationContext deserializationContext, JavaType javaType, final BeanDescription beanDescription) throws JsonMappingException {
        final Class<?> rawClass = javaType.getRawClass();
        final String name = rawClass.getName();
        if (rawClass.isPrimitive() || name.startsWith("java.")) {
            JsonDeserializer<?> jsonDeserializer;
            if (rawClass == BasicDeserializerFactory.CLASS_OBJECT) {
                jsonDeserializer = UntypedObjectDeserializer.instance;
            }
            else {
                if (rawClass == BasicDeserializerFactory.CLASS_STRING || rawClass == BasicDeserializerFactory.CLASS_CHAR_BUFFER) {
                    return StringDeserializer.instance;
                }
                if (rawClass == BasicDeserializerFactory.CLASS_ITERABLE) {
                    final TypeFactory typeFactory = deserializationContext.getTypeFactory();
                    if (javaType.containedTypeCount() > 0) {
                        javaType = javaType.containedType(0);
                    }
                    else {
                        javaType = TypeFactory.unknownType();
                    }
                    return this.createCollectionDeserializer(deserializationContext, typeFactory.constructCollectionType(Collection.class, javaType), beanDescription);
                }
                if ((jsonDeserializer = NumberDeserializers.find(rawClass, name)) == null && (jsonDeserializer = DateDeserializers.find(rawClass, name)) == null) {
                    return JdkDeserializers.find(rawClass, name);
                }
            }
            return jsonDeserializer;
        }
        if (name.startsWith("com.fasterxml.")) {
            return JacksonDeserializers.find(rawClass);
        }
        return null;
    }
    
    protected JsonDeserializer<Object> findDeserializerFromAnnotation(final DeserializationContext deserializationContext, final Annotated annotated) throws JsonMappingException {
        final Object deserializer = deserializationContext.getAnnotationIntrospector().findDeserializer(annotated);
        if (deserializer == null) {
            return null;
        }
        return deserializationContext.deserializerInstance(annotated, deserializer);
    }
    
    public TypeDeserializer findPropertyContentTypeDeserializer(final DeserializationConfig deserializationConfig, JavaType contentType, final AnnotatedMember annotatedMember) throws JsonMappingException {
        final AnnotationIntrospector annotationIntrospector = deserializationConfig.getAnnotationIntrospector();
        final TypeResolverBuilder<?> propertyContentTypeResolver = annotationIntrospector.findPropertyContentTypeResolver(deserializationConfig, annotatedMember, contentType);
        contentType = contentType.getContentType();
        if (propertyContentTypeResolver == null) {
            return this.findTypeDeserializer(deserializationConfig, contentType);
        }
        return propertyContentTypeResolver.buildTypeDeserializer(deserializationConfig, contentType, deserializationConfig.getSubtypeResolver().collectAndResolveSubtypes(annotatedMember, deserializationConfig, annotationIntrospector, contentType));
    }
    
    public TypeDeserializer findPropertyTypeDeserializer(final DeserializationConfig deserializationConfig, final JavaType javaType, final AnnotatedMember annotatedMember) throws JsonMappingException {
        final AnnotationIntrospector annotationIntrospector = deserializationConfig.getAnnotationIntrospector();
        final TypeResolverBuilder<?> propertyTypeResolver = annotationIntrospector.findPropertyTypeResolver(deserializationConfig, annotatedMember, javaType);
        if (propertyTypeResolver == null) {
            return this.findTypeDeserializer(deserializationConfig, javaType);
        }
        return propertyTypeResolver.buildTypeDeserializer(deserializationConfig, javaType, deserializationConfig.getSubtypeResolver().collectAndResolveSubtypes(annotatedMember, deserializationConfig, annotationIntrospector, javaType));
    }
    
    @Override
    public TypeDeserializer findTypeDeserializer(final DeserializationConfig deserializationConfig, final JavaType javaType) throws JsonMappingException {
        Collection<NamedType> collectAndResolveSubtypes = null;
        final AnnotatedClass classInfo = deserializationConfig.introspectClassAnnotations(javaType.getRawClass()).getClassInfo();
        final AnnotationIntrospector annotationIntrospector = deserializationConfig.getAnnotationIntrospector();
        TypeResolverBuilder<?> typeResolverBuilder = annotationIntrospector.findTypeResolver(deserializationConfig, classInfo, javaType);
        if (typeResolverBuilder == null) {
            if ((typeResolverBuilder = deserializationConfig.getDefaultTyper(javaType)) == null) {
                return null;
            }
        }
        else {
            collectAndResolveSubtypes = deserializationConfig.getSubtypeResolver().collectAndResolveSubtypes(classInfo, deserializationConfig, annotationIntrospector);
        }
        Object defaultImpl = typeResolverBuilder;
        if (typeResolverBuilder.getDefaultImpl() == null) {
            defaultImpl = typeResolverBuilder;
            if (javaType.isAbstract()) {
                final JavaType mapAbstractType = this.mapAbstractType(deserializationConfig, javaType);
                defaultImpl = typeResolverBuilder;
                if (mapAbstractType != null) {
                    defaultImpl = typeResolverBuilder;
                    if (mapAbstractType.getRawClass() != javaType.getRawClass()) {
                        defaultImpl = typeResolverBuilder.defaultImpl(mapAbstractType.getRawClass());
                    }
                }
            }
        }
        return ((TypeResolverBuilder)defaultImpl).buildTypeDeserializer(deserializationConfig, javaType, collectAndResolveSubtypes);
    }
    
    public ValueInstantiator findValueInstantiator(final DeserializationContext deserializationContext, final BeanDescription beanDescription) throws JsonMappingException {
        final DeserializationConfig config = deserializationContext.getConfig();
        ValueInstantiator valueInstantiatorInstance = null;
        final AnnotatedClass classInfo = beanDescription.getClassInfo();
        final Object valueInstantiator = deserializationContext.getAnnotationIntrospector().findValueInstantiator(classInfo);
        if (valueInstantiator != null) {
            valueInstantiatorInstance = this._valueInstantiatorInstance(config, classInfo, valueInstantiator);
        }
        ValueInstantiator valueInstantiator2;
        if ((valueInstantiator2 = valueInstantiatorInstance) == null && (valueInstantiator2 = this._findStdValueInstantiator(config, beanDescription)) == null) {
            valueInstantiator2 = this._constructDefaultValueInstantiator(deserializationContext, beanDescription);
        }
        Label_0181: {
            if (this._factoryConfig.hasValueInstantiators()) {
                final Iterator<ValueInstantiators> iterator = this._factoryConfig.valueInstantiators().iterator();
                ValueInstantiator valueInstantiator3 = valueInstantiator2;
                ValueInstantiators valueInstantiators;
                do {
                    valueInstantiator2 = valueInstantiator3;
                    if (!iterator.hasNext()) {
                        break Label_0181;
                    }
                    valueInstantiators = iterator.next();
                } while ((valueInstantiator3 = valueInstantiators.findValueInstantiator(config, beanDescription, valueInstantiator3)) != null);
                throw new JsonMappingException("Broken registered ValueInstantiators (of type " + valueInstantiators.getClass().getName() + "): returned null ValueInstantiator");
            }
        }
        if (valueInstantiator2.getIncompleteParameter() != null) {
            final AnnotatedParameter incompleteParameter = valueInstantiator2.getIncompleteParameter();
            throw new IllegalArgumentException("Argument #" + incompleteParameter.getIndex() + " of constructor " + incompleteParameter.getOwner() + " has no property name annotation; must have name when multiple-paramater constructor annotated as Creator");
        }
        return valueInstantiator2;
    }
    
    @Override
    public JavaType mapAbstractType(final DeserializationConfig deserializationConfig, JavaType javaType) throws JsonMappingException {
        while (true) {
            final JavaType mapAbstractType2 = this._mapAbstractType2(deserializationConfig, javaType);
            if (mapAbstractType2 == null) {
                return javaType;
            }
            final Class<?> rawClass = javaType.getRawClass();
            final Class<?> rawClass2 = mapAbstractType2.getRawClass();
            if (rawClass == rawClass2 || !rawClass.isAssignableFrom(rawClass2)) {
                throw new IllegalArgumentException("Invalid abstract type resolution from " + javaType + " to " + mapAbstractType2 + ": latter is not a subtype of former");
            }
            javaType = mapAbstractType2;
        }
    }
    
    protected <T extends JavaType> T modifyTypeByAnnotation(final DeserializationContext p0, final Annotated p1, final T p2) throws JsonMappingException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_1        
        //     1: invokevirtual   com/fasterxml/jackson/databind/DeserializationContext.getAnnotationIntrospector:()Lcom/fasterxml/jackson/databind/AnnotationIntrospector;
        //     4: astore          5
        //     6: aload           5
        //     8: aload_2        
        //     9: aload_3        
        //    10: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findDeserializationType:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Lcom/fasterxml/jackson/databind/JavaType;)Ljava/lang/Class;
        //    13: astore          6
        //    15: aload           6
        //    17: ifnull          435
        //    20: aload_3        
        //    21: aload           6
        //    23: invokevirtual   com/fasterxml/jackson/databind/JavaType.narrowBy:(Ljava/lang/Class;)Lcom/fasterxml/jackson/databind/JavaType;
        //    26: astore          4
        //    28: aload           4
        //    30: astore_3       
        //    31: aload_3        
        //    32: invokevirtual   com/fasterxml/jackson/databind/JavaType.isContainerType:()Z
        //    35: ifeq            433
        //    38: aload           5
        //    40: aload_2        
        //    41: aload_3        
        //    42: invokevirtual   com/fasterxml/jackson/databind/JavaType.getKeyType:()Lcom/fasterxml/jackson/databind/JavaType;
        //    45: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findDeserializationKeyType:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Lcom/fasterxml/jackson/databind/JavaType;)Ljava/lang/Class;
        //    48: astore          6
        //    50: aload           6
        //    52: ifnull          427
        //    55: aload_3        
        //    56: instanceof      Lcom/fasterxml/jackson/databind/type/MapLikeType;
        //    59: ifne            167
        //    62: new             Lcom/fasterxml/jackson/databind/JsonMappingException;
        //    65: dup            
        //    66: new             Ljava/lang/StringBuilder;
        //    69: dup            
        //    70: invokespecial   java/lang/StringBuilder.<init>:()V
        //    73: ldc_w           "Illegal key-type annotation: type "
        //    76: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    79: aload_3        
        //    80: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //    83: ldc_w           " is not a Map(-like) type"
        //    86: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    89: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    92: invokespecial   com/fasterxml/jackson/databind/JsonMappingException.<init>:(Ljava/lang/String;)V
        //    95: athrow         
        //    96: astore_1       
        //    97: new             Lcom/fasterxml/jackson/databind/JsonMappingException;
        //   100: dup            
        //   101: new             Ljava/lang/StringBuilder;
        //   104: dup            
        //   105: invokespecial   java/lang/StringBuilder.<init>:()V
        //   108: ldc_w           "Failed to narrow type "
        //   111: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   114: aload_3        
        //   115: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   118: ldc_w           " with concrete-type annotation (value "
        //   121: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   124: aload           6
        //   126: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   129: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   132: ldc_w           "), method '"
        //   135: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   138: aload_2        
        //   139: invokevirtual   com/fasterxml/jackson/databind/introspect/Annotated.getName:()Ljava/lang/String;
        //   142: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   145: ldc_w           "': "
        //   148: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   151: aload_1        
        //   152: invokevirtual   java/lang/IllegalArgumentException.getMessage:()Ljava/lang/String;
        //   155: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   158: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   161: aconst_null    
        //   162: aload_1        
        //   163: invokespecial   com/fasterxml/jackson/databind/JsonMappingException.<init>:(Ljava/lang/String;Lcom/fasterxml/jackson/core/JsonLocation;Ljava/lang/Throwable;)V
        //   166: athrow         
        //   167: aload_3        
        //   168: checkcast       Lcom/fasterxml/jackson/databind/type/MapLikeType;
        //   171: aload           6
        //   173: invokevirtual   com/fasterxml/jackson/databind/type/MapLikeType.narrowKey:(Ljava/lang/Class;)Lcom/fasterxml/jackson/databind/JavaType;
        //   176: astore          4
        //   178: aload           4
        //   180: invokevirtual   com/fasterxml/jackson/databind/JavaType.getKeyType:()Lcom/fasterxml/jackson/databind/JavaType;
        //   183: astore          6
        //   185: aload           4
        //   187: astore_3       
        //   188: aload           6
        //   190: ifnull          241
        //   193: aload           4
        //   195: astore_3       
        //   196: aload           6
        //   198: invokevirtual   com/fasterxml/jackson/databind/JavaType.getValueHandler:()Ljava/lang/Object;
        //   201: ifnonnull       241
        //   204: aload_1        
        //   205: aload_2        
        //   206: aload           5
        //   208: aload_2        
        //   209: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findKeyDeserializer:(Lcom/fasterxml/jackson/databind/introspect/Annotated;)Ljava/lang/Object;
        //   212: invokevirtual   com/fasterxml/jackson/databind/DeserializationContext.keyDeserializerInstance:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Ljava/lang/Object;)Lcom/fasterxml/jackson/databind/KeyDeserializer;
        //   215: astore          6
        //   217: aload           4
        //   219: astore_3       
        //   220: aload           6
        //   222: ifnull          241
        //   225: aload           4
        //   227: checkcast       Lcom/fasterxml/jackson/databind/type/MapLikeType;
        //   230: aload           6
        //   232: invokevirtual   com/fasterxml/jackson/databind/type/MapLikeType.withKeyValueHandler:(Ljava/lang/Object;)Lcom/fasterxml/jackson/databind/type/MapLikeType;
        //   235: astore_3       
        //   236: aload_3        
        //   237: invokevirtual   com/fasterxml/jackson/databind/JavaType.getKeyType:()Lcom/fasterxml/jackson/databind/JavaType;
        //   240: pop            
        //   241: aload           5
        //   243: aload_2        
        //   244: aload_3        
        //   245: invokevirtual   com/fasterxml/jackson/databind/JavaType.getContentType:()Lcom/fasterxml/jackson/databind/JavaType;
        //   248: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findDeserializationContentType:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Lcom/fasterxml/jackson/databind/JavaType;)Ljava/lang/Class;
        //   251: astore          6
        //   253: aload_3        
        //   254: astore          4
        //   256: aload           6
        //   258: ifnull          269
        //   261: aload_3        
        //   262: aload           6
        //   264: invokevirtual   com/fasterxml/jackson/databind/JavaType.narrowContentsBy:(Ljava/lang/Class;)Lcom/fasterxml/jackson/databind/JavaType;
        //   267: astore          4
        //   269: aload           4
        //   271: astore_3       
        //   272: aload           4
        //   274: invokevirtual   com/fasterxml/jackson/databind/JavaType.getContentType:()Lcom/fasterxml/jackson/databind/JavaType;
        //   277: invokevirtual   com/fasterxml/jackson/databind/JavaType.getValueHandler:()Ljava/lang/Object;
        //   280: ifnonnull       309
        //   283: aload_1        
        //   284: aload_2        
        //   285: aload           5
        //   287: aload_2        
        //   288: invokevirtual   com/fasterxml/jackson/databind/AnnotationIntrospector.findContentDeserializer:(Lcom/fasterxml/jackson/databind/introspect/Annotated;)Ljava/lang/Object;
        //   291: invokevirtual   com/fasterxml/jackson/databind/DeserializationContext.deserializerInstance:(Lcom/fasterxml/jackson/databind/introspect/Annotated;Ljava/lang/Object;)Lcom/fasterxml/jackson/databind/JsonDeserializer;
        //   294: astore_1       
        //   295: aload           4
        //   297: astore_3       
        //   298: aload_1        
        //   299: ifnull          309
        //   302: aload           4
        //   304: aload_1        
        //   305: invokevirtual   com/fasterxml/jackson/databind/JavaType.withContentValueHandler:(Ljava/lang/Object;)Lcom/fasterxml/jackson/databind/JavaType;
        //   308: astore_3       
        //   309: aload_3        
        //   310: areturn        
        //   311: astore_1       
        //   312: new             Lcom/fasterxml/jackson/databind/JsonMappingException;
        //   315: dup            
        //   316: new             Ljava/lang/StringBuilder;
        //   319: dup            
        //   320: invokespecial   java/lang/StringBuilder.<init>:()V
        //   323: ldc_w           "Failed to narrow key type "
        //   326: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   329: aload_3        
        //   330: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   333: ldc_w           " with key-type annotation ("
        //   336: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   339: aload           6
        //   341: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   344: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   347: ldc_w           "): "
        //   350: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   353: aload_1        
        //   354: invokevirtual   java/lang/IllegalArgumentException.getMessage:()Ljava/lang/String;
        //   357: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   360: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   363: aconst_null    
        //   364: aload_1        
        //   365: invokespecial   com/fasterxml/jackson/databind/JsonMappingException.<init>:(Ljava/lang/String;Lcom/fasterxml/jackson/core/JsonLocation;Ljava/lang/Throwable;)V
        //   368: athrow         
        //   369: astore_1       
        //   370: new             Lcom/fasterxml/jackson/databind/JsonMappingException;
        //   373: dup            
        //   374: new             Ljava/lang/StringBuilder;
        //   377: dup            
        //   378: invokespecial   java/lang/StringBuilder.<init>:()V
        //   381: ldc_w           "Failed to narrow content type "
        //   384: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   387: aload_3        
        //   388: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   391: ldc_w           " with content-type annotation ("
        //   394: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   397: aload           6
        //   399: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
        //   402: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   405: ldc_w           "): "
        //   408: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   411: aload_1        
        //   412: invokevirtual   java/lang/IllegalArgumentException.getMessage:()Ljava/lang/String;
        //   415: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   418: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   421: aconst_null    
        //   422: aload_1        
        //   423: invokespecial   com/fasterxml/jackson/databind/JsonMappingException.<init>:(Ljava/lang/String;Lcom/fasterxml/jackson/core/JsonLocation;Ljava/lang/Throwable;)V
        //   426: athrow         
        //   427: aload_3        
        //   428: astore          4
        //   430: goto            178
        //   433: aload_3        
        //   434: areturn        
        //   435: goto            31
        //    Exceptions:
        //  throws com.fasterxml.jackson.databind.JsonMappingException
        //    Signature:
        //  <T:Lcom/fasterxml/jackson/databind/JavaType;>(Lcom/fasterxml/jackson/databind/DeserializationContext;Lcom/fasterxml/jackson/databind/introspect/Annotated;TT;)TT;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                
        //  -----  -----  -----  -----  ------------------------------------
        //  20     28     96     167    Ljava/lang/IllegalArgumentException;
        //  167    178    311    369    Ljava/lang/IllegalArgumentException;
        //  261    269    369    427    Ljava/lang/IllegalArgumentException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0269:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    protected JavaType resolveType(final DeserializationContext deserializationContext, final BeanDescription beanDescription, JavaType javaType, final AnnotatedMember annotatedMember) throws JsonMappingException {
        JavaType withContentTypeHandler = javaType;
        if (javaType.isContainerType()) {
            final AnnotationIntrospector annotationIntrospector = deserializationContext.getAnnotationIntrospector();
            JavaType withKeyValueHandler = javaType;
            if (javaType.getKeyType() != null) {
                final KeyDeserializer keyDeserializerInstance = deserializationContext.keyDeserializerInstance(annotatedMember, annotationIntrospector.findKeyDeserializer(annotatedMember));
                withKeyValueHandler = javaType;
                if (keyDeserializerInstance != null) {
                    withKeyValueHandler = ((MapLikeType)javaType).withKeyValueHandler(keyDeserializerInstance);
                    withKeyValueHandler.getKeyType();
                }
            }
            final JsonDeserializer<Object> deserializerInstance = deserializationContext.deserializerInstance(annotatedMember, annotationIntrospector.findContentDeserializer(annotatedMember));
            javaType = withKeyValueHandler;
            if (deserializerInstance != null) {
                javaType = withKeyValueHandler.withContentValueHandler(deserializerInstance);
            }
            withContentTypeHandler = javaType;
            if (annotatedMember instanceof AnnotatedMember) {
                final TypeDeserializer propertyContentTypeDeserializer = this.findPropertyContentTypeDeserializer(deserializationContext.getConfig(), javaType, annotatedMember);
                withContentTypeHandler = javaType;
                if (propertyContentTypeDeserializer != null) {
                    withContentTypeHandler = javaType.withContentTypeHandler(propertyContentTypeDeserializer);
                }
            }
        }
        TypeDeserializer typeDeserializer;
        if (annotatedMember instanceof AnnotatedMember) {
            typeDeserializer = this.findPropertyTypeDeserializer(deserializationContext.getConfig(), withContentTypeHandler, annotatedMember);
        }
        else {
            typeDeserializer = this.findTypeDeserializer(deserializationContext.getConfig(), withContentTypeHandler);
        }
        javaType = withContentTypeHandler;
        if (typeDeserializer != null) {
            javaType = withContentTypeHandler.withTypeHandler(typeDeserializer);
        }
        return javaType;
    }
}
