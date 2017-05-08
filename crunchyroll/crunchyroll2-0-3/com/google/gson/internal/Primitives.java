// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gson.internal;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Primitives
{
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;
    
    static {
        final HashMap<Class<?>, Class<?>> hashMap = new HashMap<Class<?>, Class<?>>(16);
        final HashMap<Class<?>, Class<?>> hashMap2 = new HashMap<Class<?>, Class<?>>(16);
        add(hashMap, hashMap2, Boolean.TYPE, Boolean.class);
        add(hashMap, hashMap2, Byte.TYPE, Byte.class);
        add(hashMap, hashMap2, Character.TYPE, Character.class);
        add(hashMap, hashMap2, Double.TYPE, Double.class);
        add(hashMap, hashMap2, Float.TYPE, Float.class);
        add(hashMap, hashMap2, Integer.TYPE, Integer.class);
        add(hashMap, hashMap2, Long.TYPE, Long.class);
        add(hashMap, hashMap2, Short.TYPE, Short.class);
        add(hashMap, hashMap2, Void.TYPE, Void.class);
        PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap((Map<?, ?>)hashMap);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap((Map<?, ?>)hashMap2);
    }
    
    private static void add(final Map<Class<?>, Class<?>> map, final Map<Class<?>, Class<?>> map2, final Class<?> clazz, final Class<?> clazz2) {
        map.put(clazz, clazz2);
        map2.put(clazz2, clazz);
    }
    
    public static boolean isPrimitive(final Type type) {
        return Primitives.PRIMITIVE_TO_WRAPPER_TYPE.containsKey(type);
    }
    
    public static <T> Class<T> wrap(final Class<T> clazz) {
        final Class<?> clazz2 = Primitives.PRIMITIVE_TO_WRAPPER_TYPE.get($Gson$Preconditions.checkNotNull(clazz));
        if (clazz2 == null) {
            return clazz;
        }
        return (Class<T>)clazz2;
    }
}
