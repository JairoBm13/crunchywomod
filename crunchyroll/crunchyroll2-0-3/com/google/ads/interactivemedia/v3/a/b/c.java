// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b;

import java.util.LinkedHashMap;
import com.google.ads.interactivemedia.v3.a.c.a;
import java.util.TreeMap;
import java.util.SortedMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.LinkedHashSet;
import java.util.Set;
import com.google.ads.interactivemedia.v3.a.m;
import java.lang.reflect.ParameterizedType;
import java.util.EnumSet;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.Collection;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import com.google.ads.interactivemedia.v3.a.h;
import java.lang.reflect.Type;
import java.util.Map;

public final class c
{
    private final Map<Type, h<?>> a;
    
    public c(final Map<Type, h<?>> a) {
        this.a = a;
    }
    
    private <T> com.google.ads.interactivemedia.v3.a.b.h<T> a(final Class<? super T> clazz) {
        try {
            final Constructor<? super T> declaredConstructor = clazz.getDeclaredConstructor((Class<?>[])new Class[0]);
            if (!declaredConstructor.isAccessible()) {
                declaredConstructor.setAccessible(true);
            }
            return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                @Override
                public T a() {
                    try {
                        return declaredConstructor.newInstance((Object[])null);
                    }
                    catch (InstantiationException ex) {
                        throw new RuntimeException("Failed to invoke " + declaredConstructor + " with no args", ex);
                    }
                    catch (InvocationTargetException ex2) {
                        throw new RuntimeException("Failed to invoke " + declaredConstructor + " with no args", ex2.getTargetException());
                    }
                    catch (IllegalAccessException ex3) {
                        throw new AssertionError((Object)ex3);
                    }
                }
            };
        }
        catch (NoSuchMethodException ex) {
            return null;
        }
    }
    
    private <T> com.google.ads.interactivemedia.v3.a.b.h<T> a(final Type type, final Class<? super T> clazz) {
        if (Collection.class.isAssignableFrom(clazz)) {
            if (SortedSet.class.isAssignableFrom(clazz)) {
                return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                    @Override
                    public T a() {
                        return (T)new TreeSet();
                    }
                };
            }
            if (EnumSet.class.isAssignableFrom(clazz)) {
                return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                    @Override
                    public T a() {
                        if (!(type instanceof ParameterizedType)) {
                            throw new m("Invalid EnumSet type: " + type.toString());
                        }
                        final Type type = ((ParameterizedType)type).getActualTypeArguments()[0];
                        if (type instanceof Class) {
                            return (T)EnumSet.noneOf((Class<Enum>)type);
                        }
                        throw new m("Invalid EnumSet type: " + type.toString());
                    }
                };
            }
            if (Set.class.isAssignableFrom(clazz)) {
                return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                    @Override
                    public T a() {
                        return (T)new LinkedHashSet();
                    }
                };
            }
            if (Queue.class.isAssignableFrom(clazz)) {
                return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                    @Override
                    public T a() {
                        return (T)new LinkedList();
                    }
                };
            }
            return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                @Override
                public T a() {
                    return (T)new ArrayList();
                }
            };
        }
        else {
            if (!Map.class.isAssignableFrom(clazz)) {
                return null;
            }
            if (SortedMap.class.isAssignableFrom(clazz)) {
                return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                    @Override
                    public T a() {
                        return (T)new TreeMap();
                    }
                };
            }
            if (type instanceof ParameterizedType && !String.class.isAssignableFrom(com.google.ads.interactivemedia.v3.a.c.a.a(((ParameterizedType)type).getActualTypeArguments()[0]).a())) {
                return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                    @Override
                    public T a() {
                        return (T)new LinkedHashMap();
                    }
                };
            }
            return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                @Override
                public T a() {
                    return (T)new g();
                }
            };
        }
    }
    
    private <T> com.google.ads.interactivemedia.v3.a.b.h<T> b(final Type type, final Class<? super T> clazz) {
        return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
            private final k d = k.a();
            
            @Override
            public T a() {
                try {
                    return this.d.a(clazz);
                }
                catch (Exception ex) {
                    throw new RuntimeException("Unable to invoke no-args constructor for " + type + ". " + "Register an InstanceCreator with Gson for this type may fix this problem.", ex);
                }
            }
        };
    }
    
    public <T> com.google.ads.interactivemedia.v3.a.b.h<T> a(final a<T> a) {
        final Type b = a.b();
        final Class<? super T> a2 = a.a();
        final h<?> h = this.a.get(b);
        Object o;
        if (h != null) {
            o = new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                @Override
                public T a() {
                    return h.a(b);
                }
            };
        }
        else {
            final h<?> h2 = this.a.get(a2);
            if (h2 != null) {
                return new com.google.ads.interactivemedia.v3.a.b.h<T>() {
                    @Override
                    public T a() {
                        return h2.a(b);
                    }
                };
            }
            if ((o = this.a((Class<? super Object>)a2)) == null && (o = this.a(b, (Class<? super Object>)a2)) == null) {
                return (com.google.ads.interactivemedia.v3.a.b.h<T>)this.b(b, (Class<? super Object>)a2);
            }
        }
        return (com.google.ads.interactivemedia.v3.a.b.h<T>)o;
    }
    
    @Override
    public String toString() {
        return this.a.toString();
    }
}
