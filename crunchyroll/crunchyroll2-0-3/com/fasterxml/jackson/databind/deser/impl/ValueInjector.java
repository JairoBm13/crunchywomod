// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.deser.impl;

import java.io.IOException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.BeanProperty;

public class ValueInjector extends Std
{
    protected final Object _valueId;
    
    public ValueInjector(final String s, final JavaType javaType, final Annotations annotations, final AnnotatedMember annotatedMember, final Object valueId) {
        super(s, javaType, null, annotations, annotatedMember, false);
        this._valueId = valueId;
    }
    
    public Object findValue(final DeserializationContext deserializationContext, final Object o) {
        return deserializationContext.findInjectableValue(this._valueId, this, o);
    }
    
    public void inject(final DeserializationContext deserializationContext, final Object o) throws IOException {
        this._member.setValue(o, this.findValue(deserializationContext, o));
    }
}
