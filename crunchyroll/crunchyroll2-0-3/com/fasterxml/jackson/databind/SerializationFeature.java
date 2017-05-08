// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;

public enum SerializationFeature implements ConfigFeature
{
    CLOSE_CLOSEABLE(false), 
    EAGER_SERIALIZER_FETCH(true), 
    FAIL_ON_EMPTY_BEANS(true), 
    FLUSH_AFTER_WRITE_VALUE(true), 
    INDENT_OUTPUT(false), 
    ORDER_MAP_ENTRIES_BY_KEYS(false), 
    WRAP_EXCEPTIONS(true), 
    WRAP_ROOT_VALUE(false), 
    WRITE_BIGDECIMAL_AS_PLAIN(false), 
    WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS(false), 
    WRITE_DATES_AS_TIMESTAMPS(true), 
    WRITE_DATE_KEYS_AS_TIMESTAMPS(false), 
    WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS(true), 
    WRITE_EMPTY_JSON_ARRAYS(true), 
    WRITE_ENUMS_USING_INDEX(false), 
    WRITE_ENUMS_USING_TO_STRING(false), 
    WRITE_NULL_MAP_VALUES(true), 
    WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED(false);
    
    private final boolean _defaultState;
    
    private SerializationFeature(final boolean defaultState) {
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
