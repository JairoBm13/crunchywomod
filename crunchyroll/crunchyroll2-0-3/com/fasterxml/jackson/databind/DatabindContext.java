// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.annotation.NoClass;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.introspect.Annotated;
import java.lang.reflect.Type;

public abstract class DatabindContext
{
    public final boolean canOverrideAccessModifiers() {
        return this.getConfig().canOverrideAccessModifiers();
    }
    
    public JavaType constructSpecializedType(final JavaType javaType, final Class<?> clazz) {
        return this.getConfig().constructSpecializedType(javaType, clazz);
    }
    
    public JavaType constructType(final Type type) {
        return this.getTypeFactory().constructType(type);
    }
    
    public Converter<Object, Object> converterInstance(final Annotated annotated, final Object o) throws JsonMappingException {
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
        final MapperConfig<?> config = this.getConfig();
        final HandlerInstantiator handlerInstantiator = config.getHandlerInstantiator();
        Converter<?, ?> converterInstance;
        if (handlerInstantiator == null) {
            converterInstance = converter;
        }
        else {
            converterInstance = handlerInstantiator.converterInstance(config, annotated, clazz);
        }
        Converter<Object, Object> converter2 = (Converter<Object, Object>)converterInstance;
        if (converterInstance == null) {
            converter2 = ClassUtil.createInstance((Class<Converter<Object, Object>>)clazz, config.canOverrideAccessModifiers());
        }
        return converter2;
    }
    
    public abstract MapperConfig<?> getConfig();
    
    public abstract TypeFactory getTypeFactory();
    
    public final boolean isEnabled(final MapperFeature mapperFeature) {
        return this.getConfig().isEnabled(mapperFeature);
    }
    
    public ObjectIdGenerator<?> objectIdGeneratorInstance(final Annotated annotated, final ObjectIdInfo objectIdInfo) throws JsonMappingException {
        final Class<? extends ObjectIdGenerator<?>> generatorType = objectIdInfo.getGeneratorType();
        final MapperConfig<?> config = this.getConfig();
        final HandlerInstantiator handlerInstantiator = config.getHandlerInstantiator();
        ObjectIdGenerator<?> objectIdGeneratorInstance;
        if (handlerInstantiator == null) {
            objectIdGeneratorInstance = null;
        }
        else {
            objectIdGeneratorInstance = handlerInstantiator.objectIdGeneratorInstance(config, annotated, generatorType);
        }
        ObjectIdGenerator<?> objectIdGenerator = objectIdGeneratorInstance;
        if (objectIdGeneratorInstance == null) {
            objectIdGenerator = ClassUtil.createInstance(generatorType, config.canOverrideAccessModifiers());
        }
        return objectIdGenerator.forScope(objectIdInfo.getScope());
    }
}
