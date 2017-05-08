// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.util.TimeZone;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import java.util.Locale;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import java.text.DateFormat;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import java.lang.reflect.Type;
import com.fasterxml.jackson.databind.type.TypeBindings;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MapperFeature;
import java.io.Serializable;
import com.fasterxml.jackson.databind.introspect.ClassIntrospector;

public abstract class MapperConfig<T extends MapperConfig<T>> implements MixInResolver, Serializable
{
    protected final BaseSettings _base;
    protected final int _mapperFeatures;
    
    protected MapperConfig(final BaseSettings base, final int mapperFeatures) {
        this._base = base;
        this._mapperFeatures = mapperFeatures;
    }
    
    public static <F extends Enum> int collectFeatureDefaults(final Class<F> clazz) {
        final java.lang.Enum[] array = (java.lang.Enum[])clazz.getEnumConstants();
        final int length = array.length;
        int i = 0;
        int n = 0;
        while (i < length) {
            final java.lang.Enum enum1 = array[i];
            if (((ConfigFeature)enum1).enabledByDefault()) {
                n |= ((ConfigFeature)enum1).getMask();
            }
            ++i;
        }
        return n;
    }
    
    public final boolean canOverrideAccessModifiers() {
        return this.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
    }
    
    public JavaType constructSpecializedType(final JavaType javaType, final Class<?> clazz) {
        return this.getTypeFactory().constructSpecializedType(javaType, clazz);
    }
    
    public final JavaType constructType(final Class<?> clazz) {
        return this.getTypeFactory().constructType(clazz, null);
    }
    
    public AnnotationIntrospector getAnnotationIntrospector() {
        return this._base.getAnnotationIntrospector();
    }
    
    public Base64Variant getBase64Variant() {
        return this._base.getBase64Variant();
    }
    
    public ClassIntrospector getClassIntrospector() {
        return this._base.getClassIntrospector();
    }
    
    public final DateFormat getDateFormat() {
        return this._base.getDateFormat();
    }
    
    public final TypeResolverBuilder<?> getDefaultTyper(final JavaType javaType) {
        return this._base.getTypeResolverBuilder();
    }
    
    public VisibilityChecker<?> getDefaultVisibilityChecker() {
        return this._base.getVisibilityChecker();
    }
    
    public final HandlerInstantiator getHandlerInstantiator() {
        return this._base.getHandlerInstantiator();
    }
    
    public final Locale getLocale() {
        return this._base.getLocale();
    }
    
    public final PropertyNamingStrategy getPropertyNamingStrategy() {
        return this._base.getPropertyNamingStrategy();
    }
    
    public final TimeZone getTimeZone() {
        return this._base.getTimeZone();
    }
    
    public final TypeFactory getTypeFactory() {
        return this._base.getTypeFactory();
    }
    
    public abstract BeanDescription introspectClassAnnotations(final JavaType p0);
    
    public BeanDescription introspectClassAnnotations(final Class<?> clazz) {
        return this.introspectClassAnnotations(this.constructType(clazz));
    }
    
    public final boolean isAnnotationProcessingEnabled() {
        return this.isEnabled(MapperFeature.USE_ANNOTATIONS);
    }
    
    public final boolean isEnabled(final MapperFeature mapperFeature) {
        return (this._mapperFeatures & mapperFeature.getMask()) != 0x0;
    }
    
    public final boolean shouldSortPropertiesAlphabetically() {
        return this.isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
    }
    
    public TypeIdResolver typeIdResolverInstance(final Annotated annotated, final Class<? extends TypeIdResolver> clazz) {
        final HandlerInstantiator handlerInstantiator = this.getHandlerInstantiator();
        if (handlerInstantiator != null) {
            final TypeIdResolver typeIdResolverInstance = handlerInstantiator.typeIdResolverInstance(this, annotated, clazz);
            if (typeIdResolverInstance != null) {
                return typeIdResolverInstance;
            }
        }
        return ClassUtil.createInstance(clazz, this.canOverrideAccessModifiers());
    }
    
    public TypeResolverBuilder<?> typeResolverBuilderInstance(final Annotated annotated, final Class<? extends TypeResolverBuilder<?>> clazz) {
        final HandlerInstantiator handlerInstantiator = this.getHandlerInstantiator();
        if (handlerInstantiator != null) {
            final TypeResolverBuilder<?> typeResolverBuilderInstance = handlerInstantiator.typeResolverBuilderInstance(this, annotated, clazz);
            if (typeResolverBuilderInstance != null) {
                return typeResolverBuilderInstance;
            }
        }
        return ClassUtil.createInstance(clazz, this.canOverrideAccessModifiers());
    }
}
