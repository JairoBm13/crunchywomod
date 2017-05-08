// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class i
{
    private static final Map<Class<?>, Class<?>> a;
    private static final Map<Class<?>, Class<?>> b;
    
    static {
        final HashMap<Class<?>, Class<?>> hashMap = new HashMap<Class<?>, Class<?>>(16);
        final HashMap<Class<?>, Class<?>> hashMap2 = new HashMap<Class<?>, Class<?>>(16);
        a(hashMap, hashMap2, Boolean.TYPE, Boolean.class);
        a(hashMap, hashMap2, Byte.TYPE, Byte.class);
        a(hashMap, hashMap2, Character.TYPE, Character.class);
        a(hashMap, hashMap2, Double.TYPE, Double.class);
        a(hashMap, hashMap2, Float.TYPE, Float.class);
        a(hashMap, hashMap2, Integer.TYPE, Integer.class);
        a(hashMap, hashMap2, Long.TYPE, Long.class);
        a(hashMap, hashMap2, Short.TYPE, Short.class);
        a(hashMap, hashMap2, Void.TYPE, Void.class);
        a = Collections.unmodifiableMap((Map<?, ?>)hashMap);
        b = Collections.unmodifiableMap((Map<?, ?>)hashMap2);
    }
    
    public static <T> Class<T> a(final Class<T> clazz) {
        final Class<?> clazz2 = i.a.get(com.google.ads.interactivemedia.v3.a.b.a.a(clazz));
        if (clazz2 == null) {
            return clazz;
        }
        return (Class<T>)clazz2;
    }
    
    private static void a(final Map<Class<?>, Class<?>> map, final Map<Class<?>, Class<?>> map2, final Class<?> clazz, final Class<?> clazz2) {
        map.put(clazz, clazz2);
        map2.put(clazz2, clazz);
    }
    
    public static boolean a(final Type type) {
        return i.a.containsKey(type);
    }
}
