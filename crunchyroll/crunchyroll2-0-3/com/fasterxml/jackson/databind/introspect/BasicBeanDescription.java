// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.lang.reflect.Type;
import java.util.ArrayList;
import com.fasterxml.jackson.databind.util.Annotations;
import java.lang.reflect.Constructor;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import java.lang.reflect.Method;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Iterator;
import java.util.HashMap;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.util.Converter;
import java.util.Collections;
import com.fasterxml.jackson.databind.JavaType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanDescription;

public class BasicBeanDescription extends BeanDescription
{
    protected final AnnotationIntrospector _annotationIntrospector;
    protected AnnotatedMember _anyGetter;
    protected AnnotatedMethod _anySetterMethod;
    protected TypeBindings _bindings;
    protected final AnnotatedClass _classInfo;
    protected final MapperConfig<?> _config;
    protected Set<String> _ignoredPropertyNames;
    protected Map<Object, AnnotatedMember> _injectables;
    protected AnnotatedMethod _jsonValueMethod;
    protected ObjectIdInfo _objectIdInfo;
    protected final List<BeanPropertyDefinition> _properties;
    
    protected BasicBeanDescription(final MapperConfig<?> config, final JavaType javaType, final AnnotatedClass classInfo, final List<BeanPropertyDefinition> properties) {
        super(javaType);
        this._config = config;
        AnnotationIntrospector annotationIntrospector;
        if (config == null) {
            annotationIntrospector = null;
        }
        else {
            annotationIntrospector = config.getAnnotationIntrospector();
        }
        this._annotationIntrospector = annotationIntrospector;
        this._classInfo = classInfo;
        this._properties = properties;
    }
    
    protected BasicBeanDescription(final POJOPropertiesCollector pojoPropertiesCollector) {
        this(pojoPropertiesCollector.getConfig(), pojoPropertiesCollector.getType(), pojoPropertiesCollector.getClassDef(), pojoPropertiesCollector.getProperties());
        this._objectIdInfo = pojoPropertiesCollector.getObjectIdInfo();
    }
    
    public static BasicBeanDescription forDeserialization(final POJOPropertiesCollector pojoPropertiesCollector) {
        final BasicBeanDescription basicBeanDescription = new BasicBeanDescription(pojoPropertiesCollector);
        basicBeanDescription._anySetterMethod = pojoPropertiesCollector.getAnySetterMethod();
        basicBeanDescription._ignoredPropertyNames = pojoPropertiesCollector.getIgnoredPropertyNames();
        basicBeanDescription._injectables = pojoPropertiesCollector.getInjectables();
        basicBeanDescription._jsonValueMethod = pojoPropertiesCollector.getJsonValueMethod();
        return basicBeanDescription;
    }
    
    public static BasicBeanDescription forOtherUse(final MapperConfig<?> mapperConfig, final JavaType javaType, final AnnotatedClass annotatedClass) {
        return new BasicBeanDescription(mapperConfig, javaType, annotatedClass, Collections.emptyList());
    }
    
    public static BasicBeanDescription forSerialization(final POJOPropertiesCollector pojoPropertiesCollector) {
        final BasicBeanDescription basicBeanDescription = new BasicBeanDescription(pojoPropertiesCollector);
        basicBeanDescription._jsonValueMethod = pojoPropertiesCollector.getJsonValueMethod();
        basicBeanDescription._anyGetter = pojoPropertiesCollector.getAnyGetter();
        return basicBeanDescription;
    }
    
    public Converter<Object, Object> _createConverter(final Object o) {
        final Converter<Object, Object> converter = null;
        if (o == null) {
            return null;
        }
        if (o instanceof Converter) {
            return (Converter<Object, Object>)o;
        }
        if (!(o instanceof Class)) {
            throw new IllegalStateException("AnnotationIntrospector returned Converter definition of type " + o.getClass().getName() + "; expected type Converter or Class<Converter> instead");
        }
        final Class clazz = (Class)o;
        if (clazz == Converter.None.class || clazz == NoClass.class) {
            return null;
        }
        if (!Converter.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException("AnnotationIntrospector returned Class " + clazz.getName() + "; expected Class<Converter>");
        }
        final HandlerInstantiator handlerInstantiator = this._config.getHandlerInstantiator();
        Converter<?, ?> converterInstance;
        if (handlerInstantiator == null) {
            converterInstance = converter;
        }
        else {
            converterInstance = handlerInstantiator.converterInstance(this._config, this._classInfo, clazz);
        }
        Converter<Object, Object> converter2 = (Converter<Object, Object>)converterInstance;
        if (converterInstance == null) {
            converter2 = ClassUtil.createInstance((Class<Converter<Object, Object>>)clazz, this._config.canOverrideAccessModifiers());
        }
        return converter2;
    }
    
    @Override
    public TypeBindings bindingsForBeanType() {
        if (this._bindings == null) {
            this._bindings = new TypeBindings(this._config.getTypeFactory(), this._type);
        }
        return this._bindings;
    }
    
    @Override
    public AnnotatedMember findAnyGetter() throws IllegalArgumentException {
        if (this._anyGetter != null && !Map.class.isAssignableFrom(this._anyGetter.getRawType())) {
            throw new IllegalArgumentException("Invalid 'any-getter' annotation on method " + this._anyGetter.getName() + "(): return type is not instance of java.util.Map");
        }
        return this._anyGetter;
    }
    
    @Override
    public AnnotatedMethod findAnySetter() throws IllegalArgumentException {
        if (this._anySetterMethod != null) {
            final Class<?> rawParameterType = this._anySetterMethod.getRawParameterType(0);
            if (rawParameterType != String.class && rawParameterType != Object.class) {
                throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + this._anySetterMethod.getName() + "(): first argument not of type String or Object, but " + rawParameterType.getName());
            }
        }
        return this._anySetterMethod;
    }
    
    @Override
    public Map<String, AnnotatedMember> findBackReferenceProperties() {
        HashMap<String, AnnotatedMember> hashMap = null;
        final Iterator<BeanPropertyDefinition> iterator = this._properties.iterator();
        while (iterator.hasNext()) {
            final AnnotatedMember mutator = iterator.next().getMutator();
            if (mutator != null) {
                final AnnotationIntrospector.ReferenceProperty referenceType = this._annotationIntrospector.findReferenceType(mutator);
                if (referenceType == null || !referenceType.isBackReference()) {
                    continue;
                }
                if (hashMap == null) {
                    hashMap = new HashMap<String, AnnotatedMember>();
                }
                final String name = referenceType.getName();
                if (hashMap.put(name, mutator) != null) {
                    throw new IllegalArgumentException("Multiple back-reference properties with name '" + name + "'");
                }
                continue;
            }
        }
        return hashMap;
    }
    
    @Override
    public AnnotatedConstructor findDefaultConstructor() {
        return this._classInfo.getDefaultConstructor();
    }
    
    @Override
    public Converter<Object, Object> findDeserializationConverter() {
        if (this._annotationIntrospector == null) {
            return null;
        }
        return this._createConverter(this._annotationIntrospector.findDeserializationConverter(this._classInfo));
    }
    
    @Override
    public JsonFormat.Value findExpectedFormat(final JsonFormat.Value value) {
        JsonFormat.Value value2 = value;
        if (this._annotationIntrospector != null) {
            final JsonFormat.Value format = this._annotationIntrospector.findFormat(this._classInfo);
            value2 = value;
            if (format != null) {
                value2 = format;
            }
        }
        return value2;
    }
    
    @Override
    public Method findFactoryMethod(final Class<?>... array) {
        for (final AnnotatedMethod annotatedMethod : this._classInfo.getStaticMethods()) {
            if (this.isFactoryMethod(annotatedMethod)) {
                final Class<?> rawParameterType = annotatedMethod.getRawParameterType(0);
                for (int length = array.length, i = 0; i < length; ++i) {
                    if (rawParameterType.isAssignableFrom(array[i])) {
                        return annotatedMethod.getAnnotated();
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public Map<Object, AnnotatedMember> findInjectables() {
        return this._injectables;
    }
    
    @Override
    public AnnotatedMethod findJsonValueMethod() {
        return this._jsonValueMethod;
    }
    
    @Override
    public AnnotatedMethod findMethod(final String s, final Class<?>[] array) {
        return this._classInfo.findMethod(s, array);
    }
    
    @Override
    public Class<?> findPOJOBuilder() {
        if (this._annotationIntrospector == null) {
            return null;
        }
        return this._annotationIntrospector.findPOJOBuilder(this._classInfo);
    }
    
    @Override
    public JsonPOJOBuilder.Value findPOJOBuilderConfig() {
        if (this._annotationIntrospector == null) {
            return null;
        }
        return this._annotationIntrospector.findPOJOBuilderConfig(this._classInfo);
    }
    
    @Override
    public List<BeanPropertyDefinition> findProperties() {
        return this._properties;
    }
    
    @Override
    public Converter<Object, Object> findSerializationConverter() {
        if (this._annotationIntrospector == null) {
            return null;
        }
        return this._createConverter(this._annotationIntrospector.findSerializationConverter(this._classInfo));
    }
    
    @Override
    public JsonInclude.Include findSerializationInclusion(final JsonInclude.Include include) {
        if (this._annotationIntrospector == null) {
            return include;
        }
        return this._annotationIntrospector.findSerializationInclusion(this._classInfo, include);
    }
    
    @Override
    public Constructor<?> findSingleArgConstructor(final Class<?>... array) {
        for (final AnnotatedConstructor annotatedConstructor : this._classInfo.getConstructors()) {
            if (annotatedConstructor.getParameterCount() == 1) {
                final Class<?> rawParameterType = annotatedConstructor.getRawParameterType(0);
                for (int length = array.length, i = 0; i < length; ++i) {
                    if (array[i] == rawParameterType) {
                        return annotatedConstructor.getAnnotated();
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public Annotations getClassAnnotations() {
        return this._classInfo.getAnnotations();
    }
    
    @Override
    public AnnotatedClass getClassInfo() {
        return this._classInfo;
    }
    
    @Override
    public List<AnnotatedConstructor> getConstructors() {
        return this._classInfo.getConstructors();
    }
    
    @Override
    public List<AnnotatedMethod> getFactoryMethods() {
        final List<AnnotatedMethod> staticMethods = this._classInfo.getStaticMethods();
        if (staticMethods.isEmpty()) {
            return staticMethods;
        }
        final ArrayList<AnnotatedMethod> list = new ArrayList<AnnotatedMethod>();
        for (final AnnotatedMethod annotatedMethod : staticMethods) {
            if (this.isFactoryMethod(annotatedMethod)) {
                list.add(annotatedMethod);
            }
        }
        return list;
    }
    
    @Override
    public Set<String> getIgnoredPropertyNames() {
        if (this._ignoredPropertyNames == null) {
            return Collections.emptySet();
        }
        return this._ignoredPropertyNames;
    }
    
    @Override
    public ObjectIdInfo getObjectIdInfo() {
        return this._objectIdInfo;
    }
    
    @Override
    public boolean hasKnownClassAnnotations() {
        return this._classInfo.hasAnnotations();
    }
    
    @Override
    public Object instantiateBean(final boolean b) {
        final AnnotatedConstructor defaultConstructor = this._classInfo.getDefaultConstructor();
        if (defaultConstructor == null) {
            return null;
        }
        if (b) {
            defaultConstructor.fixAccess();
        }
    Label_0036:
        while (true) {
            try {
                return defaultConstructor.getAnnotated().newInstance(new Object[0]);
                Throwable cause = null;
                Label_0075: {
                    throw new IllegalArgumentException("Failed to instantiate bean of type " + this._classInfo.getAnnotated().getName() + ": (" + ((Exception)cause).getClass().getName() + ") " + cause.getMessage(), cause);
                }
                // iftrue(Label_0063:, !cause instanceof Error)
                // iftrue(Label_0051:, cause.getCause() == null)
                while (true) {
                    cause = cause.getCause();
                    break Label_0036;
                    Label_0051:
                    throw (Error)cause;
                    continue;
                }
                Label_0063:
                // iftrue(Label_0075:, !cause instanceof RuntimeException)
                throw (RuntimeException)cause;
            }
            catch (Exception cause) {
                continue Label_0036;
            }
            break;
        }
    }
    
    protected boolean isFactoryMethod(final AnnotatedMethod annotatedMethod) {
        if (this.getBeanClass().isAssignableFrom(annotatedMethod.getRawReturnType())) {
            if (this._annotationIntrospector.hasCreatorAnnotation(annotatedMethod)) {
                return true;
            }
            if ("valueOf".equals(annotatedMethod.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean removeProperty(final String s) {
        final Iterator<BeanPropertyDefinition> iterator = this._properties.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equals(s)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public JavaType resolveType(final Type type) {
        if (type == null) {
            return null;
        }
        return this.bindingsForBeanType().resolveType(type);
    }
}
