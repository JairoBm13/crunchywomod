// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;

public enum MapperFeature implements ConfigFeature
{
    ALLOW_FINAL_FIELDS_AS_MUTATORS(true), 
    AUTO_DETECT_CREATORS(true), 
    AUTO_DETECT_FIELDS(true), 
    AUTO_DETECT_GETTERS(true), 
    AUTO_DETECT_IS_GETTERS(true), 
    AUTO_DETECT_SETTERS(true), 
    CAN_OVERRIDE_ACCESS_MODIFIERS(true), 
    DEFAULT_VIEW_INCLUSION(true), 
    INFER_PROPERTY_MUTATORS(true), 
    REQUIRE_SETTERS_FOR_GETTERS(false), 
    SORT_PROPERTIES_ALPHABETICALLY(false), 
    USE_ANNOTATIONS(true), 
    USE_GETTERS_AS_SETTERS(true), 
    USE_STATIC_TYPING(false), 
    USE_WRAPPER_NAME_AS_PROPERTY_NAME(false);
    
    private final boolean _defaultState;
    
    private MapperFeature(final boolean defaultState) {
        this._defaultState = defaultState;
    }
    
    @Override
    public boolean enabledByDefault() {
        return this._defaultState;
    }
    
    @Override
    public int getMask() {
        return 1 << this.ordinal();
    }
}
