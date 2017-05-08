// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;

public enum DeserializationFeature implements ConfigFeature
{
    ACCEPT_EMPTY_STRING_AS_NULL_OBJECT(false), 
    ACCEPT_SINGLE_VALUE_AS_ARRAY(false), 
    ADJUST_DATES_TO_CONTEXT_TIME_ZONE(true), 
    EAGER_DESERIALIZER_FETCH(true), 
    FAIL_ON_INVALID_SUBTYPE(true), 
    FAIL_ON_NULL_FOR_PRIMITIVES(false), 
    FAIL_ON_NUMBERS_FOR_ENUMS(false), 
    FAIL_ON_UNKNOWN_PROPERTIES(true), 
    READ_DATE_TIMESTAMPS_AS_NANOSECONDS(true), 
    READ_ENUMS_USING_TO_STRING(false), 
    READ_UNKNOWN_ENUM_VALUES_AS_NULL(false), 
    UNWRAP_ROOT_VALUE(false), 
    USE_BIG_DECIMAL_FOR_FLOATS(false), 
    USE_BIG_INTEGER_FOR_INTS(false), 
    USE_JAVA_ARRAY_FOR_JSON_ARRAY(false), 
    WRAP_EXCEPTIONS(true);
    
    private final boolean _defaultState;
    
    private DeserializationFeature(final boolean defaultState) {
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
