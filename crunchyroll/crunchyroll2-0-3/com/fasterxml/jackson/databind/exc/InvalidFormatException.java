// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.exc;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.JsonMappingException;

public class InvalidFormatException extends JsonMappingException
{
    protected final Class<?> _targetType;
    protected final Object _value;
    
    public InvalidFormatException(final String s, final JsonLocation jsonLocation, final Object value, final Class<?> targetType) {
        super(s, jsonLocation);
        this._value = value;
        this._targetType = targetType;
    }
    
    public static InvalidFormatException from(final JsonParser jsonParser, final String s, final Object o, final Class<?> clazz) {
        return new InvalidFormatException(s, jsonParser.getTokenLocation(), o, clazz);
    }
}
