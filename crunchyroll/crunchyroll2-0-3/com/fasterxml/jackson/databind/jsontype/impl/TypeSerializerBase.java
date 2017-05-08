// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public abstract class TypeSerializerBase extends TypeSerializer
{
    protected final TypeIdResolver _idResolver;
    protected final BeanProperty _property;
    
    protected TypeSerializerBase(final TypeIdResolver idResolver, final BeanProperty property) {
        this._idResolver = idResolver;
        this._property = property;
    }
    
    protected String idFromValue(final Object o) {
        return this._idResolver.idFromValue(o);
    }
    
    protected String idFromValueAndType(final Object o, final Class<?> clazz) {
        return this._idResolver.idFromValueAndType(o, clazz);
    }
}
