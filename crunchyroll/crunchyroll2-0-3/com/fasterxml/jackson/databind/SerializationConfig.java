// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.Map;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import java.io.Serializable;
import com.fasterxml.jackson.databind.cfg.MapperConfigBase;

public final class SerializationConfig extends MapperConfigBase<SerializationFeature, SerializationConfig> implements Serializable
{
    protected final FilterProvider _filterProvider;
    protected final int _serFeatures;
    protected JsonInclude.Include _serializationInclusion;
    
    public SerializationConfig(final BaseSettings baseSettings, final SubtypeResolver subtypeResolver, final Map<ClassKey, Class<?>> map) {
        super(baseSettings, subtypeResolver, map);
        this._serializationInclusion = null;
        this._serFeatures = MapperConfig.collectFeatureDefaults(SerializationFeature.class);
        this._filterProvider = null;
    }
    
    @Override
    public AnnotationIntrospector getAnnotationIntrospector() {
        if (this.isEnabled(MapperFeature.USE_ANNOTATIONS)) {
            return super.getAnnotationIntrospector();
        }
        return AnnotationIntrospector.nopInstance();
    }
    
    @Override
    public VisibilityChecker<?> getDefaultVisibilityChecker() {
        Object o;
        final VisibilityChecker<?> visibilityChecker = (VisibilityChecker<?>)(o = super.getDefaultVisibilityChecker());
        if (!this.isEnabled(MapperFeature.AUTO_DETECT_GETTERS)) {
            o = visibilityChecker.withGetterVisibility(JsonAutoDetect.Visibility.NONE);
        }
        Object withIsGetterVisibility = o;
        if (!this.isEnabled(MapperFeature.AUTO_DETECT_IS_GETTERS)) {
            withIsGetterVisibility = ((VisibilityChecker<?>)o).withIsGetterVisibility(JsonAutoDetect.Visibility.NONE);
        }
        Object withFieldVisibility = withIsGetterVisibility;
        if (!this.isEnabled(MapperFeature.AUTO_DETECT_FIELDS)) {
            withFieldVisibility = ((VisibilityChecker<?>)withIsGetterVisibility).withFieldVisibility(JsonAutoDetect.Visibility.NONE);
        }
        return (VisibilityChecker<?>)withFieldVisibility;
    }
    
    public FilterProvider getFilterProvider() {
        return this._filterProvider;
    }
    
    public JsonInclude.Include getSerializationInclusion() {
        if (this._serializationInclusion != null) {
            return this._serializationInclusion;
        }
        return JsonInclude.Include.ALWAYS;
    }
    
    public <T extends BeanDescription> T introspect(final JavaType javaType) {
        return (T)this.getClassIntrospector().forSerialization(this, javaType, (ClassIntrospector.MixInResolver)this);
    }
    
    @Override
    public BeanDescription introspectClassAnnotations(final JavaType javaType) {
        return this.getClassIntrospector().forClassAnnotations(this, javaType, (ClassIntrospector.MixInResolver)this);
    }
    
    public final boolean isEnabled(final SerializationFeature serializationFeature) {
        return (this._serFeatures & serializationFeature.getMask()) != 0x0;
    }
    
    @Override
    public String toString() {
        return "[SerializationConfig: flags=0x" + Integer.toHexString(this._serFeatures) + "]";
    }
}
