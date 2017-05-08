// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.Map;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.cfg.BaseSettings;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import com.fasterxml.jackson.databind.util.LinkedNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import java.io.Serializable;
import com.fasterxml.jackson.databind.cfg.MapperConfigBase;

public final class DeserializationConfig extends MapperConfigBase<DeserializationFeature, DeserializationConfig> implements Serializable
{
    protected final int _deserFeatures;
    protected final JsonNodeFactory _nodeFactory;
    protected final LinkedNode<DeserializationProblemHandler> _problemHandlers;
    
    public DeserializationConfig(final BaseSettings baseSettings, final SubtypeResolver subtypeResolver, final Map<ClassKey, Class<?>> map) {
        super(baseSettings, subtypeResolver, map);
        this._deserFeatures = MapperConfig.collectFeatureDefaults(DeserializationFeature.class);
        this._nodeFactory = JsonNodeFactory.instance;
        this._problemHandlers = null;
    }
    
    @Override
    public AnnotationIntrospector getAnnotationIntrospector() {
        if (this.isEnabled(MapperFeature.USE_ANNOTATIONS)) {
            return super.getAnnotationIntrospector();
        }
        return NopAnnotationIntrospector.instance;
    }
    
    @Override
    public VisibilityChecker<?> getDefaultVisibilityChecker() {
        Object o;
        final VisibilityChecker<?> visibilityChecker = (VisibilityChecker<?>)(o = super.getDefaultVisibilityChecker());
        if (!this.isEnabled(MapperFeature.AUTO_DETECT_SETTERS)) {
            o = visibilityChecker.withSetterVisibility(JsonAutoDetect.Visibility.NONE);
        }
        Object withCreatorVisibility = o;
        if (!this.isEnabled(MapperFeature.AUTO_DETECT_CREATORS)) {
            withCreatorVisibility = ((VisibilityChecker<?>)o).withCreatorVisibility(JsonAutoDetect.Visibility.NONE);
        }
        Object withFieldVisibility = withCreatorVisibility;
        if (!this.isEnabled(MapperFeature.AUTO_DETECT_FIELDS)) {
            withFieldVisibility = ((VisibilityChecker<?>)withCreatorVisibility).withFieldVisibility(JsonAutoDetect.Visibility.NONE);
        }
        return (VisibilityChecker<?>)withFieldVisibility;
    }
    
    public final int getDeserializationFeatures() {
        return this._deserFeatures;
    }
    
    public final JsonNodeFactory getNodeFactory() {
        return this._nodeFactory;
    }
    
    public LinkedNode<DeserializationProblemHandler> getProblemHandlers() {
        return this._problemHandlers;
    }
    
    public <T extends BeanDescription> T introspect(final JavaType javaType) {
        return (T)this.getClassIntrospector().forDeserialization(this, javaType, (ClassIntrospector.MixInResolver)this);
    }
    
    @Override
    public BeanDescription introspectClassAnnotations(final JavaType javaType) {
        return this.getClassIntrospector().forClassAnnotations(this, javaType, (ClassIntrospector.MixInResolver)this);
    }
    
    public <T extends BeanDescription> T introspectForBuilder(final JavaType javaType) {
        return (T)this.getClassIntrospector().forDeserializationWithBuilder(this, javaType, (ClassIntrospector.MixInResolver)this);
    }
    
    public <T extends BeanDescription> T introspectForCreation(final JavaType javaType) {
        return (T)this.getClassIntrospector().forCreation(this, javaType, (ClassIntrospector.MixInResolver)this);
    }
    
    public final boolean isEnabled(final DeserializationFeature deserializationFeature) {
        return (this._deserFeatures & deserializationFeature.getMask()) != 0x0;
    }
    
    public boolean useRootWrapping() {
        if (this._rootName != null) {
            return this._rootName.length() > 0;
        }
        return this.isEnabled(DeserializationFeature.UNWRAP_ROOT_VALUE);
    }
}
