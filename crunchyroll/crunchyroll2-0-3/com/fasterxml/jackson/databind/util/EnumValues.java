// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.util.HashMap;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import java.util.Map;
import com.fasterxml.jackson.core.io.SerializedString;
import java.util.EnumMap;

public final class EnumValues
{
    private final Class<Enum<?>> _enumClass;
    private final EnumMap<?, SerializedString> _values;
    
    private EnumValues(final Class<Enum<?>> enumClass, final Map<Enum<?>, SerializedString> map) {
        this._enumClass = enumClass;
        this._values = new EnumMap<Object, SerializedString>(map);
    }
    
    public static EnumValues construct(final Class<Enum<?>> clazz, final AnnotationIntrospector annotationIntrospector) {
        return constructFromName(clazz, annotationIntrospector);
    }
    
    public static EnumValues constructFromName(final Class<Enum<?>> clazz, final AnnotationIntrospector annotationIntrospector) {
        final Enum[] array = (Enum[])ClassUtil.findEnumType(clazz).getEnumConstants();
        if (array != null) {
            final HashMap<Enum<?>, SerializedString> hashMap = new HashMap<Enum<?>, SerializedString>();
            for (int length = array.length, i = 0; i < length; ++i) {
                final Enum enum1 = array[i];
                hashMap.put(enum1, new SerializedString(annotationIntrospector.findEnumValue(enum1)));
            }
            return new EnumValues(clazz, hashMap);
        }
        throw new IllegalArgumentException("Can not determine enum constants for Class " + clazz.getName());
    }
    
    public static EnumValues constructFromToString(final Class<Enum<?>> clazz, final AnnotationIntrospector annotationIntrospector) {
        final Enum[] array = (Enum[])ClassUtil.findEnumType(clazz).getEnumConstants();
        if (array != null) {
            final HashMap<Enum, SerializedString> hashMap = new HashMap<Enum, SerializedString>();
            for (int length = array.length, i = 0; i < length; ++i) {
                final Enum enum1 = array[i];
                hashMap.put(enum1, new SerializedString(enum1.toString()));
            }
            return new EnumValues(clazz, (Map<Enum<?>, SerializedString>)hashMap);
        }
        throw new IllegalArgumentException("Can not determine enum constants for Class " + clazz.getName());
    }
    
    public SerializedString serializedValueFor(final Enum<?> enum1) {
        return this._values.get(enum1);
    }
}
