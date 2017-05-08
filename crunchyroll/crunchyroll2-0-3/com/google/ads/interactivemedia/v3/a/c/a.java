// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.c;

import java.lang.reflect.ParameterizedType;
import com.google.ads.interactivemedia.v3.a.b.b;
import java.lang.reflect.Type;

public class a<T>
{
    final Class<? super T> a;
    final Type b;
    final int c;
    
    protected a() {
        this.b = a(this.getClass());
        this.a = (Class<? super T>)com.google.ads.interactivemedia.v3.a.b.b.e(this.b);
        this.c = this.b.hashCode();
    }
    
    a(final Type type) {
        this.b = com.google.ads.interactivemedia.v3.a.b.b.d(com.google.ads.interactivemedia.v3.a.b.a.a(type));
        this.a = (Class<? super T>)com.google.ads.interactivemedia.v3.a.b.b.e(this.b);
        this.c = this.b.hashCode();
    }
    
    public static a<?> a(final Type type) {
        return new a<Object>(type);
    }
    
    static Type a(final Class<?> clazz) {
        final Type genericSuperclass = clazz.getGenericSuperclass();
        if (genericSuperclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        return b.d(((ParameterizedType)genericSuperclass).getActualTypeArguments()[0]);
    }
    
    public static <T> a<T> b(final Class<T> clazz) {
        return new a<T>(clazz);
    }
    
    public final Class<? super T> a() {
        return this.a;
    }
    
    public final Type b() {
        return this.b;
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o instanceof a && com.google.ads.interactivemedia.v3.a.b.b.a(this.b, ((a)o).b);
    }
    
    @Override
    public final int hashCode() {
        return this.c;
    }
    
    @Override
    public final String toString() {
        return com.google.ads.interactivemedia.v3.a.b.b.f(this.b);
    }
}
