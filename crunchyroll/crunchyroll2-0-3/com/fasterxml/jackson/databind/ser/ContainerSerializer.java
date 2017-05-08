// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public abstract class ContainerSerializer<T> extends StdSerializer<T>
{
    protected ContainerSerializer(final ContainerSerializer<?> containerSerializer) {
        super(containerSerializer._handledType, false);
    }
    
    protected ContainerSerializer(final Class<T> clazz) {
        super(clazz);
    }
    
    protected ContainerSerializer(final Class<?> clazz, final boolean b) {
        super(clazz, b);
    }
    
    protected abstract ContainerSerializer<?> _withValueTypeSerializer(final TypeSerializer p0);
    
    protected boolean hasContentTypeAnnotation(final SerializerProvider serializerProvider, final BeanProperty beanProperty) {
        if (beanProperty != null) {
            final AnnotationIntrospector annotationIntrospector = serializerProvider.getAnnotationIntrospector();
            if (annotationIntrospector != null && annotationIntrospector.findSerializationContentType(beanProperty.getMember(), beanProperty.getType()) != null) {
                return true;
            }
        }
        return false;
    }
    
    public abstract boolean hasSingleElement(final T p0);
    
    public ContainerSerializer<?> withValueTypeSerializer(final TypeSerializer typeSerializer) {
        if (typeSerializer == null) {
            return this;
        }
        return this._withValueTypeSerializer(typeSerializer);
    }
}
