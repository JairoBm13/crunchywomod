// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.util;

import java.lang.reflect.Method;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import java.util.HashMap;
import java.io.Serializable;

public class EnumResolver<T extends Enum<T>> implements Serializable
{
    protected final Class<T> _enumClass;
    protected final T[] _enums;
    protected final HashMap<String, T> _enumsById;
    
    protected EnumResolver(final Class<T> enumClass, final T[] enums, final HashMap<String, T> enumsById) {
        this._enumClass = enumClass;
        this._enums = enums;
        this._enumsById = enumsById;
    }
    
    public static <ET extends Enum<ET>> EnumResolver<ET> constructFor(final Class<ET> clazz, final AnnotationIntrospector annotationIntrospector) {
        final ET[] array = clazz.getEnumConstants();
        if (array == null) {
            throw new IllegalArgumentException("No enum constants for class " + clazz.getName());
        }
        final HashMap<String, Enum> hashMap = new HashMap<String, Enum>();
        for (int length = array.length, i = 0; i < length; ++i) {
            final Enum<ET> enum1 = array[i];
            hashMap.put(annotationIntrospector.findEnumValue(enum1), enum1);
        }
        return new EnumResolver<ET>((Class<Enum>)clazz, array, hashMap);
    }
    
    public static EnumResolver<?> constructUnsafe(final Class<?> clazz, final AnnotationIntrospector annotationIntrospector) {
        return constructFor(clazz, annotationIntrospector);
    }
    
    public static EnumResolver<?> constructUnsafeUsingMethod(final Class<?> clazz, final Method method) {
        return constructUsingMethod(clazz, method);
    }
    
    public static EnumResolver<?> constructUnsafeUsingToString(final Class<?> clazz) {
        return constructUsingToString(clazz);
    }
    
    public static <ET extends Enum<ET>> EnumResolver<ET> constructUsingMethod(final Class<ET> clazz, final Method method) {
        final ET[] array = clazz.getEnumConstants();
        final HashMap<String, Enum> hashMap = new HashMap<String, Enum>();
        int length = array.length;
        while (true) {
            final int n = length - 1;
            if (n >= 0) {
                final Enum<ET> enum1 = array[n];
                try {
                    final Object invoke = method.invoke(enum1, new Object[0]);
                    length = n;
                    if (invoke != null) {
                        hashMap.put(invoke.toString(), enum1);
                        length = n;
                        continue;
                    }
                    continue;
                }
                catch (Exception ex) {
                    throw new IllegalArgumentException("Failed to access @JsonValue of Enum value " + enum1 + ": " + ex.getMessage());
                }
                break;
            }
            break;
        }
        return new EnumResolver<ET>((Class<Enum>)clazz, array, hashMap);
    }
    
    public static <ET extends Enum<ET>> EnumResolver<ET> constructUsingToString(final Class<ET> clazz) {
        final ET[] array = clazz.getEnumConstants();
        final HashMap<String, Enum> hashMap = new HashMap<String, Enum>();
        int length = array.length;
        while (true) {
            --length;
            if (length < 0) {
                break;
            }
            final Enum<ET> enum1 = array[length];
            hashMap.put(enum1.toString(), enum1);
        }
        return new EnumResolver<ET>((Class<Enum>)clazz, array, hashMap);
    }
    
    public T findEnum(final String s) {
        return this._enumsById.get(s);
    }
    
    public T getEnum(final int n) {
        if (n < 0 || n >= this._enums.length) {
            return null;
        }
        return (T)this._enums[n];
    }
    
    public Class<T> getEnumClass() {
        return this._enumClass;
    }
    
    public int lastValidIndex() {
        return this._enums.length - 1;
    }
}
