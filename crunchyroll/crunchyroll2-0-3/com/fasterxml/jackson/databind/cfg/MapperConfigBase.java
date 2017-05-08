// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.cfg;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.type.ClassKey;
import java.util.Map;
import java.io.Serializable;

public abstract class MapperConfigBase<CFG extends ConfigFeature, T extends MapperConfigBase<CFG, T>> extends MapperConfig<T> implements Serializable
{
    private static final int DEFAULT_MAPPER_FEATURES;
    protected final Map<ClassKey, Class<?>> _mixInAnnotations;
    protected final String _rootName;
    protected final SubtypeResolver _subtypeResolver;
    protected final Class<?> _view;
    
    static {
        DEFAULT_MAPPER_FEATURES = MapperConfig.collectFeatureDefaults(MapperFeature.class);
    }
    
    protected MapperConfigBase(final BaseSettings baseSettings, final SubtypeResolver subtypeResolver, final Map<ClassKey, Class<?>> mixInAnnotations) {
        super(baseSettings, MapperConfigBase.DEFAULT_MAPPER_FEATURES);
        this._mixInAnnotations = mixInAnnotations;
        this._subtypeResolver = subtypeResolver;
        this._rootName = null;
        this._view = null;
    }
    
    @Override
    public final Class<?> findMixInClassFor(final Class<?> clazz) {
        if (this._mixInAnnotations == null) {
            return null;
        }
        return this._mixInAnnotations.get(new ClassKey(clazz));
    }
    
    public final Class<?> getActiveView() {
        return this._view;
    }
    
    public final String getRootName() {
        return this._rootName;
    }
    
    public final SubtypeResolver getSubtypeResolver() {
        return this._subtypeResolver;
    }
}
